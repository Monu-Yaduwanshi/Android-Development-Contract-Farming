package com.example.cropbazaar.Home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
//import com.example.cropbazaar.model.FeedPost
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadPostScreen(
    postId: String,
    navController: NavController,
    feedViewModel: FeedAuthViewModel = viewModel()
) {
    val feedPosts by feedViewModel.feedPosts.collectAsState()
    val targetPost = feedPosts.find { it.crop.timestamp == postId }

    var editedPostContent by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (targetPost != null) {
            Column(modifier = Modifier
                .padding(padding)
                .padding(16.dp)
            ) {
                Text("Editing post from: ${targetPost.userProfile.fullName}")
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = editedPostContent.ifBlank { targetPost.crop.cropName },
                    onValueChange = { editedPostContent = it },
                    label = { Text("Crop Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(20.dp))
                Row {
                    Button(onClick = {
                        // TODO: Push to Firebase + notify other chat user
                        navController.navigate("payments")
                    }) {
                        Text("Final")
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(onClick = {
                        // Maybe go back without confirming
                        navController.popBackStack()
                    }) {
                        Text("Not Final")
                    }
                }
            }
        } else {
            Text("Post not found.", modifier = Modifier.padding(16.dp))
        }
    }
}
