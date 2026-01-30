package com.example.messapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IndicatorDot(
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(if (isSelected) 8.dp else 6.dp)
            .background(
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    Color.LightGray,
                shape = CircleShape
            )
    )
}
