package com.example.powertracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class InsightsScreenViewModel: ViewModel() {

    var avgSpending = mutableStateOf("GHS 330")
        private set

    var avgDailyUsage = mutableStateOf("2.3 kWh")
        private set

    var peakUsageTime = mutableStateOf("6:00 PM - 9:00 PM")
        private set

    var weekendVsWeekday = mutableStateOf("+12% higher")
        private set

    var aiForecast = mutableStateOf(
        "Your credit will likely run out on Dec 20, 2025 based on current consumption patterns."
    )
        private set

    val recommendations = mutableStateListOf(
        "Consider reducing usage during peak hours (6–9 PM) to save on electricity costs",
        "Your weekend usage is 12% higher. Unplugging devices when not in use can help reduce consumption",
        "Purchase tokens early to avoid running out of credit during critical times"
    )
}

