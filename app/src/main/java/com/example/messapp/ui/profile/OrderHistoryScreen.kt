package com.example.messapp.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.R
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

private val SubTextGray = Color(0xFF667085)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    onBackClick: () -> Unit
) {
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
                title = { Text("Order History", style = MaterialTheme.typography.titleLarge) },
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
