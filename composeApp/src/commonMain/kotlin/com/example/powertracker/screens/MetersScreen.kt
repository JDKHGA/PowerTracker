package com.example.powertracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.card.MeterItemCard
import com.example.powertracker.elements.TopBar.TopBar.TopBar
import com.example.powertracker.ui.theme.Indigo
import com.example.powertracker.viewmodel.MeterScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MetersScreen(navController: NavController? = null) {
    val viewModel: MeterScreenViewModel = viewModel { MeterScreenViewModel() }
    
    Scaffold(
        topBar = {
            TopBar(
                text = "My Meters",
                onBack = {
                    navController?.popBackStack()
                },
                icon = Icons.AutoMirrored.Filled.ArrowBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController?.navigate("addMeter")
                },
                containerColor = Indigo,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add New Meter")
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = viewModel.isLoading.value,
            onRefresh = { viewModel.loadMeters() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (viewModel.meters.isEmpty() && !viewModel.isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = viewModel.error.value ?: "No meters found. Add one!")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.meters) { meter ->
                        MeterItemCard(
                            meterName = meter.name,
                            meterNumber = meter.meterNumber,
                            meterType = meter.type,
                            lastUpdated = "Added on ${meter.createdAt?.take(10) ?: "N/A"}",
                            isActive = true
                        )
                    }
                }
            }
        }
    }
}
