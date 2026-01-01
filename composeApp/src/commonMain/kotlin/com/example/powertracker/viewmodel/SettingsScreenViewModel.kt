package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.AppSettings
import com.example.powertracker.auth.supabase
import com.example.powertracker.models.Meter
import com.example.powertracker.models.Token
import com.example.powertracker.models.UsageLog
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class SettingsScreenViewModel : ViewModel() {

    private val settings: Settings = Settings()

    // User Information
    val userEmail = mutableStateOf("Loading...")

    // Persistent States
    val notificationsEnabled = mutableStateOf(settings.getBoolean("notifications_enabled", false))
    val alertThreshold = mutableStateOf(settings.getFloat("alert_threshold", 10f))
    
    // Linked to Global AppSettings
    val darkModeEnabled = AppSettings.isDarkMode

    // Dialog & UI States
    val showLogoutDialog = mutableStateOf(false)
    val showClearDataDialog = mutableStateOf(false)
    val showPrivacyPolicy = mutableStateOf(false)
    val showTermsOfService = mutableStateOf(false)

    val exportedData = mutableStateOf<String?>(null)
    val isExporting = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    
    val backupEnabled = mutableStateOf(settings.getBoolean("backup_enabled", false))

    // Static app information
    val appVersion = "1.0.0"
    val appBuild = "2024.12"
    val developer = "JDKorp"

    init {
        loadUserEmail()
    }

    private fun loadUserEmail() {
        val user = supabase.auth.currentUserOrNull()
        userEmail.value = user?.email ?: "Not logged in"
    }

    fun toggleNotifications(enabled: Boolean) {
        notificationsEnabled.value = enabled
        settings["notifications_enabled"] = enabled
    }

    fun updateAlertThreshold(value: Float) {
        alertThreshold.value = value
        settings["alert_threshold"] = value
    }

    fun toggleDarkMode(enabled: Boolean) {
        AppSettings.setDarkMode(enabled)
    }

    fun toggleBackup(enabled: Boolean) {
        backupEnabled.value = enabled
        settings["backup_enabled"] = enabled
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    val meters = supabase.postgrest.from("meters")
                        .select { filter { eq("user_id", user.id) } }
                        .decodeList<Meter>()
                    
                    val meterIds = meters.mapNotNull { it.id }
                    
                    if (meterIds.isNotEmpty()) {
                        supabase.postgrest.from("tokens").delete { filter { isIn("meter_id", meterIds) } }
                        supabase.postgrest.from("usage_logs").delete { filter { isIn("meter_id", meterIds) } }
                        supabase.postgrest.from("meters").delete { filter { eq("user_id", user.id) } }
                    }
                    showClearDataDialog.value = false
                }
            } catch (e: Exception) {
                errorMessage.value = "Failed to clear data: ${e.message}"
            }
        }
    }

    private fun formatIsoDate(isoDate: String?): String {
        if (isoDate == null) return ""
        // Formats "2025-12-22T11:33:49.053Z" -> "2025-12-22 11:33"
        return isoDate.substringBefore(".").replace("T", " ")
    }

    fun exportData() {
        if (isExporting.value) return
        isExporting.value = true
        errorMessage.value = null
        
        viewModelScope.launch {
            try {
                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    val meters = supabase.postgrest.from("meters")
                        .select { filter { eq("user_id", user.id) } }
                        .decodeList<Meter>()
                    
                    val meterMap = meters.associate { it.id to it.name }
                    val meterIds = meters.mapNotNull { it.id }
                    
                    val tokens = if (meterIds.isNotEmpty()) {
                        supabase.postgrest.from("tokens")
                            .select { filter { isIn("meter_id", meterIds) } }
                            .decodeList<Token>()
                    } else emptyList()
                    
                    val usageLogs = if (meterIds.isNotEmpty()) {
                        supabase.postgrest.from("usage_logs")
                            .select { filter { isIn("meter_id", meterIds) } }
                            .decodeList<UsageLog>()
                    } else emptyList()
                    
                    // Create a list of all activities
                    val activities = mutableListOf<ExportRow>()
                    
                    tokens.forEach {
                        activities.add(ExportRow(
                            meter = meterMap[it.meterId] ?: "Unknown",
                            date = it.purchaseDate,
                            type = "Token Purchase",
                            value = "${it.amount} GHS",
                            units = "${it.units} kWh",
                            details = "Token: ${it.tokenCode}"
                        ))
                    }
                    
                    usageLogs.forEach {
                        activities.add(ExportRow(
                            meter = meterMap[it.meterId] ?: "Unknown",
                            date = formatIsoDate(it.loggedAt),
                            type = "Power Usage",
                            value = "",
                            units = "${it.usageKwh} kWh",
                            details = ""
                        ))
                    }
                    
                    // Sort by date descending
                    val sortedActivities = activities.sortedByDescending { it.date }
                    
                    // Build CSV
                    val csvResult = buildString {
                        append("Meter,Date,Activity,Value,Units,Details\n")
                        sortedActivities.forEach {
                            append("${it.meter},${it.date},${it.type},${it.value},${it.units},\"${it.details}\"\n")
                        }
                    }
                    
                    exportedData.value = csvResult
                } else {
                    errorMessage.value = "User not logged in"
                }
            } catch (e: Exception) {
                errorMessage.value = "Export failed: ${e.message}"
            } finally {
                isExporting.value = false
            }
        }
    }

    private data class ExportRow(
        val meter: String,
        val date: String,
        val type: String,
        val value: String,
        val units: String,
        val details: String
    )

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                onLogoutSuccess()
            } catch (e: Exception) {
                errorMessage.value = "Logout failed: ${e.message}"
            }
        }
    }
}
