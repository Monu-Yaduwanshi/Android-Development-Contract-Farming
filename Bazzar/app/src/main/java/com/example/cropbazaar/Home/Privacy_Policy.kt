package com.example.cropbazaar.Home
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// CropBazaar's green color scheme
val CropBazaarLightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50), // Green
    onPrimary = Color.White,
    background = Color(0xFFCBD6AD), // Light Green
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun CropBazaarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CropBazaarLightColorScheme,
        content = content
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Privacy Policy",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF1F8E9)) // Light Green Background
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            PrivacyHeader()
            Spacer(modifier = Modifier.height(16.dp))
            PrivacyContent()
        }
    }
}



@Composable
fun PrivacyHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50) // Vibrant Green
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center // Centers content both horizontally and vertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CropBazaar",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Privacy Policy",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



//@Composable
//fun PrivacyContent() {
//    val sections = listOf(
//        "Last Modified" to "27 June 2023",
//        "Data Controller" to "CropBazaar's data is managed by PEAT GmbH, based in Berlin, Germany. Email: PrivacyTeam@cropbazaar.net",
//        "Scope of Privacy Policy" to "This Privacy Policy applies to the use of the CropBazaar Website, App, and Community.",
//        "Website Data Collected" to "We collect information like IP address, browser details, and preferred language settings to ensure security and improve user experience.",
//        "App Data Collected" to "Images, location data, network type, and timestamps are collected to improve services and disease analysis features.",
//        "Community Usage" to "User information like nickname, user ID, and messages are stored for community interactions and reputation tracking.",
//        "Push Notifications" to "We send push notifications for community updates and important information. No ads are sent via notifications.",
//        "Analytics" to "CropBazaar uses Firebase and Google Analytics for performance tracking and improving app features.",
//        "Data Transfers" to "Your data may be transferred securely as part of using the app services. International transfers comply with required standards."
//    )
//
//    sections.forEach { (title, description) ->
//        PrivacySection(title = title, description = description)
//    }
//
//    Spacer(modifier = Modifier.height(16.dp))
//
//    Text(
//        text = "By using the services, you consent to the collection and use of your personal data as outlined in this policy.",
//        style = MaterialTheme.typography.bodyMedium,
//        color = Color(0xFF2E7D32), // Dark Green
//        fontWeight = FontWeight.Bold,
//        textAlign = TextAlign.Center,
//        modifier = Modifier.fillMaxWidth().padding(16.dp)
//    )
//}

//@Composable
//fun PrivacySection(title: String, description: String) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//        ) {
//            Text(
//                text = title,
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.primary,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = description,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//        }
//    }
//}

@Composable
fun PrivacyContent() {
    val sections = listOf(
        "Last Modified" to "27 June 2023",
        "Data Controller" to "CropBazaar's data is managed by PEAT GmbH, based in Berlin, Germany. Email: PrivacyTeam@cropbazaar.net",
        "Scope of Privacy Policy" to "This Privacy Policy applies to the use of the CropBazaar Website, App, and Community.",
        "Website Data Collected" to "We collect information like IP address, browser details, and preferred language settings to ensure security and improve user experience.",
        "App Data Collected" to "Images, location data, network type, and timestamps are collected to improve services and disease analysis features.",
        "Community Usage" to "User information like nickname, user ID, and messages are stored for community interactions and reputation tracking.",
        "Push Notifications" to "We send push notifications for community updates and important information. No ads are sent via notifications.",
        "Analytics" to "CropBazaar uses Firebase and Google Analytics for performance tracking and improving app features.",
        "Data Transfers" to "Your data may be transferred securely as part of using the app services. International transfers comply with required standards."
    )

    sections.forEach { (title, description) ->
        PrivacySection(title = title, description = description)
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "By using the services, you consent to the collection and use of your personal data as outlined in this policy.",
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF2E7D32), // Dark Green
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun PrivacySection(title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // White background for the card
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Section heading styled in green
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4CAF50), // Green color for section headings
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Section description
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PrivacyPolicyPreview() {
    val navController = rememberNavController()
    PrivacyPolicyScreen(navController = navController)
}

//@Preview(showBackground = true)
//@Composable
//fun PrivacyPolicyPreview() {
//    CropBazaarTheme {
//        PrivacyPolicyScreen()
//    }
//}
//@Preview(showBackground = true)
//@Composable
//fun PrivacyPolicyPreview() {
//    val navController = rememberNavController()
//    PrivacyPolicyScreen(navController = navController)
//}