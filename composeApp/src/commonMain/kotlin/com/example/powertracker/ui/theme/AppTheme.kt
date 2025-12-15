package com.example.powertracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3F51B5), // Indigo
    secondary = Color(0xFFC5CAE9), // Light Indigo
    tertiary = Color(0xFF4CAF50) // Green
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3F51B5), // Indigo
    secondary = Color(0xFF7986CB), // Medium Indigo
    tertiary = Color(0xFF66BB6A), // Medium Green

    // You can override other default colors here, for example:
    background = Color.White,
    surface = Color.White,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography(),
        content = {
            // Apply the background color to the root Surface
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background // Use the explicit background color
            ) {
                content()
            }
        }
    )
}
