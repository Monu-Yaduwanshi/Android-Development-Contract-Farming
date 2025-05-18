package com.example.cropbazaar.Sign_In_Up

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(application: Application) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("RegistrationData")

    sealed class AuthState {
        object Loading : AuthState()
        object Success : AuthState()
        class Error(val message: String) : AuthState()
        object Idle : AuthState()
    }

    private val _loginState = MutableLiveData<AuthState>(AuthState.Idle)
    val loginState: LiveData<AuthState> get() = _loginState

    private val _signUpState = MutableLiveData<AuthState>(AuthState.Idle)
    val signUpState: LiveData<AuthState> get() = _signUpState

    fun login(email: String, password: String, context: Context, navController: Any) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = AuthState.Error("Please enter both email and password.")
            return
        }

        _loginState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                db.child(uid).get().addOnSuccessListener { snapshot ->
                    val userType = snapshot.child("userType").getValue(String::class.java)
                    if (userType.isNullOrEmpty()) {
                        _loginState.value = AuthState.Error("User type not found.")
                    } else {
                        _loginState.value = AuthState.Success
                    }
                }.addOnFailureListener {
                    _loginState.value = AuthState.Error("Could not fetch user type.")
                }
            }
            .addOnFailureListener { exception ->
                val message = when (exception) {
                    is FirebaseAuthInvalidUserException -> "Account does not exist."
                    is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password."
                    else -> "Login failed: ${exception.localizedMessage}"
                }
                _loginState.value = AuthState.Error(message)
            }
    }

    fun signUp(
        email: String,
        password: String,
        fullName: String,
        address: String,
        contact: String,
        userType: String,
        context: Context,
        navController: Any
    ) {
        if (email.isBlank() || password.isBlank() || fullName.isBlank()
            || address.isBlank() || contact.isBlank() || userType.isBlank()
        ) {
            _signUpState.value = AuthState.Error("All fields must be filled.")
            return
        }

        _signUpState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                val userMap = mapOf(
                    "fullName" to fullName,
                    "address" to address,
                    "contact" to contact,
                    "email" to email,
                    "userType" to userType
                )

                db.child(uid).setValue(userMap)
                    .addOnSuccessListener {
                        _signUpState.value = AuthState.Success
                    }
                    .addOnFailureListener {
                        _signUpState.value = AuthState.Error("Failed to save user data.")
                    }
            }
            .addOnFailureListener { exception ->
                val message = when (exception) {
                    is FirebaseAuthUserCollisionException -> "Email is already registered."
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email format."
                    else -> "Sign-up failed: ${exception.localizedMessage}"
                }
                _signUpState.value = AuthState.Error(message)
            }
    }

    fun resetLoginState() {
        _loginState.postValue(AuthState.Idle)
    }

    fun resetSignUpState() {
        _signUpState.postValue(AuthState.Idle)
    }
}
