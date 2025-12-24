package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.auth.supabase
import com.example.powertracker.models.Meter
import com.example.powertracker.models.Token
import com.example.powertracker.models.UsageLog
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.ktor.client.call.body
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

@Serializable
data class AIInsightRequest(
    val meterId: String,
    val balanceKwh: Double
)

@Serializable
data class AIInsightResponse(
    val forecastDate: String,
    val peakUsageDescription: String,
    val suggestions: List<String>
)

@OptIn(ExperimentalTime::class)
class InsightsScreenViewModel: ViewModel() {

    var isLoading = mutableStateOf(false)
        private set

    var avgSpending = mutableStateOf("Calculating...")
        private set

    var avgDailyUsage = mutableStateOf("Calculating...")
        private set

    var peakUsageTime = mutableStateOf("Calculating...")
        private set

    var weekendVsWeekday = mutableStateOf("Calculating...")
        private set

    var aiForecast = mutableStateOf(
        "Analyzing your consumption patterns..."
    )
        private set

    val recommendations = mutableStateListOf<String>()
    
    // Data for the 30-day trend graph
    val trendData = mutableStateListOf<Pair<Int, Float>>()

    fun loadInsights(meterId: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                // Fetch meter details to get the current balance
                val meter = supabase.postgrest.from("meters")
                    .select {
                        filter { eq("id", meterId) }
                    }.decodeSingle<Meter>()

                val now = Clock.System.now()
                val thirtyDaysAgo = now.minus(30, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                
                val logs = supabase.postgrest.from("usage_logs")
                    .select {
                        filter {
                            eq("meter_id", meterId)
                            gte("logged_at", thirtyDaysAgo.toString())
                        }
                        order("logged_at", Order.ASCENDING)
                        limit(500) 
                    }.decodeList<UsageLog>()

                val tokens = supabase.postgrest.from("tokens")
                    .select {
                        filter {
                            eq("meter_id", meterId)
                            gte("purchase_date", thirtyDaysAgo.toString())
                        }
                    }.decodeList<Token>()

                calculateLocalStats(logs, tokens)

                // Pass the actual balance to the AI for accuracy
                fetchAIAnalysis(meterId, meter.balanceKwh)

            } catch (e: Exception) {
                aiForecast.value = "Error loading insights: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun calculateLocalStats(logs: List<UsageLog>, tokens: List<Token>) {
        if (logs.isEmpty()) {
            avgDailyUsage.value = "0 kWh"
            peakUsageTime.value = "N/A"
            weekendVsWeekday.value = "N/A"
            avgSpending.value = "GHS 0"
            trendData.clear()
            return
        }

        // --- Average Daily Usage ---
        val totalUsage = logs.sumOf { it.usageKwh }
        val distinctDays = logs.mapNotNull { it.loggedAt?.take(10) }.distinct().size
        val avg = if (distinctDays > 0) totalUsage / distinctDays else 0.0
        avgDailyUsage.value = "${(avg * 10).roundToInt() / 10.0} kWh"

        // --- Average Spending ---
        val totalSpent = tokens.sumOf { it.amount }
        avgSpending.value = "GHS ${totalSpent.roundToInt()}"

        // --- Peak Usage Time ---
        val hourGroups = logs.groupBy { 
            it.loggedAt?.let { logTime ->
                try {
                    Instant.parse(logTime).toLocalDateTime(TimeZone.currentSystemDefault()).hour 
                } catch(e: Exception) { null }
            }
        }
        val peakHour = hourGroups.maxByOrNull { it.value.sumOf { log -> log.usageKwh } }?.key
        peakUsageTime.value = if (peakHour != null) {
            val endHour = (peakHour + 1) % 24
            "$peakHour:00 - $endHour:00"
        } else "N/A"

        // --- Weekend vs Weekday ---
        val weekendUsage = logs.filter { 
            try {
                val date = Instant.parse(it.loggedAt!!).toLocalDateTime(TimeZone.currentSystemDefault())
                date.dayOfWeek == kotlinx.datetime.DayOfWeek.SATURDAY || 
                date.dayOfWeek == kotlinx.datetime.DayOfWeek.SUNDAY
            } catch(e: Exception) { false }
        }.sumOf { it.usageKwh }
        
        val weekdayUsage = totalUsage - weekendUsage
        val weekendDailyAvg = if (distinctDays > 0) weekendUsage / 2.0 else 0.0
        val weekdayDailyAvg = if (distinctDays > 0) weekdayUsage / 5.0 else 0.0
        
        val diff = if (weekdayDailyAvg > 0) {
            ((weekendDailyAvg - weekdayDailyAvg) / weekdayDailyAvg * 100).roundToInt()
        } else 0

        weekendVsWeekday.value = when {
            diff > 5 -> "+$diff% higher"
            diff < -5 -> "${diff.coerceAtLeast(-100)}% lower"
            else -> "Consistent"
        }

        // --- Graph Data (30-Day Trend) ---
        val dailyGroups = logs.groupBy { it.loggedAt?.take(10) }
        val trendPoints = mutableListOf<Pair<Int, Float>>()
        
        // Sort keys to get chronological order
        val sortedDays = dailyGroups.keys.filterNotNull().sorted()
        sortedDays.forEachIndexed { index, day ->
            val dailySum = dailyGroups[day]?.sumOf { it.usageKwh } ?: 0.0
            trendPoints.add(Pair(index + 1, dailySum.toFloat()))
        }
        
        trendData.clear()
        trendData.addAll(trendPoints)

        recommendations.clear()
        if (diff > 10) {
            recommendations.add("Your weekend usage is $diff% higher than weekdays.")
        }
    }

    private suspend fun fetchAIAnalysis(meterId: String, balanceKwh: Double) {
        try {
            val response = supabase.functions.invoke(
                function = "get-ai-insights",
                body = AIInsightRequest(meterId = meterId, balanceKwh = balanceKwh)
            )

            val aiData = response.body<AIInsightResponse>()
            aiForecast.value = aiData.forecastDate
            
            recommendations.addAll(aiData.suggestions)

        } catch (e: Exception) {
            aiForecast.value = "AI Analysis Error: ${e.message}"
        }
    }
}
