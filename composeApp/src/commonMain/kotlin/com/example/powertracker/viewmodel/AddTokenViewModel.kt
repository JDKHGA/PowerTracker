package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf

class AddTokenViewModel {
    val tokenCode = mutableStateOf("")
    val amount = mutableStateOf("")
    val units = mutableStateOf("")
    val purchaseDate = mutableStateOf("")
}



