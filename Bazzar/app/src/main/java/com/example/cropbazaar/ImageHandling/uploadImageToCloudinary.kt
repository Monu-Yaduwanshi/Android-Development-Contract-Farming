//package com.example.cropbazaar.ImageHandling
//
//import android.content.Context
//import android.net.Uri
//import android.util.Log
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import java.io.File
//
//fun uploadImageToCloudinary(
//    context: Context,
//    imageUri: Uri,
//    uploadPreset: String,
//    onResult: (String?) -> Unit
//) {
//    val cloudName = "dfkfuassi"  // Replace with your Cloudinary Cloud Name
//    val contentResolver = context.contentResolver
//    val inputStream = contentResolver.openInputStream(imageUri)
//    val file = File(context.cacheDir, "temp_image.jpg").apply {
//        outputStream().use { inputStream?.copyTo(it) }
//    }
//    val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
//    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//    val presetRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), uploadPreset)
//
//    CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val api = RetrofitInstance.getClient()
//            val response = api.uploadImage(cloudName, body, presetRequestBody)
//            if (response.isSuccessful) {
//                val cloudinaryResponse = response.body()
//                val secureUrl = cloudinaryResponse?.secure_url
//                onResult(secureUrl)
//            } else {
//                Log.e("Cloudinary", "Error: ${response.errorBody()?.string()}")
//                onResult(null)
//            }
//        } catch (e: Exception) {
//            Log.e("Cloudinary", "Exception: ${e.message}")
//            onResult(null)
//        }
//    }
//}
