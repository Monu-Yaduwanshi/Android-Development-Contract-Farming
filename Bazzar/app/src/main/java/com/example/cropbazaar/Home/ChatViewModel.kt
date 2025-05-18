package com.example.cropbazaar.Home
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.cropbazaar.Home.calculateTotalAmount

class ChatViewModel : ViewModel() {
    private val db = Firebase.database
    private val auth = Firebase.auth

    // State Flows
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _chatsAsOwner = MutableStateFlow<List<ChatPreview>>(emptyList())
    val chatsAsOwner: StateFlow<List<ChatPreview>> = _chatsAsOwner

    private val _chatsAsParticipant = MutableStateFlow<List<ChatPreview>>(emptyList())
    val chatsAsParticipant: StateFlow<List<ChatPreview>> = _chatsAsParticipant

    private val _negotiationData = MutableStateFlow<NegotiationData?>(null)
    val negotiationData: StateFlow<NegotiationData?> = _negotiationData

    // Message Functions
    fun listenForMessages(postId: String, chatId: String) {
        db.getReference("NegotiationChats/$postId/$chatId/messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val msgList = mutableListOf<ChatMessage>()
                    snapshot.children.forEach { msgSnapshot ->
                        msgSnapshot.getValue(ChatMessage::class.java)?.let {
                            msgList.add(it)
                        }
                    }
                    _messages.value = msgList.sortedBy { it.timestamp }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatViewModel", "Messages load error", error.toException())
                }
            })
    }
    fun saveOrder(order: Order) {
        val database = Firebase.database.reference
        database.child("orders").child(order.orderId).setValue(order)
    }

    fun sendMessage(
        postId: String,
        chatId: String,
        senderEmail: String,
        message: String,
        receiverEmail: String
    ) {
        if (message.isBlank()) return

        val timestamp = System.currentTimeMillis().toString()
        val ref = db.getReference("NegotiationChats/$postId/$chatId")

        val metaRef = ref.child("metadata")
        val msgRef = ref.child("messages").push()

        metaRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                currentData.child("lastMessage").value = message
                currentData.child("timestamp").value = timestamp
                if (!currentData.hasChild("user1")) {
                    currentData.child("user1").value = sanitizeEmail(senderEmail)
                }
                if (!currentData.hasChild("user2")) {
                    currentData.child("user2").value = sanitizeEmail(receiverEmail)
                }
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                if (error == null) {
                    msgRef.setValue(ChatMessage(
                        sender = senderEmail,
                        message = message,
                        timestamp = timestamp
                    ))
                } else {
                    Log.e("ChatViewModel", "Message send failed", error.toException())
                }
            }
        })
    }



    fun listenForChats(currentUserEmail: String) {
        val ref = db.getReference("NegotiationChats")
        val currentUserSafe = sanitizeEmail(currentUserEmail)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ownerChats = mutableListOf<ChatPreview>()
                val participantChats = mutableListOf<ChatPreview>()

                snapshot.children.forEach { postNode ->
                    val postId = postNode.key ?: return@forEach
                    postNode.children.forEach { chatNode ->
                        val metadata = chatNode.child("metadata")
                        val user1 = metadata.child("user1").getValue(String::class.java) ?: return@forEach
                        val user2 = metadata.child("user2").getValue(String::class.java) ?: return@forEach

                        if (currentUserSafe == user1 || currentUserSafe == user2) {
                            val chatId = chatNode.key ?: return@forEach
                            val lastMessage = metadata.child("lastMessage").getValue(String::class.java) ?: ""
                            val timestamp = metadata.child("timestamp").getValue(String::class.java) ?: ""
                            val otherUser = if (currentUserSafe == user1) user2 else user1

                            val preview = ChatPreview(
                                chatId = chatId,
                                postId = postId,
                                otherUserName = unsanitizeEmail(otherUser),
                                otherUserImage = getProfileImageUrl(otherUser),
                                lastMessage = lastMessage,
                                timestamp = timestamp
                            )

                            if (postId.startsWith(currentUserSafe)) {
                                ownerChats.add(preview)
                            } else {
                                participantChats.add(preview)
                            }
                        }
                    }
                }

                _chatsAsOwner.value = ownerChats.sortedByDescending { it.timestamp }
                _chatsAsParticipant.value = participantChats.sortedByDescending { it.timestamp }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatViewModel", "Chats load error", error.toException())
            }
        })
    }
    // Negotiation Functions
    fun listenForNegotiation(postId: String, chatId: String) {
        db.getReference("NegotiationChats/$postId/$chatId/negotiationData")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(NegotiationData::class.java)
                    _negotiationData.value = data ?: NegotiationData(
                        postId = postId,
                        buyerEmail = "",
                        sellerEmail = "",
                        buyerFinalized = false,
                        sellerFinalized = false
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatViewModel", "Negotiation load error", error.toException())
                }
            })
    }

    fun updateNegotiation(
        postId: String,
        chatId: String,
        currentUserEmail: String,
        currentUserType: String,
        accepted: Boolean,
        finalPrice: String? = null,
        finalQuantity: String? = null
    ) {
        val ref = db.getReference("NegotiationChats/$postId/$chatId")

        // First, fetch metadata to determine roles
        ref.child("metadata").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user1 = snapshot.child("user1").getValue(String::class.java) ?: return
                val user2 = snapshot.child("user2").getValue(String::class.java) ?: return

                val safeCurrent = sanitizeEmail(currentUserEmail)
                val isSeller = (safeCurrent == user1 && currentUserType == "Farmer") ||
                        (safeCurrent == user2 && currentUserType == "Farmer")

                ref.child("negotiationData").runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        var data = currentData.getValue(NegotiationData::class.java)
                            ?: NegotiationData(postId = postId)

                        data = if (isSeller) {
                            data.copy(
                                sellerFinalized = accepted || data.sellerFinalized,
                                sellerEmail = if (data.sellerEmail.isBlank()) currentUserEmail else data.sellerEmail
                            )
                        } else {
                            data.copy(
                                buyerFinalized = accepted || data.buyerFinalized,
                                buyerEmail = if (data.buyerEmail.isBlank()) currentUserEmail else data.buyerEmail
                            )
                        }

                        finalPrice?.let { data = data.copy(finalPrice = it) }
                        finalQuantity?.let { data = data.copy(finalQuantity = it) }

                        currentData.value = data
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(error: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                        if (error != null) {
                            Log.e("ChatViewModel", "Negotiation update failed", error.toException())
                        }
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatViewModel", "Metadata fetch failed", error.toException())
            }
        })
    }


    // Post Loading Functions
    fun loadPostDetails(postId: String, callback: (CropInfo?) -> Unit) {
        viewModelScope.launch {
            Log.d("ChatViewModel", "Loading post: ${postId.trim()}")

            db.getReference("Posts")
                .orderByChild("timestamp")
                .equalTo(postId.trim())
                .limitToFirst(1)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.firstOrNull()?.getValue(CropInfo::class.java)?.let {
                            Log.d("ChatViewModel", "Found in Posts collection")
                            callback(it)
                            return
                        }
                        searchAllUserPosts(postId.trim(), callback)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("ChatViewModel", "Posts query failed", error.toException())
                        searchAllUserPosts(postId.trim(), callback)
                    }
                })
        }
    }

    private fun searchAllUserPosts(postId: String, callback: (CropInfo?) -> Unit) {
        val collections = listOf("BuyCropData", "SellCropData")
        val tasks = collections.map { collection ->
            db.getReference(collection).get()
        }

        Tasks.whenAllSuccess<DataSnapshot>(tasks)
            .addOnSuccessListener { snapshots ->
                for ((index, snapshot) in snapshots.withIndex()) {
                    snapshot.children.forEach { userNode ->
                        userNode.children.forEach { postNode ->
                            postNode.getValue(CropInfo::class.java)?.let { cropInfo ->
                                if (cropInfo.timestamp.trim() == postId) {
                                    Log.d("ChatViewModel", "Found in ${collections[index]} under user ${userNode.key}")
                                    callback(cropInfo)
                                    return@addOnSuccessListener
                                }
                            }
                        }
                    }
                }
                Log.d("ChatViewModel", "Post not found in any collection")
                callback(null)
            }
            .addOnFailureListener { e ->
                Log.e("ChatViewModel", "Legacy query failed", e)
                callback(null)
            }
    }

    // Utility Functions
    private fun sanitizeEmail(email: String): String {
        return email.replace(".", "_")
    }

    private fun unsanitizeEmail(safeEmail: String): String {
        return safeEmail.replace("_", ".")
    }

    private fun getProfileImageUrl(email: String): String {
        return "https://api.dicebear.com/7.x/initials/png?seed=${email.substringBefore('@')}"
    }
    fun calculateTotalAmount(price: Double, quantity: Double, unit: String): Double {
        return calculateTotalAmount(pricePerKg = price, quantity = quantity, unit = unit)
    }


