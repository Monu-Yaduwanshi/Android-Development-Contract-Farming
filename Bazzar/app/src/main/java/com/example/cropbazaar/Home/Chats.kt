package com.example.cropbazaar.Home
//import ChatViewModel
//import NegotiationData
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cropbazaar.Home.ChatMessage
//import com.example.cropbazaar.Home.ChatMessage
//import com.example.cropbazaar.Home.NegotiationData
//import com.example.cropbazaar.Home.FeedAuthViewModel.NegotiationData
//import com.example.cropbazaar.Home.ChatMessage
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType
import java.util.UUID


//working fine onlu edit and quantity conversion issue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    postId: String,
    chatId: String,
    currentUserEmail: String,
    receiverEmail: String,
    navController: NavController,
    viewModel: ChatViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    // State variables
    val messages by viewModel.messages.collectAsState()
    var message by remember { mutableStateOf("") }
    val negotiationData by viewModel.negotiationData.collectAsState()
    val profileData by profileViewModel.profileData.observeAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // Loading states
    var isLoading by remember { mutableStateOf(false) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var loadAttempts by remember { mutableStateOf(0) }

    // Negotiation editing states
    var isEditing by remember { mutableStateOf(false) }
    var editedPrice by remember { mutableStateOf("") }
    var editedQuantity by remember { mutableStateOf("") }
    var quantityUnit by remember { mutableStateOf("kg") }
    var unitExpanded by remember { mutableStateOf(false) }
    val totalAmount = remember(editedPrice, editedQuantity, quantityUnit) {
        viewModel.calculateTotalAmount(
            price = editedPrice.toDoubleOrNull() ?: 0.0,
            quantity = editedQuantity.toDoubleOrNull() ?: 0.0,
            unit = quantityUnit
        )
    }

    // Determine user type
    val currentUserType = remember(profileData) {
        profileData?.userType?.takeIf { it.isNotBlank() } ?:
        if (postId.startsWith(sanitizeEmail(currentUserEmail))) "Farmer" else "Buyer"
    }

    // Initialize values when negotiation data changes
    LaunchedEffect(negotiationData) {
        negotiationData?.let { data ->
            editedPrice = data.finalPrice
            val quantityParts = data.finalQuantity.split(" ")
            editedQuantity = quantityParts.firstOrNull() ?: ""
            quantityUnit = quantityParts.getOrNull(1) ?: "kg"
        }
    }

    // Load initial data
    LaunchedEffect(postId, chatId) {
        viewModel.listenForMessages(postId, chatId)
        viewModel.listenForNegotiation(postId, chatId)
    }

    // Handle loading post details
    val onLoadPostClick = {
        isLoading = true
        loadError = null
        viewModel.loadPostDetails(postId) { cropInfo ->
            isLoading = false
            if (cropInfo != null) {
                viewModel.updateNegotiation(
                    postId = postId,
                    chatId = chatId,
                    currentUserEmail = currentUserEmail,
                    currentUserType = currentUserType,
                    accepted = false,
                    finalPrice = cropInfo.expectedPrice,
                    finalQuantity = cropInfo.quantity
                )
            } else {
                loadError = if (loadAttempts < 2) {
                    "Failed to load details. Tap to try again."
                } else {
                    "Couldn't load post details. It may have been deleted."
                }
                loadAttempts++
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat Room", fontSize = 20.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    ChatOptionsMenu(
                        receiverEmail = receiverEmail,
                        onCallClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${getPhoneNumber(receiverEmail)}")
                            }
                            context.startActivity(intent)
                        },
                        onLoadPostClick = onLoadPostClick
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7A9D54)))
        },
        containerColor = Color(0xFFE6F4EA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            loadError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { if (loadAttempts < 3) onLoadPostClick() }
                )
            }

            // Negotiation Section
            negotiationData?.let { data ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Negotiation Details",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF7A9D54),
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { isEditing = !isEditing }) {
                                Icon(
                                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                                    contentDescription = if (isEditing) "Save" else "Edit",
                                    tint = Color(0xFF7A9D54)
                                )
                            }
                        }
                        Divider(color = Color.LightGray, thickness = 1.dp)

                        if (isEditing) {
                            // Editable fields
                            OutlinedTextField(
                                value = editedPrice,
                                onValueChange = { editedPrice = it },
                                label = { Text("Price per kg (₹)") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
                            )

                            BoxWithConstraints {
                                val halfWidth = maxWidth / 2 - 4.dp
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ExposedDropdownMenuBox(
                                        expanded = unitExpanded,
                                        onExpandedChange = { unitExpanded = it }
                                    ) {
                                        OutlinedTextField(
                                            value = quantityUnit,
                                            onValueChange = {},
                                            label = { Text("Unit") },
                                            readOnly = true,
                                            modifier = Modifier
                                                .width(halfWidth)
                                                .menuAnchor(),
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(
                                                    expanded = unitExpanded
                                                )
                                            },
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color.White,
                                                unfocusedContainerColor = Color.White
                                            )
                                        )
                                        ExposedDropdownMenu(
                                            expanded = unitExpanded,
                                            onDismissRequest = { unitExpanded = false }
                                        ) {
                                            listOf("kg", "quintal", "tonne").forEach { unit ->
                                                DropdownMenuItem(
                                                    text = { Text(unit) },
                                                    onClick = {
                                                        quantityUnit = unit
                                                        unitExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    OutlinedTextField(
                                        value = editedQuantity,
                                        onValueChange = { editedQuantity = it },
                                        label = { Text("Quantity") },
                                        modifier = Modifier.width(halfWidth),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White
                                        )
                                    )
                                }
                            }
                        } else {
                            // Display only
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Price:", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                                Text("₹${data.finalPrice}")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Quantity:", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                                Text(data.finalQuantity)
                            }
                        }

                        // Total amount
                        Text(
                            text = "Total Amount: ₹${"%.2f".format(totalAmount)}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Action buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if ((currentUserType == "Farmer" && !data.sellerFinalized) ||
                                (currentUserType == "Buyer" && !data.buyerFinalized)) {
                                Button(
                                    onClick = {
                                        viewModel.updateNegotiation(
                                            postId = postId,
                                            chatId = chatId,
                                            currentUserEmail = currentUserEmail,
                                            currentUserType = currentUserType,
                                            accepted = true,
                                            finalPrice = if (isEditing) editedPrice else data.finalPrice,
                                            finalQuantity = if (isEditing) "$editedQuantity $quantityUnit" else data.finalQuantity
                                        )
                                        isEditing = false
                                    },
                                    enabled = true,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A9D54)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Accept Terms")
                                }

                                Button(
                                    onClick = {
                                        viewModel.updateNegotiation(
                                            postId = postId,
                                            chatId = chatId,
                                            currentUserEmail = currentUserEmail,
                                            currentUserType = currentUserType,
                                            accepted = false,
                                            finalPrice = if (isEditing) editedPrice else data.finalPrice,
                                            finalQuantity = if (isEditing) "$editedQuantity $quantityUnit" else data.finalQuantity
                                        )
                                        isEditing = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Reject Terms")
                                }
                            }
                        }

                        if (shouldShowPaymentButton(data, currentUserType)) {
                            Button(
                                onClick = {
                                    val order = Order(
                                        orderId = UUID.randomUUID().toString(),
                                        postId = postId,
                                        chatId = chatId,
                                        sellerEmail = data.sellerEmail,
                                        buyerEmail = currentUserEmail,
                                        totalAmount = totalAmount,
                                        paymentMethod = "Pending",
                                        timestamp = System.currentTimeMillis()
                                    )
                                    viewModel.saveOrder(order)
                                    navController.navigate("payments/${postId}/${chatId}")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Proceed to Payment")
                            }
                        }

                        if (data.paymentTriggered) {
                            Text(
                                "✅ Payment Completed",
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        val statusText = when {
                            data.buyerFinalized && data.sellerFinalized -> "Deal Finalized!" to Color(0xFF4CAF50)
                            data.buyerFinalized || data.sellerFinalized -> "Waiting for counterparty" to Color(0xFFFFA000)
                            else -> "Negotiation in progress" to Color(0xFF2196F3)
                        }

                        Text(
                            text = statusText.first,
                            color = statusText.second,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Messages List
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
                items(messages.reversed()) { msg ->
                    ChatMessageItem(
                        msg = msg,
                        isMe = msg.sender == currentUserEmail,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            // Message Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type your message...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (message.isNotBlank()) {
                            viewModel.sendMessage(
                                postId = postId,
                                chatId = chatId,
                                senderEmail = currentUserEmail,
                                message = message,
                                receiverEmail = receiverEmail
                            )
                            message = ""
                            focusManager.clearFocus()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A9D54))
                ) {
                    Text("Send")
                }
            }
        }
    }
}

@Composable
private fun ChatMessageItem(
    msg: ChatMessage,
    isMe: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isMe) Color(0xFFDCF8C6) else Color.White
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (isMe) "You" else msg.sender.split("@").firstOrNull() ?: "User",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Text(text = msg.message, fontSize = 16.sp)
                Text(
                    text = formatTimestamp(msg.timestamp, "hh:mm a"),
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun ChatOptionsMenu(
    receiverEmail: String,
    onCallClick: () -> Unit,
    onLoadPostClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Call") },
                onClick = {
                    expanded = false
                    onCallClick()
                },
                leadingIcon = {
                    Icon(Icons.Default.Call, contentDescription = null)
                }
            )
            DropdownMenuItem(
                text = { Text("Load Post Details") },
                onClick = {
                    expanded = false
                    onLoadPostClick()
                },
                leadingIcon = {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
            )
        }
    }
}
