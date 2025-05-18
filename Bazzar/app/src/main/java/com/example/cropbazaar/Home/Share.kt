package com.example.cropbazaar.Home

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cropbazaar.R

// Function to handle share content
fun shareContent(platform: String, context: android.content.Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Check out this amazing app! https://www.example.com")
    }

    when (platform) {
        "whatsapp" -> shareIntent.setPackage("com.whatsapp")
        "facebook" -> shareIntent.setPackage("com.facebook.katana")
        "twitter" -> shareIntent.setPackage("com.twitter.android")
        "email" -> shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
    }

    try {
        ContextCompat.startActivity(context, shareIntent, null)
    } catch (e: Exception) {
        Toast.makeText(context, "App not installed or error occurred", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Share with Friends",
                        color = Color.White // White text
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                          //  imageVector = Icons.Default.ArrowBack, // Back arrow icon
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Default back arrow icon

                            contentDescription = "Back",
                            tint = Color.White // White icon
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50) // Green background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,


        ) {
            // "Share with Friends" Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.share), // Replace with your share icon resource
                    contentDescription = "Share Icon",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Share CropBazaar",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF4CAF50) // Green color
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // App Description
            Text(
                text = "Tell your friends about the great features of this app!",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Share Buttons for Different Platforms
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ShareButton(
                    drawableId = R.drawable.whatapp,
                    text = "WhatsApp",
                    onClick = { shareContent("whatsapp", context) }
                )
                Spacer(modifier = Modifier.width(20.dp))
                ShareButton(
                    drawableId = R.drawable.facebook,
                    text = "Facebook",
                    onClick = { shareContent("facebook", context) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ShareButton(
                    drawableId = R.drawable.twitter,
                    text = "Twitter",
                    onClick = { shareContent("twitter", context) }
                )
                Spacer(modifier = Modifier.width(20.dp))
                ShareButton(
                    drawableId = R.drawable.othershare,
                    text = "Other",
                    onClick = { shareContent("other", context) }
                )
            }
        }
    }
}

// Share Button Composable
@Composable
fun ShareButton(
    drawableId: Int,
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = text,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShareScreenPreview() {
    val navController = rememberNavController()
    ShareScreen(navController = navController)
}
