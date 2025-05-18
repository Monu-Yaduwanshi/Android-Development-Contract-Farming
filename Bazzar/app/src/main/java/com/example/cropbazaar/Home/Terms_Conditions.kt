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
fun TermsAndConditionsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Terms and Conditions",
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
                    .background(Color(0xFFCBD6AD)) // Lime Green background
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
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text(
//                                text = "CropBazaar",
//                                style = MaterialTheme.typography.headlineMedium,
//                                color = Color(0xFFffff), // Dark Green
//                                fontWeight = FontWeight.Bold,
//                                textAlign = TextAlign.Center
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Terms and Conditions",
//                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
//                                color = Color(0xFFffff),
//                                textAlign = TextAlign.Center
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
                                text = "Terms and Conditions",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Introductory Paragraph
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "THIS DOCUMENT IS PUBLISHED IN ACCORDANCE WITH THE PROVISIONS OF RULE 3 (1) OF THE INFORMATION TECHNOLOGY (INTERMEDIARIES GUIDELINES) RULES, 2011 THAT REQUIRE PUBLISHING THE RULES AND REGULATIONS, PRIVACY POLICY AND TERMS OF USE FOR ACCESS OR USAGE OF THE PLATFORM.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sections with Cards
                SectionCard(title = "Eligibility Criteria", content = "You represent and warrant that you have the right to access or use the Platform. The Platform can only be availed by those individuals or business entities, including sole proprietorship firms, companies and partnerships, which can form legally binding contracts under Indian Contract Act, 1872...\"\n")
                SectionCard(title = "Registration Process", content = "The User shall register on the Platform by providing information including but not limited to name and mobile number etc. We shall send a one-time password (“OTP”) to the mobile number provided by You. On completion of verification of mobile number, the User shall be registered on the Platform.\"\n")
                SectionCard(title = "Account and Registration Obligations", content = "If you use the platform, you shall be responsible for maintaining the confidentiality of your account and password and shall be responsible for all activities that occur in or throughout your account...\"\n")
                SectionCard(title = "User Liability", content = "The User, by the act of logging onto the platform, is deemed to have consented to and has expressly/impliedly and irrevocably authorized Harvesting to use, reveal, analyze, display, or transmit all information required by Harvesting.\"\n")
                SectionCard(title = "Communication", content = "By agreeing to these Terms, you acknowledge that you are interested in availing or purchasing products or services that you have selected and consent to receive communications via electronic records from us periodically as and when required.\"\n")

                Spacer(modifier = Modifier.height(16.dp))

                // Acknowledgement Section
                Text(
                    text = "By accessing or using the Platform of the Company, you agree to be bound by these Terms.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF4CAF50), // Green text for emphasis
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }
        }
    )
}

@Composable
fun SectionCard(title: String, content: String) {
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
fun TermsAndConditionsScreenPreview() {
    val navController = rememberNavController()
    TermsAndConditionsScreen(navController = navController)
}
//
//@Preview(showBackground = true)
//@Composable
//fun TermsAndConditionsPreview() {
//    TermsAndConditionsScreen()
//}

