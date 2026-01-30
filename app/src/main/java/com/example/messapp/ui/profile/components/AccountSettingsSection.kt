package com.example.messapp.ui.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AccountSettingsSection(
    onPersonalInfoClick: () -> Unit,
    onManageAddressesClick: () -> Unit,
    onPaymentMethodsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "ACCOUNT SETTINGS",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 8.dp)
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            Column {

                AccountSettingsItem(
                    icon = Icons.Default.Person,
                    iconBackground = Color(0xFFFFEDE5),
                    iconTint = Color(0xFFFF5722),
                    title = "Personal Information",
                    onClick = onPersonalInfoClick
                )

                Divider(
                    thickness = 0.6.dp,
                    color = Color(0xFFE0E0E0)
                )

                AccountSettingsItem(
                    icon = Icons.Default.LocationOn,
                    iconBackground = Color(0xFFE8F0FF),
                    iconTint = Color(0xFF2962FF),
                    title = "Manage Addresses",
                    onClick = onManageAddressesClick
                )

                Divider(
                    thickness = 0.6.dp,
                    color = Color(0xFFE0E0E0)
                )

                AccountSettingsItem(
                    icon = Icons.Default.Payment,
                    iconBackground = Color(0xFFF3E8FF),
                    iconTint = Color(0xFF7C4DFF),
                    title = "Payment Methods",
                    onClick = onPaymentMethodsClick
                )
            }
        }
    }
}
