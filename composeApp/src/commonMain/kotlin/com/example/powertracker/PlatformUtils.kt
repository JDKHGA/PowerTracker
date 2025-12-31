package com.example.powertracker

import androidx.compose.runtime.Composable

@Composable
expect fun ShareData(data: String, onFinished: () -> Unit)
