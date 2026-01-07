package com.example.powertracker

import android.app.Application
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

class PowerTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotifierManager.initialize(
            NotificationPlatformConfiguration.Android(
                notificationIconResId = android.R.drawable.ic_dialog_info, // Replace with your app icon
                showBadge = true,
                notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
                    id = "power_tracker_alerts",
                    name = "PowerTracker Alerts",
                    description = "Alerts for low meter balance"
                )
            )
        )
    }
}
