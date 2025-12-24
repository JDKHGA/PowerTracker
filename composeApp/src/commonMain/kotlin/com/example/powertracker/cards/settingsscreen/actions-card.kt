package com.example.powertracker.card.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionsCard(onLogout: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            ActionItem(
                "Export Data"
            ) { /* TODO: Handle click */ }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )
            ActionItem("Privacy Policy") { /* TODO: Handle click */ }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )
            ActionItem("Terms of Service") { /* TODO: Handle click */ }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )
            ActionItem("Clear All Data", color = Color.Red) { /* TODO: Handle click */ }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )
            ActionItem("Logout", color = Color(0xFF3F51B5)) { onLogout() }
        }
    }
}

@Composable
private fun ActionItem(text: String, color: Color = Color.Unspecified, onClick: () -> Unit) {
    Text(
        text = text,
        color = color,
        fontSize = 14.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    )
}
