package com.example.cropbazaar.Home
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavHostController

@Composable
fun WeatherNewsScreen(navController: NavHostController, viewModel: WeatherNewsViewModel = viewModel()) {
    val weatherNews by viewModel.weatherNews.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(weatherNews) { news ->
                        WeatherNewsItem(news)
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherNewsItem(news: WeatherNews) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(news.imageUrl),
            contentDescription = news.title,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 8.dp), // Corrected "end: 8.dp" to "end = 8.dp"
            contentScale = ContentScale.Crop // Ensure proper import
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = news.title, style = MaterialTheme.typography.titleMedium)
            Text(text = news.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}
