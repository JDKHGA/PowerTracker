package com.example.powertracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.card.MeterItemCard
import com.example.powertracker.elements.TopBar.TopBar
import com.example.powertracker.ui.theme.Indigo
import com.example.powertracker.viewmodel.AddMeterScreenViewModel
import com.example.powertracker.viewmodel.MeterScreenViewModel
import kotlinx.coroutines.NonCancellable.isActive
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun MetersScreen(navController: NavController? = null) {
    val viewModel: MeterScreenViewModel = viewModel { MeterScreenViewModel() }
    Scaffold(
        topBar = {
            TopBar(
                text = "Add New Meter",
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
                containerColor = Indigo, // Indigo background
                contentColor = Color.White, // White icon color
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add New Meter")
            }

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp), // Add overall padding for the content
            verticalArrangement = Arrangement.spacedBy(16.dp) // Add space between each element
        ) {
            MeterItemCard(
                meterName = viewModel.meterName.value,
                meterNumber = viewModel.meterNumber.value,
                meterType = viewModel.meterTypes.value,
                lastUpdated = viewModel.lastUpdated.value,
                isActive = true // Assuming this meter is currently active
            )

        }

    }

}




