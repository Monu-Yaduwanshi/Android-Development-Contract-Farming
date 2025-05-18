package com.example.cropbazaar.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cropbazaar.R
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCBD6AD)), // ✅ Set Background Color
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.circlelogo),
            contentDescription = "App Logo",
            modifier = Modifier.size(250.dp), // ✅ Increased Logo Size
            tint = Color.Unspecified // ✅ Fixes black logo issue
        )

        LaunchedEffect(Unit) {
            delay(1500) // 1.5-second delay

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
}
