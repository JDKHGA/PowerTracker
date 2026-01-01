package com.example.powertracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.writeToURL
import platform.Foundation.NSUTF8StringEncoding
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
                // Create a temporary file URL
                val tempDir = NSTemporaryDirectory()
                val fileName = "powertracker_data.csv"
                val fileURL = NSURL.fileURLWithPath(tempDir + fileName)
                
                // Write the CSV data to the file
                (data as platform.Foundation.NSString).writeToURL(
                    url = fileURL,
                    atomically = true,
                    encoding = NSUTF8StringEncoding,
                    error = null
                )

                val activityViewController = UIActivityViewController(
                    activityItems = listOf(fileURL),
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
