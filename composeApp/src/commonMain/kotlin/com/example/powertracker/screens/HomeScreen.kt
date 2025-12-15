package com.example.powertracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.powertracker.cards.BalanceCard
import com.example.powertracker.cards.PredictionCard
import com.example.powertracker.cards.UsageCard
import com.example.powertracker.row.QuickActionsRow
import com.example.powertracker.viewmodel.HomeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun HomeScreen(navController: NavController? = null) {

    // Create ViewModel (simple version, no DI yet)
    val viewModel = remember { HomeViewModel() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ECG Tracker") },
                actions = {
                    Box {
                        TextButton(
                            onClick = {
                                viewModel.meterDropdownExpanded.value = true
                            }
                        ) {
                            Text("Meter: ${viewModel.selectedMeter.value}")
                        }

                        DropdownMenu(
                            expanded = viewModel.meterDropdownExpanded.value,
                            onDismissRequest = {
                                viewModel.meterDropdownExpanded.value = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Home Meter") },
                                onClick = { viewModel.selectMeter("Home Meter") }
                            )

                            DropdownMenuItem(
                                text = { Text("Shop Meter") },
                                onClick = { viewModel.selectMeter("Shop Meter") }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // ---------------- Balance Card ----------------
            BalanceCard(
                balanceKwh = viewModel.balanceKwh.value,
                balanceGhs = viewModel.balanceGhs.value,
                daysLeft = viewModel.daysLeft.value
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---------------- Usage Card ----------------
            UsageCard(
                usage = viewModel.usage.value
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---------------- Prediction Card ----------------
            PredictionCard(
                prediction = viewModel.prediction.value
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ---------------- Quick Actions ----------------
            QuickActionsRow(navController)
        }
    }
}
