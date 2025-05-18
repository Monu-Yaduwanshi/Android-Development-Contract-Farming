package com.example.cropbazaar.Home
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cropbazaar.Sign_In_Up.AuthViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.cropbazaar.Sign_In_Up.AuthViewModel
//import com.example.cropbazaar.ViewModels.AuthViewModelFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize Firebase
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        // ✅ Crashlytics Debugging (Remove in production)
        FirebaseCrashlytics.getInstance().log("App started")
        FirebaseCrashlytics.getInstance().setUserId("test_user_123")
        FirebaseCrashlytics.getInstance().recordException(Exception("Test Exception"))

        setContent {
            CropBazaarTheme {
                // ✅ Fix: Ensure correct imports
                val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(application))
                AppNavigation(authViewModel) // Start Navigation
            }
        }
    }
}

