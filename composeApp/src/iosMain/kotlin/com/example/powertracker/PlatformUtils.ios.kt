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
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ShareData(data: String, onFinished: () -> Unit) {
    LaunchedEffect(data) {
        if (data.isNotEmpty()) {
            val window = UIApplication.sharedApplication.keyWindow
            val viewController = window?.rootViewController
            
            if (viewController != null) {
                val tempDir = NSTemporaryDirectory()
                val fileName = "powertracker_data.csv"
                val fileURL = NSURL.fileURLWithPath(tempDir + fileName)
                
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

actual fun getPlatformName(): String = "iOS"

class IosNotificationService : NSObject(), NotificationService, UNUserNotificationCenterDelegateProtocol {
    
    init {
        UNUserNotificationCenter.currentNotificationCenter().delegate = this
    }

    override fun showNotification(title: String, message: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
            setSound(null)
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(1.0, false)
        val request = UNNotificationRequest.requestWithIdentifier("test_alert", content, trigger)

        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) { error ->
            if (error != null) {
                println("Error showing notification: ${error.localizedDescription}")
            }
        }
    }

    override fun requestPermission(onGranted: (Boolean) -> Unit) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        val options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        center.requestAuthorizationWithOptions(options) { granted, error ->
            onGranted(granted)
        }
    }

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: platform.UserNotifications.UNNotification,
        withCompletionHandler: (platform.UserNotifications.UNNotificationPresentationOptions) -> Unit
    ) {
        withCompletionHandler(UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound)
    }
}

private val notificationService = IosNotificationService()

@Composable
actual fun getNotificationService(): NotificationService = notificationService
