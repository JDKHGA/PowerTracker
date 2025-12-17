package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf

class HomeViewModel {

    val balanceKwh = mutableStateOf("12.6 kWh")
    val balanceGhs = mutableStateOf("GHS 42")
    val daysLeft = mutableStateOf("Estimated 5 days left")

    val usage = mutableStateOf("Today's Usage: 2.3 kWh")
    val prediction = mutableStateOf("Based on your usage, credit will run out on Thursday afternoon")

    val selectedMeter = mutableStateOf("Home Meter")
    val meterDropdownExpanded = mutableStateOf(false)

    fun selectMeter(meter: String) {
        selectedMeter.value = meter
        meterDropdownExpanded.value = false
    }


}