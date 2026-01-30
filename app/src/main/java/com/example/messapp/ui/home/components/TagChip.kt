package com.example.messapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TagChip(
    text: String
) {
    val (bgColor, textColor) = when {
        text.contains("Pure Veg", true) ->
            Color(0xFFE8F5E9) to Color(0xFF2E7D32)

        text.contains("Veg/Non-Veg", true) ->
            Color(0xFFFFF3E0) to Color(0xFFEF6C00)

        text.contains("Home Delivery", true) ->
            Color(0xFFE3F2FD) to Color(0xFF1565C0)

        text.contains("Dine", true) ->
            Color(0xFFF3E5F5) to Color(0xFF7B1FA2)

        else ->
            Color(0xFFF0F0F0) to Color.DarkGray
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold
        ),
        color = textColor,
        modifier = Modifier
            .background(
                color = bgColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}
