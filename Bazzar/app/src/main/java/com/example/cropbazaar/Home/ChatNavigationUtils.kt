package com.example.cropbazaar.Home

import android.util.Base64
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: String, pattern: String): String {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val date = Date(timestamp.toLong())
        sdf.format(date)
    } catch (e: Exception) {
        timestamp
    }
}
data class FinalNegotiation(
    val postId: String,
    val buyerEmail: String,
    val sellerEmail: String,
    val finalPrice: String,
    val finalQuantity: String,
    val buyerConfirmed: Boolean = false,
    val sellerConfirmed: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
