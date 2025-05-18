//package com.example.cropbazaar.Home//package com.example.cropbazaar.DrawerContent//package com.example.cropBazaar.Home
//import android.annotation.SuppressLint
//import android.os.Build.VERSION.SDK_INT
//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import coil.ImageLoader
//import coil.compose.AsyncImage
//import coil.decode.GifDecoder
//import coil.decode.ImageDecoderDecoder
//import coil.request.ImageRequest
//import com.example.cropBazaar.R
//
//@SuppressLint("SuspiciousIndentation")
//@Composable
//fun GifDisplayScreen() {
//
//    val context = LocalContext.current
//
//    val gifEnabledLoader = ImageLoader.Builder(context)
//        .components {
//            if (SDK_INT >= 28) {
//                add(ImageDecoderDecoder.Factory())
//            } else {
//                add(GifDecoder.Factory())
//            }
//        }
//        .build()
//
//
//
//        AsyncImage(
//
//            model = ImageRequest.Builder(context)
//                .data(R.drawable.profiles)
//                .build(),
//            contentDescription = "Sample GIF",
//            imageLoader = gifEnabledLoader,
//            modifier = Modifier.size(250.dp)
//
//        )
//    }
//
