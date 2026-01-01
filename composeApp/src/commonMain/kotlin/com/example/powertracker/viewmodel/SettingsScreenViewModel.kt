package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.AppSettings
import com.example.powertracker.auth.supabase
import com.example.powertracker.models.Meter
import com.example.powertracker.models.Token
import com.example.powertracker.models.UsageLog
import com.example.powertracker.models.UserSettings
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
    val backupEnabled = mutableStateOf(settings.getBoolean("backup_enabled", false))
    
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

    // Static app information
    val appVersion = "1.0.0"
    val appBuild = "2024.12"
    val developer = "JDKorp"

    init {
        loadUserEmail()
        syncWithSupabase()
    }

    private fun loadUserEmail() {
        val user = supabase.auth.currentUserOrNull()
        userEmail.value = user?.email ?: "Not logged in"
    }

    private fun syncWithSupabase() {
        viewModelScope.launch {
            try {
                val user = supabase.auth.currentUserOrNull() ?: return@launch
                val userSettings = supabase.postgrest.from("user_settings")
                    .select { filter { eq("user_id", user.id) } }
                    .decodeSingleOrNull<UserSettings>()

                if (userSettings != null) {
                    notificationsEnabled.value = userSettings.notificationsEnabled
                    alertThreshold.value = userSettings.alertThreshold
                    backupEnabled.value = userSettings.backupEnabled
                    
                    // Update local settings too
                    settings["notifications_enabled"] = userSettings.notificationsEnabled
                    settings["alert_threshold"] = userSettings.alertThreshold
                    settings["backup_enabled"] = userSettings.backupEnabled
                } else {
                    // Initialize Supabase with local settings if they don't exist there
                    saveSettingsToSupabase()
                }
            } catch (e: Exception) {
                // Silently fail or log, as this might be offline
            }
        }
    }

    private fun saveSettingsToSupabase() {
        viewModelScope.launch {
            try {
                val user = supabase.auth.currentUserOrNull() ?: return@launch
                val userSettings = UserSettings(
                    userId = user.id,
                    notificationsEnabled = notificationsEnabled.value,
                    alertThreshold = alertThreshold.value,
                    backupEnabled = backupEnabled.value
                )
                supabase.postgrest.from("user_settings").upsert(userSettings)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        notificationsEnabled.value = enabled
        settings["notifications_enabled"] = enabled
        saveSettingsToSupabase()
    }

    fun updateAlertThreshold(value: Float) {
        alertThreshold.value = value
        settings["alert_threshold"] = value
        saveSettingsToSupabase()
    }

    fun toggleDarkMode(enabled: Boolean) {
        AppSettings.setDarkMode(enabled)
    }

    fun toggleBackup(enabled: Boolean) {
        backupEnabled.value = enabled
        settings["backup_enabled"] = enabled
        saveSettingsToSupabase()
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
                    
                    val sortedActivities = activities.sortedByDescending { it.date }
                    
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
