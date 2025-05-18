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
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SellAuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val cropDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("SellCropData")
    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dfkfuassi/image/upload"
    private val cloudinaryUploadPreset = "thread"

    private val _sellCrops = MutableLiveData<List<CropData>>()
    val sellCrops: LiveData<List<CropData>> get() = _sellCrops

    init { fetchUserCrops() }

    // Updated CropData model with timestamp
    data class CropData(
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
        val timestamp: String = "" // Changed to String to store formatted timestamp
    )

    fun fetchUserCrops() {
        val currentUser = auth.currentUser ?: return
        val userEmail = currentUser.email ?: return
        val sanitizedEmail = userEmail.replace(".", "_")

        cropDatabase.child(sanitizedEmail).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val crops = mutableListOf<CropData>()
                snapshot.children.forEach { it.getValue(CropData::class.java)?.let { crop -> crops.add(crop) } }
                _sellCrops.postValue(crops)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SellAuthViewModel", "Database error: ${error.message}")
            }
        })
    }

    fun saveCropData(crop: CropData) {
        val currentUser = auth.currentUser ?: return
        val userEmail = currentUser.email ?: return
        val sanitizedEmail = userEmail.replace(".", "_")

        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()) // Format timestamp

        // Create a new crop object with the formatted timestamp
        val cropWithTimestamp = crop.copy(userEmail = userEmail, timestamp = timestamp)
        val ref = cropDatabase.child(sanitizedEmail).push()
        ref.setValue(cropWithTimestamp).addOnSuccessListener { fetchUserCrops() }
    }

    fun uploadImageToCloudinary(imageUri: Uri, crop: CropData) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageFile = uriToFile(imageUri)
                val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())

                val request = Request.Builder().url(cloudinaryUrl).post(
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("upload_preset", cloudinaryUploadPreset)
                        .addFormDataPart("file", imageFile.name, requestBody)
                        .build()
                ).build()

                val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

                val response = client.newCall(request).execute()
                val body = response.body?.string()

                if (response.isSuccessful && body != null) {
                    val imageUrl = JSONObject(body).getString("secure_url")
                    saveCropData(crop.copy(cropImageUrl = imageUrl))
                }
            } catch (e: Exception) {
                Log.e("SellAuthViewModel", "Upload failed: ${e.message}")
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload", ".jpg")
        FileOutputStream(file).use { output -> inputStream?.copyTo(output) }
        return file
    }

    fun logoutUser() {
        auth.signOut()
        _sellCrops.postValue(emptyList())
    }
}
