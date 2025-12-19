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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.button.MultiplatformRadioGroup
import com.example.powertracker.elements.TopBar.TopBar
import com.example.powertracker.textfield.CustomTextField
import com.example.powertracker.ui.theme.IndigoGradient
import com.example.powertracker.viewmodel.AddMeterScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AddMeterScreen(navController: NavController? = null) {
    val viewModel: AddMeterScreenViewModel = viewModel { AddMeterScreenViewModel() }

    Scaffold(
        topBar = {
            TopBar(
                text = "Add New Meter",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                value = viewModel.meterName.value,
                onValueChange = { viewModel.meterName.value = it },
                label = "Meter Name",
                placeholder = "e.g., Home meter, Office Meter"
            )

            CustomTextField(
                value = viewModel.meterNumber.value,
                onValueChange = { viewModel.meterNumber.value = it },
                label = "Meter Number",
                placeholder = "e.g., 04-0575-475893-00"
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Meter Type", fontWeight = FontWeight.Medium)
                MultiplatformRadioGroup(
                    options = viewModel.meterTypes,
                    selectedOption = viewModel.selectedMeterType.value,
                    onOptionSelected = { viewModel.selectedMeterType.value = it }
                )
            }

            viewModel.error.value?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !viewModel.isLoading.value,
                onClick = { 
                    viewModel.saveMeter {
                        navController?.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = if (viewModel.isLoading.value) SolidColor(Color.Gray) else IndigoGradient, 
                            shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.isLoading.value) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.height(24.dp))
                    } else {
                        Text("Add Meter", color = Color.White)
                    }
                }
            }
        }
    }
}
