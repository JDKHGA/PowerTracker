package com.example.powertracker

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun ShareData(data: String, onFinished: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(data) {
        if (data.isNotEmpty()) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, data)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Export PowerTracker Data")
            context.startActivity(shareIntent)
            onFinished()
        }
    }
}
