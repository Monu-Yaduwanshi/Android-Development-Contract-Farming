package com.example.cropbazaar.Home
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cropbazaar.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController)
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            bottomBar = { BottomNavBar(navController, selectedItem) }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF68cb66))
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            SearchBar()
                            Spacer(modifier = Modifier.height(15.dp))
//                            IconRow(navController = navController)
                        }
                    }

                    // ✅ Inject NotificationsScreen below SearchBar
                    FeedScreen(
                        navController = navController,
                        selectedItem = selectedItem
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(navController: NavController, onMenuClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF189f5d)) // Background color
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo2),
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CropBazaar",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
            Row {
                IconButton(onClick = { navController.navigate("MsgNotifications") }) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { navController.navigate("chatbot") }) {
                    Icon(
                        imageVector = Icons.Filled.SupportAgent,
                        contentDescription = "Support",
                        tint = Color.White
                    )

//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ContactSupport,
//                        contentDescription = "Chatbot",
//                        tint = Color.White
//                    )
                }
            }

//            Row {
//                IconButton(onClick = { navController.navigate("chats") }) {
//                    Icon(
//                        imageVector = Icons.Filled.Notifications,
//                        contentDescription = "Notifications",
//                        tint = Color.White
//                    )
//                }
//                IconButton(onClick = { navController.navigate("chats") }) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.SupportAgent,
//                        contentDescription = "chats",
//                        tint = Color.White
//                    )
//                }
//            }
        }
        Text(
            text = "                        Crop prices at your fingertips", // Tagline
            style = MaterialTheme.typography.bodySmall,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun DrawerContent(navController: NavController) {
    val menuItems = listOf(
        "profile", "My Farm", "My Crop","payment History",
        "Setting", "Feedback", "Terms and Conditions", "Contact & Social", "License",
        "Share App", "Privacy Policy", "About Us", "Logout"
    )

    val menuIcons = listOf(
        Icons.Filled.AccountCircle,     // Account
//        Icons.Filled.AccountCircle,     // Account
        Icons.Filled.Agriculture,       // My Farm
        Icons.Filled.LocalFlorist,      // My Crop
        Icons.Filled.Payment,           //payments
        Icons.Filled.Settings,          // Setting
        Icons.AutoMirrored.Filled.Comment,           // Feedback
        Icons.Filled.Description,       // Terms and Conditions
        Icons.Filled.ContactPhone,      // Contact & Social
        Icons.Filled.FileCopy,          // License
        Icons.Filled.Share,             // Share App
        Icons.Filled.PrivacyTip,        // Privacy Policy
        Icons.Filled.Info,              // About Us
        Icons.AutoMirrored.Filled.ExitToApp          // Logout
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .fillMaxHeight()
            .background(Color(0xFF189f5d)) // Green background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Header with user image and app name
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF147a43)) // Darker green for header
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shoppingcart),
                    contentDescription = "User Profile Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "CropBazaar",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = "Crop prices at your fingertips",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Items
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
//                items(menuItems.size) { index ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                when (menuItems[index]) {
//                                    "Payment History" -> navController.navigate("payment_history")
//                                    "Profile" -> navController.navigate("profile")
//                                    "My Farm" -> navController.navigate("my_farm")
//                                    "My Crop" -> navController.navigate("my_crop")
//                                    "Setting" -> navController.navigate("settings")
//                                    "Feedback" -> navController.navigate("feedback")
//                                    "Terms and Conditions" -> navController.navigate("terms")
//                                    "Contact & Social" -> navController.navigate("contact_social")
//                                    "License" -> navController.navigate("license")
//                                    "Share App" -> navController.navigate("share")
//                                    "Privacy Policy" -> navController.navigate("privacy")
//                                    "About Us" -> navController.navigate("about")
//                                    "Logout" -> navController.navigate("logout")
//                                }
//                            }
//                            .padding(vertical = 8.dp)
//                    ) {
//                        Icon(
//                            imageVector = menuIcons[index],
//                            contentDescription = menuItems[index],
//                            modifier = Modifier.size(24.dp),
//                            tint = Color.White
//                        )
//                        Spacer(modifier = Modifier.width(16.dp))
//                        Text(
//                            text = menuItems[index],
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = Color.White
//                        )
                items(menuItems.size) { index ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(menuItems[index])
//                                                                when (menuItems[index]) {
//                                                                    "Payment History" -> navController.navigate(
//                                                                        "payment_history"
//                                                                    )
//                                                                }
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = menuIcons[index],
                            contentDescription = menuItems[index],
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = menuItems[index],
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            },
            trailingIcon = {
                Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.LocalSee,
                        contentDescription = "Camera",
                        tint = Color.White
                    )
                }
            },
            placeholder = {
                Text("Search", color = Color.Black.copy(alpha = 0.7f))
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.Black,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
            ),
            textStyle = TextStyle(color = Color.Black) // Ensure the text color is black when typing
        )
        IconButton(
            onClick = { },
            modifier = Modifier.padding(start = 8.dp).size(30.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = Color.White
            )
        }
    }
}




