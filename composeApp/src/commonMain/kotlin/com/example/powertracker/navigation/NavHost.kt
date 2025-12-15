package com.example.powertracker.navigation

import androidx.compose.material3.Scaffold

import com.example.powertracker.navigation.BottomNavBar
import com.example.powertracker.navigation.BottomNavItem
import androidx.compose.runtime.Composable
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
            startDestination = BottomNavItem.Home.route
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }
            composable(BottomNavItem.Meters.route) {
                MetersScreen()
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
        }
    }
}
