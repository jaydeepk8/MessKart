package com.example.messapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.messapp.navigation.Routes.HOME
import com.example.messapp.navigation.Routes.SIGN_UP
import com.example.messapp.ui.auth.AuthViewModel
import com.example.messapp.ui.auth.LoginScreen
import com.example.messapp.ui.auth.SignUpScreen
import com.example.messapp.ui.cart.CartViewModel
import com.example.messapp.ui.details.MessDetailsRoute
import com.example.messapp.ui.home.HomeScreen
import com.example.messapp.ui.map.MapScreen
import com.example.messapp.ui.order.OrderSummaryScreen
import com.example.messapp.ui.profile.ProfileScreen
import com.example.messapp.ui.subscription.SubscriptionFlowScreen
import com.example.messapp.ui.subscription.SubscriptionScreen
import com.example.messapp.ui.subscription.SubscriptionViewModel

object Routes {
    const val LOGIN        = "login"
    const val HOME         = "home"
    const val MAP          = "map"
    const val SUBSCRIPTION = "subscription"
    const val PROFILE      = "profile"
    const val ORDER_SUMMARY = "order_summary"
    const val MESS_DETAILS = "mess_details"
    const val SUBSCRIPTION_FLOW = "subscription_flow"
    const val SIGN_UP      = "sign_up"
}

@Composable
fun MessNavGraph() {

    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val subscriptionViewModel: SubscriptionViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    val startDestination =  Routes.HOME

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            val hideBottomBar =
                currentRoute == Routes.LOGIN ||
                        currentRoute == Routes.SIGN_UP ||
                        currentRoute == Routes.ORDER_SUMMARY ||
                        currentRoute == Routes.MAP ||
                        currentRoute?.startsWith(Routes.SUBSCRIPTION_FLOW) == true ||
                        currentRoute?.startsWith(Routes.MESS_DETAILS) == true

            if (!hideBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginClick = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onSignUpClick = { navController.navigate(SIGN_UP) },
                    authViewModel = authViewModel
                )
            }

            composable(SIGN_UP) {
                SignUpScreen(
                    onBackClick = { navController.popBackStack() },
                    onCreateAccountClick = {
                        navController.navigate(HOME) {
                            popUpTo(SIGN_UP) { inclusive = true }
                        }
                    },
                    onLoginClick = { navController.popBackStack() },
                    authViewModel = authViewModel
                )
            }

            composable(Routes.HOME) {
                HomeScreen(navController)
            }

            composable(Routes.MAP) {
                MapScreen(
                    onMessClick = { messId ->
                        navController.navigate("${Routes.MESS_DETAILS}/$messId")
                    }
                )
            }

            composable(Routes.SUBSCRIPTION) {
                SubscriptionScreen(subscriptionViewModel)
            }

            composable(Routes.PROFILE) {
                ProfileScreen()
            }

            composable(
                route = "${Routes.MESS_DETAILS}/{messId}",
                arguments = listOf(navArgument("messId") { type = NavType.IntType })
            ) { backStackEntry ->
                MessDetailsRoute(
                    messId = backStackEntry.arguments!!.getInt("messId"),
                    navController = navController,
                    cartViewModel = cartViewModel,
                    subscriptionViewModel = subscriptionViewModel
                )
            }

            composable(
                route = "${Routes.SUBSCRIPTION_FLOW}/{messId}",
                arguments = listOf(navArgument("messId") { type = NavType.IntType })
            ) { backStackEntry ->
                SubscriptionFlowScreen(
                    messId = backStackEntry.arguments!!.getInt("messId"),
                    navController = navController,
                    subscriptionViewModel = subscriptionViewModel
                )
            }

            composable(Routes.ORDER_SUMMARY) {
                OrderSummaryScreen(
                    onBackClick = { navController.popBackStack() },
                    cartViewModel = cartViewModel
                )
            }
        }
    }
}
