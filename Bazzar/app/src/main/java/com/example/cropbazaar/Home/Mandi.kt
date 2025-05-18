package com.example.cropbazaar.Home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// Color Scheme
val customColors = darkColorScheme(
    primary = Color(0xFF4CAF50), // Green
    onPrimary = Color.White,
    background = Color(0xFFF1F7E9), // Light Green
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MandiScreen(navController: NavController) {
    val selectedItem = remember { mutableIntStateOf(4) }
    val context = LocalContext.current

    var userType by remember { mutableStateOf<String?>(null) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Fetch user type from Firebase Realtime DB
    LaunchedEffect(Unit) {
        userId?.let { uid ->
            val dbRef = FirebaseDatabase.getInstance().getReference("RegistrationData").child(uid)
            dbRef.child("userType").get().addOnSuccessListener {
                userType = it.getValue(String::class.java)
            }.addOnFailureListener {
                Log.e("MandiScreen", "Failed to fetch userType: ${it.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mandi",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Icon",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = customColors.primary)
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                selectedItem = selectedItem,
                modifier = Modifier.fillMaxWidth()
            )
        }
//        bottomBar = {
//            BottomNavBar(navController, selectedItem)
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Mandi",
                fontSize = 24.sp,
                color = customColors.onBackground
            )
            Spacer(modifier = Modifier.height(20.dp))

            when (userType) {
                "Farmer" -> {
                    SellButton(navController)
                }

                "Buyer" -> {
                    BuyButton(navController)
                }

                "Delivery Man" -> {
                    SellButton(navController)
                    Spacer(modifier = Modifier.height(16.dp))
                    BuyButton(navController)
                }

                null -> {
                    CircularProgressIndicator(color = customColors.primary)
                }

                else -> {
                    Text("Invalid user type", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun SellButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("sell") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = customColors.primary)
    ) {
        Icon(Icons.Default.Sell, contentDescription = "Sell", tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sell", color = Color.White)
    }
}

@Composable
fun BuyButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("buy") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = customColors.primary)
    ) {
        Icon(Icons.Default.ShoppingCart, contentDescription = "Buy", tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Buy", color = Color.White)
    }
}