//    fun calculateTotalAmount(pricePerQuintal: String, quantity: String): Double {
//        return try {
//            val price = pricePerQuintal.toDouble()
//            val qty = quantity.toDouble()
//            price * qty
//        } catch (e: NumberFormatException) {
//            0.0
//        }
//    }
}

// Data Classes

//data class ChatMessage(
//    val sender: String = "",
//    val message: String = "",
//    val timestamp: String = ""
//)

data class ChatPreview(
    val chatId: String,
    val postId: String,
    val otherUserName: String,
    val otherUserImage: String,
    val lastMessage: String,
    val timestamp: String,
    val negotiationFinalized: Boolean = false
)

//data class NegotiationData(
//    val postId: String = "",
//    val buyerEmail: String = "",
//    val sellerEmail: String = "",
//    val finalPrice: String = "",
//    val finalQuantity: String = "",
//    val buyerFinalized: Boolean = false,
//    val sellerFinalized: Boolean = false,
//    val isPaymentTriggered: Boolean = false
//)
data class NegotiationData(
    val postId: String = "",
    val buyerEmail: String = "",
    val sellerEmail: String = "",
    val finalPrice: String = "",

    val unit: String = "kg", // <-- this line must be there
    val finalQuantity: String = "",
    val buyerFinalized: Boolean = false,
    val sellerFinalized: Boolean = false,
    val paymentTriggered: Boolean = false
)

