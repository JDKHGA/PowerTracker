package com.example.powertracker.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.powertracker.auth.supabase
import io.github.jan.supabase.auth.auth

import com.example.powertracker.screens.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    
    // Check if user is already logged in
    val session = supabase.auth.currentSessionOrNull()
    val startDestination = if (session != null) BottomNavItem.Home.route else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegistrationScreen(navController)
        }
        composable("main") {
            MainScaffold(navController)
        }
        // Redirect home to main scaffold
        composable(BottomNavItem.Home.route) {
            MainScaffold(navController)
        }
    }
}

@Composable
fun MainScaffold(rootNavController: androidx.navigation.NavHostController) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = 70.dp
            )
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
                AddMeterScreen(navController)
            }
        }
    }
}
