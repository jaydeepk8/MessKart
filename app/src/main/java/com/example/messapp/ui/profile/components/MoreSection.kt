package com.example.messapp.ui.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoreSection(
    onNotificationsClick: () -> Unit,
    onHelpSupportClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "MORE",
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
                    icon = Icons.Default.Notifications,
                    iconBackground = Color(0xFFF1F1F1),
                    iconTint = Color.DarkGray,
                    title = "Notifications",
                    onClick = onNotificationsClick
                )

                Divider(
                    thickness = 0.6.dp,
                    color = Color(0xFFE0E0E0)
                )

                AccountSettingsItem(
                    icon = Icons.Default.HelpOutline,
                    iconBackground = Color(0xFFF1F1F1),
                    iconTint = Color.DarkGray,
                    title = "Help & Support",
                    onClick = onHelpSupportClick
                )
            }
        }
    }
}
