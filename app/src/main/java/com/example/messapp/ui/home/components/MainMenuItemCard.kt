package com.example.messapp.ui.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuItemCard(
    itemName: String,
    description: String,
    price: Int,
    imageRes: Int,
    enabled: Boolean,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {

    val greenPrimary = Color(0xFF8BC34A)

    val buttonInteractionSource = remember { MutableInteractionSource() }
    val isPressed by buttonInteractionSource.collectIsPressedAsState()

    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(120),
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F9FB)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = itemName,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "₹$price",
                    fontWeight = FontWeight.Bold
                )
            }

            if (quantity == 0) {

                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .graphicsLayer {
                            scaleX = buttonScale
                            scaleY = buttonScale
                        }
                        .background(greenPrimary, RoundedCornerShape(12.dp))
                        .clickable(
                            enabled = enabled,
                            interactionSource = buttonInteractionSource,
                            indication = null
                        ) {
                            onAdd()
                        }
                        .padding(horizontal = 16.dp),
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
                        .background(
                            Color(0xFFE8F5E9),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Text("-", fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = quantity.toString(),
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = onAdd,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Text("+", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}