package com.example.cropbazaar.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cropbazaar.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountAuthView = viewModel()
) {
    val userInfo by viewModel.userInfo.observeAsState()
    val userType by viewModel.userType.observeAsState()
    val sellCrops by viewModel.sellCrops.observeAsState(emptyList())
    val buyCrops by viewModel.buyCrops.observeAsState(emptyList())

    // BottomNav selected item state
    val selectedItem = remember { mutableStateOf(3) } // 3 for "Account"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Dashboard", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32))
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                selectedItem = selectedItem,
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9)),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            val painter = rememberAsyncImagePainter(
                                model = userInfo?.profileImageUrl.takeIf { !it.isNullOrBlank() }
                                    ?: R.drawable.profile
                            )

                            Image(
                                painter = painter,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(userInfo?.fullName ?: "", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text(userInfo?.email ?: "", fontSize = 14.sp)
                                Text("📞 ${userInfo?.contact}", fontSize = 14.sp)
                                Text("📍 ${userInfo?.address}", fontSize = 14.sp)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }

                if (userType == "Farmer") {
                    item { Text("🌾 My Sell Crops", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    items(sellCrops) { crop -> CropCard(crop) }
                } else if (userType == "Buyer") {
                    item { Text("🛒 My Buy Crops", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    items(buyCrops) { crop -> CropCard(crop) }
                }
            }
        }
    )
}

@Composable
fun CropCard(crop: CropData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🌱 ${crop.cropName}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("📦 Quantity: ${crop.quantity}", fontSize = 14.sp)
            Text("💰 Price: ₹${crop.expectedPrice}", fontSize = 14.sp)
            Text("📍 Pin Code: ${crop.pinCode}", fontSize = 14.sp)
            Text("🕒 ${crop.timestamp}", fontSize = 12.sp)

            if (crop.cropImageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(crop.cropImageUrl),
                    contentDescription = "Crop Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
