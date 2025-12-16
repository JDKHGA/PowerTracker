package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf

class TokenScreenViewModel {

    val amount = mutableStateOf("GHS 100.00")
    val date = mutableStateOf("8 Dec 2025")
    val units = mutableStateOf("30.0 kWh")
    val tokenCode = mutableStateOf("1234-5678-9012-3456")
    val duration = mutableStateOf("Estimated: Lasted 7 days")

}