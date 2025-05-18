package com.example.cropbazaar.Home

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class BuyAuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val buyCropDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("BuyCropData")

    private val _buyCrops = MutableLiveData<List<CropData>>()
    val buyCrops: LiveData<List<CropData>> get() = _buyCrops

    /** ✅ Data Model */
    data class CropData(
        val category: String = "",
        val cropName: String = "",
        val variety: String = "",
        val expectedPrice: String = "",
        val pinCode: String = "",
        val quantity: String = "",
        val userEmail: String = "",
        val timestamp: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
    )

    init {
        fetchUserBuyCrops()
    }

    /** ✅ Save Buy Crop Request */
    fun saveBuyCropData(crop: CropData) {
        val currentUser = auth.currentUser ?: return
        val userEmail = currentUser.email ?: return
        val sanitizedEmail = userEmail.replace(".", "_")

        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val updatedCrop = crop.copy(
            userEmail = userEmail,
            timestamp = timestamp
        )

        val newCropRef = buyCropDatabase.child(sanitizedEmail).push()
        newCropRef.setValue(updatedCrop).addOnSuccessListener {
            fetchUserBuyCrops()
        }
    }

    /** ✅ Set location on crop object before saving */
    fun saveBuyCropWithLocation(crop: CropData, location: Location?) {
        val locCrop = if (location != null) {
            crop.copy(latitude = location.latitude, longitude = location.longitude)
        } else crop

        saveBuyCropData(locCrop)
    }

    /** ✅ Fetch All Buy Crop Requests for the Logged-In User */
    fun fetchUserBuyCrops() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _buyCrops.postValue(emptyList())
            return
        }

        val userEmail = currentUser.email ?: return
        val sanitizedEmail = userEmail.replace(".", "_")

        buyCropDatabase.child(sanitizedEmail).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cropsList = mutableListOf<CropData>()
                snapshot.children.forEach { data ->
                    data.getValue(CropData::class.java)?.let { cropsList.add(it) }
                }
                _buyCrops.postValue(cropsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BuyAuthViewModel", "Database error: ${error.message}")
            }
        })
    }

    fun logoutUser() {
        auth.signOut()
        _buyCrops.postValue(emptyList())
    }
}

