package com.example.cropbazaar.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignOutScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) } // State for showing the dialog

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32) // Dark Green
                )
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF1F8E9)), // Light Lime Green
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50), // Vibrant Green
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Sign Out", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // Dialog Box for Confirmation
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = {
                            Text(
                                text = "Warning",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            )
                        },
                        text = {
                            Text(
                                text = "Do you really want to sign out?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    FirebaseAuth.getInstance().signOut() // ✅ Sign out the user
                                    navController.navigate("login") {
                                        popUpTo("splash") { inclusive = true } // ✅ Clears navigation history
                                    }
                                    showDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2E7D32), // Lime Green
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Sign Out")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color(0xFF2E7D32) // Lime Green
                                )
                            ) {
                                Text(text = "Cancel")
                            }
                        },
                        containerColor = Color.White // Dialog Background
                    )
                }
            }
        }
    )
}
