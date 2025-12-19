package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powertracker.auth.supabase
import com.example.powertracker.models.Token
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class TokenScreenViewModel : ViewModel() {
    val tokens = mutableStateListOf<Token>()
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    init {
        loadTokens()
    }

    fun loadTokens() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                val result = supabase.postgrest.from("tokens")
                    .select {
                        order("purchase_date", Order.DESCENDING)
                    }
                    .decodeList<Token>()
                
                tokens.clear()
                tokens.addAll(result)
            } catch (e: Exception) {
                error.value = "Failed to load tokens: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
