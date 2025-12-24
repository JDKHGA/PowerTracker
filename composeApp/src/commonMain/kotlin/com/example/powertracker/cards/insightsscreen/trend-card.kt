package com.example.powertracker.cards.insightsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.DefaultPoint
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun TrendCard(points: List<Pair<Int, Float>>) {
    val primaryColor = Color(0xFF4CAF50)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = "Trend",
                    tint = primaryColor
                )
                Spacer(Modifier.width(8.dp))
                Text("30-Day Usage Trend", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(Modifier.height(16.dp))

            if (points.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No usage data yet", color = Color.Gray)
                }
            } else {
                ChartLayout(
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                ) {
                    XYGraph(
                        xAxisModel = FloatLinearAxisModel(1f..30f),
                        yAxisModel = FloatLinearAxisModel(0f..(points.maxOfOrNull { it.second } ?: 5f) + 1f),
                        xAxisTitle = "Day",
                        yAxisTitle = "kWh"
                    ) {
                        LinePlot(
                            data = points.map { DefaultPoint(it.first.toFloat(), it.second) },
                            lineStyle = LineStyle(
                                brush = SolidColor(primaryColor),
                                strokeWidth = 2.dp
                            ),
                            symbol = {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(color = primaryColor, shape = CircleShape)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
