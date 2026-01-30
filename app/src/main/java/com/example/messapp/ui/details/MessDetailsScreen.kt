package com.example.messapp.ui.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.messapp.R
import com.example.messapp.ui.cart.CartViewModel
import com.example.messapp.ui.home.components.MainMenuItemCard
import com.example.messapp.ui.home.components.SubscriptionPlanCard
import com.example.messapp.ui.home.components.TodaySpecialItemCard
import com.example.messapp.ui.home.components.ViewOrderBar
import com.example.messapp.ui.subscription.SubscriptionDialog
import com.example.messapp.ui.subscription.SubscriptionViewModel

data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val imageRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessDetailsScreen(
    imageRes: Int,
    name: String,
    rating: Double,
    time: String,
    type: String,
    onBackClick: () -> Unit,
    onViewOrderClick: () -> Unit,
    cartViewModel: CartViewModel,
    subscriptionViewModel: SubscriptionViewModel
) {
    val greenPrimary = Color(0xFF8BC34A)
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Today's Special", "Main Menu", "Subscription")
    var showSubscriptionDialog by remember { mutableStateOf(false) }

    val todaySpecialItems = remember {
        listOf(
            MenuItem(101, "Special Veg Thali", "Fresh home-style food", 120, R.drawable.paneer1),
            MenuItem(102, "Paneer Butter Masala", "Rich creamy gravy", 150, R.drawable.paneer1),
            MenuItem(103, "Dal Tadka", "Slow cooked dal", 100, R.drawable.paneer1)
        )
    }

    val mainMenuItems = remember {
        listOf(
            MenuItem(1, "Veg Thali", "Dal, sabzi, chapati, rice", 120, R.drawable.paneer1),
            MenuItem(2, "Paneer Bhaji", "Rich paneer gravy", 150, R.drawable.paneer1),
            MenuItem(3, "Dal Rice", "Comfort food", 100, R.drawable.paneer1),
            MenuItem(4, "Chapati", "Soft wheat chapatis", 20, R.drawable.paneer1)
        )
    }

    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalItems by cartViewModel.totalItems.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

    Scaffold(
        bottomBar = {
            if (totalItems > 0) {
                ViewOrderBar(
                    itemCount = totalItems,
                    totalPrice = totalPrice,
                    onClick = onViewOrderClick
                )
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
        ) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                        }

                        Row {
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.Search, null, tint = Color.White)
                            }
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.FavoriteBorder, null, tint = Color.White)
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            name,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(Modifier.height(6.dp))
                        Text("⭐ $rating • $time • $type", color = Color.White)
                    }
                }
            }

            stickyHeader {
                TabRow(
                    selectedTabIndex = selectedTab,
                    indicator = {
                        Box(
                            Modifier
                                .tabIndicatorOffset(it[selectedTab])
                                .height(3.dp)
                                .background(greenPrimary)
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            selectedContentColor = greenPrimary,
                            unselectedContentColor = Color.Gray,
                            text = { Text(title, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            when (selectedTab) {

                0 -> items(todaySpecialItems) { item ->
                    val qty = cartItems.firstOrNull { it.id == item.id }?.quantity ?: 0
                    TodaySpecialItemCard(
                        imageRes = item.imageRes,
                        title = item.name,
                        description = item.description,
                        price = item.price,
                        quantity = qty,
                        onAdd = { cartViewModel.addItem(item) },
                        onRemove = { cartViewModel.removeItem(item.id) }
                    )
                }

                1 -> items(mainMenuItems) { item ->
                    val qty = cartItems.firstOrNull { it.id == item.id }?.quantity ?: 0
                    MainMenuItemCard(
                        itemName = item.name,
                        description = item.description,
                        price = item.price,
                        imageRes = item.imageRes,
                        enabled = true,
                        quantity = qty,
                        onAdd = { cartViewModel.addItem(item) },
                        onRemove = { cartViewModel.removeItem(item.id) }
                    )
                }

                2 -> item {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Monthly Subscription",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(12.dp))
                        SubscriptionPlanCard(
                            onSubscribeClick = {
                                showSubscriptionDialog = true
                            }
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }

    if (showSubscriptionDialog) {
        SubscriptionDialog(
            messName = name,
            messImageRes = imageRes,
            pricePerMonth = 250,
            subscriptionViewModel = subscriptionViewModel,
            onDismiss = { showSubscriptionDialog = false }
        )
    }
}
