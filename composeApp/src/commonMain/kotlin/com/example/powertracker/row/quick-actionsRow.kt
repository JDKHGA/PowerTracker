package com.example.powertracker.row

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.powertracker.navigation.BottomNavItem
import com.example.powertracker.ui.theme.IndigoGradient

@Composable
fun QuickActionsRow(navController: NavController? = null) {
    Column {
        Text("Quick Actions", fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryActionButton("Add Token", Icons.Default.Add, {navController?.navigate("addToken")})
            SecondaryActionButton("History", Icons.Default.History, {navController?.navigate(BottomNavItem.History.route)})
            SecondaryActionButton("Insights", Icons.Default.Insights, {navController?.navigate(BottomNavItem.Insights.route)})
        }
    }
}

@Composable
fun RowScope.PrimaryActionButton(text: String, icon: ImageVector, onClick: ()-> Unit) {
    Button(
        modifier = Modifier.weight(1f).height(80.dp),
        onClick = onClick ,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .background(IndigoGradient, RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 4.dp) // Added padding to match OutlinedButton
            ) {
                Icon(imageVector = icon, contentDescription = text, tint = Color.White)
                Text(text, color = Color.White)
            }
        }
    }
}

@Composable
fun RowScope.SecondaryActionButton(text: String, icon: ImageVector, onClick: ()-> Unit) {
    OutlinedButton(
        modifier = Modifier.weight(1f).height(80.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = text)
            Text(text)
        }
    }
}
