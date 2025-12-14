package com.example.powertracker.cards

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.powertracker.ui.theme.IndigoGradient

@Composable
fun UsageCard(
    usage: String
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

            // Placeholder for the line graph
            LineGraph()
        }
    }
}

@Composable
fun LineGraph() {
    val indigoColor = Color(0xFF3F51B5) // A solid color from your Indigo theme for the graph
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.6f)
            cubicTo(
                size.width * 0.15f, size.height * 0.8f,
                size.width * 0.25f, size.height * 0.4f,
                size.width * 0.4f, size.height * 0.5f
            )
            cubicTo(
                size.width * 0.55f, size.height * 0.6f,
                size.width * 0.65f, size.height * 0.2f,
                size.width * 0.8f, size.height * 0.3f
            )
            lineTo(size.width, size.height * 0.4f)
        }

        drawPath(
            path = path,
            color = indigoColor,
            style = Stroke(width = 5f)
        )
    }
}
