package com.example.cropbazaar.Home

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

// Crop Data Model
data class CropDetail(
    val id: Int,
    var name: String,
    var variety: String,
    var plantedDate: String,
    var expectedHarvestDate: String,
    var imageUri: Uri? = null,
    var imageBitmap: Bitmap? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCropScreen(navController: NavController) {
    var crops by remember { mutableStateOf(emptyList<CropDetail>().toMutableList()) }
    var showAddCropDialog by remember { mutableStateOf(false) }
    var showEditCropDialog by remember { mutableStateOf<CropDetail?>(null) }

    if (showAddCropDialog) {
        AddCropDialog(
            onAdd = { newCrop ->
                crops = (crops + newCrop).toMutableList()
                showAddCropDialog = false
            },
            onCancel = { showAddCropDialog = false }
        )
    }

    showEditCropDialog?.let { crop ->
        EditCropDialog(
            crop = crop,
            onSave = { updatedCrop ->
                crops = crops.map { if (it.id == updatedCrop.id) updatedCrop else it }.toMutableList()
                showEditCropDialog = null
            },
            onCancel = { showEditCropDialog = null }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Crops", color = Color.White, style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showAddCropDialog = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Crop", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF4CAF50))
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.padding(16.dp)
        ) {
            items(crops) { crop ->
                CropCard(
                    crop = crop,
                    onEdit = { showEditCropDialog = crop },
                    onDelete = { crops = crops.filter { it.id != crop.id }.toMutableList() }
                )
            }
        }
    }
}

@Composable
fun CropCard(crop: CropDetail, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFCBD6AD))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (crop.imageBitmap != null) {
                    Image(
                        bitmap = crop.imageBitmap!!.asImageBitmap(),
                        contentDescription = crop.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.Green.copy(alpha = 0.2f)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Image", color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = crop.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Variety: ${crop.variety}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Planted: ${crop.plantedDate}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Expected Harvest: ${crop.expectedHarvestDate}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp, top = 4.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit Crop",
                        tint = Color(0xFF2B2B2B)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete Crop",
                        tint = Color(0xFF2B2B2B)
                    )
                }
            }
        }
    }
}

        @Composable
fun AddCropDialog(onAdd: (CropDetail) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var variety by remember { mutableStateOf("") }
    var plantedDate by remember { mutableStateOf("") }
    var harvestDate by remember { mutableStateOf("") }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            imageBitmap = bitmap
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            imageBitmap = bitmap
        }
    }

    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        onAdd(
                            CropDetail(
                                id = System.currentTimeMillis().toInt(),
                                name = name,
                                variety = variety,
                                plantedDate = plantedDate,
                                expectedHarvestDate = harvestDate,
                                imageBitmap = imageBitmap
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Add", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                Text("Cancel", color = Color.White)
            }
        },
        title = { Text("Add New Crop") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Crop Name") })
                OutlinedTextField(value = variety, onValueChange = { variety = it }, label = { Text("Variety") })
                OutlinedTextField(value = plantedDate, onValueChange = { plantedDate = it }, label = { Text("Planted Date") })
                OutlinedTextField(value = harvestDate, onValueChange = { harvestDate = it }, label = { Text("Harvest Date") })

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { cameraLauncher.launch(null) }) {
                    Text("Capture from Camera")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Choose from File")
                }
            }
        },
        containerColor = Color(0xFFCBD6AD)
    )
}

