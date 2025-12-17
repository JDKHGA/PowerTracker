package com.example.powertracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.card.TokenHistoryCard
import com.example.powertracker.elements.TopBar.TopBar
import com.example.powertracker.ui.theme.Indigo
import com.example.powertracker.viewmodel.TokenScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun TokenHistoryScreen(navController: NavController? = null) {

    val viewModel: TokenScreenViewModel = viewModel { TokenScreenViewModel() }

    Scaffold(
        topBar = {
            TopBar(
                text = "Token History",
                onBack = {
                    navController?.popBackStack()
                },
                icon = Icons.AutoMirrored.Filled.ArrowBack
            )
        },

        ) { paddingValues ->

        // Use a LazyColumn for displaying lists efficiently
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // This can be replaced with items(tokenList) when you have a real list
            item {
                TokenHistoryCard(
                    amount = viewModel.amount.value,
                    date = viewModel.date.value,
                    units = viewModel.units.value,
                    tokenCode = viewModel.tokenCode.value,
                    duration = viewModel.duration.value
                )
            }
            item {
                // You can add more cards here to test scrolling
                TokenHistoryCard(
                    amount = viewModel.amount.value,
                    date = viewModel.date.value,
                    units = viewModel.units.value,
                    tokenCode = viewModel.tokenCode.value,
                    duration = viewModel.duration.value
                )
            }
        }
    }
}

