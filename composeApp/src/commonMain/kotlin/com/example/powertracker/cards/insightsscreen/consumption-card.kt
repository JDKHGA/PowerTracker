package com.example.powertracker.card.insights

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConsumptionCard(peakUsageTime: String, weekendVsWeekday: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            // Increase the vertical spacing to make the card taller
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ElectricBolt,
                    contentDescription = "Consumption",
                    tint = Color(0xFFF9A825)
                )
                Spacer(Modifier.width(8.dp))
                Text("Consumption Pattern", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Peak Usage Time", color = Color.Gray)
                Text(peakUsageTime, fontWeight = FontWeight.Medium)
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Weekend vs Weekday", color = Color.Gray)
                Text(
                    weekendVsWeekday,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red.copy(alpha = 0.8f)
                )
            }
        }
    }
}
