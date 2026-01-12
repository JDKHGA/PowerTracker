package com.example.powertracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val viewModel: HomeViewModel = viewModel { HomeViewModel() }
    var showMeterDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadMeters()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
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
                },
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
            // Meter Selection Dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .clickable { if (!viewModel.isLoading.value) showMeterDropdown = true }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = viewModel.selectedMeter.value?.name ?: "Select a meter",
                        color = if (viewModel.selectedMeter.value == null) Color.Gray else Color.Black
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = showMeterDropdown,
                    onDismissRequest = { showMeterDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    viewModel.meters.forEach { meter ->
                        DropdownMenuItem(
                            text = { Text(meter.name) },
                            onClick = {
                                viewModel.selectMeter(meter)
                                showMeterDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BalanceCard(
                balanceKwh = viewModel.balanceKwh.value,
                balanceGhs = viewModel.balanceGhs.value,
                daysLeft = viewModel.daysLeft.value
            )

            Spacer(modifier = Modifier.height(16.dp))

            UsageCard(
                usage = viewModel.usage.value,
                points = viewModel.dailyUsagePoints
            )

            Spacer(modifier = Modifier.height(16.dp))

            PredictionCard(
                prediction = viewModel.prediction.value
            )

            Spacer(modifier = Modifier.height(24.dp))

            QuickActionsRow(navController)
        }
    }
}
