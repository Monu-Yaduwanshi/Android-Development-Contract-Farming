package com.example.cropbazaar.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cropbazaar.R

// Note: You'll need to customize your color scheme to include green
val AboutUsColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50), // Vibrant green
    onPrimary = Color.White,
    background = Color(0xFFCBD6AD), // Light green (#FFF1F7E9)
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(navController: NavController) {
    MaterialTheme(colorScheme = AboutUsColorScheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("About Us") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Default back arrow icon
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { AboutUsHeader() }
                item { AboutUsMissionVision() }
                item { AboutUsFeatures() }
            }
        }
    }
}

// Rest of the code remains the same as in the previous implementation

@Composable
fun AboutUsHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Assuming a logo drawable
            contentDescription = "CropBazaar Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Welcome to CropBazaar",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Empowering Farmers for a Sustainable Future",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun AboutUsMissionVision() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Our Mission",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "To create a comprehensive platform that ensures market access, income stability, and transparency for farmers through technology-driven solutions.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Our Vision",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "To revolutionize agriculture with innovative tools, enabling farmers to grow, manage, and sell crops effectively in a sustainable ecosystem.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun AboutUsFeatures() {
    val features = listOf(
        FeatureItem("MyCrop", Icons.Default.Grass, "Track and manage your crops efficiently."),
        FeatureItem("Krishi Gyan", Icons.AutoMirrored.Filled.MenuBook, "Access expert agricultural knowledge."),
        FeatureItem("Crop Care", Icons.Default.Spa, "Ensure crop health with our tools."),
        FeatureItem("Weather", Icons.Default.CloudCircle, "Get accurate weather forecasts."),
        FeatureItem("Market Price", Icons.Default.MonetizationOn, "Stay updated on market prices."),
        FeatureItem("Schemes", Icons.Default.Policy, "Explore government schemes for farmers."),
        FeatureItem("Hardware", Icons.Default.Build, "Discover agricultural tools and hardware."),
        FeatureItem("Mandi", Icons.Default.Store, "Buy and sell crops with trusted buyers.")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Features",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        features.forEach { feature ->
            FeatureCard(feature)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FeatureCard(feature: FeatureItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Optional: Add feature-specific action */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = feature.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = feature.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class FeatureItem(
    val title: String,
    val icon: ImageVector,
    val description: String
)
