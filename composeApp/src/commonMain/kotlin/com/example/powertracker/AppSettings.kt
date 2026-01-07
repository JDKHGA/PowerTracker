package com.example.powertracker

import androidx.compose.runtime.mutableStateOf
import com.russhwolf.settings.Settings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object AppSettings {
    private val settings: Settings = Settings()

    // Global reactive state for the theme
    val isDarkMode = mutableStateOf(settings.getBoolean("dark_mode_enabled", false))
    
    val lastSyncTime = mutableStateOf(settings.getStringOrNull("last_sync_time"))

    fun setDarkMode(enabled: Boolean) {
        isDarkMode.value = enabled
        settings.putBoolean("dark_mode_enabled", enabled)
    }

    fun updateSyncTime() {
        val now = Clock.System.now()
        val timeString = formatSyncTime(now)
        lastSyncTime.value = timeString
        settings.putString("last_sync_time", timeString)
    }

    private fun formatSyncTime(instant: Instant): String {
        val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${dt.hour.toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}"
    }
}
