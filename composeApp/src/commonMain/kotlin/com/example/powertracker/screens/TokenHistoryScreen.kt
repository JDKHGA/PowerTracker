package com.example.powertracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.card.TokenHistoryCard
import com.example.powertracker.elements.TopBar.TopBar
import com.example.powertracker.viewmodel.TokenScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
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
        PullToRefreshBox(
            isRefreshing = viewModel.isLoading.value,
            onRefresh = { viewModel.loadTokens() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (viewModel.tokens.isEmpty() && !viewModel.isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = viewModel.error.value ?: "No token history found.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.tokens) { token ->
                        TokenHistoryCard(
                            amount = "GHS ${token.amount}",
                            date = token.purchaseDate,
                            units = "${token.units} kWh",
                            tokenCode = token.tokenCode,
                            duration = "Purchased on ${token.purchaseDate}"
                        )
                    }
                }
            }
        }
    }
}
