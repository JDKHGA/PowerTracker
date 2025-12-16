package com.example.powertracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.powertracker.navigation.BottomNavBar
import com.example.powertracker.navigation.BottomNavItem
import com.example.powertracker.screens.*
import com.example.powertracker.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AppTheme {
        Scaffold(
            bottomBar = {
                // Show bottom bar only on main screens
                if (currentRoute in listOf("home", "meters", "insights", "settings")) {
                    BottomNavBar(navController = navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier.padding(innerPadding) // Apply padding here
            ) {
                composable("home") { HomeScreen(navController) }
                composable("meters") { MetersScreen(navController) }
                composable("addMeter") { AddMeterScreen(navController) }
                composable("addToken") { AddTokenScreen(navController) }
                composable("insights") { InsightsScreen(navController) }
                composable("settings") { SettingsScreen(navController) }
                composable("history") { TokenHistoryScreen(navController) }
            }
        }
    }
}