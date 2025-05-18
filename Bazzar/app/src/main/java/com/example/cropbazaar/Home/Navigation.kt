package com.example.cropbazaar.Home
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.cropbazaar.Sign_In_Up.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
        val navController = rememberNavController()
        val selectedItem = remember { mutableStateOf(0) }

        val profileViewModel: ProfileViewModel = viewModel()
        val feedAuthViewModel: FeedAuthViewModel = viewModel()

        NavHost(navController = navController, startDestination = "splash") {
              //  composable("payment_history") { PaymentHistoryScreen(navController) }
                composable("MsgNotifications") { NewNotificationScreen(navController) }
                composable("chatbot") { ChatBotScreen(navController) }
                composable("splash") { SplashScreen(navController) }
                composable("login") { LoginScreen(navController, authViewModel) }
                composable("signup") { SignUpScreen(navController, authViewModel) }
                composable("home") { HomeScreen(navController) }
                composable("KrishiGyaan") { KrishiGyaanScreen(navController) }
                composable("WeatherNews") { WeatherNewsScreen(navController) }
                composable("MarketPrice") { MarketPrice_Screen(navController) }
                composable("Schemes") { SchemesScreen(navController) }
                composable("Hardware") { HardwareScreen(navController) }
                composable("QAndA") { QAndAScreen(navController) }
                composable("mandi") { MandiScreen(navController) }
                composable("profile") { ProfileScreen(navController = navController) }
                composable("notifications") { FeedScreen(navController = navController, selectedItem = selectedItem) }
//                composable("payments") { PaymentsScreen(navController) }
                composable(
                        route = "payments/{postId}/{chatId}",
                        arguments = listOf(
                                navArgument("postId") { type = NavType.StringType },
                                navArgument("chatId") { type = NavType.StringType }
                        )
                ) { backStackEntry ->
                        val postId = backStackEntry.arguments?.getString("postId") ?: ""
                        val chatId = backStackEntry.arguments?.getString("chatId") ?: ""

                        val viewModel: ChatViewModel = viewModel()
                        val negotiationState = viewModel.negotiationData.collectAsState()

                        LaunchedEffect(Unit) {
                                viewModel.listenForNegotiation(postId, chatId)
                        }

                        negotiationState.value?.let { data ->
                                val price = data.finalPrice.toDoubleOrNull() ?: 0.0
                                val quantity = data.finalQuantity
                                        .replace("kg", "", ignoreCase = true)
                                        .replace(" ", "")
                                        .toDoubleOrNull() ?: 0.0
                                val totalAmount = price * quantity

                                PaymentsScreen(
                                        postId = postId,
                                        chatId = chatId,
                                        totalAmount = totalAmount,
                                        navController = navController,
                                        viewModel = viewModel
                                )
                        }
                }
                 composable("payment History") { PaymentHistoryScreen(navController) }
                composable("buy") { BuyScreen(navController) }
                composable("sell") { SellCropScreen(navController) }
                composable("Account") { AccountScreen(navController) }
                composable("My Farm") { MyFarmScreen(navController) }
                composable("Setting") { SettingsScreen(navController) }
                composable("my Crop") { MyCropScreen(navController) }
                composable("Feedback") { FeedbackScreen(navController) }
                composable("Terms and Conditions") { TermsAndConditionsScreen(navController) }
                composable("Contact & Social") { ContactAndSocialPage(navController) }
                composable("License") { LicenseScreen(navController) }
                composable("Share App") { ShareScreen(navController) }
                composable("Privacy Policy") { PrivacyPolicyScreen(navController) }
                composable("About Us") { AboutUsScreen(navController) }
                composable("Logout") { SignOutScreen(navController) }
                composable("chats") { ChatListScreen(navController = navController) }


                // ChatRoom with encoded arguments
                composable(
                        route = "chatRoom/{encoded}",
                        arguments = listOf(navArgument("encoded") { type = NavType.StringType })
                ) { backStackEntry ->
                        val encoded = backStackEntry.arguments?.getString("encoded") ?: ""
                        val args = decodeChatArgs(encoded)
                        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email.orEmpty()

                        ChatRoomScreen(
                                postId = args.postId,
                                chatId = args.chatId,
                                currentUserEmail = currentUserEmail,
                                receiverEmail = args.receiverEmail,
                                navController = navController
                        )
                }
                composable(
                        route = "loadPost/{postId}",
                        arguments = listOf(navArgument("postId") { type = NavType.StringType })
                ) { backStackEntry ->
                        val postId = backStackEntry.arguments?.getString("postId")?.let { Uri.decode(it) } ?: ""
                        LoadPostScreen(postId = postId, navController = navController)
                }

        }
}
