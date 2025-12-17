package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class AddMeterScreenViewModel: ViewModel(){
    val meterName = mutableStateOf("")
    val meterNumber  = mutableStateOf("")

    val meterTypes = listOf("Prepaid", "Postpaid")
    val selectedMeterType = mutableStateOf(meterTypes.first())
}



