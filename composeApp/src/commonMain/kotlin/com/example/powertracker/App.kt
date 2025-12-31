package com.example.powertracker

import androidx.compose.runtime.Composable
import com.example.powertracker.navigation.AppNavHost
import com.example.powertracker.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    // Observe the global dark mode state
    val isDarkMode = AppSettings.isDarkMode.value

    AppTheme(darkTheme = isDarkMode) {
        AppNavHost()
    }
}
