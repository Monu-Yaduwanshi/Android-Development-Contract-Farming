package com.example.cropbazaar.Home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyScreen(navController: NavController, viewModel: BuyAuthViewModel = viewModel()) {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<Location?>(null) }

    // ✅ FusedLocationProviderClient instead of LocationManager
    LaunchedEffect(Unit) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                    Log.d("BuyScreen", "Fetched Location: ${location.latitude}, ${location.longitude}")
                } else {
                    Log.e("BuyScreen", "Location is null")
                }
            }.addOnFailureListener {
                Log.e("BuyScreen", "Failed to get location: ${it.message}")
            }
        } else {
            Log.e("BuyScreen", "Permission not granted for location")
        }
    }

    var category by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var cropName by remember { mutableStateOf("") }
    var variety by remember { mutableStateOf("") }
    var expectedPrice by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var quantityUnit by remember { mutableStateOf("kg") }
    var unitExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buy Crop", fontSize = 20.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32))
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // Category Dropdown
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Category Icon") }
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            listOf("Vegetable", "Fruit", "Grains", "Spices", "Others").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        category = option
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = cropName,
                        onValueChange = { cropName = it },
                        label = { Text("Crop Name") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Grass, contentDescription = "Crop Name Icon") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = variety,
                        onValueChange = { variety = it },
                        label = { Text("Variety") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.LocalFlorist, contentDescription = "Variety Icon") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = expectedPrice,
                        onValueChange = { expectedPrice = it },
                        label = { Text("Expected Price") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Money, contentDescription = "Price Icon") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = pinCode,
                        onValueChange = { pinCode = it },
                        label = { Text("Pin Code") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Pin Code Icon") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BoxWithConstraints {
                        val halfWidth = maxWidth / 2 - 4.dp

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ExposedDropdownMenuBox(
                                expanded = unitExpanded,
                                onExpandedChange = { unitExpanded = !unitExpanded }
                            ) {
                                OutlinedTextField(
                                    value = quantityUnit,
                                    onValueChange = { quantityUnit = it },
                                    label = { Text("Unit") },
                                    readOnly = true,
                                    modifier = Modifier.width(halfWidth).menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = unitExpanded,
                                    onDismissRequest = { unitExpanded = false }
                                ) {
                                    listOf("kg", "quintal", "tonne").forEach { unit ->
                                        DropdownMenuItem(
                                            text = { Text(unit) },
                                            onClick = {
                                                quantityUnit = unit
                                                unitExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = quantity,
                                onValueChange = { quantity = it },
                                label = { Text("Quantity") },
                                modifier = Modifier.width(halfWidth),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (currentLocation == null) {
                                Toast.makeText(context, "Location not available yet", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val cropData = BuyAuthViewModel.CropData(
                                category = category,
                                cropName = cropName,
                                variety = variety,
                                expectedPrice = expectedPrice,
                                pinCode = pinCode,
                                quantity = "$quantity $quantityUnit"
                            )
                            Log.d("BuyScreen", "Submitting CropData with Location: ${currentLocation?.latitude}, ${currentLocation?.longitude}")
                            viewModel.saveBuyCropWithLocation(cropData, currentLocation)
                            navController.navigate("Account")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    )
}
