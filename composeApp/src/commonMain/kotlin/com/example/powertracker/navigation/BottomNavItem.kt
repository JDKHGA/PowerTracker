package com.example.powertracker.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.ElectricMeter
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.powertracker.navigation.BottomNavItem.Home.label

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        label = "Home",
        icon = Icons.Outlined.Home
    )

    object Meters : BottomNavItem(
        route = "meters",
        label = "Meters",
        icon = Icons.Outlined.ElectricMeter

    )

    object History : BottomNavItem(
        route = "history",
        label = "History",
        icon = Icons.Outlined.History

    )

    object Insights : BottomNavItem(
        route = "insights",
        label = "Insights",
        icon = Icons.Outlined.ShowChart
    )

    object Settings : BottomNavItem(
        route = "settings",
        label = "Settings",
        icon = Icons.Outlined.Settings
    )

}