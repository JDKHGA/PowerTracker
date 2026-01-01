package com.example.powertracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import java.io.File

@Composable
actual fun ShareData(data: String, onFinished: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(data) {
        if (data.isNotEmpty()) {
            try {
                val cachePath = File(context.cacheDir, "exports")
                cachePath.mkdirs()
                val file = File(cachePath, "powertracker_data.csv")
                file.writeText(data)

                val contentUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, contentUri)
                    type = "text/csv"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                val shareIntent = Intent.createChooser(sendIntent, "Export PowerTracker Data")
                context.startActivity(shareIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                onFinished()
            }
        }
    }
}

actual fun getPlatformName(): String = "Android"

class AndroidNotificationService(private val context: Context) : NotificationService {
    private val channelId = "power_tracker_alerts"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PowerTracker Alerts"
            val descriptionText = "Alerts for low meter balance"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }

    override fun requestPermission(onGranted: (Boolean) -> Unit) {
        // On Android 13+, permissions are requested at runtime.
        // For simplicity, we assume granted or handled by the system for now.
        onGranted(true)
    }
}

private var notificationService: NotificationService? = null

@Composable
actual fun getNotificationService(): NotificationService {
    val context = LocalContext.current
    if (notificationService == null) {
        notificationService = AndroidNotificationService(context.applicationContext)
    }
    return notificationService!!
}
