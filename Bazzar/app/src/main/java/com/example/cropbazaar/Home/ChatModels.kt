package com.example.cropbazaar.Home

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


fun calculateTotalAmount(pricePerKg: Double, quantity: Double, unit: String): Double {
    val quantityInKg = when (unit.lowercase()) {
        "kg" -> quantity
        "quintal" -> quantity * 100
        "tonne" -> quantity * 1000
        else -> quantity
    }
    return pricePerKg * quantityInKg
}

// In ChatViewModel
internal fun getPhoneNumber(email: String): String {
    // TODO: Implement actual phone number lookup from database
    return "1234567890"
}

internal fun shouldShowPaymentButton(data: NegotiationData, currentUserType: String): Boolean {
    return currentUserType == "Buyer" &&
            data.buyerFinalized &&
            data.sellerFinalized &&
            !data.paymentTriggered
}

@IgnoreExtraProperties
data class ChatMessage(
    val sender: String = "",
    val message: String = "",
    val timestamp: String = ""
)

data class Order(
    val orderId: String = "",
    val postId: String = "",
    val chatId: String = "",
    val sellerEmail: String = "",
    val buyerEmail: String = "",
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "",
    val timestamp: Long = System.currentTimeMillis()  // Add this line
)


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
//    val timestamp: Long = System.currentTimeMillis()
//)
//
//data class ChatPreview(
//    val chatId: String,
//    val postId: String,
//    val otherUserName: String,
//    val otherUserImage: String,
//    val lastMessage: String,
//    val timestamp: String,
//    val negotiationFinalized: Boolean = false
//)
//
//data class NegotiationData(
//    val postId: String = "",
//    val buyerEmail: String = "",
//    val sellerEmail: String = "",
//    val finalPrice: String = "",
//    val finalQuantity: String = "",
//    val buyerFinalized: Boolean = false,
//    val sellerFinalized: Boolean = false,
//    val paymentTriggered: Boolean = false
//)
//
//data class CropInfo(
//    val category: String = "",
//    val cropName: String = "",
//    val variety: String = "",
//    val expectedPrice: String = "",
//    val pinCode: String = "",
//    val quantity: String = "",
//    val cropImageUrl: String = "",
//    val userEmail: String = "",
//    val latitude: Double = 0.0,
//    val longitude: Double = 0.0,
//    val timestamp: String = ""
//)