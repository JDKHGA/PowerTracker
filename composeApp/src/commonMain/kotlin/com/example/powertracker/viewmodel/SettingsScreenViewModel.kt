package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf

class SettingsScreenViewModel {

    // State for the notification toggle
    val notificationsEnabled = mutableStateOf(true)

    // State for the alert threshold slider (0f to 50f range, starts at 10f)
    val alertThreshold = mutableStateOf(10f)

    // State for the dark mode toggle
    val darkModeEnabled = mutableStateOf(false)

    // State for the backup & sync toggle
    val backupEnabled = mutableStateOf(false)

    // Static app information
    val appVersion = "1.0.0"
    val appBuild = "2024.12"
    val developer = "ECG Tracker Team"

}