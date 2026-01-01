package com.example.powertracker

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
actual fun ShareData(data: String, onFinished: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(data) {
        if (data.isNotEmpty()) {
            try {
                // Create a temporary file in the cache directory
                val cachePath = File(context.cacheDir, "exports")
                cachePath.mkdirs()
                val file = File(cachePath, "powertracker_data.csv")
                file.writeText(data)

                // Get the content URI for the file
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
