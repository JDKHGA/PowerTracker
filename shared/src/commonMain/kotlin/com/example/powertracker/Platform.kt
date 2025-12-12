package com.example.powertracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform