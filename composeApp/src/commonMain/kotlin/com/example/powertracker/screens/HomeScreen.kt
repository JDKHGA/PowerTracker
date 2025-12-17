package com.example.powertracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.cards.homescreen.BalanceCard
import com.example.powertracker.cards.homescreen.PredictionCard
import com.example.powertracker.cards.homescreen.UsageCard
import com.example.powertracker.row.QuickActionsRow
import com.example.powertracker.viewmodel.HomeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun HomeScreen(navController: NavController? = null) {

    // Create ViewModel (simple version, no DI yet)
    val viewModel: HomeViewModel = viewModel { HomeViewModel() }

    val options = listOf("Home Meter", "Shop Meter")

    Scaffold(
        // Set the background color of the main content area to white
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 1. Styled App Title
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 30.sp
                                    )
                                ) {
                                    append("Power")
                                }
                                withStyle(style = SpanStyle(fontSize = 30.sp, color = Color.Gray)) {
                                    append("Tracker")
                                }
                            }
                        )

                        Box(modifier = Modifier.width(150.dp)) {
                            // 2. ExposedDropdownMenuBox for the meter selection
                            ExposedDropdownMenuBox(
                                expanded = viewModel.meterDropdownExpanded.value,
                                onExpandedChange = { viewModel.meterDropdownExpanded.value = it }
                            ) {
                                // This is the visible part of the dropdown menu
                                TextField(
                                    value = viewModel.selectedMeter.value,
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = TextStyle(fontSize = 14.sp), // Reduce font size
                                    trailingIcon = {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "Dropdown"
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    modifier = Modifier.menuAnchor()
                                )

                                // This is the dropdown menu that appears
                                ExposedDropdownMenu(
                                    expanded = viewModel.meterDropdownExpanded.value,
                                    onDismissRequest = {
                                        viewModel.meterDropdownExpanded.value = false
                                    }
                                ) {
                                    options.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = {
                                                viewModel.selectMeter(selectionOption)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                // Set the TopAppBar background to white
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
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
