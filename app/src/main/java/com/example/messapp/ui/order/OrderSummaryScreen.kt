package com.example.messapp.ui.order

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.ui.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    onBackClick: () -> Unit,
    cartViewModel: CartViewModel
) {
    val deliveryFee = 10
    val cartItems by cartViewModel.cartItems.collectAsState()

    val subTotal = cartItems.sumOf { it.price * it.quantity }
    val tax = (subTotal * 0.05).toInt()
    val total = subTotal + deliveryFee + tax

    val greenPrimary = Color(0xFF8BC34A)


    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCheckoutSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 2.dp, color = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                    Text(
                        text = "Order Summary",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        },

        bottomBar = {
            Surface(shadowElevation = 10.dp, color = Color.White) {
                Button(
                    onClick = { showCheckoutSheet = true },
                    enabled = cartItems.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (cartItems.isEmpty())
                            Color.LightGray else greenPrimary
                    )
                ) {
                    Text(
                        "Proceed to Checkout | ₹$total",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {

            item {
                SectionTitle("DELIVERY DETAILS")

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFEBEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = null,
                                tint = Color(0xFFE53935)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text("Home", fontWeight = FontWeight.Bold)
                            Text(
                                "123 Main Street, Apt 4B, New York, NY 10001",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Text(
                            "Change",
                            color = greenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SectionTitle("YOUR ITEMS", bottomPadding = 0.dp)
                    Text(
                        "+ Add Items",
                        color = greenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            items(cartItems) { item ->

                val animatedQty by animateIntAsState(
                    targetValue = item.quantity,
                    label = "qtyAnim"
                )

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(item.imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text(item.name, fontWeight = FontWeight.Bold)
                            Text(
                                "₹${item.price}",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF3F3F3))
                            ) {
                                QtyButton("−") {
                                    cartViewModel.decreaseQuantity(item.id)
                                }

                                Text(
                                    animatedQty.toString(),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 14.dp)
                                )

                                QtyButton("+") {
                                    cartViewModel.increaseQuantity(item.id)
                                }
                            }
                        }

                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_delete),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
                SectionTitle("PAYMENT SUMMARY")

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        SummaryRow("Subtotal", "₹$subTotal")
                        SummaryRow("Delivery Fee", "₹$deliveryFee")
                        SummaryRow("Taxes & Fees", "₹$tax")

                        Divider(Modifier.padding(vertical = 12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(
                                "₹$total",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = greenPrimary
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCheckoutSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCheckoutSheet = false },
            sheetState = sheetState
        ) {
            CheckoutBottomSheet(
                total = total,
                onConfirm = {
                    showCheckoutSheet = false

                }
            )
        }
    }
}


@Composable
private fun SectionTitle(
    text: String,
    bottomPadding: Dp = 12.dp
) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = bottomPadding)
    )
}

@Composable
private fun QtyButton(
    symbol: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(32.dp)
    ) {
        Text(symbol, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Medium)
    }
}
