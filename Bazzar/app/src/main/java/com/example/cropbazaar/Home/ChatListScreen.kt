package com.example.cropbazaar.Home
//import ChatPreview
//import ChatViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatViewModel = viewModel(),
    selectedItem: MutableState<Int> = remember { mutableStateOf(1) }
) {
    val currentUser = Firebase.auth.currentUser
    val currentUserEmail = currentUser?.email ?: ""

    val chatsAsOwner by viewModel.chatsAsOwner.collectAsState()
    val chatsAsParticipant by viewModel.chatsAsParticipant.collectAsState()

    LaunchedEffect(currentUserEmail) {
        if (currentUserEmail.isNotBlank()) {
            viewModel.listenForChats(currentUserEmail)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chats", fontSize = 20.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7A9D54))
           )
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7A9D54))
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                selectedItem = selectedItem,
                modifier = Modifier.fillMaxWidth()
            )
        },
        containerColor = Color(0xFFE6F4EA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color(0xFFE6F4EA))
        ) {
            LazyColumn {
                if (chatsAsOwner.isNotEmpty()) {
                    item {
                        Text(
                            "📥 Messages Received on Your Posts",
                            fontSize = 16.sp,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    items(chatsAsOwner) { chat ->
                        ChatPreviewItem(chat = chat, navController = navController)
                    }
                }

                if (chatsAsParticipant.isNotEmpty()) {
                    item {
                        Text(
                            "📤 Messages You Sent to Other Posts",
                            fontSize = 16.sp,
                            color = Color(0xFF1565C0),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    items(chatsAsParticipant) { chat ->
                        ChatPreviewItem(chat = chat, navController = navController)
                    }
                }

                if (chatsAsOwner.isEmpty() && chatsAsParticipant.isEmpty()) {
                    item {
                        Text(
                            "No chats available yet.",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatPreviewItem(chat: ChatPreview, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                val navArgs = ChatNavArgs(
                    postId = chat.postId,
                    chatId = chat.chatId,
                    receiverEmail = chat.otherUserName,
                    imgUrl = chat.otherUserImage
                )
                val encodedArgs = encodeChatArgs(navArgs)
                navController.navigate("chatRoom/$encodedArgs")
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = chat.otherUserImage),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(chat.otherUserName, fontSize = 18.sp)
                Text(chat.lastMessage, fontSize = 14.sp, color = Color.Gray)
            }
            Text(
                formatTimestamp(chat.timestamp, "MMM dd"),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}