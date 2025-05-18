//
//package com.example.cropbazaar.Home
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.google.firebase.auth.FirebaseAuth
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CartScreen(
//    navController: NavController,
//    cartViewModel: CartViewModel
//) {
//    // Refresh cart when screen is loaded or user changes
//    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
//    LaunchedEffect(currentUserEmail) {
//        cartViewModel.refreshUserCart()
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Your Cart 🧺", fontSize = 20.sp, color = Color.White) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32))
//            )
//        },
//        content = { padding ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding)
//                    .padding(16.dp)
//                    .background(Color(0xFFF5F5F5))
//            ) {
//                if (cartViewModel.cartItems.isEmpty()) {
//                    Text("🛒 Your cart is empty!", fontSize = 18.sp, color = Color.Gray)
//                } else {
//                    LazyColumn(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        items(cartViewModel.cartItems) { post ->
//                            Card(
//                                modifier = Modifier.fillMaxWidth(),
//                                colors = CardDefaults.cardColors(containerColor = Color(0xFFDFF0D8)),
//                                elevation = CardDefaults.cardElevation(4.dp)
//                            ) {
//                                Column(modifier = Modifier.padding(16.dp)) {
//                                    Text("🌾 Crop: ${post.crop.cropName}", fontSize = 18.sp)
//                                    Text("📦 Variety: ${post.crop.variety}")
//                                    Text("💰 Price: ₹${post.crop.expectedPrice}")
//                                    Text("📍 Location: ${post.crop.pinCode}")
//                                    Text("📊 Quantity: ${post.crop.quantity}")
//                                    Text("👤 Seller: ${post.userProfile.firstName} ${post.userProfile.lastName}")
//
//                                    if (post.crop.cropImageUrl.isNotEmpty()) {
//                                        Spacer(modifier = Modifier.height(8.dp))
//                                        Image(
//                                            painter = rememberAsyncImagePainter(post.crop.cropImageUrl),
//                                            contentDescription = "Crop Image",
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .height(180.dp)
//                                        )
//                                    }
//
//                                    Spacer(modifier = Modifier.height(10.dp))
//
//                                    Row(
//                                        horizontalArrangement = Arrangement.SpaceBetween,
//                                        modifier = Modifier.fillMaxWidth()
//                                    ) {
//                                        Button(
//                                            onClick = {
//                                                cartViewModel.removeFromCart(post)
//                                            },
//                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
//                                        ) {
//                                            Text("❌ Remove", color = Color.White)
//                                        }
//
//                                        Button(
//                                            onClick = {
//                                                navController.navigate("payments")
//                                            },
//                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
//                                        ) {
//                                            Text("🛍 Buy Now", color = Color.White)
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Button(
//                        onClick = {
//                            navController.navigate("payments")
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(50.dp)
//                    ) {
//                        Text("Place All Orders ✅", fontSize = 16.sp, color = Color.White)
//                    }
//                }
//            }
//        }
//    )
//}
