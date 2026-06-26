package com.example.messapp.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import kotlinx.coroutines.launch

private enum class NotifType { ORDER, OFFER, SUBSCRIPTION }

private data class AppNotification(
    val id: Int,
    val type: NotifType,
    val title: String,
    val message: String,
    val timeAgo: String,
    val unread: Boolean,
    val action: String? = null
)

private val SubTextGray = Color(0xFF667085)

private fun styleFor(type: NotifType): Triple<ImageVector, Color, Color> = when (type) {
    NotifType.ORDER -> Triple(Icons.Default.Inventory2, Color(0xFFFFEDE5), Color(0xFFFF5722))
    NotifType.OFFER -> Triple(Icons.Default.LocalOffer, Color(0xFFF3E8FF), Color(0xFF7C4DFF))
    NotifType.SUBSCRIPTION -> Triple(Icons.Default.NotificationsActive, Color(0xFFE8F0FF), Color(0xFF2962FF))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit
) {
    val notifications = remember {
        mutableStateListOf(
            AppNotification(
                1, NotifType.ORDER, "Order Delivered",
                "Your feast from Green Leaf Tiffins has arrived! Hope you enjoy your meal.",
                "2m ago", true, action = "Rate Order"
            ),
            AppNotification(
                2, NotifType.OFFER, "New Offer Just for You",
                "Unlock 20% OFF your next order. Use code MESS20 at checkout.",
                "1h ago", true
            ),
            AppNotification(
                3, NotifType.SUBSCRIPTION, "Subscription Ending Soon",
                "Your monthly mess plan expires in 2 days. Renew now to keep your free delivery perks!",
                "5h ago", false
            ),
            AppNotification(
                4, NotifType.ORDER, "Order Confirmed",
                "The chef has started preparing your order from Shri Krishna Tiffins.",
                "Yesterday", false
            )
        )
    }

    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Orders", "Offers")

    val visible = notifications.filter { notif ->
        when (selectedFilter) {
            "Orders" -> notif.type == NotifType.ORDER
            "Offers" -> notif.type == NotifType.OFFER
            else -> true
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun notify(message: String) {
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Notifications", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Text(
                        text = "Mark all as read",
                        color = AppPrimaryGreen,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .clickable {
                                for (i in notifications.indices) {
                                    notifications[i] = notifications[i].copy(unread = false)
                                }
                                notify("All notifications marked as read")
                            }
                            .padding(end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AppBackground)
        ) {

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) AppPrimaryGreen else Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) AppPrimaryGreen else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 20.dp, vertical = 9.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.White else Color.DarkGray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            if (visible.isEmpty()) {
                EmptyNotifications()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(visible, key = { it.id }) { notif ->
                        NotificationCard(
                            notif = notif,
                            onClick = {
                                val index = notifications.indexOfFirst { it.id == notif.id }
                                if (index != -1) {
                                    notifications[index] = notifications[index].copy(unread = false)
                                }
                                notif.action?.let { notify("$it is coming soon") }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notif: AppNotification,
    onClick: () -> Unit
) {
    val (icon, iconBg, iconTint) = styleFor(notif.type)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notif.unread) Color.White else Color(0xFFFAFAFA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFEDEDED))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notif.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (notif.unread) Color(0xFF202124) else SubTextGray,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = notif.timeAgo.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9AA0A6),
                        fontSize = 10.sp
                    )
                    if (notif.unread) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(AppPrimaryGreen, CircleShape)
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = notif.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubTextGray,
                    lineHeight = 19.sp
                )

                if (notif.action != null) {
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = notif.action,
                            color = AppPrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = AppPrimaryGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyNotifications() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(AppSoftGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = null,
                tint = AppPrimaryGreen,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = "You're all caught up",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "No notifications here right now.",
            style = MaterialTheme.typography.bodyMedium,
            color = SubTextGray
        )
    }
}
