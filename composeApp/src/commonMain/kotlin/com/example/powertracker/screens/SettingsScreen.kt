package com.example.powertracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.cards.settingsscreen.AccountCard
import com.example.powertracker.cards.settingsscreen.ActionsCard
import com.example.powertracker.cards.settingsscreen.AlertThresholdCard
import com.example.powertracker.cards.settingsscreen.AppInfoCard
import com.example.powertracker.cards.settingsscreen.NotificationCard
import com.example.powertracker.cards.settingsscreen.ThemeCard
import com.example.powertracker.elements.TopBar.TopBar.TopBar
import com.example.powertracker.viewmodel.SettingsScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SettingsScreen(navController: NavController? = null) {
    val viewModel: SettingsScreenViewModel = viewModel { SettingsScreenViewModel() }

    Scaffold(
        topBar = {
            TopBar(
                text = "Settings",
                onBack = {
                    navController?.popBackStack()
                },
                icon = Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AccountCard(email = viewModel.userEmail.value)
            }
            item {
                NotificationCard(
                    isChecked = viewModel.notificationsEnabled.value,
                    onCheckedChange = { viewModel.notificationsEnabled.value = it }
                )
            }
            item {
                AlertThresholdCard(
                    sliderPosition = viewModel.alertThreshold.value,
                    onSliderChange = { viewModel.alertThreshold.value = it }
                )
            }
            item {
                ThemeCard(
                    title = "Dark Mode",
                    subtitle = "Use dark theme",
                    icon = Icons.Outlined.DarkMode,
                    isChecked = viewModel.darkModeEnabled.value,
                    onCheckedChange = { viewModel.darkModeEnabled.value = it }
                )
            }
            item {
                ThemeCard(
                    title = "Backup & Sync",
                    subtitle = "Sync data with Google Drive",
                    icon = Icons.Outlined.CloudSync,
                    isChecked = viewModel.backupEnabled.value,
                    onCheckedChange = { viewModel.backupEnabled.value = it }
                )
            }
            item {
                AppInfoCard(
                    version = viewModel.appVersion,
                    build = viewModel.appBuild,
                    developer = viewModel.developer
                )
            }
            item {
                ActionsCard(
                    onLogout = {
                        viewModel.logout {
                            navController?.navigate("login") {
                                popUpTo(0)
                            }
                        }
                    }
                )
            }
        }
    }
}
