package com.example.powertracker.card.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotificationCard(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp), // Main padding is now on the Row
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Color(0xFF4CAF50)
            )

            // Text content (takes up all available space)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp) // Add padding around the text
            ) {
                Text("Notifications", fontWeight = FontWeight.Bold)
                Text("Receive alerts when balance is low", fontSize = 12.sp, color = Color.Gray)
            }

            // Switch with its own padding
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 16.dp) // Padding to keep it from the edge
            )
        }
    }
}
