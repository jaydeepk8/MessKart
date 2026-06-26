package com.example.messapp.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import kotlinx.coroutines.launch

private data class SavedCard(
    val brand: String,
    val last4: String,
    val expiry: String,
    val gradient: List<Color>
)

private val LabelGray = Color(0xFF9AA0A6)
private val SubTextGray = Color(0xFF667085)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsScreen(
    onBackClick: () -> Unit
) {
    val cards = remember {
        listOf(
            SavedCard("VISA", "4242", "12/26", listOf(Color(0xFF2E1F6B), Color(0xFF4B3FB0))),
            SavedCard("Mastercard", "8817", "05/28", listOf(Color(0xFF0F6B3A), Color(0xFF1FA45C)))
        )
    }

    var selectedUpi by remember { mutableStateOf("Google Pay") }

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
                title = {
                    Text("Payment Methods", style = MaterialTheme.typography.titleLarge)
                },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AppBackground)
                .verticalScroll(rememberScrollState())
        ) {

            // ---------- Saved Cards ----------
            SectionHeader(
                title = "Saved Cards",
                action = "Manage",
                onAction = { notify("Manage cards is coming soon") }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(cards) { card ->
                    PaymentCard(card = card, onEdit = { notify("Edit ${card.brand} card") })
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---------- UPI ----------
            SectionHeader(title = "Unified Payments (UPI)")

            UpiRow(
                icon = Icons.Default.AccountBalance,
                iconBackground = Color(0xFFE8F0FF),
                iconTint = Color(0xFF2962FF),
                name = "Google Pay",
                detail = "jaydeepk@oksbi",
                selected = selectedUpi == "Google Pay",
                onClick = { selectedUpi = "Google Pay" }
            )

            Spacer(Modifier.height(12.dp))

            UpiRow(
                icon = Icons.Default.Smartphone,
                iconBackground = Color(0xFFF3E8FF),
                iconTint = Color(0xFF7C4DFF),
                name = "PhonePe",
                detail = null,
                selected = selectedUpi == "PhonePe",
                onClick = { selectedUpi = "PhonePe" }
            )

            Spacer(Modifier.height(24.dp))

            // ---------- Digital Wallets ----------
            SectionHeader(title = "Digital Wallets")

            InternalBalanceCard(
                balance = "₹250.00",
                onTopUp = { notify("Top up wallet is coming soon") }
            )

            Spacer(Modifier.height(12.dp))

            UpiRow(
                icon = Icons.Default.AccountBalanceWallet,
                iconBackground = Color(0xFFE0F2F1),
                iconTint = Color(0xFF00897B),
                name = "Paytm Wallet",
                detail = "Link Account",
                detailColor = AppPrimaryGreen,
                trailing = {
                    Icon(
                        Icons.Default.Link,
                        contentDescription = null,
                        tint = LabelGray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = { notify("Link Paytm wallet is coming soon") }
            )

            Spacer(Modifier.height(24.dp))

            // ---------- Add new ----------
            Text(
                text = "Add a new way to pay",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AddOptionTile(
                    icon = Icons.Default.AddCard,
                    label = "Add New Card",
                    modifier = Modifier.weight(1f),
                    onClick = { notify("Add new card is coming soon") }
                )
                AddOptionTile(
                    icon = Icons.Default.QrCode,
                    label = "Add UPI ID",
                    modifier = Modifier.weight(1f),
                    onClick = { notify("Add UPI ID is coming soon") }
                )
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    action: String? = null,
    onAction: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        if (action != null) {
            Text(
                text = action,
                style = MaterialTheme.typography.bodyMedium,
                color = AppPrimaryGreen,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onAction() }
            )
        }
    }
}

@Composable
private fun PaymentCard(card: SavedCard, onEdit: () -> Unit) {
    Box(
        modifier = Modifier
            .width(290.dp)
            .height(172.dp)
            .background(
                brush = Brush.linearGradient(card.gradient),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        // chip
        Box(
            modifier = Modifier
                .size(width = 38.dp, height = 28.dp)
                .background(Color.White.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
        )

        Text(
            text = card.brand,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Text(
            text = "••••  ••••  ••••  ${card.last4}",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        Text(
            text = card.expiry,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 13.sp,
            modifier = Modifier.align(Alignment.BottomStart)
        )

        IconButton(
            onClick = onEdit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit card",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun UpiRow(
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    name: String,
    detail: String?,
    detailColor: Color = SubTextGray,
    selected: Boolean? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFEDEDED))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (detail != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.bodySmall,
                        color = detailColor,
                        fontWeight = if (detailColor == AppPrimaryGreen) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }

            when {
                trailing != null -> trailing()
                selected != null -> RadioDot(selected = selected)
                else -> Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = LabelGray
                )
            }
        }
    }
}

@Composable
private fun RadioDot(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .background(
                color = if (selected) AppPrimaryGreen else Color.White,
                shape = CircleShape
            )
            .border(
                width = if (selected) 0.dp else 2.dp,
                color = if (selected) Color.Transparent else Color(0xFFCBD2D9),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.White, CircleShape)
            )
        }
    }
}

@Composable
private fun InternalBalanceCard(balance: String, onTopUp: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFF2E7D32), Color(0xFF7CB342))
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {
        Column {
            Text(
                text = "INTERNAL BALANCE",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.8.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "MessKart Credits",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Current: $balance",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .background(Color.White, RoundedCornerShape(20.dp))
                .clickable { onTopUp() }
                .padding(horizontal = 18.dp, vertical = 9.dp)
        ) {
            Text(
                text = "Top Up",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AddOptionTile(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(AppSoftGreen, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = AppPrimaryGreen, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF202124)
        )
    }
}
