package com.example.powertracker.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold

import com.example.powertracker.navigation.BottomNavBar
import com.example.powertracker.navigation.BottomNavItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.powertracker.screens.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController)
            }
            composable(BottomNavItem.Meters.route) {
                MetersScreen(navController)
            }
            composable(BottomNavItem.History.route) {
                TokenHistoryScreen()
            }
            composable(BottomNavItem.Insights.route) {
                InsightsScreen()
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen()
            }
            composable("addToken") {
                AddTokenScreen(navController)
            }
            composable("addMeter") {
                AddMeterScreen()
            }

        }
    }
}
