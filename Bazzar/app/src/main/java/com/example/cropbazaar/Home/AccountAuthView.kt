package com.example.cropbazaar.Home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cropbazaar.Home.CropData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AccountAuthView (application: Application) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val registrationDB = FirebaseDatabase.getInstance().getReference("RegistrationData")
    private val sellDB = FirebaseDatabase.getInstance().getReference("SellCropData")
    private val buyDB = FirebaseDatabase.getInstance().getReference("BuyCropData")
    private val prefs = application.getSharedPreferences("account_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _userType = MutableLiveData<String?>()
    val userType: LiveData<String> get() = _userType as LiveData<String>

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    private val _sellCrops = MutableLiveData<List<CropData>>()
    val sellCrops: LiveData<List<CropData>> get() = _sellCrops

    private val _buyCrops = MutableLiveData<List<CropData>>()
    val buyCrops: LiveData<List<CropData>> get() = _buyCrops

    data class UserInfo(
        val fullName: String = "",
        val contact: String = "",
        val address: String = "",
        val email: String = "",
        val profileImageUrl: String = ""
    )

    init {
        loadFromCache()
        fetchUserProfile()
    }

    private fun loadFromCache() {
        val userJson = prefs.getString("user_info", null)
        val typeJson = prefs.getString("user_type", null)

        if (!userJson.isNullOrEmpty()) {
            val userInfo = gson.fromJson(userJson, UserInfo::class.java)
            _userInfo.value = userInfo
        }

        if (!typeJson.isNullOrEmpty()) {
            _userType.value = typeJson
        }

        val cropsJson = prefs.getString("cached_crops", null)
        if (!cropsJson.isNullOrEmpty()) {
            val cropListType = object : TypeToken<List<CropData>>() {}.type
            val cropList: List<CropData> = gson.fromJson(cropsJson, cropListType)
            if (_userType.value == "Farmer") {
                _sellCrops.value = cropList
            } else {
                _buyCrops.value = cropList
            }
        }
    }

    private fun fetchUserProfile() {
        val uid = auth.currentUser?.uid ?: return

        registrationDB.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fullName = snapshot.child("fullName").getValue(String::class.java) ?: ""
                val contact = snapshot.child("contact").getValue(String::class.java) ?: ""
                val address = snapshot.child("address").getValue(String::class.java) ?: ""
                val email = snapshot.child("email").getValue(String::class.java) ?: ""
                val userType = snapshot.child("userType").getValue(String::class.java) ?: "Farmer"
                val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java) ?: ""

                val userInfo = UserInfo(fullName, contact, address, email, profileImageUrl)
                _userInfo.value = userInfo
                _userType.value = userType

                prefs.edit().apply {
                    putString("user_info", gson.toJson(userInfo))
                    putString("user_type", userType)
                    apply()
                }

                if (userType == "Farmer") {
                    fetchSellCrops(email)
                } else {
                    fetchBuyCrops(email)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AccountAuthView", "Error loading user: ${error.message}")
            }
        })
    }

    private fun fetchSellCrops(email: String) {
        val emailKey = email.replace(".", "_")
        sellDB.child(emailKey).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cropList = snapshot.children.mapNotNull { it.getValue(CropData::class.java) }
                _sellCrops.value = cropList
                prefs.edit().putString("cached_crops", gson.toJson(cropList)).apply()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SellCrops", "Error: ${error.message}")
            }
        })
    }

    private fun fetchBuyCrops(email: String) {
        val emailKey = email.replace(".", "_")
        buyDB.child(emailKey).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cropList = snapshot.children.mapNotNull { it.getValue(CropData::class.java) }
                _buyCrops.value = cropList
                prefs.edit().putString("cached_crops", gson.toJson(cropList)).apply()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BuyCrops", "Error: ${error.message}")
            }
        })
    }
}
