package com.example.messapp.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.R
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import com.example.messapp.ui.theme.AppSuccessGreen
import kotlinx.coroutines.launch

private data class Coupon(
    val code: String,
    val title: String,
    val description: String,
    val expiry: String,
    val accent: Color
)

private data class BankOffer(
    val title: String,
    val subtitle: String,
    val iconBackground: Color,
    val iconTint: Color
)

private val SubTextGray = Color(0xFF667085)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
    onBackClick: () -> Unit
) {
    val coupons = remember {
        listOf(
            Coupon("MESS50", "Valid on all Gourmet Mess Plans",
                "Save up to ₹50 on your first monthly bundle.", "Expires 24 Oct", Color(0xFFFF6D00)),
            Coupon("WEEKEND25", "Friday Feast Special",
                "Flat 25% off on orders above ₹300 during weekends.", "Expires in 2 days", Color(0xFF7C4DFF)),
            Coupon("FREEDESSERT", "Free Dessert with Main",
                "Add any main course and get a chef's special dessert free.", "Never expires", AppPrimaryGreen)
        )
    }

    val bankOffers = remember {
        listOf(
            BankOffer("10% Cashback on HDFC Cards", "Instant discount on all mess subscriptions",
                Color(0xFFE8F0FF), Color(0xFF2962FF)),
            BankOffer("Earn 2x Mess Credits", "Use UPI to earn 2x points on every order",
                Color(0xFFF3E8FF), Color(0xFF7C4DFF)),
            BankOffer("₹50 Flat Off via AMEX", "Applicable on single meal orders above ₹150",
                Color(0xFFE0F2F1), Color(0xFF00897B))
        )
    }

    val clipboard = LocalClipboardManager.current
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
                title = { Text("Offers & Promos", style = MaterialTheme.typography.titleLarge) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item { HeroOfferBanner(onClaim = { notify("Discount claimed! Applied at checkout.") }) }

            // ---------- Active Coupons ----------
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Active Coupons", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${coupons.size} AVAILABLE",
                        color = AppSuccessGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            items(coupons.size) { index ->
                val coupon = coupons[index]
                CouponCard(
                    coupon = coupon,
                    onCopy = {
                        clipboard.setText(AnnotatedString(coupon.code))
                        notify("Code ${coupon.code} copied")
                    }
                )
            }

            // ---------- Bank & Wallet Offers ----------
            item {
                Text("Bank & Wallet Offers", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            items(bankOffers.size) { index ->
                BankOfferRow(offer = bankOffers[index], onClick = { notify("Offer details are coming soon") })
            }

            // ---------- Rewards & Credits ----------
            item {
                RewardsCard(
                    balance = "2,450",
                    onRefer = { notify("Refer a friend is coming soon") },
                    onRedeem = { notify("Redeem credits is coming soon") },
                    onEarnMore = { notify("Earn more credits is coming soon") }
                )
            }

            // ---------- Referral ----------
            item {
                ReferralBanner(onInvite = { notify("Invite friends is coming soon") })
            }
        }
    }
}

@Composable
private fun HeroOfferBanner(onClaim: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(R.drawable.b3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.35f), Color.Black.copy(alpha = 0.75f))
                    )
                )
        )
        Column(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFEF6C00), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    "EXCLUSIVE OFFER",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                "Chef's Special Discount",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "50% OFF your first subscription for gourmet meal plans.",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .clickable { onClaim() }
                    .padding(horizontal = 22.dp, vertical = 11.dp)
            ) {
                Text("Claim Discount", color = AppPrimaryGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CouponCard(coupon: Coupon, onCopy: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFEDEDED))
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(coupon.accent)
            )

            Column(modifier = Modifier.padding(14.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = coupon.code,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = coupon.accent
                    )
                    Row(
                        modifier = Modifier
                            .background(AppSoftGreen, RoundedCornerShape(8.dp))
                            .clickable { onCopy() }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = AppPrimaryGreen,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "COPY",
                            color = AppPrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text(coupon.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
                Text(coupon.description, style = MaterialTheme.typography.bodySmall, color = SubTextGray, lineHeight = 18.sp)

                Spacer(Modifier.height(12.dp))
                Divider(thickness = 0.8.dp, color = Color(0xFFF0F0F0))
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, null, tint = SubTextGray, modifier = Modifier.size(13.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(coupon.expiry, style = MaterialTheme.typography.labelMedium, color = SubTextGray)
                    }
                    Text(
                        "TERMS APPLY",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppPrimaryGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun BankOfferRow(offer: BankOffer, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
                    .size(44.dp)
                    .background(offer.iconBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AccountBalance, null, tint = offer.iconTint, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(offer.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(offer.subtitle, style = MaterialTheme.typography.bodySmall, color = SubTextGray, lineHeight = 17.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF9AA0A6))
        }
    }
}

@Composable
private fun RewardsCard(
    balance: String,
    onRefer: () -> Unit,
    onRedeem: () -> Unit,
    onEarnMore: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(listOf(Color(0xFF2E7D32), Color(0xFF7CB342)))
            )
            .padding(18.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Stars, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    "REWARDS & CREDITS",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                "AVAILABLE BALANCE",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    balance,
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Mess\nCredits",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.labelMedium,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(Modifier.height(14.dp))

            RewardRow(
                icon = Icons.Default.PersonAddAlt1,
                title = "Earn 500 more",
                subtitle = "Refer a friend to join the mess",
                onClick = onRefer
            )
            Spacer(Modifier.height(10.dp))
            RewardRow(
                icon = Icons.Default.CardGiftcard,
                title = "Redeem Credits",
                subtitle = "Use credits for free meal upgrades",
                onClick = onRedeem
            )

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(14.dp))
                    .clickable { onEarnMore() }
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Earn More Credits", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun RewardRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text(subtitle, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.labelMedium)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.White.copy(alpha = 0.8f))
    }
}

@Composable
private fun ReferralBanner(onInvite: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppSoftGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Spread the Word, Share the Meal.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Invite friends and get ₹100 each on your next subscription. No limits on invites!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF2E7D32),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 19.sp
            )
            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .background(AppPrimaryGreen, RoundedCornerShape(24.dp))
                    .clickable { onInvite() }
                    .padding(horizontal = 32.dp, vertical = 12.dp)
            ) {
                Text("Invite Now", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