@Composable
fun EditCropDialog(
    crop: CropDetail,
    onSave: (CropDetail) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(crop.name) }
    var variety by remember { mutableStateOf(crop.variety) }
    var plantedDate by remember { mutableStateOf(crop.plantedDate) }
    var harvestDate by remember { mutableStateOf(crop.expectedHarvestDate) }
    var imageBitmap by remember { mutableStateOf(crop.imageBitmap) }
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            imageBitmap = bitmap
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            imageBitmap = bitmap
        }
    }

    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        crop.copy(
                            name = name,
                            variety = variety,
                            plantedDate = plantedDate,
                            expectedHarvestDate = harvestDate,
                            imageBitmap = imageBitmap
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                Text("Cancel", color = Color.White)
            }
        },
        title = { Text("Edit Crop") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Crop Name") })
                OutlinedTextField(value = variety, onValueChange = { variety = it }, label = { Text("Variety") })
                OutlinedTextField(value = plantedDate, onValueChange = { plantedDate = it }, label = { Text("Planted Date") })
                OutlinedTextField(value = harvestDate, onValueChange = { harvestDate = it }, label = { Text("Harvest Date") })

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { cameraLauncher.launch(null) }) {
                    Text("Capture from Camera")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Choose from File")
                }
            }
        },
        containerColor = Color(0xFFCBD6AD)
    )
}


