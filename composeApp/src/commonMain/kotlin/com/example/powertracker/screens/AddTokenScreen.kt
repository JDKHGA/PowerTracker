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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powertracker.elements.TopBar.TopBar.TopBar
import com.example.powertracker.textfield.CustomTextField
import com.example.powertracker.ui.theme.IndigoGradient
import com.example.powertracker.viewmodel.AddTokenViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
@Preview
fun AddTokenScreen(navController: NavController? = null) {
    val viewModel: AddTokenViewModel = viewModel { AddTokenViewModel() }
    var showDatePicker by remember { mutableStateOf(false) }
    var showMeterDropdown by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                        val formattedDate = "${date.year}-${date.monthNumber.toString().padStart(2, '0')}-${date.dayOfMonth.toString().padStart(2, '0')}"
                        viewModel.purchaseDate.value = formattedDate
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Meter Selection Dropdown
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Select Meter", fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .clickable { if (!viewModel.isFetchingMeters.value) showMeterDropdown = true }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewModel.selectedMeter.value?.name ?: "Select a meter",
                            color = if (viewModel.selectedMeter.value == null) Color.Gray else Color.Black
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = showMeterDropdown,
                        onDismissRequest = { showMeterDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        viewModel.meters.value.forEach { meter ->
                            DropdownMenuItem(
                                text = { Text(meter.name) },
                                onClick = {
                                    viewModel.selectedMeter.value = meter
                                    showMeterDropdown = false
                                }
                            )
                        }
                    }
                }
            }

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

            Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                CustomTextField(
                    value = viewModel.purchaseDate.value,
                    onValueChange = { },
                    label = "Purchase Date",
                    placeholder = "YYYY-MM-DD",
                )
                // Overlay to prevent typing but allow clicking
                Box(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable { showDatePicker = true })
            }

            viewModel.error.value?.let {
                Text(text = it, color = Color.Red)
            }

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

            Spacer(Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !viewModel.isLoading.value,
                onClick = {
                    viewModel.saveToken {
                        navController?.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = if (viewModel.isLoading.value) {
                        Modifier.background(Color.Gray, RoundedCornerShape(12.dp))
                    } else {
                        Modifier.background(IndigoGradient, RoundedCornerShape(12.dp))
                    }.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.isLoading.value) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.height(24.dp))
                    } else {
                        Text("Save Token", color = Color.White)
                    }
                }
            }
        }
    }
}
