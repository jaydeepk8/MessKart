package com.example.messapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.ui.profile.components.AccountSettingsSection
import com.example.messapp.ui.profile.components.FoodOrdersSection
import com.example.messapp.ui.profile.components.MoreSection
import com.example.messapp.ui.profile.components.ProfileUserCard
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onPersonalInfoClick: () -> Unit = {},
    onManageAddressesClick: () -> Unit = {},
    onPaymentMethodsClick: () -> Unit = {},
    onSubscriptionsAndOrdersClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onOffersClick: () -> Unit = {}
) {

    var vegModeEnabled by rememberSaveable { mutableStateOf(false) }
    var userName by rememberSaveable { mutableStateOf("Rohan Mehta") }
    var userPhone by rememberSaveable { mutableStateOf("9876543210") }
    var showEditSheet by remember { mutableStateOf(false) }

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
                    userName = userName,
                    phoneNumber = userPhone,
                    onEditClick = { showEditSheet = true }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                AccountSettingsSection(
                    onPersonalInfoClick = onPersonalInfoClick,
                    onManageAddressesClick = onManageAddressesClick,
                    onPaymentMethodsClick = onPaymentMethodsClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                FoodOrdersSection(
                    vegModeEnabled = vegModeEnabled,
                    onVegModeToggle = { vegModeEnabled = it },
                    onSubscriptionsAndOrdersClick = onSubscriptionsAndOrdersClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                MoreSection(
                    onNotificationsClick = onNotificationsClick,
                    onOffersClick = onOffersClick,
                    onHelpSupportClick = { showComingSoon("Help and support") }
                )
            }
        }
    }

    if (showEditSheet) {
        EditProfileSheet(
            initialName = userName,
            initialPhone = userPhone,
            onDismiss = { showEditSheet = false },
            onSave = { name, phone ->
                userName = name
                userPhone = phone
                showEditSheet = false
                scope.launch { snackbarHostState.showSnackbar("Profile updated") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileSheet(
    initialName: String,
    initialPhone: String,
    onDismiss: () -> Unit,
    onSave: (name: String, phone: String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var phone by remember { mutableStateOf(initialPhone) }

    val canSave = name.isNotBlank() && phone.length >= 10
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                colors = profileFieldColors()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { input -> phone = input.filter { it.isDigit() }.take(10) },
                label = { Text("Phone Number") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = profileFieldColors()
            )

            Button(
                onClick = { onSave(name.trim(), phone) },
                enabled = canSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppPrimaryGreen,
                    disabledContainerColor = Color(0xFFCDE3AE)
                )
            ) {
                Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun profileFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppPrimaryGreen,
    unfocusedBorderColor = Color.Black.copy(alpha = 0.25f),
    focusedLabelColor = AppPrimaryGreen,
    focusedLeadingIconColor = AppPrimaryGreen,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = AppPrimaryGreen
)

