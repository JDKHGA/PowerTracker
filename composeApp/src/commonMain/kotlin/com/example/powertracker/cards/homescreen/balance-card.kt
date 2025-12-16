package com.example.powertracker.cards.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.powertracker.ui.theme.IndigoGradient

@Composable
fun BalanceCard(
    balanceKwh: String,
    balanceGhs: String,
    daysLeft: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Remaining Balance", color = Color.Gray)

            Spacer(Modifier.height(8.dp))

            Text(
                text = balanceKwh,
                style = TextStyle(
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    brush = IndigoGradient
                )
            )

            Text(balanceGhs, color = Color.Gray)
            Spacer(Modifier.height(4.dp))
            Text(daysLeft, color = Color.Gray)
        }
    }
}
