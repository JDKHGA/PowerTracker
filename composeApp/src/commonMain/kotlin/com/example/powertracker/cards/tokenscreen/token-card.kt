package com.example.powertracker.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokenHistoryCard(
    amount: String,
    units: String,
    tokenCode: String,
    duration: String,
    date: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Row: Amount and Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = amount,
                    color = Color(0xFF3F51B5), // Indigo color
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Date",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = date, color = Color.Gray, fontSize = 14.sp)
                }
            }

            // kWh Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ElectricBolt,
                    contentDescription = "kWh",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFFF9A825) // Amber/Yellow color
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = units,
                    color = Color(0xFFF9A825),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            // Token Code Section
            Column {
                Text("Token Code", color = Color.Gray, fontSize = 12.sp)
                Spacer(Modifier.height(5.dp))
                Text(
                    tokenCode,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Estimated Duration Chip
            AssistChip(
                onClick = { /* No action needed */ },
                label = { Text(duration, fontSize = 12.sp) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFFE8F5E9), // Light green background
                    labelColor = Color.DarkGray
                ),
                border = null,
                modifier = Modifier.height(28.dp)
            )
        }
    }
}
