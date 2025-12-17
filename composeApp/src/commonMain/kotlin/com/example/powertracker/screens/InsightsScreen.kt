package com.example.powertracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.card.insights.*
import com.example.powertracker.cards.insightsscreen.RecommendationsCard
import com.example.powertracker.elements.TopBar.TopBar
import com.example.powertracker.viewmodel.InsightsScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun InsightsScreen(navController: NavController? = null) {
    val viewModel: InsightsScreenViewModel = viewModel { InsightsScreenViewModel() }

    Scaffold(
        topBar = {
            TopBar(
                text = "Usage Insights ",
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
                TrendCard()
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
