//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    id("com.google.gms.google-services") version "4.4.2" apply false
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false
//    alias(libs.plugins.kotlin.compose) apply false
//}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false  // ✅ Ensure this is present
    id("com.google.firebase.crashlytics") version "2.9.9" apply false  // ✅ Add this line
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
