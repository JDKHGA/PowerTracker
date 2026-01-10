package com.example.powertracker.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Theme Colors (Indigo)
val Indigo = Color(0xFF3F51B5)
val IndigoDark = Color(0xFF303F9F)
val IndigoLight = Color(0xFFC5CAE9)

// Accent Color
val ElectricBlue = Color(0xFF00B0FF)

// Neutral Colors
val OffWhite = Color(0xFFF8F8F8)
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)

val IndigoGradient = Brush.verticalGradient(
    colors = listOf(
        Indigo,
        IndigoDark
    )
)
