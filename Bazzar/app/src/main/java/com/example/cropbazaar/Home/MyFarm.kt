package com.example.cropbazaar.Home

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class FarmDetail(
    val id: Int,
    var name: String,
    var location: String,
    var area: String,
    var imageBitmap: Bitmap? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFarmScreen(navController: NavController) {
    var farmList by remember { mutableStateOf(mutableListOf<FarmDetail>()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Farms",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Filled.AddLocation, contentDescription = "Add Farm", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32)) // Dark green
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFCBD6AD)) // Lime green background
                .padding(paddingValues)
        ) {
            if (farmList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Farms Added Yet", color = Color.DarkGray)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(farmList) { farm ->
                        FarmCard(
                            farm = farm,
                            onDelete = {
                                farmList = farmList.filter { it.id != farm.id }.toMutableList()
                            },
                            onEdit = { updatedFarm ->
                                farmList = farmList.map { if (it.id == updatedFarm.id) updatedFarm else it }.toMutableList()
                            }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            AddFarmDialog(
                onAdd = { newFarm ->
                    farmList = (farmList + newFarm).toMutableList()
                    showAddDialog = false
                },
                onDismiss = { showAddDialog = false }
            )
        }
    }
}

@Composable
fun FarmCard(farm: FarmDetail, onDelete: (FarmDetail) -> Unit, onEdit: (FarmDetail) -> Unit) {
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            if (farm.imageBitmap != null) {
                Image(
                    bitmap = farm.imageBitmap!!.asImageBitmap(),
                    contentDescription = farm.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                ) {
                    Text(
                        text = "No Image",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = farm.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Farm", tint = Color.White)
                        }
                        IconButton(onClick = { onDelete(farm) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete Farm", tint = Color.White)
                        }
                    }
                }
                Column {
                    Text(text = "Location: ${farm.location}", color = Color.White)
                    Text(text = "Area: ${farm.area}", color = Color.White)
                }
            }
        }

        if (showEditDialog) {
            EditFarmDialog(
                farm = farm,
                onSave = {
                    onEdit(it)
                    showEditDialog = false
                },
                onDismiss = { showEditDialog = false }
            )
        }
    }
}

@Composable
fun EditFarmDialog(
    farm: FarmDetail,
    onSave: (FarmDetail) -> Unit,
    onDismiss: () -> Unit
) {
    FarmDialog(farm = farm, onSave = onSave, onDismiss = onDismiss)
}

@Composable
fun AddFarmDialog(
    onAdd: (FarmDetail) -> Unit,
    onDismiss: () -> Unit
) {
    FarmDialog(
        onSave = { newFarm ->
            onAdd(newFarm)
        },
        onDismiss = onDismiss
    )
}


@Composable
fun FarmDialog(
    farm: FarmDetail? = null,
    onSave: (FarmDetail) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(farm?.name.orEmpty()) }
    var location by remember { mutableStateOf(farm?.location.orEmpty()) }
    var area by remember { mutableStateOf(farm?.area.orEmpty()) }
    var imageBitmap by remember { mutableStateOf(farm?.imageBitmap) }

    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            imageBitmap = bitmap
        }
    }

    val fileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            imageBitmap = bitmap
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Farm Details") },
        text = {
            Column {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Image", color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { cameraLauncher.launch(null) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Capture from Camera", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { fileLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Choose from File", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(value = name, onValueChange = { name = it }, label = { Text("Farm Name") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = area, onValueChange = { area = it }, label = { Text("Area") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotEmpty() && location.isNotEmpty() && area.isNotEmpty()) {
                    onSave(
                        FarmDetail(
                            id = farm?.id ?: System.currentTimeMillis().toInt(),
                            name = name,
                            location = location,
                            area = area,
                            imageBitmap = imageBitmap
                        )
                    )
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
