package com.example.powertracker.card.insights

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Insights
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
fun ForecastCard(forecast: String) {
    // Define the colors based on your theme
    val primaryBlue = Color(0xFF3F51B5) // Your project's Indigo
    val lightBlueBg = Color(0xFFE3F2FD) // The light blue background from the image

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // The image has no shadow
        colors = CardDefaults.cardColors(containerColor = lightBlueBg),
        // Add the blue border
        border = BorderStroke(1.dp, primaryBlue.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Use the correct Insights icon
                Icon(
                    Icons.Default.Insights,
                    contentDescription = "Forecast",
                    tint = primaryBlue
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "AI Forecast",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = primaryBlue
                )
            }
            Text(
                forecast,
                fontSize = 14.sp,
                color = Color.DarkGray.copy(alpha = 0.8f),
                lineHeight = 20.sp // Add some line height for better readability
            )
        }
    }
}
