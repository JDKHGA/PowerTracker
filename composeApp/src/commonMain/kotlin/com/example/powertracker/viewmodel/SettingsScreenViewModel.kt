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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SettingsScreenViewModel : ViewModel() {

    private val settings: Settings = Settings()
    private val json = Json { prettyPrint = true }

    // User Information
    val userEmail = mutableStateOf("Loading...")

    // Persistent States
    val notificationsEnabled = mutableStateOf(settings.getBoolean("notifications_enabled", false))
    val alertThreshold = mutableStateOf(settings.getFloat("alert_threshold", 10f))
    
    // Linked to Global AppSettings
    val darkModeEnabled = AppSettings.isDarkMode

    // Dialog States
    val showLogoutDialog = mutableStateOf(false)
    val showClearDataDialog = mutableStateOf(false)
    val exportedData = mutableStateOf<String?>(null)
    
    val backupEnabled = mutableStateOf(settings.getBoolean("backup_enabled", false))

    // Static app information
    val appVersion = "1.0.0"
    val appBuild = "2024.12"
    val developer = "ECG Tracker Team"

    init {
        loadUserEmail()
    }

    private fun loadUserEmail() {
        val user = supabase.auth.currentUserOrNull()
        userEmail.value = user?.email ?: "Not logged in"
    }

    // Toggle Handlers with Persistence
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
                    // 1. Get all meters for the user to delete their related data
                    val meters = supabase.postgrest.from("meters")
                        .select { filter { eq("user_id", user.id) } }
                        .decodeList<Meter>()
                    
                    val meterIds = meters.mapNotNull { it.id }
                    
                    if (meterIds.isNotEmpty()) {
                        // 2. Delete tokens related to these meters
                        supabase.postgrest.from("tokens").delete {
                            filter {
                                isIn("meter_id", meterIds)
                            }
                        }
                        
                        // 3. Delete usage logs related to these meters
                        supabase.postgrest.from("usage_logs").delete {
                            filter {
                                isIn("meter_id", meterIds)
                            }
                        }
                        
                        // 4. Delete the meters themselves
                        supabase.postgrest.from("meters").delete {
                            filter {
                                eq("user_id", user.id)
                            }
                        }
                    }
                    
                    showClearDataDialog.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun exportData() {
        viewModelScope.launch {
            try {
                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    val meters = supabase.postgrest.from("meters")
                        .select { filter { eq("user_id", user.id) } }
                        .decodeList<Meter>()
                    
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
                    
                    val exportMap = mapOf(
                        "meters" to meters,
                        "tokens" to tokens,
                        "usage_logs" to usageLogs
                    )
                    
                    exportedData.value = json.encodeToString(exportMap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                onLogoutSuccess()
            } catch (e: Exception) {
                // Handle logout error
            }
        }
    }
}
