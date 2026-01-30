package com.example.messapp.ui.subscription

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SubscriptionDialog(
    messName: String,
    messImageRes: Int,                     // 🔑 NEW
    pricePerMonth: Int,
    subscriptionViewModel: SubscriptionViewModel, // 🔑 NEW
    onDismiss: () -> Unit
) {
    val greenPrimary = Color(0xFF8BC34A)
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Dialog(
        onDismissRequest = {
            visible = false
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
        ) {

            AnimatedVisibility(
                visible = visible,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    /* ---------- CLOSE BUTTON ---------- */
                    IconButton(
                        onClick = {
                            visible = false
                            onDismiss()
                        },
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .size(40.dp)
                            .background(
                                Color.Black.copy(alpha = 0.5f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }

                    /* ---------- CARD ---------- */
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(
                            topStart = 28.dp,
                            topEnd = 28.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF2F4F7)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 24.dp)
                                .navigationBarsPadding(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(greenPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = greenPrimary
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = "$messName Meal Plan",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "Healthy home-style meals delivered daily",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(20.dp))

                            SubscriptionFeature(
                                icon = Icons.Default.CheckCircle,
                                title = "Daily Lunch & Dinner",
                                subtitle = "Freshly prepared meals every day"
                            )

                            SubscriptionFeature(
                                icon = Icons.Default.Restaurant,
                                title = "Diet Preferences",
                                subtitle = "Veg, Non-veg & Jain options"
                            )

                            SubscriptionFeature(
                                icon = Icons.Default.PauseCircle,
                                title = "Pause Anytime",
                                subtitle = "No charges during pause"
                            )

                            Spacer(Modifier.height(24.dp))

                            /* ---------- SUBSCRIBE ACTION ---------- */
                            Button(
                                onClick = {
                                    subscriptionViewModel.subscribe(
                                        ActiveSubscription(
                                            messName = messName,
                                            messImageRes = messImageRes,
                                            pricePerWeek = pricePerMonth,
                                            nextDelivery = "Tomorrow, 12:30 PM"
                                        )
                                    )
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = greenPrimary
                                )
                            ) {
                                Text(
                                    "Subscribe Now | ₹$pricePerMonth / mo",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionFeature(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF6F6F6))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
