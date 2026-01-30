package com.example.messapp.ui.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val activeSubscription by subscriptionViewModel.activeSubscription.collectAsState()
    val greenPrimary = Color(0xFF8BC34A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(16.dp)
    ) {

        Text(
            text = "My Subscription",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        if (activeSubscription != null) {
            ActiveSubscriptionCard(
                subscription = activeSubscription!!,
                greenPrimary = greenPrimary
            )
        } else {
            Text(
                text = "No active subscription yet.",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ActiveSubscriptionCard(
    subscription: ActiveSubscription,
    greenPrimary: Color
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
                        .background(Color.White.copy(alpha = 0.35f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Active",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }

            }

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = subscription.messName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(12.dp))

                InfoRow(
                    icon = Icons.Default.AccessTime,
                    text = "Next delivery: ${subscription.nextDelivery}"
                )

                Spacer(Modifier.height(8.dp))

                InfoRow(
                    icon = Icons.Default.Restaurant,
                    text = "Lunch & Dinner • 7 days a week"
                )

                Spacer(Modifier.height(8.dp))

                InfoRow(
                    icon = Icons.Default.CheckCircle,
                    text = "₹${subscription.pricePerWeek} / week"
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenPrimary
                        )
                    ) {
                        Text("Manage Plan", color = Color.White)
                    }

                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Skip Week")
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
        Icon(
            icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.Gray, fontSize = 14.sp)
    }
}