//@Composable
//fun IconRow(navController: NavController) {
//    val icons = listOf(
//        R.drawable.crop,
//        R.drawable.learning,
//        R.drawable.cropsanalytics,
//        R.drawable.weather,
//        R.drawable.analysis,
//        R.drawable.bonus,
//        R.drawable.tractors,
//        R.drawable.question
//    )
//
//    val labels = listOf(
//        "All Crop", "Krishi Gyaan","Crop Care", "Weather News", "Market Price", "Schemes", "Hardware", "Q & A"
//    )
//
//    val routes = listOf(
//        "AllCrop", "KrishiGyaan","Crop Care", "WeatherNews", "MarketPrice", "Schemes", "Hardware", "QAndA"
//    )
//
//    LazyRow(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        horizontalArrangement = Arrangement.spacedBy(12.dp),
//        contentPadding = PaddingValues(horizontal = 16.dp)
//    ) {
//        items(icons.size) { index ->
//            IconItem(
//                icon = icons[index],
//                label = labels[index],
//                onClick = { navController.navigate(routes[index]) }
//            )
//        }
//    }
//}






//@Composable
//fun IconItem(icon: Int, label: String, onClick: () -> Unit) {
//    Column(
//        modifier = Modifier.padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        IconButton(onClick = onClick, modifier = Modifier.size(60.dp)) {
//            Box(
//                modifier = Modifier
//                    .size(70.dp)
//                    .background(color = Color.White, shape = CircleShape),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painter = painterResource(id = icon),
//                    contentDescription = label,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(8.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//        Text(text = label, fontSize = 14.sp, color = Color.White)
//    }
//}



@Composable
fun BottomNavBar(
    navController: NavController,
    selectedItem: MutableState<Int>,
    modifier: Modifier = Modifier // Add the modifier parameter with a default value
) {
    val items = listOf("Home", "Chat", "Mandi", "Account")
    val icons = listOf(
        R.drawable.home,
//        R.drawable.play,
        R.drawable.chats,
        R.drawable.shoppingbag,
        R.drawable.account
    )

    NavigationBar(
        containerColor = Color(0xFFfa9c26),
        contentColor = Color.White,
        tonalElevation = 5.dp,
        modifier = modifier // Use the passed modifier here
    ) {
        items.forEachIndexed { index, label ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(text = label) },
                selected = selectedItem.value == index,
                onClick = {
                    selectedItem.value = index
                    when (index) {
                        0 -> navController.navigate("home")
//                        1 -> navController.navigate("feed")
                        1 -> navController.navigate("Chats")
                        2 -> navController.navigate("mandi")
                        3 -> navController.navigate("Account")
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = Color(0xFF7a9d54)
                )
            )
        }
    }
}





@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}

