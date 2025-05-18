plugins {
    id("com.google.gms.google-services")  // ✅ Google Services Plugin
    id("com.google.firebase.crashlytics") // ✅ Crashlytics Plugin (ADD THIS)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}


android {
    namespace = "com.example.cropbazaar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cropbazaar"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // ✅ Enable Firebase Crashlytics Mapping File Upload for release builds
            isDebuggable = false
            signingConfig = signingConfigs.getByName("debug")
            ext["firebaseCrashlytics.mappingFileUploadEnabled"] = true
        }

        debug {
            // ✅ Disable Crashlytics in Debug mode (Optional)
            isDebuggable = true
            ext["firebaseCrashlytics.mappingFileUploadEnabled"] = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}






//android {
//    namespace = "com.example.cropbazaar"
//    compileSdk = 35
//
//    defaultConfig {
//        applicationId = "com.example.cropbazaar"
//        minSdk = 24
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
//    buildFeatures {
//        compose = true
//    }
//}

//dependencies {
//
//    // AndroidX Core & Jetpack Compose Dependencies
//    implementation("androidx.core:core-ktx:1.12.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
//    implementation("androidx.activity:activity-compose:1.7.2")
//
//    // Jetpack Compose UI Dependencies
//    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3:1.2.0-alpha02")
//    implementation("androidx.compose.material:material-icons-extended:1.5.4")
//    implementation("androidx.compose.foundation:foundation:1.5.0")
//
//    // Navigation
//    implementation("androidx.navigation:navigation-compose:2.7.5")
//
//    // Splash Screen API
//    implementation("androidx.core:core-splashscreen:1.0.1")
//
//    // Retrofit (Network Calls)
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//
//    // OkHttp (For Networking)
//    implementation("com.squareup.okhttp3:okhttp:4.10.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
//
//    // Kotlin Coroutines (Async Processing)
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//
//    // Firebase BoM (Bill of Materials to manage versions automatically)
//    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
//
//    // Individual Firebase Services (No Version Needed!)
//    implementation("com.google.firebase:firebase-auth-ktx")
//    implementation("com.google.firebase:firebase-firestore-ktx")
//    implementation("com.google.firebase:firebase-storage-ktx")
//    implementation("com.google.firebase:firebase-messaging-ktx")
//    implementation("com.google.firebase:firebase-analytics-ktx")
//    implementation("com.google.firebase:firebase-crashlytics-ktx")
//    implementation("com.google.firebase:firebase-database-ktx")
//
//    // Coil (Image Loading for Jetpack Compose)
//    implementation("io.coil-kt:coil-compose:2.6.0")
//    implementation("io.coil-kt:coil-gif:2.6.0")
//    implementation(libs.androidx.runtime.livedata)
//    implementation(libs.androidx.appcompat)
//
//    // Testing Dependencies
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2024.01.00"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//
//
//
//        // Firebase BoM (Bill of Materials to manage versions)
//        implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
//
//        // ✅ Add Firebase Crashlytics dependency
//        implementation("com.google.firebase:firebase-crashlytics-ktx")
//    implementation("com.google.android.gms:play-services-location:21.0.1")
//
//
//
////
////    implementation("com.google.firebase:firebase-analytics")
////    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
//}

dependencies {
    // AndroidX Core & Jetpack Compose Dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Jetpack Compose UI Dependencies
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-alpha02")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    implementation("androidx.compose.foundation:foundation:1.5.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Splash Screen API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Retrofit (Network Calls)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp (For Networking)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Kotlin Coroutines (Async Processing)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Firebase BoM (Bill of Materials to manage versions automatically)
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))

    // Individual Firebase Services (No Version Needed!)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    // Coil (Image Loading for Jetpack Compose)
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("io.coil-kt:coil-gif:2.6.0")

    // LiveData and AppCompat libraries
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.appcompat)

    // Testing Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.01.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Firebase Crashlytics (to monitor app stability)
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    // Location Services (optional, if used in the app)
    implementation("com.google.android.gms:play-services-location:21.0.1")

}