package com.example.powertracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Insights",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        Box(modifier = Modifier.width(150.dp)) {
                            ExposedDropdownMenuBox(
                                expanded = homeViewModel.meterDropdownExpanded.value,
                                onExpandedChange = { homeViewModel.meterDropdownExpanded.value = it }
                            ) {
                                TextField(
                                    value = selectedMeter?.name ?: "Select Meter",
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = TextStyle(fontSize = 14.sp),
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

                                ExposedDropdownMenu(
                                    expanded = homeViewModel.meterDropdownExpanded.value,
                                    onDismissRequest = {
                                        homeViewModel.meterDropdownExpanded.value = false
                                    }
                                ) {
                                    homeViewModel.meters.forEach { meter ->
                                        DropdownMenuItem(
                                            text = { Text(meter.name) },
                                            onClick = {
                                                homeViewModel.selectMeter(meter)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (viewModel.isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
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
