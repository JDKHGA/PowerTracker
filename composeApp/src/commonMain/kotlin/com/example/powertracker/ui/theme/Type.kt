package com.example.powertracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import powertracker.composeapp.generated.resources.montserrat_bold
import powertracker.composeapp.generated.resources.montserrat_medium
import powertracker.composeapp.generated.resources.montserrat_regular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import powertracker.composeapp.generated.resources.Res
import powertracker.composeapp.generated.resources.montserrat_black
import powertracker.composeapp.generated.resources.montserrat_extrabold
import powertracker.composeapp.generated.resources.montserrat_light
import powertracker.composeapp.generated.resources.montserrat_semibold
import powertracker.composeapp.generated.resources.montserrat_thin

@OptIn(ExperimentalResourceApi::class)
@Composable
fun appTypography(): Typography {
    val montserratFontFamily = FontFamily(
        Font(Res.font.montserrat_thin, weight = FontWeight.Thin),
        Font(Res.font.montserrat_light, weight = FontWeight.Light),
        Font(Res.font.montserrat_regular, weight = FontWeight.Normal),
        Font(Res.font.montserrat_medium, weight = FontWeight.Medium),
        Font(Res.font.montserrat_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.montserrat_bold, weight = FontWeight.Bold),
        Font(Res.font.montserrat_extrabold, weight = FontWeight.ExtraBold),
        Font(Res.font.montserrat_black, weight = FontWeight.Black)
    )

    return Typography(
        displayLarge = TextStyle(fontFamily = montserratFontFamily, fontSize = 57.sp),
        displayMedium = TextStyle(fontFamily = montserratFontFamily, fontSize = 45.sp),
        displaySmall = TextStyle(fontFamily = montserratFontFamily, fontSize = 36.sp),
        headlineLarge = TextStyle(fontFamily = montserratFontFamily, fontSize = 32.sp),
        headlineMedium = TextStyle(fontFamily = montserratFontFamily, fontSize = 28.sp),
        headlineSmall = TextStyle(fontFamily = montserratFontFamily, fontSize = 24.sp),
        titleLarge = TextStyle(fontFamily = montserratFontFamily, fontSize = 22.sp),
        titleMedium = TextStyle(fontFamily = montserratFontFamily, fontSize = 16.sp),
        titleSmall = TextStyle(fontFamily = montserratFontFamily, fontSize = 14.sp),
        bodyLarge = TextStyle(fontFamily = montserratFontFamily, fontSize = 16.sp),
        bodyMedium = TextStyle(fontFamily = montserratFontFamily, fontSize = 14.sp),
        bodySmall = TextStyle(fontFamily = montserratFontFamily, fontSize = 12.sp),
        labelLarge = TextStyle(fontFamily = montserratFontFamily, fontSize = 14.sp),
        labelMedium = TextStyle(fontFamily = montserratFontFamily, fontSize = 12.sp),
        labelSmall = TextStyle(fontFamily = montserratFontFamily, fontSize = 11.sp),
    )
}
