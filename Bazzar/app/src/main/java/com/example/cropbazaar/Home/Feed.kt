package com.example.cropbazaar.Home
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    navController: NavController,
    selectedItem: MutableState<Int>,
    feedViewModel: FeedAuthViewModel = viewModel()
) {
    val feedPosts by feedViewModel.feedPosts.collectAsState()
    val context = LocalContext.current
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email.orEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            if (feedPosts.isEmpty()) {
                Text(
                    text = "No crops available yet!",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(feedPosts) { feedPost ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                // Profile Info Row
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = rememberAsyncImagePainter(feedPost.userProfile.profileImageUrl),
                                        contentDescription = "Profile Image",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = feedPost.userProfile.fullName,
                                            fontSize = 17.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = feedPost.userProfile.userType.ifEmpty { feedPost.type },
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Crop Image (Only for Sell posts)
                                if (feedPost.crop.cropImageUrl.isNotEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(feedPost.crop.cropImageUrl),
                                        contentDescription = "Crop Image",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(250.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                                // Crop Info
                                Text("🌾 Crop: ${feedPost.crop.cropName}", fontSize = 18.sp)
                                Text("📁 Category: ${feedPost.crop.category}")
                                Text("🔖 Variety: ${feedPost.crop.variety}")
                                Text("📍 Location: ${feedPost.crop.pinCode}")
                                Text("💰 Price: ₹${feedPost.crop.expectedPrice}")
                                Text("📦 Quantity: ${feedPost.crop.quantity}")
                                Text("🧭 Latitude: ${feedPost.crop.latitude}")
                                Text("🧭 Longitude: ${feedPost.crop.longitude}")
                                Text("⏱️ Uploaded on: ${feedPost.crop.timestamp}")

                                Spacer(modifier = Modifier.height(12.dp))


                                Button(
                                    onClick = {
                                        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email.orEmpty()
                                        val ownerEmail = feedPost.crop.userEmail

                                        if (currentUserEmail == ownerEmail) {
                                            Toast.makeText(context, "🚫 You can't message yourself!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            val postId = feedPost.crop.timestamp
                                            val chatId = generateChatId(postId, currentUserEmail, ownerEmail)

                                            // ✅ Base64-safe argument passing
                                            val args = ChatNavArgs(
                                                postId = postId,
                                                chatId = chatId,
                                                receiverEmail = ownerEmail,
                                                imgUrl = feedPost.userProfile.profileImageUrl.ifBlank { "https://default.com/img.jpg" }
                                            )

                                            val encoded = encodeChatArgs(args)
                                            val route = "chatRoom/$encoded"

                                            Log.d("NAV_ROUTE", "Navigating to: $route")
                                            navController.navigate(route)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                                ) {
                                    Icon(Icons.Default.Message, contentDescription = "Message")
                                    Spacer(Modifier.width(6.dp))
                                    Text("Message")
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}
