package com.example.powertracker.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    val email: String,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class UserSettings(
    @SerialName("user_id")
    val userId: String,
    @SerialName("notifications_enabled")
    val notificationsEnabled: Boolean = false,
    @SerialName("alert_threshold")
    val alertThreshold: Float = 10f,
    @SerialName("backup_enabled")
    val backupEnabled: Boolean = false,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class DeviceToken(
    @SerialName("user_id")
    val userId: String,
    @SerialName("fcm_token")
    val fcmToken: String,
    val platform: String,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class Meter(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    val name: String,
    @SerialName("meter_number")
    val meterNumber: String,
    val type: String,
    @SerialName("balance_kwh")
    val balanceKwh: Double = 0.0,
    @SerialName("balance_ghs")
    val balanceGhs: Double = 0.0,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class Token(
    val id: String? = null,
    @SerialName("meter_id")
    val meterId: String,
    @SerialName("token_code")
    val tokenCode: String,
    val amount: Double,
    val units: Double,
    @SerialName("purchase_date")
    val purchaseDate: String,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class UsageLog(
    val id: String? = null,
    @SerialName("meter_id")
    val meterId: String,
    @SerialName("usage_kwh")
    val usageKwh: Double,
    @SerialName("logged_at")
    val loggedAt: String? = null
)
