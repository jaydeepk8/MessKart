package com.example.messapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.messapp.ui.profile.components.AccountSettingsSection
import com.example.messapp.ui.profile.components.FoodOrdersSection
import com.example.messapp.ui.profile.components.MoreSection
import com.example.messapp.ui.profile.components.ProfileUserCard
import com.example.messapp.ui.theme.AppBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onPersonalInfoClick: () -> Unit = {}
) {

    var vegModeEnabled by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showComingSoon(feature: String) {
        scope.launch {
            snackbarHostState.showSnackbar("$feature is coming soon")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.titleLarge
                    )
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
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(16.dp))

                ProfileUserCard(
                    userName = "Jaydeep Kulkarni",
                    phoneNumber = "9888745238",
                    onEditClick = { showComingSoon("Profile editing") }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                AccountSettingsSection(
                    onPersonalInfoClick = onPersonalInfoClick,
                    onManageAddressesClick = { showComingSoon("Address management") },
                    onPaymentMethodsClick = { showComingSoon("Payment methods") }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                FoodOrdersSection(
                    vegModeEnabled = vegModeEnabled,
                    onVegModeToggle = { vegModeEnabled = it },
                    onSubscriptionsAndOrdersClick = { showComingSoon("Subscriptions and orders") },
                    onOrderHistoryClick = { showComingSoon("Order history") }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                MoreSection(
                    onNotificationsClick = { showComingSoon("Notifications") },
                    onHelpSupportClick = { showComingSoon("Help and support") }
                )
            }
        }
    }
}

