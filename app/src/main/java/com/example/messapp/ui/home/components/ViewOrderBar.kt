package com.example.messapp.ui.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
fun ViewOrderBar(
    modifier: Modifier = Modifier,
    itemCount: Int,
    totalPrice: Int,
    onClick: () -> Unit
) {
    val greenPrimary = Color(0xFF8BC34A)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = ""
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(
                color = greenPrimary,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    color = Color.White.copy(alpha = 0.25f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = itemCount.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "View order",
            modifier = Modifier.weight(1f),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "₹$totalPrice",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}