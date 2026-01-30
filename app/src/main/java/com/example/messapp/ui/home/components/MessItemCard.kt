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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.messapp.data.model.Mess

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MessItemCard(
    mess: Mess,
    onClick: (Mess) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick(mess) },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1F3F7)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = mess.imageRes),
                contentDescription = mess.name,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = mess.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xD8FCFCFC),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${mess.rating} ★",
                            color = Color(0xFF2E7D32),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = mess.cuisine,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row {
                    Text(mess.distanceKm, style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(mess.timeMin, style = MaterialTheme.typography.labelSmall)
                }

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    mess.tags.forEach { tag ->
                        TagChip(tag)
                    }
                }
            }
        }
    }
}