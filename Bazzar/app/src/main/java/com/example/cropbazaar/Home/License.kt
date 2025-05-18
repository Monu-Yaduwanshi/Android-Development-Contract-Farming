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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "License",
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
                    .background(Color(0xFFF1F8E9)) // Lime Green background
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // App Title inside Card
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
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "CropBazaar",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Licence",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // License Details Section
                SectionCard(title = "PEAT ", content = "Progressive Environmental & Agricultural Technologies verified company")

                SectionCard(title = "Register Court", content = "District Court Bhopal")

                SectionCard(title = "Company Registration Number", content = "HRB 192 727 B")

                SectionCard(
                    title = "TAX Identification Number (VAT-NUMBER)",
                    content = "DE303589874"
                )

                SectionCard(
                    title = "Responsible for Content",
                    content = "Robert Strey (address available to the right/below).\nUsing contact information in this imprint for commercial advertising is expressly prohibited unless the provider has given written consent or there is an existing business relationship. The provider and all persons named in this declaration hereby oppose any commercial use and disclosure of their data."
                )

                SectionCard(
                    title = "Contact",
                    content = "Phone: +91 - (0)176 43537145\nE-Mail: contact [at] cropbazaar@gmail.com"
                )

                SectionCard(
                    title = "Office Address",
                    content = "PEAT - Progressive Environmental & Agricultural Technologies\nGovindpura 4\n10435 Bhopal, India"
                )
            }
        }
    )
}

@Composable
fun SectionCards(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF4CAF50) // Green for section titles
                ),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                textAlign = TextAlign.Justify
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun LicenseScreenPreview() {
    val navController = rememberNavController()
    LicenseScreen(navController = navController)
}

//
//@Preview(showBackground = true)
//@Composable
//fun LicenseScreenPreview() {
//    LicenseScreen()
//}
