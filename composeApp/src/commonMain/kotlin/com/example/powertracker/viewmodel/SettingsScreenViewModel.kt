package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.auth.supabase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class SettingsScreenViewModel : ViewModel() {

    // User Information
    val userEmail = mutableStateOf("Loading...")

    // State for the notification toggle
    val notificationsEnabled = mutableStateOf(false)

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

    init {
        loadUserEmail()
    }

    private fun loadUserEmail() {
        val user = supabase.auth.currentUserOrNull()
        userEmail.value = user?.email ?: "Not logged in"
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
