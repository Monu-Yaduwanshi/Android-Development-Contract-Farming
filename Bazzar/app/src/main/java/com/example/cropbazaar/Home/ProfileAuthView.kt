package com.example.cropbazaar.Home

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val profileDatabase = FirebaseDatabase.getInstance().getReference("ProfileData")
    private val registrationDatabase = FirebaseDatabase.getInstance().getReference("RegistrationData")

    private val _profileData = MutableLiveData<ProfileData?>()
    val profileData: LiveData<ProfileData?> get() = _profileData

    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dfkfuassi/image/upload"
    private val cloudinaryUploadPreset = "thread"

    init {
        observeAuthState()
    }

    data class ProfileData(
        val firstName: String = "",
        val lastName: String = "",
        val mobileNumber: String = "",
        val gmail: String = "",
        val address: String = "",
        val language: String = "",
        val pinCode: String = "",
        val state: String = "",
        val profileImageUrl: String = "",
        val userType: String = "",
        val fullName: String = "",
        val contact: String = ""
    )

    private fun observeAuthState() {
        auth.addAuthStateListener {
            val user = it.currentUser
            if (user != null) {
                fetchRegistrationAndProfileData(user.uid)
            } else {
                _profileData.postValue(ProfileData())
            }
        }
    }

    private fun fetchRegistrationAndProfileData(uid: String) {
        registrationDatabase.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("REG_FETCH", "No registration data found.")
                    return
                }

                val dataMap = snapshot.value as? Map<*, *> ?: return

                val baseData = ProfileData(
                    gmail = dataMap["email"]?.toString() ?: "",
                    userType = dataMap["userType"]?.toString() ?: "",
                    address = dataMap["address"]?.toString() ?: "",
                    contact = dataMap["contact"]?.toString() ?: "",
                    fullName = dataMap["fullName"]?.toString() ?: ""
                )

                fetchAndMergeProfile(uid, baseData)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("REG_FETCH", "Error: ${error.message}")
            }
        })
    }

    private fun fetchAndMergeProfile(uid: String, baseData: ProfileData) {
        profileDatabase.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val savedProfile = snapshot.getValue(ProfileData::class.java)

                val finalProfile = baseData.copy(
                    firstName = savedProfile?.firstName ?: "",
                    lastName = savedProfile?.lastName ?: "",
                    mobileNumber = savedProfile?.mobileNumber ?: baseData.contact,
                    address = savedProfile?.address ?: baseData.address,
                    language = savedProfile?.language ?: "",
                    pinCode = savedProfile?.pinCode ?: "",
                    state = savedProfile?.state ?: "",
                    profileImageUrl = savedProfile?.profileImageUrl ?: ""
                )

                _profileData.postValue(finalProfile)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PROFILE_FETCH", "Error: ${error.message}")
            }
        })
    }

    fun saveUserProfile(profile: ProfileData): Boolean {
        val user = auth.currentUser ?: return false
        val uid = user.uid

        if (profile.firstName.isBlank() || profile.lastName.isBlank() ||
            profile.mobileNumber.length != 10 || profile.address.isBlank() ||
            profile.language.isBlank() || profile.pinCode.length != 6 ||
            profile.state.isBlank()
        ) {
            return false
        }

        profileDatabase.child(uid).setValue(profile)

        // Update only editable fields in RegistrationData
        registrationDatabase.child(uid).updateChildren(
            mapOf(
                "address" to profile.address,
                "contact" to profile.mobileNumber,
                "fullName" to "${profile.firstName} ${profile.lastName}"
            )
        )

        _profileData.postValue(profile)
        return true
    }

    fun uploadImageToCloudinary(imageUri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageFile = uriToFile(imageUri)
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)

                val request = Request.Builder()
                    .url(cloudinaryUrl)
                    .post(
                        MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("upload_preset", cloudinaryUploadPreset)
                            .addFormDataPart("file", imageFile.name, requestBody)
                            .build()
                    )
                    .build()

                val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val imageUrl = JSONObject(responseBody).getString("secure_url")
                    val updatedProfile = _profileData.value?.copy(profileImageUrl = imageUrl)
                    updatedProfile?.let {
                        _profileData.postValue(it)
                        saveUserProfile(it)
                    }
                } else {
                    Log.e("UPLOAD_IMAGE", "Upload failed: $responseBody")
                }
            } catch (e: Exception) {
                Log.e("UPLOAD_IMAGE", "Exception: ${e.message}")
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream: InputStream? = getApplication<Application>().contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload", ".jpg")
        FileOutputStream(file).use { output -> inputStream?.copyTo(output) }
        return file
    }

    fun logoutUser() {
        auth.signOut()
        _profileData.postValue(ProfileData())
    }
}
