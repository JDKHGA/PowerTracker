package com.example.powertracker.cards.settingsscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun AlertThresholdCard(
    sliderPosition: Float,
    onSliderChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.WarningAmber, contentDescription = "Alert Threshold", tint = Color(0xFF4CAF50))
                Spacer(Modifier.width(16.dp))
                // This column will now take up all available horizontal space
                Column(modifier = Modifier.weight(1f)) {
                    Text("Alert Threshold", fontWeight = FontWeight.Bold)
                    Text("Get notified when balance falls below this value", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(Modifier.height(8.dp))
            Slider(
                value = sliderPosition,
                onValueChange = onSliderChange,
                valueRange = 0f..50f,
                steps = 49,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Threshold", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "${sliderPosition.roundToInt()} kWh",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}
