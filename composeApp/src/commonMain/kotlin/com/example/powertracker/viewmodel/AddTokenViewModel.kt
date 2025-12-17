package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddTokenViewModel: ViewModel() {
    val tokenCode = mutableStateOf("")
    val amount = mutableStateOf("")
    val units = mutableStateOf("")
    val purchaseDate = mutableStateOf("")
}



