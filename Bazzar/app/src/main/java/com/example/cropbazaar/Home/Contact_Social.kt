package com.example.cropbazaar.Home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cropbazaar.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactAndSocialPage(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Contact & Social",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF2E7D32) // Dark Green
            )
        )


        // Social Media Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFCBD6AD)) // Add your desired background color here
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.share), // Add your social media icon
                        contentDescription = "Social Media",
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Social Media",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                SocialMediaItem(
                    iconId = R.drawable.facebook, // Replace with your Facebook drawable
                    name = "Facebook",
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"))
                        context.startActivity(intent)
                    }
                )
                SocialMediaItem(
                    iconId = R.drawable.twitter, // Replace with your Twitter drawable
                    name = "X (Twitter)",
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com"))
                        context.startActivity(intent)
                    }
                )
                SocialMediaItem(
                    iconId = R.drawable.youtube, // Replace with your YouTube drawable
                    name = "YouTube",
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                        context.startActivity(intent)
                    }
                )
                SocialMediaItem(
                    iconId = R.drawable.instagram, // Replace with your Instagram drawable
                    name = "Instagram",
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"))
                        context.startActivity(intent)
                    }
                )
            }
        }

        // Contact Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFCBD6AD)), // Add your desired background color here

                    shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.share), // Add your contact icon
                        contentDescription = "Contact Us",
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Contact Us",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                ContactItem(
                    iconId = R.drawable.location, // Replace with your location drawable
                    content = "Cropbazaar Technology Pvt Ltd\nC-64 Bharati Niketan\nGovindpura Hosangabad Road\nBhopal 462023\nGSTIN No: 1234567890",
                    onClick = { /* Open Maps */ }
                )

                ContactItem(
                    iconId = R.drawable.phonecall, // Replace with your phone drawable
                    content = "+91 78284548052",
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+917828454805"))
                        context.startActivity(intent)
                    }
                )

                ContactItem(
                    iconId = R.drawable.email, // Replace with your email drawable
                    content = "monuyaduvanshi2003@gmail.com\nSend us an email",
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:monuyaduvanshi2003@gmail.com")
                        }
                        context.startActivity(intent)
                    }
                )

                ContactItem(
                    iconId = R.drawable.internet, // Replace with your website drawable
                    content = "www.Cropbazaar.com\nVisit our website",
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cropbazaar.com"))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun SocialMediaItem(
    iconId: Int,
    name: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = name,
            modifier = Modifier.size(32.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = name,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun ContactItem(
    iconId: Int,
    content: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = content,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactAndSocialPagePreview() {
    val navController = rememberNavController()
    ContactAndSocialPage(navController = navController)
}

//// Preview
//@Composable
//@Preview(showBackground = true)
//fun ContactAndSocialPagePreview() {
//    ContactAndSocialPage()
//}