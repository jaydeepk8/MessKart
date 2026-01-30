package com.example.messapp.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Subscription,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val primaryGreen = Color(0xFF8BC34A)
    val inactiveGray = Color(0xFF9E9E9E)
    val barBackground = Color.White

    NavigationBar(
        containerColor = barBackground,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->

            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },

                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = primaryGreen,
                    selectedTextColor = primaryGreen,
                    unselectedIconColor = inactiveGray,
                    unselectedTextColor = inactiveGray
                ),

                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}
