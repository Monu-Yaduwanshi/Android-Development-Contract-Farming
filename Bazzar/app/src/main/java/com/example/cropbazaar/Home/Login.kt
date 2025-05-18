package com.example.cropbazaar.Home

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.cropbazaar.R
import com.example.cropbazaar.Sign_In_Up.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val gifEnabledLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var selectedUserType by remember { mutableStateOf("") }

    val loginState by authViewModel.loginState.observeAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is AuthViewModel.AuthState.Success -> {
                navController.navigate("home") {
                    popUpTo("LoginScreen") { inclusive = true }
                }
                authViewModel.resetLoginState()
            }
            is AuthViewModel.AuthState.Error -> {
                val message = (loginState as AuthViewModel.AuthState.Error).message
                Log.e("LoginScreen", message)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show() // 👈 Add this
                authViewModel.resetLoginState()
            }
            else -> {}
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCBD6AD))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color(0xFF825534))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.logo)
                    .build(),
                contentDescription = "Company Logo",
                imageLoader = gifEnabledLoader,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val customColor = Color(0xFF455A64)
        val buttonTextColor = Color(0xffededed)
        val linkTextColor = Color(0xFF455A64)

        Text(
            text = "Sign-In",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter ID", color = customColor) },
            modifier = Modifier.fillMaxWidth().padding(start = 40.dp),
            leadingIcon = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.login)
                        .build(),
                    contentDescription = "Email Icon",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = customColor,
                cursorColor = customColor,
                focusedBorderColor = customColor,
                unfocusedBorderColor = customColor,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter Password", color = customColor) },
            modifier = Modifier.fillMaxWidth().padding(start = 40.dp),
            leadingIcon = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.twoverification)
                        .build(),
                    contentDescription = "Password Icon",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Hide" else "Show", color = customColor)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = customColor,
                cursorColor = customColor,
                focusedBorderColor = customColor,
                unfocusedBorderColor = customColor,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        UserTypeSelector(selectedUserType, gifEnabledLoader) { selectedUserType = it }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedUserType.isNotEmpty()) {
                    authViewModel.login(email, password, context, navController)
                } else {
                    Log.e("LoginScreen", "User type not selected.")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = customColor,
                contentColor = buttonTextColor
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            Text(text = "Sign-In")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Don't have an account? Sign Up",
            color = linkTextColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navController.navigate("signup")
            }
        )
    }
}

@Composable
fun UserTypeSelector(
    selectedUserType: String,
    gifEnabledLoader: ImageLoader,
    onUserTypeSelected: (String) -> Unit
) {
    val userTypes = listOf(
        "Farmer" to R.drawable.farmer,
        "Buyer" to R.drawable.businessman,
      //  "Delivery Man" to R.drawable.logisticsassistant
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        userTypes.forEach { (userType, iconResId) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onUserTypeSelected(userType) }
                    .background(
                        if (selectedUserType == userType) Color.LightGray else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(iconResId)
                        .build(),
                    contentDescription = "$userType Icon",
                    imageLoader = gifEnabledLoader,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = userType)
            }
        }
    }
}
