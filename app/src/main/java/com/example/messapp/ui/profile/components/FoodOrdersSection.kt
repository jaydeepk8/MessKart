package com.example.messapp.ui.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen

@Composable
fun FoodOrdersSection(
    vegModeEnabled: Boolean,
    onVegModeToggle: (Boolean) -> Unit,
    onSubscriptionsAndOrdersClick: () -> Unit,
    onOrderHistoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "FOOD & ORDERS",
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
                    icon = Icons.Default.RestaurantMenu,
                    iconBackground = AppSoftGreen,
                    iconTint = AppPrimaryGreen,
                    title = "Subscriptions & Orders",
                    onClick = onSubscriptionsAndOrdersClick
                )

                Divider(thickness = 0.6.dp, color = Color(0xFFE0E0E0))

                AccountSettingsItem(
                    icon = Icons.Default.History,
                    iconBackground = AppSoftGreen,
                    iconTint = AppPrimaryGreen,
                    title = "Order History",
                    onClick = onOrderHistoryClick
                )

                Divider(thickness = 0.6.dp, color = Color(0xFFE0E0E0))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(
                        shape = CircleShape,
                        color = AppSoftGreen,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                tint = AppPrimaryGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Veg Mode",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Show only vegetarian options",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    Switch(
                        checked = vegModeEnabled,
                        onCheckedChange = { onVegModeToggle(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF8BC34A),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFDADADA),
                            uncheckedBorderColor = Color.Transparent,
                            checkedBorderColor = Color.Transparent
                        )
                    )

                }

            }
        }
    }
}
