package com.example.powertracker.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MultiplatformRadioGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    // A Column to stack the card-like options vertically
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Space between the cards
    ) {
        options.forEach { option ->
            val isSelected = (option == selectedOption)
            val borderColor = if (isSelected) Color(0xFF3F51B5) else Color.LightGray

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, borderColor),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onOptionSelected(option) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF3F51B5), // Solid Indigo
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}
