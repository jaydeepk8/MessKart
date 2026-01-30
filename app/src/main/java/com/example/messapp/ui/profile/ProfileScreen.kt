package com.example.messapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.messapp.ui.profile.components.AccountSettingsSection
import com.example.messapp.ui.profile.components.FoodOrdersSection
import com.example.messapp.ui.profile.components.MoreSection
import com.example.messapp.ui.profile.components.ProfileUserCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {

    var vegModeEnabled by rememberSaveable { mutableStateOf(false) }

    Scaffold(
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
                .background(Color(0xFFF7F7F7)),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(16.dp))

                ProfileUserCard(
                    userName = "Jaydeep Kulkarni",
                    phoneNumber = "9888745238",
                    onEditClick = { }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                AccountSettingsSection(
                    onPersonalInfoClick = { },
                    onManageAddressesClick = { },
                    onPaymentMethodsClick = { }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                FoodOrdersSection(
                    vegModeEnabled = vegModeEnabled,
                    onVegModeToggle = { vegModeEnabled = it },
                    onSubscriptionsAndOrdersClick = { },
                    onOrderHistoryClick = { }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                MoreSection(
                    onNotificationsClick = { },
                    onHelpSupportClick = { }
                )
            }
        }
    }
}

