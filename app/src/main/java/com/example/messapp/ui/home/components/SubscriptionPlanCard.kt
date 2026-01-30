package com.example.messapp.ui.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SubscriptionPlanCard(
    pricePerMonth: Int = 250,
    onSubscribeClick: () -> Unit
) {

    val greenPrimary = Color(0xFF8BC34A)
    val lightCard = Color(0xFFF4F6F8)
    val softGreen = Color(0xFFE7F6EC)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = ""
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = lightCard),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Premium Meal Subscription",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .background(softGreen, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "SAVE 20%",
                        color = greenPrimary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Healthy home-style meals delivered daily to your doorstep.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    SubscriptionInfo(
                        icon = Icons.Default.CalendarMonth,
                        label = "Duration",
                        value = "30 Days",
                        color = greenPrimary
                    )

                    SubscriptionInfo(
                        icon = Icons.Default.Restaurant,
                        label = "Meals",
                        value = "Lunch & Dinner",
                        color = greenPrimary
                    )

                    SubscriptionInfo(
                        icon = Icons.Default.Schedule,
                        label = "Frequency",
                        value = "Daily",
                        color = greenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹$pricePerMonth",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "₹315",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = "per month / per person",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .background(greenPrimary, RoundedCornerShape(14.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onSubscribeClick()
                        }
                        .padding(horizontal = 22.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Subscribe",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SubscriptionInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = color)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Text(
            text = value,
            fontWeight = FontWeight.Bold
        )
    }
}