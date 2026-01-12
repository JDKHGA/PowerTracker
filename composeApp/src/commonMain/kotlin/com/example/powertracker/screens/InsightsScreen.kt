package com.example.powertracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.card.insights.*
import com.example.powertracker.cards.insightsscreen.RecommendationsCard
import com.example.powertracker.cards.insightsscreen.TrendCard
import com.example.powertracker.viewmodel.HomeViewModel
import com.example.powertracker.viewmodel.InsightsScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun InsightsScreen(navController: NavController? = null) {
    val viewModel: InsightsScreenViewModel = viewModel { InsightsScreenViewModel() }
    val homeViewModel: HomeViewModel = viewModel { HomeViewModel() }
    var showMeterDropdown by remember { mutableStateOf(false) }

    val selectedMeter = homeViewModel.selectedMeter.value

    LaunchedEffect(selectedMeter) {
        selectedMeter?.id?.let {
            viewModel.loadInsights(it)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Insights", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
        ) {
            // Meter Selection Dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .clickable { if (!homeViewModel.isLoading.value) showMeterDropdown = true }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = homeViewModel.selectedMeter.value?.name ?: "Select a meter",
                        color = if (homeViewModel.selectedMeter.value == null) Color.Gray else Color.Black
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = showMeterDropdown,
                    onDismissRequest = { showMeterDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    homeViewModel.meters.forEach { meter ->
                        DropdownMenuItem(
                            text = { Text(meter.name) },
                            onClick = {
                                homeViewModel.selectMeter(meter)
                                showMeterDropdown = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (viewModel.isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        TrendCard(points = viewModel.trendData)
                    }
                    item {
                        SummaryCard(
                            avgSpending = viewModel.avgSpending.value,
                            avgDailyUsage = viewModel.avgDailyUsage.value
                        )
                    }
                    item {
                        ConsumptionCard(
                            peakUsageTime = viewModel.peakUsageTime.value,
                            weekendVsWeekday = viewModel.weekendVsWeekday.value
                        )
                    }
                    item {
                        ForecastCard(forecast = viewModel.aiForecast.value)
                    }
                    item {
                        RecommendationsCard(recommendations = viewModel.recommendations)
                    }
                }
            }
        }
    }
}
