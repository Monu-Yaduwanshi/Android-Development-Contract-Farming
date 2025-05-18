package com.example.cropbazaar.Home
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    // States for settings
    var weatherUnit by remember { mutableStateOf("Celsius") }
    var isWeatherDropdownExpanded by remember { mutableStateOf(false) }

    var isGoogleAnalyticsDisabled by remember { mutableStateOf(false) }
    var isCrashReportingDisabled by remember { mutableStateOf(false) }

    var selectedLanguage by remember { mutableStateOf("English") }
    var isLanguageDropdownExpanded by remember { mutableStateOf(false) }

    var selectedCountry by remember { mutableStateOf("India") }
    var isCountryDropdownExpanded by remember { mutableStateOf(false) }

    var notificationSettings = remember { mutableStateMapOf<String, Boolean>() }
    val notificationOptions = listOf(
        "Information about my crops",
        "Popular Posts",
        "Answer to your post",
        "Upvote to your post",
        "New Follower!",
        "Post from someone you follow"
    )
    notificationOptions.forEach { option -> notificationSettings.putIfAbsent(option, false) }

    var showSignOutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFf1f7e9)) // LimeGreen Background
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // General Section
                item {
                    Text(
                        text = "General",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF4CAF50) // Green
                    )
                }
                item {
                    DropdownRow(
                        label = "Select your CropBazaar language",
                        selectedOption = selectedLanguage,
                        options = listOf("English", "Hindi", "Gujarati", "Punjabi", "Tamil", "Telugu"),
                        onOptionSelected = { selectedLanguage = it },
                        isDropdownExpanded = isLanguageDropdownExpanded,
                        onDropdownExpand = { isLanguageDropdownExpanded = it }
                    )
                }
                item {
                    DropdownRow(
                        label = "App country",
                        selectedOption = selectedCountry,
                        options = listOf("India", "USA", "UK", "Canada", "Australia", "Germany"),
                        onOptionSelected = { selectedCountry = it },
                        isDropdownExpanded = isCountryDropdownExpanded,
                        onDropdownExpand = { isCountryDropdownExpanded = it }
                    )
                }
                item { Divider() }

                // Notifications Section
                item {
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF4CAF50) // Green
                    )
                }
                items(notificationOptions.size) { index ->
                    val option = notificationOptions[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = notificationSettings[option] ?: false,
                            onCheckedChange = { isChecked -> notificationSettings[option] = isChecked }
                        )
                    }
                }

                // Weather Section
                item { Divider() }
                item {
                    Text(
                        text = "Weather",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF4CAF50) // Green
                    )
                }
                item {
                    DropdownRow(
                        label = "Weather temperature units",
                        selectedOption = weatherUnit,
                        options = listOf("Celsius", "Fahrenheit"),
                        onOptionSelected = { weatherUnit = it },
                        isDropdownExpanded = isWeatherDropdownExpanded,
                        onDropdownExpand = { isWeatherDropdownExpanded = it }
                    )
                }

                // Other Section
                item { Divider() }
                item {
                    Text(
                        text = "Other",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF4CAF50) // Green
                    )
                }
                item {
                    SwitchRow(
                        label = "No Google Analytics",
                        description = "Deactivate the registration of anonymous data via Google Analytics",
                        isChecked = isGoogleAnalyticsDisabled,
                        onCheckedChange = { isGoogleAnalyticsDisabled = it }
                    )
                }
                item {
                    SwitchRow(
                        label = "No Crash Reporting",
                        description = "Deactivate the reporting of anonymous crash reports.",
                        isChecked = isCrashReportingDisabled,
                        onCheckedChange = { isCrashReportingDisabled = it }
                    )
                }

                // Save Button
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    Button(
                        onClick = { showSignOutDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Green button
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save", color = Color.White)
                    }
                }
            }

            // Save Dialog
            if (showSignOutDialog) {
                AlertDialog(
                    onDismissRequest = { showSignOutDialog = false },
                    title = { Text(text = "Save Changes") },
                    text = { Text(text = "Do you really want to save changes?") },
                    confirmButton = {
                        TextButton(onClick = { /* Handle save logic */ showSignOutDialog = false }) {
                            Text(text = "Save", color = Color(0xFF4CAF50))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showSignOutDialog = false }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun DropdownRow(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    isDropdownExpanded: Boolean,
    onDropdownExpand: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Box {
            ClickableText(
                text = AnnotatedString(selectedOption),
                onClick = { onDropdownExpand(true) }
            )
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { onDropdownExpand(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(option)
                            onDropdownExpand(false)
                        },
                        text = { Text(option) }
                    )
                }
            }
        }
    }
}

@Composable
fun SwitchRow(
    label: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label)
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val navController = rememberNavController()
    SettingsScreen(navController = navController)
}