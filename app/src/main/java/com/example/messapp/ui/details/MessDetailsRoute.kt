package com.example.messapp.ui.details

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messapp.ui.cart.CartViewModel
import com.example.messapp.ui.subscription.SubscriptionViewModel

@Composable
fun MessDetailsRoute(
    messId: Int,
    navController: NavController,
    cartViewModel: CartViewModel,
    subscriptionViewModel: SubscriptionViewModel
) {
    val viewModel: MessDetailsViewModel = viewModel(
        factory = MessDetailsViewModelFactory(messId)
    )

    val mess = viewModel.mess

    MessDetailsScreen(
        imageRes = mess.imageRes,
        name = mess.name,
        rating = mess.rating,
        time = mess.timeMin,
        type = mess.tags.firstOrNull() ?: "Veg",
        onBackClick = { navController.popBackStack() },
        onViewOrderClick = { navController.navigate("order_summary") },
        cartViewModel = cartViewModel,
        subscriptionViewModel = subscriptionViewModel
    )

}
