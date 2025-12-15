package com.example.powertracker.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MeterItemCard(
    meterName: String,
    meterNumber: String,
    meterType: String,
    lastUpdated: String,
    isActive: Boolean
) {
    // Determine the color of the status icon based on the 'isActive' flag
    val activeColor = if (isActive) Color(0xFF3F51B5) else Color.Gray

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left-side content (Texts and Chip)
            Column(modifier = Modifier.weight(1f)) {
                Text(meterName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.padding(4.dp))

                Text(meterNumber, color = Color.Gray, fontSize = 14.sp)

                Spacer(modifier = Modifier.padding(4.dp))

                // Row for the chip and last updated text
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AssistChip(
                        onClick = { /* No action needed for a status chip */ },
                        label = { Text(meterType, fontSize = 12.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFE8F5E9), // Light green background
                            labelColor = Color.DarkGray
                        ),
                        border = null, // Remove the default border
                        modifier = Modifier.height(24.dp) // Control the height
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(lastUpdated, color = Color.Gray, fontSize = 12.sp)
                }
            }

            // Right-side icon
            Icon(
                imageVector = Icons.Outlined.Speed,
                contentDescription = "Status",
                tint = activeColor
            )
        }
    }
}
