package com.example.powertracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.AppSettings
import com.example.powertracker.ShareData
import com.example.powertracker.cards.settingsscreen.*
import com.example.powertracker.elements.TopBar.TopBar.TopBar
import com.example.powertracker.elements.dialogs.ConfirmationDialog
import com.example.powertracker.getNotificationService
import com.example.powertracker.viewmodel.SettingsScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SettingsScreen(navController: NavController? = null, onLogout: () -> Unit = {}) {
    val viewModel: SettingsScreenViewModel = viewModel { SettingsScreenViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    val notificationService = getNotificationService()

    LaunchedEffect(viewModel.errorMessage.value) {
        viewModel.errorMessage.value?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.errorMessage.value = null
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                text = "Settings",
                onBack = {
                    navController?.popBackStack()
                },
                icon = Icons.AutoMirrored.Filled.ArrowBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AccountCard(email = viewModel.userEmail.value)
                }
                item {
                    NotificationCard(
                        isChecked = viewModel.notificationsEnabled.value,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                notificationService.requestPermission { granted ->
                                    if (granted) {
                                        viewModel.toggleNotifications(true)
                                        notificationService.getPushToken { token ->
                                            viewModel.updateDeviceToken(token)
                                        }
                                    } else {
                                        viewModel.errorMessage.value = "Notification permission denied"
                                    }
                                }
                            } else {
                                viewModel.toggleNotifications(false)
                            }
                        }
                    )
                }
                item {
                    AlertThresholdCard(
                        sliderPosition = viewModel.alertThreshold.value,
                        onSliderChange = { viewModel.updateAlertThreshold(it) }
                    )
                }
                item {
                    ThemeCard(
                        title = "Dark Mode",
                        subtitle = "Use dark theme",
                        icon = Icons.Outlined.DarkMode,
                        isChecked = viewModel.darkModeEnabled.value,
                        onCheckedChange = { viewModel.toggleDarkMode(it) }
                    )
                }
                item {
                    ThemeCard(
                        title = "Backup & Sync",
                        subtitle = viewModel.lastSyncTime.value?.let { "Last synced: $it" } ?: "Sync data with Supabase Cloud",
                        icon = Icons.Outlined.CloudSync,
                        isChecked = viewModel.backupEnabled.value,
                        onCheckedChange = { viewModel.toggleBackup(it) }
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
                        onLogoutRequest = { viewModel.showLogoutDialog.value = true },
                        onClearDataRequest = { viewModel.showClearDataDialog.value = true },
                        onExportData = { viewModel.exportData() },
                        onPrivacyPolicyRequest = { viewModel.showPrivacyPolicy.value = true },
                        onTermsOfServiceRequest = { viewModel.showTermsOfService.value = true }
                    )
                }
            }

            if (viewModel.isExporting.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        // --- Data Export Logic ---
        viewModel.exportedData.value?.let { data ->
            ShareData(data = data) {
                viewModel.exportedData.value = null
            }
        }

        // --- Dialogs ---

        if (viewModel.showLogoutDialog.value) {
            ConfirmationDialog(
                title = "Confirm Logout",
                message = "Are you sure you want to sign out of your account?",
                confirmText = "Logout",
                onConfirm = {
                    viewModel.showLogoutDialog.value = false
                    viewModel.logout {
                        onLogout()
                    }
                },
                onDismiss = { viewModel.showLogoutDialog.value = false }
            )
        }

        if (viewModel.showClearDataDialog.value) {
            ConfirmationDialog(
                title = "Clear All Data",
                message = "This will permanently delete all your usage logs and token history. This action cannot be undone.",
                confirmText = "Delete Everything",
                confirmColor = Color.Red,
                onConfirm = {
                    viewModel.clearAllData()
                },
                onDismiss = { viewModel.showClearDataDialog.value = false }
            )
        }

        if (viewModel.showPrivacyPolicy.value) {
            LegalDialog(
                title = "Privacy Policy",
                content = privacyPolicyText,
                onDismiss = { viewModel.showPrivacyPolicy.value = false }
            )
        }

        if (viewModel.showTermsOfService.value) {
            LegalDialog(
                title = "Terms of Service",
                content = termsOfServiceText,
                onDismiss = { viewModel.showTermsOfService.value = false }
            )
        }
    }
}

@Composable
fun LegalDialog(title: String, content: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(content, fontSize = 14.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

private val privacyPolicyText = """
    Last Updated: December 2024
    
    1. Information We Collect
    PowerTracker stores your meter numbers, token purchases, and power usage logs to provide tracking services.
    
    2. Data Security
    Your data is stored securely using Supabase. We do not share your personal energy usage data with third parties.
    
    3. Your Rights
    You can export or delete all your data at any time from the Settings menu.
    
    4. Contact
    For any questions, please contact the development team.
""".trimIndent()

private val termsOfServiceText = """
    Last Updated: January 2025
    
    1. Use of Service
    PowerTracker is provided "as is" for monitoring electricity usage.
    
    2. Accuracy
    While we strive for accuracy, balances and usage logs are estimates based on your input. Always refer to your official utility provider for billing.
    
    3. Account Responsibility
    You are responsible for maintaining the security of your login credentials.
    
    4. Changes to Terms
    We reserve the right to modify these terms at any time.
""".trimIndent()
