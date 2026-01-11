package com.example.powertracker

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual fun getCurrentEpochMillis(): Long = System.currentTimeMillis()