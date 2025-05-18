package com.example.cropbazaar.Home

data class Crop(
    val cropName: String = "",
    val category: String = "",  // ✅ ADD THIS
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

data class UserProfile(
    val userId: String = "",
    val name: String = "", // ✅ This is your full name
    val location: String = "",
    val contact: String = "",
    val profileImageUrl: String = "",
    val description: String = ""
)
