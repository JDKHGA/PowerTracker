package com.example.powertracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.popoverPresentationController

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ShareData(data: String, onFinished: () -> Unit) {
    LaunchedEffect(data) {
        if (data.isNotEmpty()) {
            val window = UIApplication.sharedApplication.keyWindow
            val viewController = window?.rootViewController
            
            if (viewController != null) {
                val activityViewController = UIActivityViewController(
                    activityItems = listOf(data),
                    applicationActivities = null
                )
                
                activityViewController.popoverPresentationController?.let {
                    it.sourceView = viewController.view
                    it.sourceRect = viewController.view.bounds
                }
                
                viewController.presentViewController(
                    viewControllerToPresent = activityViewController,
                    animated = true,
                    completion = onFinished
                )
            } else {
                onFinished()
            }
        }
    }
}
