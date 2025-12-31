package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.AppSettings
import com.example.powertracker.auth.supabase
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.github.jan.supabase.auth.auth
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
