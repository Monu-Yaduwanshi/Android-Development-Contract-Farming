package com.example.cropbazaar.Home
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedAuthViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _feedPosts = MutableStateFlow<List<FeedPost>>(emptyList())
    val feedPosts: StateFlow<List<FeedPost>> get() = _feedPosts

    fun ProfileInfo.toUserProfile(): UserProfile {
        return UserProfile(
            userId = "", // You can fill this in later if needed
            name = this.fullName,
            location = "", // If location exists in ProfileInfo, fill it
            contact = "",  // Add if available
            profileImageUrl = this.profileImageUrl,
            description = ""
        )
    }




    init {
        Log.d("FEED_DEBUG", "🔑 ViewModel initialized - Current User: ${auth.currentUser?.email}")
        fetchFeedData()
    }

    private fun fetchFeedData() {
        viewModelScope.launch {
            val allPosts = mutableListOf<FeedPost>()
            val sellRef = database.getReference("SellCropData")
            val buyRef = database.getReference("BuyCropData")
            val regRef = database.getReference("RegistrationData")
            val profileRef = database.getReference("ProfileData")

            val emailInfoMap = mutableMapOf<String, ProfileInfo>() // Key: lowercase email

            // Step 1: Fetch RegistrationData
            regRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uidToEmailMap = mutableMapOf<String, String>()

                    for (userNode in snapshot.children) {
                        val uid = userNode.key ?: continue
                        val dataMap = userNode.value as? Map<*, *> ?: continue
                        val email = dataMap["email"]?.toString()?.lowercase() ?: continue

                        val profile = ProfileInfo(
                            fullName = dataMap["fullName"]?.toString() ?: "Unknown",
                            userType = dataMap["userType"]?.toString() ?: "Unknown",
                            gmail = email,
                            profileImageUrl = "" // Add image later
                        )

                        emailInfoMap[email] = profile
                        uidToEmailMap[uid] = email

                        Log.d("FEED_DEBUG", "📨 RegistrationData: $email -> ${profile.fullName}")
                    }

                    // Step 2: Fetch ProfileData and update profile image using UID → email
                    profileRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (userNode in snapshot.children) {
                                val uid = userNode.key.orEmpty()
                                val imgUrl = userNode.child("profileImageUrl").getValue(String::class.java) ?: ""

                                val email = uidToEmailMap[uid]?.lowercase()
                                Log.d("FEED_DEBUG", "🖼️ ProfileData UID=$uid Email=$email ImageURL=$imgUrl")

                                if (!email.isNullOrEmpty() && emailInfoMap.containsKey(email)) {
                                    val profile = emailInfoMap[email]!!
                                    emailInfoMap[email] = profile.copy(profileImageUrl = imgUrl)
                                    Log.d("FEED_DEBUG", "✅ Matched profile image for $email")
                                }
                            }

                            emailInfoMap.forEach { (email, profile) ->
                                Log.d("FEED_DEBUG", "🔍 Final Profile: $email = $profile")
                            }

                            fetchSellPosts(sellRef, buyRef, emailInfoMap, allPosts)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("FEED_DEBUG", "❌ ProfileData error: ${error.message}")
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FEED_DEBUG", "❌ RegistrationData error: ${error.message}")
                }
            })
        }
    }

    private fun fetchSellPosts(
        sellRef: DatabaseReference,
        buyRef: DatabaseReference,
        emailInfoMap: Map<String, ProfileInfo>,
        allPosts: MutableList<FeedPost>
    ) {
        sellRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userNode in snapshot.children) {
                    val encodedEmail = userNode.key.orEmpty()
                    val email = encodedEmail.replace("_", ".").lowercase()
                    val profile = emailInfoMap[email] ?: ProfileInfo(fullName = "Unknown", userType = "Seller")

                    for (cropNode in userNode.children) {
                        val cropMap = cropNode.value as? Map<*, *> ?: continue
                        val cropInfo = parseCropMap(cropMap, email, isSell = true)
                        allPosts.add(FeedPost("Sell", cropInfo, profile, encodedEmail))
                    }
                }

                fetchBuyPosts(buyRef, emailInfoMap, allPosts)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FEED_DEBUG", "❌ SellCropData error: ${error.message}")
            }
        })
    }

    private fun fetchBuyPosts(
        buyRef: DatabaseReference,
        emailInfoMap: Map<String, ProfileInfo>,
        allPosts: MutableList<FeedPost>
    ) {
        buyRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userNode in snapshot.children) {
                    val encodedEmail = userNode.key.orEmpty()
                    val email = encodedEmail.replace("_", ".").lowercase()
                    val profile = emailInfoMap[email] ?: ProfileInfo(fullName = "Unknown", userType = "Buyer")

                    for (cropNode in userNode.children) {
                        val cropMap = cropNode.value as? Map<*, *> ?: continue
                        val cropInfo = parseCropMap(cropMap, email, isSell = false)
                        allPosts.add(FeedPost("Buy", cropInfo, profile, encodedEmail))
                    }
                }

                _feedPosts.value = allPosts
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FEED_DEBUG", "❌ BuyCropData error: ${error.message}")
            }
        })
    }

    private fun parseCropMap(map: Map<*, *>, email: String, isSell: Boolean): CropInfo {
        return CropInfo(
            category = map["category"] as? String ?: "Unknown",
            cropName = map["cropName"] as? String ?: "Unknown",
            variety = map["variety"] as? String ?: "Unknown",
            expectedPrice = map["expectedPrice"] as? String ?: "N/A",
            pinCode = map["pinCode"] as? String ?: "N/A",
            quantity = map["quantity"] as? String ?: "N/A",
            cropImageUrl = if (isSell) (map["cropImageUrl"] as? String ?: "") else "",
            userEmail = email,
            latitude = (map["latitude"] as? Number)?.toDouble() ?: 0.0,
            longitude = (map["longitude"] as? Number)?.toDouble() ?: 0.0,
            timestamp = map["timestamp"] as? String ?: "N/A"
        )
    }

    // ✅ Data Classes
    data class FeedPost(
        val type: String = "",
        val crop: CropInfo = CropInfo(),
        val userProfile: ProfileInfo = ProfileInfo(),
        val userUid: String = ""
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

    data class ProfileInfo(
        val fullName: String = "",
        val profileImageUrl: String = "",
        val userType: String = "",
        val gmail: String = ""
    )

    data class UserType(
        val email: String,
        val userType: String // "Buyer" or "Seller"
    )
}
