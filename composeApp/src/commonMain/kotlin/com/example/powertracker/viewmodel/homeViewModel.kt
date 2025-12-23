package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.auth.supabase
import com.example.powertracker.models.Meter
import com.example.powertracker.models.UsageLog
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.round
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class HomeViewModel : ViewModel() {

    val meters = mutableStateListOf<Meter>()
    val selectedMeter = mutableStateOf<Meter?>(null)

    val balanceKwh = mutableStateOf("0.0 kWh")
    val balanceGhs = mutableStateOf("GHS 0.00")
    val daysLeft = mutableStateOf("Calculating...")

    val usage = mutableStateOf("Today's Usage: 0.0 kWh")
    val prediction = mutableStateOf("Prediction data unavailable")
    
    val dailyUsagePoints = mutableStateListOf<Float>()

    val meterDropdownExpanded = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val isRefreshing = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    init {
        loadMeters()
    }

    private fun formatValue(value: Double): String {
        return (round(value * 100) / 100.0).toString()
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            loadMeters()
            isRefreshing.value = false
        }
    }

    fun loadMeters() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                val user = supabase.auth.currentUserOrNull()
                val userId = user?.id
                if (userId != null) {
                    val result = supabase.postgrest.from("meters")
                        .select {
                            filter {
                                eq("user_id", userId)
                            }
                        }.decodeList<Meter>()

                    meters.clear()
                    meters.addAll(result)

                    if (meters.isNotEmpty()) {
                        val current = selectedMeter.value
                        if (current == null) {
                            selectMeter(meters[0])
                        } else {
                            val updated = meters.find { it.id == current.id } ?: meters[0]
                            selectMeter(updated)
                        }
                    } else {
                        error.value = "No meters found."
                    }
                } else {
                    error.value = "User session not found."
                }
            } catch (e: Exception) {
                error.value = "Load error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun selectMeter(meter: Meter) {
        selectedMeter.value = meter
        meterDropdownExpanded.value = false
        simulateConsumption(meter)
    }

    private fun simulateConsumption(meter: Meter) {
        viewModelScope.launch {
            try {
                val meterId = meter.id ?: return@launch

                // If balance is zero, stop simulating consumption
                if (meter.balanceKwh <= 0) {
                    balanceKwh.value = "0.0 kWh"
                    balanceGhs.value = "GHS 0.00"
                    loadTodayUsage(meterId)
                    return@launch
                }

                val lastLog = supabase.postgrest.from("usage_logs")
                    .select {
                        filter { eq("meter_id", meterId) }
                        order("logged_at", Order.DESCENDING)
                        limit(1)
                    }.decodeSingleOrNull<UsageLog>()

                val now = Clock.System.now()
                val lastLogTime = lastLog?.loggedAt?.let { Instant.parse(it) } ?: (now - 1.hours)

                val duration = now - lastLogTime
                val hoursPassed = duration.inWholeMilliseconds.toDouble() / 3600000.0
                
                val cappedHours = if (lastLog == null) 1.0 else hoursPassed

                if (cappedHours > 0.05) { 
                    val hourlyRate = 0.25
                    val consumedKwh = (cappedHours * hourlyRate).coerceAtMost(meter.balanceKwh)

                    val newLog = UsageLog(
                        meterId = meterId,
                        usageKwh = consumedKwh,
                        loggedAt = now.toString()
                    )
                    supabase.postgrest.from("usage_logs").insert(newLog)

                    val newBalanceKwh = (meter.balanceKwh - consumedKwh).coerceAtLeast(0.0)
                    val ghsReduction = if (meter.balanceKwh > 0) {
                        (meter.balanceGhs / meter.balanceKwh) * consumedKwh
                    } else {
                        0.0
                    }
                    val newBalanceGhs = (meter.balanceGhs - ghsReduction).coerceAtLeast(0.0)

                    supabase.postgrest.from("meters").update(
                        {
                            set("balance_kwh", newBalanceKwh)
                            set("balance_ghs", newBalanceGhs)
                        }
                    ) {
                        filter { eq("id", meterId) }
                    }

                    balanceKwh.value = "${formatValue(newBalanceKwh)} kWh"
                    balanceGhs.value = "GHS ${formatValue(newBalanceGhs)}"
                } else {
                    balanceKwh.value = "${formatValue(meter.balanceKwh)} kWh"
                    balanceGhs.value = "GHS ${formatValue(meter.balanceGhs)}"
                }

                loadTodayUsage(meterId)

            } catch (e: Exception) {
                error.value = "Sim error: ${e.message}"
                balanceKwh.value = "${meter.balanceKwh} kWh"
                balanceGhs.value = "GHS ${meter.balanceGhs}"
            }
        }
    }

    private fun loadTodayUsage(meterId: String) {
        viewModelScope.launch {
            try {
                val nowTime = Clock.System.now()
                val now = nowTime.toLocalDateTime(TimeZone.currentSystemDefault())
                val todayStart = "${now.date}T00:00:00Z"

                val logs = supabase.postgrest.from("usage_logs")
                    .select {
                        filter {
                            eq("meter_id", meterId)
                            gte("logged_at", todayStart)
                        }
                        order("logged_at", Order.ASCENDING)
                    }.decodeList<UsageLog>()

                val totalUsage = logs.sumOf { it.usageKwh }
                usage.value = "Today's Usage: ${formatValue(totalUsage)} kWh"
                
                // Group by hour for the graph
                val hourlyUsage = FloatArray(24) { 0f }
                logs.forEach { log ->
                    val logLoggedAt = log.loggedAt
                    if (logLoggedAt != null) {
                        try {
                            val logTime = Instant.parse(logLoggedAt).toLocalDateTime(TimeZone.currentSystemDefault())
                            if (logTime.date == now.date) {
                                hourlyUsage[logTime.hour] += log.usageKwh.toFloat()
                            }
                        } catch (e: Exception) {}
                    }
                }
                
                // Show up to current hour
                val currentHour = now.hour
                dailyUsagePoints.clear()
                for (i in 0..currentHour) {
                    dailyUsagePoints.add(hourlyUsage[i])
                }

            } catch (e: Exception) {
                usage.value = "Today's Usage: --"
            }
        }
    }
}
