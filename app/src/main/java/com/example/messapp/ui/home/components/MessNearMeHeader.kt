package com.example.messapp.ui.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessNearMeHeader(
    onViewMapClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Mess near me",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.weight(1f)
        )

        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.94f else 1f,
            label = "button-scale"
        )

        Surface(
            onClick = onViewMapClick,
            interactionSource = interactionSource,
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFEAEAFF),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier
                .scale(scale)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "View on map",
                    tint = Color(0xFF5B5CE2),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "View on Map",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF5B5CE2)
                )
            }
        }
    }
}
