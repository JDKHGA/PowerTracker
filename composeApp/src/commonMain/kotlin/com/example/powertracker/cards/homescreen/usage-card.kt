package com.example.powertracker.cards.homescreen

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UsageCard(
    usage: String,
    points: List<Float> = emptyList()
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
            Text(
                text = usage,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )

            Spacer(Modifier.height(16.dp))

            LineGraph(points)
        }
    }
}

@Composable
fun LineGraph(points: List<Float>) {
    val indigoColor = Color(0xFF3F51B5)
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        if (points.size < 2) {
            // Draw a flat line if not enough data
            val y = size.height * 0.8f
            drawLine(
                color = indigoColor.copy(alpha = 0.3f),
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(size.width, y),
                strokeWidth = 3f
            )
            return@Canvas
        }

        val maxUsage = (points.maxOrNull() ?: 1f).coerceAtLeast(0.1f)
        val path = Path()
        
        val xInterval = size.width / (points.size - 1)
        
        points.forEachIndexed { index, value ->
            val x = index * xInterval
            val y = size.height - (value / maxUsage * size.height * 0.8f) - (size.height * 0.1f)
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = indigoColor,
            style = Stroke(width = 5f)
        )
    }
}
