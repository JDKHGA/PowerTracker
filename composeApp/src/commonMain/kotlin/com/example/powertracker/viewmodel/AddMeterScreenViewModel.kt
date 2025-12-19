package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.auth.supabase
import com.example.powertracker.models.Meter
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class AddMeterScreenViewModel : ViewModel() {
    val meterName = mutableStateOf("")
    val meterNumber = mutableStateOf("")

    val meterTypes = listOf("Prepaid", "Postpaid")
    val selectedMeterType = mutableStateOf(meterTypes.first())

    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    fun saveMeter(onSuccess: () -> Unit) {
        val name = meterName.value
        val number = meterNumber.value
        val type = selectedMeterType.value

        if (name.isBlank() || number.isBlank()) {
            error.value = "Please fill in all fields"
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                val userId = supabase.auth.currentUserOrNull()?.id
                if (userId == null) {
                    error.value = "User not logged in"
                    return@launch
                }

                val meter = Meter(
                    userId = userId,
                    name = name,
                    meterNumber = number,
                    type = type
                )

                supabase.postgrest.from("meters").insert(meter)
                onSuccess()
            } catch (e: Exception) {
                error.value = "Failed to save meter: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}