//package com.example.cropBazaar.DrawerContent
//import android.app.Activity
//import android.content.ContentValues
//import android.content.Intent
//import android.provider.MediaStore
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.PreviewParameter
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.example.cropBazaar.R
//import java.io.File
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//
//// Crop Data Model
//data class CropDetail(
//    val id: Int,
//    var name: String,
//    var variety: String,
//    var plantedDate: String,
//    var expectedHarvestDate: String,
//    var imageResourceId: Int
//)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyCropScreen(navController: NavController) {
//    var crops by remember { mutableStateOf(sampleCropData().toMutableList()) }
//    var showAddCropDialog by remember { mutableStateOf(false) }
//    var showEditCropDialog by remember { mutableStateOf<CropDetail?>(null) }
//
//    if (showAddCropDialog) {
//        AddCropDialog(
//            onAdd = { newCrop ->
//                crops = (crops + newCrop).toMutableList()
//                showAddCropDialog = false
//            },
//            onCancel = { showAddCropDialog = false }
//        )
//    }
//
//    showEditCropDialog?.let { crop ->
//        EditCropDialog(
//            crop = crop,
//            onSave = { updatedCrop ->
//                crops = crops.map { if (it.id == updatedCrop.id) updatedCrop else it }.toMutableList()
//                showEditCropDialog = null
//            },
//            onCancel = { showEditCropDialog = null }
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("My Crops", color = Color.White, style = MaterialTheme.typography.headlineSmall) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { showAddCropDialog = true }) {
//                        Icon(Icons.Filled.Add, contentDescription = "Add Crop", tint = Color.White)
//                    }
//                },
//                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF4CAF50))
//            )
//        }
//    ) { paddingValues ->
//        LazyColumn(
//            contentPadding = paddingValues,
//            modifier = Modifier.padding(16.dp)
//        ) {
//            items(crops) { crop ->
//                CropCard(
//                    crop = crop,
//                    onEdit = { showEditCropDialog = crop },
//                    onDelete = { crops = crops.filter { it.id != crop.id }.toMutableList() }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CropCard(crop: CropDetail, onEdit: () -> Unit, onDelete: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        elevation = CardDefaults.cardElevation(4.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFCBD6AD))
//    ) {
//        Box(modifier = Modifier.padding(16.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                // Crop Image
//                Image(
//                    painter = painterResource(id = crop.imageResourceId),
//                    contentDescription = crop.name,
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(CircleShape)
//                        .background(Color.Green.copy(alpha = 0.2f)),
//                    contentScale = ContentScale.Crop
//                )
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                // Crop Details
//                Column {
//                    Text(
//                        text = crop.name,
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(text = "Variety: ${crop.variety}", style = MaterialTheme.typography.bodyMedium)
//                    Text(text = "Planted: ${crop.plantedDate}", style = MaterialTheme.typography.bodySmall)
//                    Text(text = "Expected Harvest: ${crop.expectedHarvestDate}", style = MaterialTheme.typography.bodySmall)
//                }
//            }
//
//            // Edit and Delete Buttons
//            Row(
//                horizontalArrangement = Arrangement.End,
//                modifier = Modifier
//                    .align(Alignment.TopEnd)
//            ) {
//                IconButton(onClick = onEdit) {
//                    Icon(Icons.Filled.Edit, contentDescription = "Edit Crop", tint = Color.White)
//                }
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Filled.Delete, contentDescription = "Delete Crop", tint = Color.White)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AddCropDialog(onAdd: (CropDetail) -> Unit, onCancel: () -> Unit) {
//    var name by remember { mutableStateOf("") }
//    var variety by remember { mutableStateOf("") }
//    var plantedDate by remember { mutableStateOf("") }
//    var harvestDate by remember { mutableStateOf("") }
//    var cropImage by remember { mutableStateOf(R.drawable.logo) }
//    var showImageOptions by remember { mutableStateOf(false) }
//
//    val context = LocalContext.current
//    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
//        if (isSuccess) {
//            // Handle captured image (save or display)
//        }
//    }
//
//    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        // Handle image selection from gallery
//    }
//
//    AlertDialog(
//        onDismissRequest = onCancel,
//        confirmButton = {
//            Button(
//                onClick = {
//                    onAdd(CropDetail((0..10000).random(), name, variety, plantedDate, harvestDate, cropImage))
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//            ) {
//                Text("Add", color = Color.White)
//            }
//        },
//        dismissButton = {
//            Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
//                Text("Cancel", color = Color.White)
//            }
//        },
//        title = { Text("Add New Crop") },
//        text = {
//            Column {
//                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Crop Name") })
//                OutlinedTextField(value = variety, onValueChange = { variety = it }, label = { Text("Variety") })
//                OutlinedTextField(value = plantedDate, onValueChange = { plantedDate = it }, label = { Text("Planted Date") })
//                OutlinedTextField(value = harvestDate, onValueChange = { harvestDate = it }, label = { Text("Harvest Date") })
//
//                Button(onClick = { showImageOptions = !showImageOptions }) {
//                    Text("Add Image")
//                }
//
//                if (showImageOptions) {
//                    Column {
//                        Button(onClick = {
//                            // Create a temporary URI to store the image
//                            val contentValues = ContentValues().apply {
//                                put(MediaStore.Images.Media.TITLE, "temp_image")
//                                put(MediaStore.Images.Media.DESCRIPTION, "Captured from camera")
//                            }
//                            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//                            var cropImageUri = uri?.toString()  // Save the URI for later use
//                            if (uri != null) {
//                                cameraLauncher.launch(uri)
//                            }
//                        }) {
//                            Text("Capture from Camera")
//                        }
//                        Button(onClick = { galleryLauncher.launch("image/*") }) {
//                            Text("Choose from File")
//                        }
//                    }
//                }
//            }
//        },
//        containerColor = Color(0xFFCBD6AD)
//    )
//}
//
//
//@Composable
//fun EditCropDialog(crop: CropDetail, onSave: (CropDetail) -> Unit, onCancel: () -> Unit) {
//    var name by remember { mutableStateOf(crop.name) }
//    var variety by remember { mutableStateOf(crop.variety) }
//    var plantedDate by remember { mutableStateOf(crop.plantedDate) }
//    var harvestDate by remember { mutableStateOf(crop.expectedHarvestDate) }
//    var cropImage by remember { mutableStateOf(crop.imageResourceId) }
//    var showImageOptions by remember { mutableStateOf(false) }
//
//    val context = LocalContext.current
//    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
//        if (isSuccess) {
//            // Handle captured image (save or display)
//        }
//    }
//
//    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        // Handle image selection from gallery
//    }
//
//    AlertDialog(
//        onDismissRequest = onCancel,
//        confirmButton = {
//            Button(
//                onClick = {
//                    onSave(crop.copy(name = name, variety = variety, plantedDate = plantedDate, expectedHarvestDate = harvestDate, imageResourceId = cropImage))
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//            ) {
//                Text("Save", color = Color.White)
//            }
//        },
//        dismissButton = {
//            Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
//                Text("Cancel", color = Color.White)
//            }
//        },
//        title = { Text("Edit Crop") },
//        text = {
//            Column {
//                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Crop Name") })
//                OutlinedTextField(value = variety, onValueChange = { variety = it }, label = { Text("Variety") })
//                OutlinedTextField(value = plantedDate, onValueChange = { plantedDate = it }, label = { Text("Planted Date") })
//                OutlinedTextField(value = harvestDate, onValueChange = { harvestDate = it }, label = { Text("Harvest Date") })
//
//                Button(onClick = { showImageOptions = !showImageOptions }) {
//                    Text("Change Image")
//                }
//                if (showImageOptions) {
//                    Column {
//                        Button(onClick = {
//                            // Create a temporary URI to store the image
//                            val contentValues = ContentValues().apply {
//                                put(MediaStore.Images.Media.TITLE, "temp_image")
//                                put(MediaStore.Images.Media.DESCRIPTION, "Captured from camera")
//                            }
//                            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//                            var cropImageUri = uri?.toString()  // Save the URI for later use
//                            if (uri != null) {
//                                cameraLauncher.launch(uri)
//                            }
//                        }) {
//                            Text("Capture from Camera")
//                        }
//                        Button(onClick = { galleryLauncher.launch("image/*") }) {
//                            Text("Choose from File")
//                        }
//                    }
//                }
//            }
//        },
//        containerColor = Color(0xFFCBD6AD)
//    )
//}
//
//
//fun sampleCropData(): List<CropDetail> {
//    return listOf(
//        CropDetail(1, "Wheat", "Variety A", "2024-01-01", "2024-06-01", R.drawable.logo),
//        CropDetail(2, "Rice", "Variety B", "2024-02-01", "2024-07-01", R.drawable.logo)
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MyCropScreen(navController = rememberNavController())
//}
//import android.net.Uri
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.*
//import androidx.compose.material3.Button
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.tooling.preview.PreviewParameter
//import androidx.compose.ui.res.painterResource
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Card
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import coil.compose.rememberImagePainter
//
//// Sample CropDetail Data Class
//data class CropDetail(
//    val name: String,
//    val variety: String,
//    val plantedDate: String,
//    val expectedHarvestDate: String,
//    val imageUri: Uri? = null
//)
//
//@Composable
//fun MyCropScreen(navController: NavController, crops: List<CropDetail>, onCropUpdated: (CropDetail) -> Unit, onCropDeleted: (CropDetail) -> Unit) {
//    var showAddDialog by remember { mutableStateOf(false) }
//    var showEditDialog by remember { mutableStateOf(false) }
//    var cropToEdit by remember { mutableStateOf<CropDetail?>(null) }
//    var showImageDialog by remember { mutableStateOf(false) }
//    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
//
//    // Button to open add crop dialog
//    Button(onClick = { showAddDialog = true }) {
//        Text("Add Crop")
//    }
//
//    LazyColumn {
//        items(crops) { crop ->
//            CropCard(crop, onEditClick = {
//                cropToEdit = crop
//                showEditDialog = true
//            }, onDeleteClick = {
//                onCropDeleted(crop)
//            })
//        }
//    }
//
//    // Add Crop Dialog
//    if (showAddDialog) {
//        AddCropDialog(
//            onDismiss = { showAddDialog = false },
//            onCropAdded = {
//                // Add the crop to the list
//            },
//            selectedImageUri = selectedImageUri,
//            onImageSelected = { uri -> selectedImageUri = uri },
//            onImageChangeClick = { showImageDialog = true }
//        )
//    }
//
//    // Edit Crop Dialog
//    if (showEditDialog && cropToEdit != null) {
//        EditCropDialog(
//            crop = cropToEdit!!,
//            onDismiss = { showEditDialog = false },
//            onCropUpdated = {
//                onCropUpdated(it)
//                showEditDialog = false
//            },
//            selectedImageUri = selectedImageUri,
//            onImageSelected = { uri -> selectedImageUri = uri },
//            onImageChangeClick = { showImageDialog = true }
//        )
//    }
//
//    // Image selection dialog (Change Image options)
//    if (showImageDialog) {
//        ChangeImageDialog(
//            onDismiss = { showImageDialog = false },
//            onPickFromGallery = {
//                // Code to pick an image from the gallery
//                // Set the picked image URI
//                selectedImageUri = it
//            },
//            onTakePhoto = {
//                // Code to take a photo using the camera
//                // Set the captured image URI
//                selectedImageUri = it
//            }
//        )
//    }
//}
//
//@Composable
//fun CropCard(crop: CropDetail, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(),
////        elevation = 4.dp,
//        shape = MaterialTheme.shapes.medium // Optional: Adds rounded corners
//    ){
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Image(
//                painter = rememberImagePainter(crop.imageUri),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(CircleShape)
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(crop.name, style = MaterialTheme.typography.headlineMedium)
//                Text(crop.variety, style = MaterialTheme.typography.bodyMedium)
//                Text("Planted on: ${crop.plantedDate}", style = MaterialTheme.typography.bodyMedium)
//                Text("Harvest due: ${crop.expectedHarvestDate}", style = MaterialTheme.typography.bodyMedium)
//            }
//            Spacer(modifier = Modifier.width(16.dp))
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.End
//            ) {
//                IconButton(onClick = onEditClick) {
//                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
//                }
//                IconButton(onClick = onDeleteClick) {
//                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.primary)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AddCropDialog(
//    onDismiss: () -> Unit,
//    onCropAdded: (CropDetail) -> Unit,
//    selectedImageUri: Uri?,
//    onImageSelected: (Uri) -> Unit,
//    onImageChangeClick: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Add Crop") },
//        text = {
//            Column {
//                TextField(value = "", onValueChange = {}, label = { Text("Crop Name") })
//                TextField(value = "", onValueChange = {}, label = { Text("Crop Variety") })
//                TextField(value = "", onValueChange = {}, label = { Text("Planted Date") })
//                TextField(value = "", onValueChange = {}, label = { Text("Expected Harvest Date") })
//
//                // Display selected image if any
//                selectedImageUri?.let {
//                    Image(painter = rememberImagePainter(it), contentDescription = "Crop Image")
//                }
//
//                Button(onClick = onImageChangeClick) {
//                    Text("Change Image")
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = {
//                // Handle adding the crop
//                onCropAdded(CropDetail("", "", "", "", selectedImageUri))
//                onDismiss()
//            }) {
//                Text("Add")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}
//
//@Composable
//fun EditCropDialog(
//    crop: CropDetail,
//    onDismiss: () -> Unit,
//    onCropUpdated: (CropDetail) -> Unit,
//    selectedImageUri: Uri?,
//    onImageSelected: (Uri) -> Unit,
//    onImageChangeClick: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Edit Crop") },
//        text = {
//            Column {
//                TextField(value = crop.name, onValueChange = {}, label = { Text("Crop Name") })
//                TextField(value = crop.variety, onValueChange = {}, label = { Text("Crop Variety") })
//                TextField(value = crop.plantedDate, onValueChange = {}, label = { Text("Planted Date") })
//                TextField(value = crop.expectedHarvestDate, onValueChange = {}, label = { Text("Expected Harvest Date") })
//
//                // Display selected image if any
//                selectedImageUri?.let {
//                    Image(painter = rememberImagePainter(it), contentDescription = "Crop Image")
//                }
//
//                Button(onClick = onImageChangeClick) {
//                    Text("Change Image")
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = {
//                // Handle crop update
//                onCropUpdated(crop.copy(imageUri = selectedImageUri))
//                onDismiss()
//            }) {
//                Text("Update")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}
//
//@Composable
//fun ChangeImageDialog(
//    onDismiss: () -> Unit,
//    onPickFromGallery: (Uri) -> Unit,
//    onTakePhoto: (Uri) -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Select Image Source") },
//        text = {
//            Column {
//                Button(onClick = {
//                    // Trigger gallery selection
//                    // Example URI here, you will need to implement the gallery picker
//                    onPickFromGallery(Uri.parse("content://some-uri"))
//                }) {
//                    Text("Pick from Gallery")
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(onClick = {
//                    // Trigger camera capture
//                    // Example URI here, you will need to implement camera capture
//                    onTakePhoto(Uri.parse("content://some-other-uri"))
//                }) {
//                    Text("Take Photo")
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = onDismiss) {
//                Text("Close")
//            }
//        }
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewMyCropScreen() {
//    // Creating a sample list of crops
//    val cropList = listOf(
//        CropDetail("Wheat", "Variety 1", "2023-11-01", "2024-05-01", null),
//        CropDetail("Rice", "Variety 2", "2023-10-15", "2024-04-10", null)
//    )
//
//    // Initialize NavController for preview
//    val navController = rememberNavController()
//
//    // Call the MyCropScreen composable, passing the navController, cropList, and empty lambdas for update and delete
//    MyCropScreen(
//        navController = navController,
//        crops = cropList,
//        onCropUpdated = {},
//        onCropDeleted = {}
//    )
//}
//
//import android.net.Uri
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.*
//import androidx.compose.material3.Button
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.res.painterResource
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Card
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import coil.compose.rememberImagePainter
//
//// Sample CropDetail Data Class
//data class CropDetail(
//    val name: String,
//    val variety: String,
//    val plantedDate: String,
//    val expectedHarvestDate: String,
//    val imageUri: Uri? = null
//)
//
//@Composable
//fun MyCropScreen(
//    navController: NavController,
//    crops: List<CropDetail>,
//    onCropUpdated: (CropDetail) -> Unit,
//    onCropDeleted: (CropDetail) -> Unit
//) {
//    var showAddDialog by remember { mutableStateOf(false) }
//    var showEditDialog by remember { mutableStateOf(false) }
//    var cropToEdit by remember { mutableStateOf<CropDetail?>(null) }
//    var showImageDialog by remember { mutableStateOf(false) }
//    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
//
//    // Button to open add crop dialog
//    Button(onClick = { showAddDialog = true }) {
//        Text("Add Crop")
//    }
//
//    LazyColumn {
//        items(crops) { crop ->
//            CropCard(crop, onEditClick = {
//                cropToEdit = crop
//                showEditDialog = true
//            }, onDeleteClick = {
//                onCropDeleted(crop)
//            })
//        }
//    }
//
//    // Add Crop Dialog
//    if (showAddDialog) {
//        AddCropDialog(
//            onDismiss = { showAddDialog = false },
//            onCropAdded = {
//                // Add the crop to the list
//            },
//            selectedImageUri = selectedImageUri,
//            onImageSelected = { uri -> selectedImageUri = uri },
//            onImageChangeClick = { showImageDialog = true }
//        )
//    }
//
//    // Edit Crop Dialog
//    if (showEditDialog && cropToEdit != null) {
//        EditCropDialog(
//            crop = cropToEdit!!,
//            onDismiss = { showEditDialog = false },
//            onCropUpdated = {
//                onCropUpdated(it)
//                showEditDialog = false
//            },
//            selectedImageUri = selectedImageUri,
//            onImageSelected = { uri -> selectedImageUri = uri },
//            onImageChangeClick = { showImageDialog = true }
//        )
//    }
//
//    // Image selection dialog (Change Image options)
//    if (showImageDialog) {
//        ChangeImageDialog(
//            onDismiss = { showImageDialog = false },
//            onPickFromGallery = {
//                // Code to pick an image from the gallery
//                // Set the picked image URI
//                selectedImageUri = it
//            },
//            onTakePhoto = {
//                // Code to take a photo using the camera
//                // Set the captured image URI
//                selectedImageUri = it
//            }
//        )
//    }
//}
//
//@Composable
//fun CropCard(crop: CropDetail, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(),
//        shape = MaterialTheme.shapes.medium // Optional: Adds rounded corners
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Image(
//                painter = rememberImagePainter(crop.imageUri),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(CircleShape)
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(crop.name, style = MaterialTheme.typography.headlineMedium)
//                Text(crop.variety, style = MaterialTheme.typography.bodyMedium)
//                Text("Planted on: ${crop.plantedDate}", style = MaterialTheme.typography.bodyMedium)
//                Text("Harvest due: ${crop.expectedHarvestDate}", style = MaterialTheme.typography.bodyMedium)
//            }
//            Spacer(modifier = Modifier.width(16.dp))
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.End
//            ) {
//                IconButton(onClick = onEditClick) {
//                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
//                }
//                IconButton(onClick = onDeleteClick) {
//                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.primary)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AddCropDialog(
//    onDismiss: () -> Unit,
//    onCropAdded: (CropDetail) -> Unit,
//    selectedImageUri: Uri?,
//    onImageSelected: (Uri) -> Unit,
//    onImageChangeClick: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Add Crop") },
//        text = {
//            Column {
//                TextField(value = "", onValueChange = {}, label = { Text("Crop Name") })
//                TextField(value = "", onValueChange = {}, label = { Text("Crop Variety") })
//                TextField(value = "", onValueChange = {}, label = { Text("Planted Date") })
//                TextField(value = "", onValueChange = {}, label = { Text("Expected Harvest Date") })
//
//                // Display selected image if any
//                selectedImageUri?.let {
//                    Image(painter = rememberImagePainter(it), contentDescription = "Crop Image")
//                }
//
//                Button(onClick = onImageChangeClick) {
//                    Text("Change Image")
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = {
//                // Handle adding the crop
//                onCropAdded(CropDetail("", "", "", "", selectedImageUri))
//                onDismiss()
//            }) {
//                Text("Add")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}
//
//@Composable
//fun EditCropDialog(
//    crop: CropDetail,
//    onDismiss: () -> Unit,
//    onCropUpdated: (CropDetail) -> Unit,
//    selectedImageUri: Uri?,
//    onImageSelected: (Uri) -> Unit,
//    onImageChangeClick: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Edit Crop") },
//        text = {
//            Column {
//                TextField(value = crop.name, onValueChange = {}, label = { Text("Crop Name") })
//                TextField(value = crop.variety, onValueChange = {}, label = { Text("Crop Variety") })
//                TextField(value = crop.plantedDate, onValueChange = {}, label = { Text("Planted Date") })
//                TextField(value = crop.expectedHarvestDate, onValueChange = {}, label = { Text("Expected Harvest Date") })
//
//                // Display selected image if any
//                selectedImageUri?.let {
//                    Image(painter = rememberImagePainter(it), contentDescription = "Crop Image")
//                }
//
//                Button(onClick = onImageChangeClick) {
//                    Text("Change Image")
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = {
//                // Handle crop update
//                onCropUpdated(crop.copy(imageUri = selectedImageUri))
//                onDismiss()
//            }) {
//                Text("Update")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}
//
//@Composable
//fun ChangeImageDialog(
//    onDismiss: () -> Unit,
//    onPickFromGallery: (Uri) -> Unit,
//    onTakePhoto: (Uri) -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Select Image Source") },
//        text = {
//            Column {
//                Button(onClick = {
//                    // Trigger gallery selection
//                    // Example URI here, you will need to implement the gallery picker
//                    onPickFromGallery(Uri.parse("content://some-uri"))
//                }) {
//                    Text("Pick from Gallery")
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(onClick = {
//                    // Trigger camera capture
//                    // Example URI here, you will need to implement camera capture
//                    onTakePhoto(Uri.parse("content://some-other-uri"))
//                }) {
//                    Text("Take Photo")
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = onDismiss) {
//                Text("Close")
//            }
//        }
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewMyCropScreen() {
//    // Creating a sample list of crops
//    val cropList = listOf(
//        CropDetail("Wheat", "Variety 1", "2023-11-01", "2024-05-01", null),
//        CropDetail("Rice", "Variety 2", "2023-06-01", "2024-02-01", null)
//    )
//    MyCropScreen(
//        navController = rememberNavController(),
//        crops = cropList,
//        onCropUpdated = {},
//        onCropDeleted = {}
//    )
//}
