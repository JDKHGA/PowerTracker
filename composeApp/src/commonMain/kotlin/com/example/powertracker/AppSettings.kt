package com.example.powertracker

import androidx.compose.runtime.mutableStateOf
import com.russhwolf.settings.Settings

object AppSettings {
    private val settings: Settings = Settings()

    // Global reactive state for the theme
    val isDarkMode = mutableStateOf(settings.getBoolean("dark_mode_enabled", false))

    fun setDarkMode(enabled: Boolean) {
        isDarkMode.value = enabled
        settings.putBoolean("dark_mode_enabled", enabled)
    }
}
