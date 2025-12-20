package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.auth.supabase
import com.example.powertracker.models.Meter
import com.example.powertracker.models.Token
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class AddTokenViewModel : ViewModel() {
    val tokenCode = mutableStateOf("")
    val amount = mutableStateOf("")
    val units = mutableStateOf("")
    val purchaseDate = mutableStateOf("")

    val meters = mutableStateOf<List<Meter>>(emptyList())
    val selectedMeter = mutableStateOf<Meter?>(null)
    
    val isLoading = mutableStateOf(false)
    val isFetchingMeters = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    init {
        fetchMeters()
    }

    fun fetchMeters() {
        viewModelScope.launch {
            isFetchingMeters.value = true
            error.value = null
            try {
                val userId = supabase.auth.currentUserOrNull()?.id
                if (userId != null) {
                    val result = supabase.postgrest.from("meters")
                        .select {
                            filter {
                                eq("user_id", userId)
                            }
                        }
                        .decodeList<Meter>()
                    meters.value = result
                    if (result.isNotEmpty()) {
                        selectedMeter.value = result.first()
                    }
                }
            } catch (e: Exception) {
                error.value = "Failed to load meters: ${e.message}"
            } finally {
                isFetchingMeters.value = false
            }
        }
    }

    fun saveToken(onSuccess: () -> Unit) {
        val code = tokenCode.value
        val amt = amount.value.toDoubleOrNull()
        val unts = units.value.toDoubleOrNull()
        val date = purchaseDate.value
        val currentMeter = selectedMeter.value

        if (code.isBlank() || amt == null || unts == null || date.isBlank() || currentMeter == null) {
            error.value = "Please fill in all fields correctly"
            return
        }

        val meterId = currentMeter.id ?: run {
            error.value = "Selected meter has no ID"
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                // 1. Fetch latest meter data to ensure we have the correct current balance
                val latestMeter = supabase.postgrest.from("meters")
                    .select {
                        filter { eq("id", meterId) }
                    }.decodeSingle<Meter>()

                // 2. Insert the token record
                val token = Token(
                    meterId = meterId,
                    tokenCode = code,
                    amount = amt,
                    units = unts,
                    purchaseDate = date
                )
                supabase.postgrest.from("tokens").insert(token)

                // 3. Calculate new balance (Addition)
                val newBalanceKwh = latestMeter.balanceKwh + unts
                val newBalanceGhs = latestMeter.balanceGhs + amt

                // 4. Update the meter record
                supabase.postgrest.from("meters").update(
                    {
                        set("balance_kwh", newBalanceKwh)
                        set("balance_ghs", newBalanceGhs)
                    }
                ) {
                    filter { eq("id", meterId) }
                }

                onSuccess()
            } catch (e: Exception) {
                error.value = "Failed to save token and update balance: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
