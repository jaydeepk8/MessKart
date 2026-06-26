package com.example.messapp.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.R
import com.example.messapp.data.source.MessDataSource
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import com.example.messapp.ui.theme.AppSuccessGreen
import kotlinx.coroutines.launch

private data class PastOrder(
    val id: Int,
    val messName: String,
    val date: String,
    val itemCount: Int,
    val price: String,
    val imageRes: Int
)

private data class ActiveSubscription(
    val planName: String,
    val messName: String,
    val messCuisine: String,
    val daysLeft: Int,
    val tier: String,
    val nextMeal: String,
    val nextMenu: String,
    val imageRes: Int
)

private val SubTextGray = Color(0xFF667085)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsAndOrdersScreen(
    onBackClick: () -> Unit,
    onManageSubscriptionClick: () -> Unit = {}
) {
    // TEMPORARY: pulls a real mess so the card shows the subscribed mess's
    // photo + name. Later this comes from the user's actual subscription.
    val subscription = remember {
        val mess = MessDataSource.getMessById(2) // Green Leaf Tiffins
        ActiveSubscription(
            planName = "Monthly Gourmet Mess Plan",
            messName = mess.name,
            messCuisine = mess.cuisine,
            daysLeft = 15,
            tier = "GOURMET TIER",
            nextMeal = "Lunch (12:30 PM)",
            nextMenu = "Chef's Signature Curry",
            imageRes = mess.imageRes
        )
    }

    val orders = remember {
        listOf(
            PastOrder(1, "Green Leaf Tiffins", "May 24, 2024", 2, "₹420.00", R.drawable.paneer1),
            PastOrder(2, "Shri Krishna Tiffins", "May 21, 2024", 1, "₹180.00", R.drawable.tiffin1),
            PastOrder(3, "Annapurna Mess", "May 15, 2024", 3, "₹240.00", R.drawable.b1)
        )
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
                title = { Text("Subscriptions & Orders", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AppBackground),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ---------- Active Subscriptions ----------
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Subscriptions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .background(AppSoftGreen, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "1 ACTIVE",
                            color = AppSuccessGreen,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            item {
                SubscriptionCard(
                    subscription = subscription,
                    onManage = onManageSubscriptionClick,
                    onSettings = { notify("Subscription settings are coming soon") }
                )
            }

            // ---------- Order History ----------
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Order History",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(orders, key = { it.id }) { order ->
                OrderCard(
                    order = order,
                    onReorder = { notify("Re-ordering from ${order.messName}") },
                    onViewDetails = { notify("Order details are coming soon") }
                )
            }

            item {
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { notify("Full history is coming soon") }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VIEW FULL HISTORY",
                        color = AppPrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = AppPrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SubscriptionCard(
    subscription: ActiveSubscription,
    onManage: () -> Unit,
    onSettings: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFEDEDED))
    ) {
        Column {

            // Banner = subscribed mess photo, with its name overlaid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                Image(
                    painter = painterResource(subscription.imageRes),
                    contentDescription = subscription.messName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )

                // Tier badge (top-left)
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                        .background(Color(0xFFEF6C00), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = subscription.tier,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 0.5.sp
                    )
                }

                // Subscribed mess name + cuisine (bottom-left)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Storefront,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = subscription.messName,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = subscription.messCuisine,
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PLAN",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF9AA0A6),
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.6.sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = subscription.planName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(AppSoftGreen, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${subscription.daysLeft}",
                                color = AppSuccessGreen,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "DAYS LEFT",
                                color = AppSuccessGreen,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                InfoChip(
                    icon = Icons.Default.Schedule,
                    iconBackground = Color(0xFFE8F0FF),
                    iconTint = Color(0xFF2962FF),
                    label = "NEXT MEAL",
                    value = subscription.nextMeal
                )

                Spacer(Modifier.height(10.dp))

                InfoChip(
                    icon = Icons.Default.RestaurantMenu,
                    iconBackground = Color(0xFFFFF1E6),
                    iconTint = Color(0xFFFF6D00),
                    label = "MENU",
                    value = subscription.nextMenu
                )

                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onManage,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryGreen)
                    ) {
                        Text(
                            "Manage Subscription",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(AppSoftGreen, RoundedCornerShape(14.dp))
                            .clickable { onSettings() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Subscription settings",
                            tint = AppPrimaryGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF6F7FB), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(iconBackground, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF9AA0A6),
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.6.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun OrderCard(
    order: PastOrder,
    onReorder: () -> Unit,
    onViewDetails: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFEDEDED))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.Top) {

                Image(
                    painter = painterResource(order.imageRes),
                    contentDescription = order.messName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.messName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${order.date} • ${order.itemCount} ${if (order.itemCount == 1) "Item" else "Items"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SubTextGray
                    )
                    Spacer(Modifier.height(6.dp))
                    DeliveredBadge()
                }

                Text(
                    text = order.price,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppSuccessGreen
                )
            }

            Spacer(Modifier.height(12.dp))
            Divider(thickness = 0.8.dp, color = Color(0xFFF0F0F0))
            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .background(AppSoftGreen, RoundedCornerShape(20.dp))
                        .clickable { onReorder() }
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "REORDER",
                        color = AppPrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "VIEW DETAILS",
                    color = SubTextGray,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.clickable { onViewDetails() }
                )
            }
        }
    }
}

@Composable
private fun DeliveredBadge() {
    Row(
        modifier = Modifier
            .background(AppSoftGreen, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = AppSuccessGreen,
            modifier = Modifier.size(12.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "DELIVERED",
            color = AppSuccessGreen,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall,
            letterSpacing = 0.5.sp
        )
    }
}
