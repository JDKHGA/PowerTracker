package com.example.powertracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.powertracker.elements.TopBar.TopBar
import com.example.powertracker.textfield.CustomTextField
import com.example.powertracker.ui.theme.IndigoGradient
import com.example.powertracker.viewmodel.AddTokenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun AddTokenScreen(navController: NavController? = null) { // Removed unused tokenCode parameter
    val viewModel = remember { AddTokenViewModel() }

    Scaffold(
        topBar = {
            TopBar(
                text = "Add Token",
                onBack = {
                    navController?.popBackStack()
                },
                icon = Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp), // Add overall padding for the content
            verticalArrangement = Arrangement.spacedBy(16.dp) // Add space between each element
        ) {
            CustomTextField(
                value = viewModel.tokenCode.value,
                onValueChange = { viewModel.tokenCode.value = it },
                label = "Token Code",
                placeholder = "e.g., 1234-5678-9012-3456"
            )

            CustomTextField(
                value = viewModel.amount.value,
                onValueChange = { viewModel.amount.value = it },
                label = "Amount (GHS)",
                placeholder = "e.g., 100.00"
            )

            CustomTextField(
                value = viewModel.units.value,
                onValueChange = { viewModel.units.value = it },
                label = "Units (kWh)",
                placeholder = "e.g., 30.0"
            )

            CustomTextField(
                value = viewModel.purchaseDate.value,
                onValueChange = { viewModel.purchaseDate.value = it },
                label = "Purchase Date",
                placeholder = "15/12/2025",

                )

            Spacer(Modifier.height(8.dp)) // Add extra space before the buttons

            OutlinedButton(
                onClick = { /* TODO: Implement Scan with AI */ },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Scan Receipt",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Scan Receipt with AI")
            }

            Spacer(Modifier.weight(1f)) // Pushes the Save button to the bottom

            Button(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                onClick = { /* TODO: Implement Save Token action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(IndigoGradient, RoundedCornerShape(12.dp))
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Save Token", color = Color.White)
                }
            }
        }
    }
}
