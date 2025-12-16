package com.example.powertracker.cards.insightsscreen


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
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
fun RecommendationsCard(
    recommendations: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = "Recommendations",
                    tint = Color(0xFFFBC02D)
                )
                Spacer(Modifier.width(8.dp))
                Text("Recommendations", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            // Add some extra space between the title and the list
            Spacer(modifier = Modifier.height(4.dp))

            recommendations.forEach { recommendation ->
                Row(
                    // Add vertical padding to create more space between bullet points
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("• ", color = Color(0xFF4CAF50), fontSize = 14.sp)
                    Text(
                        text = recommendation,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        // Add line height for better readability if a single item wraps
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
