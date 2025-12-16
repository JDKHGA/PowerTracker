package com.example.powertracker.card.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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

// Define the light card color, typically a very light gray or off-white.
val LightCard = Color(0xFFF8F9FA)

@Composable
fun SummaryCard(avgSpending: String, avgDailyUsage: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Main title row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = "Monthly Summary",
                    tint = Color(0xFF4CAF50) // Use the green color from the image
                )
                Spacer(Modifier.width(8.dp))
                Text("Monthly Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(16.dp))
            // Row containing the two nested summary item cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Space between the nested cards
            ) {
                SummaryItem(
                    title = "Avg. Spending",
                    value = avgSpending,
                    unit = "per month",
                    valueColor = Color(0xFF3F51B5), // Use the project's indigo color
                    modifier = Modifier.weight(1f)
                )
                SummaryItem(
                    title = "Avg. Daily Usage",
                    value = avgDailyUsage,
                    unit = "per day",
                    valueColor = Color(0xFF4CAF50), // Use the green color for kWh
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * A private composable representing one of the nested "box" items.
 * It's a Card with a light background color.
 */
@Composable
private fun SummaryItem(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier,
    valueColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = LightCard), // Use the light gray background
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // No shadow for nested cards
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Space between text elements
        ) {
            Text(title, fontSize = 14.sp, color = Color.Gray)
            Text(
                value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Text(unit, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
