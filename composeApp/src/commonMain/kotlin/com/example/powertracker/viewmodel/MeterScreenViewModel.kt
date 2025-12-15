package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf

class MeterScreenViewModel {

    val meterName = mutableStateOf("Home Meter")
    val meterNumber = mutableStateOf("04-0123-456789-00")
    val meterTypes = mutableStateOf("Prepaid")
    val lastUpdated = mutableStateOf("Updated 10 minutes ago")



}


