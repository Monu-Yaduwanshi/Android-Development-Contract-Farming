package com.example.cropbazaar.Home
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cropbazaar.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val profileData by viewModel.profileData.observeAsState()

    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // UI State
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    // Load values when LiveData updates
    LaunchedEffect(profileData) {
        profileData?.let {
            firstName = it.firstName
            lastName = it.lastName
            mobileNumber = it.mobileNumber
            address = it.address
            language = it.language
            pinCode = it.pinCode
            state = it.state
            userType = it.userType
            fullName = it.fullName
            contact = it.contact
        }
    }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                viewModel.uploadImageToCloudinary(it)
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontSize = 20.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = customColors.primary)
            )
        },
//        bottomBar = {
//            BottomNavBar(navController, selectedItem = remember { mutableIntStateOf(5) })
//        } ,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(120.dp)) {
                        val imagePainter = rememberAsyncImagePainter(
                            selectedImageUri ?: profileData?.profileImageUrl.orEmpty()
                                .ifEmpty { R.drawable.profilefarmer }
                        )

                        Image(
                            painter = imagePainter,
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .clickable { pickImageLauncher.launch("image/*") },
                            contentScale = ContentScale.Crop
                        )

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile Picture",
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .clickable { pickImageLauncher.launch("image/*") }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileTextField("Gmail", profileData?.gmail ?: "", {}, isEnabled = false, leadingIcon = Icons.Default.Email)
                    ProfileTextField("User Type", profileData?.userType ?: "", {}, isEnabled = false, leadingIcon = Icons.Default.VerifiedUser)

                    ProfileTextField("First Name", firstName, { firstName = it }, isEditing, Icons.Default.Person)
                    ProfileTextField("Last Name", lastName, { lastName = it }, isEditing, Icons.Default.Person)
                    ProfileTextField("Full Name", fullName, { fullName = it }, isEditing, Icons.Default.Person)
                    ProfileTextField("Contact", contact, { contact = it }, isEditing, Icons.Default.Phone)

                    ProfileTextField("Mobile Number", mobileNumber, { mobileNumber = it }, isEditing, Icons.Default.Phone)
                    ProfileTextField("Address", address, { address = it }, isEditing, Icons.Default.Info)
                    ProfileTextField("Language", language, { language = it }, isEditing, Icons.Default.Language)
                    ProfileTextField("Pin Code", pinCode, { pinCode = it }, isEditing, Icons.Default.LocationOn)
                    ProfileTextField("State", state, { state = it }, isEditing, Icons.Default.Map)

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { isEditing = !isEditing }) {
                            Text(if (isEditing) "Cancel" else "Edit")
                        }

                        if (isEditing) {
                            Button(onClick = {
                                val success = viewModel.saveUserProfile(
                                    profileData?.copy(
                                        firstName = firstName,
                                        lastName = lastName,
                                        mobileNumber = mobileNumber,
                                        address = address,
                                        language = language,
                                        pinCode = pinCode,
                                        state = state,
                                        fullName = fullName,
                                        contact = contact
                                    ) ?: return@Button
                                )

                                if (success) {
                                    isEditing = false
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Profile updated successfully!")
                                    }
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Please fill all fields correctly.\nMobile must be 10 digits, PinCode 6 digits.")
                                    }
                                }
                            }) {
                                Text("Save Changes")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = {
                        viewModel.logoutUser()
                        navController.navigate("login")
                    }) {
                        Text("Logout")
                    }
                }
            }
        }
    )
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        keyboardOptions = keyboardType,
        enabled = isEnabled,
        leadingIcon = leadingIcon?.let { icon ->
            { Icon(imageVector = icon, contentDescription = "$label Icon") }
        }
    )
}
