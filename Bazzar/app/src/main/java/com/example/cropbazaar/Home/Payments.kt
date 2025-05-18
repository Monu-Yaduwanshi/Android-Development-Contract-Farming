package com.example.cropbazaar.Home


import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cropbazaar.R
import com.google.firebase.database.FirebaseDatabase


// payments freeze working
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(
    postId: String,
    chatId: String,
    totalAmount: Double,
    navController: NavController,
    viewModel: ChatViewModel = viewModel()
) {
    val negotiationData by viewModel.negotiationData.collectAsState()
    var selectedPaymentMethod by remember { mutableStateOf("UPI") }
    val totalAmount = remember(negotiationData) {
        negotiationData?.let { data ->
            // Extract numeric values
            val price = data.finalPrice.toDoubleOrNull() ?: 0.0
            val quantity = data.finalQuantity.split(" ").first().toDoubleOrNull() ?: 0.0
            val unit = data.finalQuantity.split(" ").getOrNull(1) ?: "kg"

            // Use the same calculation as ChatViewModel
            viewModel.calculateTotalAmount(
                price = price,
                quantity = quantity,
                unit = unit
            )
        } ?: 0.0
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Payment", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32))
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            negotiationData?.let { data ->
                PaymentContent(
                    selectedPaymentMethod = selectedPaymentMethod,
                    totalAmount = totalAmount,
                    onPaymentMethodSelected = { selectedPaymentMethod = it },
                    postId = postId,
                    chatId = chatId,
                    navController = navController,
                    sellerEmail = data.sellerEmail,
                    buyerEmail = data.buyerEmail
                )
            }
        }
    }
}

@Composable
fun PaymentContent(
    selectedPaymentMethod: String,
    totalAmount: Double,
    onPaymentMethodSelected: (String) -> Unit,
    postId: String,
    chatId: String,
    navController: NavController,
    sellerEmail: String,
    buyerEmail: String
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Select Payment Method",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PaymentMethodOptions(selectedPaymentMethod, onPaymentMethodSelected)

        Spacer(modifier = Modifier.height(24.dp))

        when (selectedPaymentMethod) {
            "UPI" -> UpiOptions()
            "Card" -> CardOptions()
            "Cash" -> CashOnDeliveryOption()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total Amount: ₹${"%.2f".format(totalAmount)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        PlaceOrderButton(
            postId = postId,
            chatId = chatId,
            sellerEmail = sellerEmail,
            buyerEmail = buyerEmail,
            totalAmount = totalAmount,
            selectedPaymentMethod = selectedPaymentMethod,
            navController = navController
        )
    }
}

@Composable
fun PlaceOrderButton(
    postId: String,
    chatId: String,
    sellerEmail: String,
    buyerEmail: String,
    totalAmount: Double,
    selectedPaymentMethod: String,
    navController: NavController
) {
    val context = LocalContext.current

    Button(
        onClick = {
            val db = FirebaseDatabase.getInstance().reference
            val ordersRef = db.child("company_orders")
            val orderId = ordersRef.push().key ?: return@Button

            val order = mapOf(
                "orderId" to orderId,
                "postId" to postId,
                "chatId" to chatId,
                "sellerEmail" to sellerEmail,
                "buyerEmail" to buyerEmail,
                "totalAmount" to totalAmount,
                "paymentMethod" to selectedPaymentMethod,
                "timestamp" to System.currentTimeMillis()
            )

            ordersRef.child(orderId).setValue(order).addOnSuccessListener {
                val negotiationRef = db.child("NegotiationChats/$postId/$chatId/negotiationData")
                negotiationRef.child("paymentTriggered").setValue(true)

                db.child("NegotiationChats/$postId/$chatId/messages").push().setValue(
                    ChatMessage(
                        sender = "system",
                        message = "💰 Payment Done",
                        timestamp = System.currentTimeMillis().toString()
                    )
                )

                Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Order failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Place Order", fontSize = 18.sp, color = Color.White)
    }
}









@Composable
fun PaymentMethodOptions(
    selectedPaymentMethod: String,
    onPaymentMethodSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PaymentOptionCard(
            title = "UPI",
            iconRes = R.drawable.bhimupi,
            selected = selectedPaymentMethod == "UPI",
            onClick = { onPaymentMethodSelected("UPI") }
        )

        PaymentOptionCard(
            title = "Card",
            iconRes = R.drawable.cardoption,
            selected = selectedPaymentMethod == "Card",
            onClick = { onPaymentMethodSelected("Card") }
        )

        PaymentOptionCard(
            title = "Cash",
            iconRes = R.drawable.cashoption,
            selected = selectedPaymentMethod == "Cash",
            onClick = { onPaymentMethodSelected("Cash") }
        )
    }
}
@Composable
fun PaymentOptionCard(
    title: String,
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color(0xFF2E7D32) else Color(0xFFA5D6A7)
    val contentColor = if (selected) Color.White else Color(0xFF2E7D32)

    Column(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, color = contentColor, fontSize = 16.sp)
    }
}
@Composable
fun UpiOptions() {
    var selectedUpi by remember { mutableStateOf("Paytm") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        UpiOptionRow("Paytm", R.drawable.paytm, selectedUpi == "Paytm") { selectedUpi = "Paytm" }
        UpiOptionRow("Google Pay", R.drawable.gpay, selectedUpi == "Google Pay") { selectedUpi = "Google Pay" }
        UpiOptionRow("PhonePe", R.drawable.phonepe, selectedUpi == "PhonePe") { selectedUpi = "PhonePe" }
    }
}

@Composable
fun UpiOptionRow(name: String, iconRes: Int, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(2.dp, if (selected) Color(0xFF2E7D32) else Color.Gray)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = iconRes), contentDescription = name, Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(name, fontSize = 18.sp)
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF2E7D32),
                unselectedColor = Color.Gray
            )
        )
    }
}
@Composable
fun CardOptions() {
    var selectedCardType by remember { mutableStateOf("Debit") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CardOptionRow("Debit", R.drawable.debitcard, selectedCardType == "Debit") {
            selectedCardType = "Debit"
        }

        CardOptionRow("Credit", R.drawable.creditcard, selectedCardType == "Credit") {
            selectedCardType = "Credit"
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Gray)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.bank),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp)
            )
            Text("Add your card", fontSize = 18.sp)
        }
    }
}

@Composable
fun CardOptionRow(name: String, iconRes: Int, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(2.dp, if (selected) Color(0xFF2E7D32) else Color.Gray)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = iconRes), contentDescription = name, Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(name, fontSize = 18.sp)
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF2E7D32),
                unselectedColor = Color.Gray
            )
        )
    }
}
@Composable
fun CashOnDeliveryOption() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFF2E7D32))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.cashoption),
                    contentDescription = "Cash On Delivery",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Cash On Delivery", fontSize = 18.sp)
            }

            RadioButton(
                selected = true,
                onClick = {},
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF2E7D32),
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}
