package com.example.messapp.ui.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen

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
            modifier = Modifier.shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {

            Column {

                AccountSettingsItem(
                    icon = Icons.Default.Notifications,
                    iconBackground = AppSoftGreen,
                    iconTint = AppPrimaryGreen,
                    title = "Notifications",
                    onClick = onNotificationsClick
                )

                Divider(thickness = 0.6.dp, color = Color(0xFFE0E0E0))

                AccountSettingsItem(
                    icon = Icons.Default.HelpOutline,
                    iconBackground = AppSoftGreen,
                    iconTint = AppPrimaryGreen,
                    title = "Help & Support",
                    onClick = onHelpSupportClick
                )
            }
        }
    }
}
