package com.example.messapp.ui.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.messapp.ui.cart.CartViewModel
import com.example.messapp.ui.home.components.MainMenuItemCard
import com.example.messapp.ui.home.components.SubscriptionPlanCard
import com.example.messapp.ui.home.components.TodaySpecialItemCard
import com.example.messapp.ui.home.components.ViewOrderBar
import com.example.messapp.ui.subscription.SubscriptionViewModel
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppCardBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import com.example.messapp.ui.theme.AppTextPrimary
import com.example.messapp.ui.theme.AppTextSecondary
import kotlinx.coroutines.launch

data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val imageRes: Int
)

private data class WeeklyMenuDay(
    val day: String,
    val lunch: String,
    val dinner: String
)

@OptIn(ExperimentalFoundationApi::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun MessDetailsScreen(
    imageRes: Int,
    name: String,
    rating: Double,
    time: String,
    cuisine: String,
    distance: String,
    type: String,
    tags: List<String>,
    onBackClick: () -> Unit,
    onViewOrderClick: () -> Unit,
    onSubscribeClick: () -> Unit,
    cartViewModel: CartViewModel,
    subscriptionViewModel: SubscriptionViewModel
) {
    val greenPrimary = AppPrimaryGreen
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Today's Special", "Main Menu", "Subscription")
    var isFavorite by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val subscriptions by subscriptionViewModel.subscriptions.collectAsState()
    val isAlreadySubscribed = subscriptions.any { it.messName == name }

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

    val weeklyMenu = remember {
        listOf(
            WeeklyMenuDay("Mon", "Dal rice, aloo sabzi, chapati", "Veg pulao, raita"),
            WeeklyMenuDay("Tue", "Paneer bhaji, dal, rice", "Seasonal veg thali"),
            WeeklyMenuDay("Wed", "Rajma rice, salad", "Dal khichdi, papad"),
            WeeklyMenuDay("Thu", "Veg thali, curd", "Paneer masala, chapati"),
            WeeklyMenuDay("Fri", "Chole rice, salad", "Special veg thali")
        )
    }

    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalItems by cartViewModel.totalItems.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
        ) {

            item {
                Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                            )
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                        }
                        Row {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Menu search is coming soon")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Search, null, tint = Color.White)
                            }
                            IconButton(
                                onClick = {
                                    isFavorite = !isFavorite
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (isFavorite) "Added to favourites" else "Removed from favourites"
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                    ) {
                        Text(name, color = Color.White,
                            style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(6.dp))
                        Text("★ $rating • $time • $type", color = Color.White)
                    }
                }
            }

            item {
                MessTrustSection(
                    rating = rating,
                    cuisine = cuisine,
                    distance = distance,
                    time = time,
                    tags = tags,
                    weeklyMenu = weeklyMenu,
                    onContactClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Contact option will be available after real mess onboarding")
                        }
                    }
                )
            }

            stickyHeader {
                TabRow(
                    selectedTabIndex = selectedTab,
                    indicator = {
                        Box(
                            Modifier.tabIndicatorOffset(it[selectedTab])
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
                            isSubscribed = isAlreadySubscribed,
                            onSubscribeClick = {
                                if (!isAlreadySubscribed) {
                                    onSubscribeClick()
                                }
                            }
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }

}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun MessTrustSection(
    rating: Double,
    cuisine: String,
    distance: String,
    time: String,
    tags: List<String>,
    weeklyMenu: List<WeeklyMenuDay>,
    onContactClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TrustMetric(Icons.Default.Star, "$rating rating", "Based on taste & service")
            TrustMetric(Icons.Default.AccessTime, time, "Average serving time")
            TrustMetric(Icons.Default.Restaurant, cuisine, distance)
        }

        CardBlock {
            Text("Good to know", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppTextPrimary)
            Spacer(Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag -> InfoChip(tag) }
                InfoChip("Trial meal available")
                InfoChip("Home-style food")
            }
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TrustPoint(Icons.Default.Verified, "Hygiene checked")
                TrustPoint(Icons.Default.CalendarMonth, "Weekly menu")
            }
            Spacer(Modifier.height(10.dp))
            TrustPoint(Icons.Default.Info, "Delivery or dine-in options will depend on each mess after real onboarding.")
        }

        CardBlock {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Weekly Menu Preview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppTextPrimary)
                    Text("Sample menu until real mess menus are added", color = AppTextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = AppPrimaryGreen)
            }
            Spacer(Modifier.height(12.dp))
            weeklyMenu.take(3).forEach { day ->
                WeeklyMenuRow(day)
            }
        }

        CardBlock {
            Text("Reviews & Trust", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppTextPrimary)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ReviewScore("Taste", "4.6")
                ReviewScore("Hygiene", "4.4")
                ReviewScore("Value", "4.5")
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "\"Feels like simple home food. Good for daily lunch.\"",
                color = AppTextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onContactClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Contact")
            }
            Button(
                onClick = onContactClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryGreen)
            ) {
                Icon(Icons.Default.DeliveryDining, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Service Info")
            }
        }
    }
}

@Composable
private fun TrustMetric(icon: ImageVector, title: String, subtitle: String) {
    Card(
        modifier = Modifier.width(170.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AppCardBackground),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = AppPrimaryGreen, modifier = Modifier.size(22.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, color = AppTextPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(subtitle, color = AppTextSecondary, style = MaterialTheme.typography.bodySmall, maxLines = 2)
        }
    }
}

@Composable
private fun CardBlock(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = AppCardBackground),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(AppSoftGreen)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(text, color = AppPrimaryGreen, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun TrustPoint(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = AppPrimaryGreen, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = AppTextSecondary, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun WeeklyMenuRow(day: WeeklyMenuDay) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Text(day.day, color = AppPrimaryGreen, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Lunch: ${day.lunch}", color = AppTextPrimary, style = MaterialTheme.typography.bodySmall)
        Text("Dinner: ${day.dinner}", color = AppTextSecondary, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ReviewScore(label: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(AppSoftGreen)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = AppPrimaryGreen, fontWeight = FontWeight.Bold)
        Text(label, color = AppTextSecondary, style = MaterialTheme.typography.labelSmall)
    }
}
