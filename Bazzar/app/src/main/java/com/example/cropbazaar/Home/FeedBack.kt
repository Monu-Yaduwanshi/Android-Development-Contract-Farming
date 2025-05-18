package com.example.cropbazaar.Home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cropbazaar.R

@Composable
fun FeedbackScreen(navController: NavController) {
    var selectedFeedback by remember { mutableStateOf<String?>(null) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCBD6AD)) // Lime green background
    ) {
        // Top Bar with Green Box and Back Arrow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4CAF50)) // Green background
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Default back arrow icon
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }, // Navigate back
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Feedback",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Feedback Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How is your experience with CropBazaar app?",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Feedback icons (Bad, Average, Good)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                FeedbackOption(
                    label = "Bad",
                    drawableId = R.drawable.badreview,
                    selectedFeedback = selectedFeedback,
                    onClick = { selectedFeedback = "Bad" }
                )
                FeedbackOption(
                    label = "Average",
                    drawableId = R.drawable.confused,
                    selectedFeedback = selectedFeedback,
                    onClick = { selectedFeedback = "Average" }
                )
                FeedbackOption(
                    label = "Good",
                    drawableId = R.drawable.smile,
                    selectedFeedback = selectedFeedback,
                    onClick = { selectedFeedback = "Good" }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add description box (TextField) for comments
            TextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp),
                label = { Text("Additional Comments (Optional)") },
                placeholder = { Text("Write your comments here...") },
                maxLines = 5,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons (Submit and Cancel)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        // Handle submit: use selectedFeedback and comment here
                        println("Feedback: $selectedFeedback, Comment: ${comment.text}")
                    },
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),  // Green background
                        contentColor = Color.White          // White text
                    )
                ) {
                    Text("Submit")
                }
                Button(
                    onClick = {
                        // Handle cancel, reset feedback and comment
                        selectedFeedback = null
                        comment = TextFieldValue("")
                    },
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),  // Green background
                        contentColor = Color.White          // White text
                    )
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun FeedbackOption(
    label: String,
    drawableId: Int,
    selectedFeedback: String?,
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
            contentDescription = label,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selectedFeedback == label) Color.Green else Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackScreenPreview() {
    val navController = rememberNavController()
    FeedbackScreen(navController = navController)
}