data class CropInfo(
    val category: String = "",
    val cropName: String = "",
    val variety: String = "",
    val expectedPrice: String = "",
    val pinCode: String = "",
    val quantity: String = "",
    val cropImageUrl: String = "",
    val userEmail: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: String = ""
)
// In ChatViewModel
fun saveOrder(order: Order) {
    val database = Firebase.database.reference
    database.child("orders").child(order.orderId).setValue(order)
}
//// Add to ChatViewModel:
//fun saveOrder(order: Order) {
//    val database = Firebase.database.reference
//    database.child("orders").child(order.orderId).setValue(order)
//}
//
//fun calculateTotalAmount(pricePerKg: Double, quantity: Double, unit: String): Double {
//    val quantityInKg = when (unit.lowercase()) {
//        "kg" -> quantity
//        "quintal" -> quantity * 100
//        "tonne" -> quantity * 1000
//        else -> quantity
//    }
//    return pricePerKg * quantityInKg
//}
//
//private fun getPhoneNumber(email: String): String {
//    // TODO: Implement actual phone number lookup from database
//    return "1234567890"
//}
//
//internal fun shouldShowPaymentButton(data: NegotiationData, currentUserType: String): Boolean {
//    return currentUserType == "Buyer" &&
//            data.buyerFinalized &&
//            data.sellerFinalized &&
//            !data.paymentTriggered
//}
//
//@IgnoreExtraProperties
//data class ChatMessage(
//    val sender: String = "",
//    val message: String = "",
//    val timestamp: String = ""
//)
//
//data class Order(
//    val orderId: String = "",
//    val postId: String = "",
//    val chatId: String = "",
//    val sellerEmail: String = "",
//    val buyerEmail: String = "",
//    val totalAmount: Double = 0.0,
//    val paymentMethod: String = "",
//)