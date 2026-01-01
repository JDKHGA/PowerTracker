package com.example.powertracker

import androidx.compose.runtime.Composable

@Composable
expect fun ShareData(data: String, onFinished: () -> Unit)

expect fun getPlatformName(): String

interface NotificationService {
    fun showNotification(title: String, message: String)
    fun requestPermission(onGranted: (Boolean) -> Unit)
}

@Composable
expect fun getNotificationService(): NotificationService
