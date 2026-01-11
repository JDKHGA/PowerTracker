package com.example.powertracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.card.TokenHistoryCard
import com.example.powertracker.elements.TopBar.TopBar.TopBar
import com.example.powertracker.ui.theme.Indigo
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController?.navigate("addToken")
                },
                containerColor = Indigo,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add New Token")
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = viewModel.isLoading.value,
            onRefresh = { viewModel.loadTokens() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (viewModel.tokens.isEmpty() && !viewModel.isLoading.value) {
                EmptyTokensState(onAddClick = { navController?.navigate("addToken") })
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

@Composable
private fun EmptyTokensState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.ReceiptLong,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Indigo.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Tokens Found",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your token history is empty. Add your first token to see your purchase history and track your spending.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = Indigo),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Add Your First Token")
        }
    }
}
