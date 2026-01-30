package com.example.messapp.ui.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TodaySpecialItemCard(
    imageRes: Int,
    title: String,
    description: String,
    price: Int,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {

    val greenPrimary = Color(0xFF8BC34A)

    val buttonInteractionSource = remember { MutableInteractionSource() }
    val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()

    val buttonScale by animateFloatAsState(
        targetValue = if (isButtonPressed) 0.96f else 1f,
        animationSpec = tween(120),
        label = ""
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(
            width = 0.6.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        )
    ) {

        Column {

            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "₹$price",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (quantity == 0) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .graphicsLayer {
                                scaleX = buttonScale
                                scaleY = buttonScale
                            }
                            .background(greenPrimary, RoundedCornerShape(12.dp))
                            .clickable(
                                interactionSource = buttonInteractionSource,
                                indication = null
                            ) {
                                onAdd()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                } else {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(
                                Color(0xFFE8F5E9),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White, CircleShape)
                                .clickable { onRemove() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("-", fontWeight = FontWeight.Bold)
                        }

                        Text(
                            text = quantity.toString(),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(greenPrimary, CircleShape)
                                .clickable { onAdd() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}