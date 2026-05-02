package com.example.messapp.ui.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip

@Composable
fun SubscriptionDialog(
    messName: String,
    messImageRes: Int,
    pricePerMonth: Int,
    subscriptionViewModel: SubscriptionViewModel,
    onDismiss: () -> Unit
) {
    val greenPrimary = Color(0xFF8BC34A)

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Subscribe to $messName",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Image(
                    painter = painterResource(id = messImageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(16.dp))

                Text("Plan Details", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Spacer(Modifier.height(8.dp))

                PlanRow("Duration", "1 Month")
                PlanRow("Meals", "Lunch & Dinner")
                PlanRow("Delivery", "7 days a week")
                PlanRow("Price", "₹$pricePerMonth / month")

                Spacer(Modifier.height(12.dp))

                Text(
                    "You can cancel anytime from the Subscriptions tab.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    subscriptionViewModel.requestSubscribe(
                        ActiveSubscription(
                            messId = messName.hashCode(),
                            messName = messName,
                            messImageRes = messImageRes,
                            nextDelivery = "Tomorrow, 12:30 PM",
                            pricePerWeek = pricePerMonth / 4
                        )
                    )
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenPrimary)
            ) {
                Text("Subscribe Now", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun PlanRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}