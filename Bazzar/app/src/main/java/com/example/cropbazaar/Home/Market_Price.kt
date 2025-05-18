package com.example.cropbazaar.Home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MarketPrice_Screen(navController: NavController) {
    Text(
        text = " Market priice", // Taglin
        style = MaterialTheme.typography.bodySmall,
        fontSize = 14.sp,
        color = Color.White,
        modifier = Modifier.padding(top = 4.dp) // Padding above the tagline
    )

}