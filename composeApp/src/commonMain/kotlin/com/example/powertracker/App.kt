package com.example.powertracker

import androidx.compose.runtime.Composable
import com.example.powertracker.navigation.AppNavHost
import com.example.powertracker.screens.HomeScreen
import com.example.powertracker.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    AppTheme {
        AppNavHost()
    }
}
