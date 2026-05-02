package com.example.messapp.ui.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SubscriptionScreen(
    subscriptionViewModel: SubscriptionViewModel
) {
    val subscriptions by subscriptionViewModel.subscriptions.collectAsState()
    val pendingSubscription by subscriptionViewModel.pendingSubscription.collectAsState()
    val greenPrimary = Color(0xFF8BC34A)

    // Conflict dialog — shown when user tries to subscribe to another mess
    if (pendingSubscription != null) {
        AlertDialog(
            onDismissRequest = { subscriptionViewModel.cancelPendingSubscription() },
            title = {
                Text("Active Subscription Exists", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "You already have an active subscription. Do you want to add \"${pendingSubscription!!.messName}\" as another subscription?",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                Button(
                    onClick = { subscriptionViewModel.confirmAddSubscription() },
                    colors = ButtonDefaults.buttonColors(containerColor = greenPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Yes, Add", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { subscriptionViewModel.cancelPendingSubscription() },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(16.dp)
    ) {
        Text(
            text = "My Subscriptions",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "${subscriptions.size} active plan${if (subscriptions.size != 1) "s" else ""}",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        if (subscriptions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🍱", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "No active subscriptions yet.",
                        color = Color.Gray,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Go to a mess and subscribe to a plan.",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(subscriptions) { subscription ->
                    ActiveSubscriptionCard(
                        subscription = subscription,
                        greenPrimary = greenPrimary,
                        onCancel = { subscriptionViewModel.cancelSubscription(subscription.messId) }
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveSubscriptionCard(
    subscription: ActiveSubscription,
    greenPrimary: Color,
    onCancel: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(subscription.messImageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(greenPrimary.copy(alpha = 0.85f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text("Active", color = Color.White,
                        fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(subscription.messName, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                Spacer(Modifier.height(12.dp))

                InfoRow(Icons.Default.AccessTime, "Next delivery: ${subscription.nextDelivery}")
                Spacer(Modifier.height(8.dp))
                InfoRow(Icons.Default.Restaurant, "Lunch & Dinner • 7 days a week")
                Spacer(Modifier.height(8.dp))
                InfoRow(Icons.Default.CheckCircle, "₹${subscription.pricePerWeek} / week")

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = greenPrimary)
                    ) {
                        Text("Manage Plan", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.Gray, fontSize = 14.sp)
    }
}