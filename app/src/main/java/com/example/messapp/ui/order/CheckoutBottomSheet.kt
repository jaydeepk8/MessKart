package com.example.messapp.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckoutBottomSheet(
    total: Int,
    onConfirm: () -> Unit
) {
    val greenPrimary = Color(0xFF8BC34A)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        Text(
            text = "Confirm Order",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Delivery Address",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "123 Main Street, Apt 4B, New York, NY\n10001",
            fontSize = 13.sp,
            color = Color.Gray,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Payment Method",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Wallet  ••••  4242",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = greenPrimary)
        ) {
            Text(
                text = "Confirm Order | ₹$total",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}
