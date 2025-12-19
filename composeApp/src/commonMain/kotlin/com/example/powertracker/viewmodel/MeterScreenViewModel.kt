package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.auth.supabase
import com.example.powertracker.models.Meter
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class MeterScreenViewModel : ViewModel() {
    val meters = mutableStateListOf<Meter>()
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    init {
        loadMeters()
    }

    fun loadMeters() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                val userId = supabase.auth.currentUserOrNull()?.id
                if (userId != null) {
                    val result = supabase.postgrest.from("meters")
                        .select {
                            filter {
                                eq("user_id", userId)
                            }
                        }.decodeList<Meter>()
                    meters.clear()
                    meters.addAll(result)
                } else {
                    error.value = "User not logged in"
                }
            } catch (e: Exception) {
                error.value = "Failed to load meters: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
