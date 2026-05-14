@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.example.messapp.ui.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppCardBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import com.example.messapp.ui.theme.AppTextPrimary
import com.example.messapp.ui.theme.AppTextSecondary
import kotlinx.coroutines.launch

@Composable
fun SubscriptionScreen(
    subscriptionViewModel: SubscriptionViewModel
) {
    val subscriptions by subscriptionViewModel.subscriptions.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var managingSubscription by remember { mutableStateOf<ActiveSubscription?>(null) }
    var cancelingSubscription by remember { mutableStateOf<ActiveSubscription?>(null) }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("My Subscriptions", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AppTextPrimary)
            Spacer(Modifier.height(4.dp))
            Text(
                "${subscriptions.size} active plan${if (subscriptions.size != 1) "s" else ""}",
                fontSize = 13.sp,
                color = AppTextSecondary
            )
            Spacer(Modifier.height(16.dp))

            if (subscriptions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No active subscriptions yet.", color = AppTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(6.dp))
                        Text("Go to a mess and subscribe to a plan.", color = AppTextSecondary, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(subscriptions) { subscription ->
                        ActiveSubscriptionCard(
                            subscription = subscription,
                            onManage = { managingSubscription = subscription },
                            onCancel = { cancelingSubscription = subscription }
                        )
                    }
                }
            }
        }
    }

    managingSubscription?.let { subscription ->
        ManageSubscriptionSheet(
            subscription = subscription,
            onDismiss = { managingSubscription = null },
            onTogglePause = {
                subscriptionViewModel.togglePause(subscription.messId)
                managingSubscription = null
                scope.launch {
                    snackbarHostState.showSnackbar(
                        if (subscription.isPaused) "Deliveries resumed" else "Deliveries paused"
                    )
                }
            },
            onToggleSkip = {
                subscriptionViewModel.toggleSkipNextMeal(subscription.messId)
                managingSubscription = null
                scope.launch {
                    snackbarHostState.showSnackbar(
                        if (subscription.isNextMealSkipped) "Next meal restored" else "Next meal skipped"
                    )
                }
            },
            onPreferenceChange = { meal, food ->
                subscriptionViewModel.updateMealPreference(subscription.messId, meal, food)
                managingSubscription = null
                scope.launch { snackbarHostState.showSnackbar("Meal preference updated") }
            }
        )
    }

    cancelingSubscription?.let { subscription ->
        CancelSubscriptionDialog(
            messName = subscription.messName,
            onDismiss = { cancelingSubscription = null },
            onConfirm = { reason ->
                subscriptionViewModel.cancelSubscription(subscription.messId, reason)
                cancelingSubscription = null
                scope.launch { snackbarHostState.showSnackbar("Subscription cancelled") }
            }
        )
    }
}

@Composable
fun ActiveSubscriptionCard(
    subscription: ActiveSubscription,
    onManage: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val statusText = when {
        subscription.isPaused -> "Paused"
        subscription.isNextMealSkipped -> "Next meal skipped"
        else -> "Active"
    }
    val statusColor = if (subscription.isPaused || subscription.isNextMealSkipped) {
        Color(0xFFFFA726)
    } else {
        AppPrimaryGreen
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppCardBackground),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(subscription.messImageRes),
                    contentDescription = subscription.messName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.9f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(statusText, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(subscription.messName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = AppTextPrimary)
                Spacer(Modifier.height(12.dp))
                InfoRow(
                    Icons.Default.AccessTime,
                    if (subscription.isPaused) "Deliveries paused" else "Next delivery: ${subscription.nextDelivery}"
                )
                Spacer(Modifier.height(8.dp))
                InfoRow(Icons.Default.Restaurant, "${subscription.mealPreference} • ${subscription.foodPreference}")
                Spacer(Modifier.height(8.dp))
                InfoRow(Icons.Default.CheckCircle, "₹${subscription.pricePerWeek} / week")
                if (subscription.isNextMealSkipped) {
                    Spacer(Modifier.height(8.dp))
                    InfoRow(Icons.Default.SkipNext, "Tomorrow's meal will be skipped")
                }
                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onManage,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryGreen)
                    ) {
                        Text("Manage Plan", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageSubscriptionSheet(
    subscription: ActiveSubscription,
    onDismiss: () -> Unit,
    onTogglePause: () -> Unit,
    onToggleSkip: () -> Unit,
    onPreferenceChange: (String, String) -> Unit
) {
    var mealPreference by remember(subscription.messId, subscription.mealPreference) {
        mutableStateOf(subscription.mealPreference)
    }
    var foodPreference by remember(subscription.messId, subscription.foodPreference) {
        mutableStateOf(subscription.foodPreference)
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Manage ${subscription.messName}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = AppTextPrimary)
            Text("Adjust your plan when your daily routine changes.", color = AppTextSecondary, fontSize = 14.sp)

            ManagementAction(
                icon = Icons.Default.PauseCircle,
                title = if (subscription.isPaused) "Resume deliveries" else "Pause deliveries",
                subtitle = if (subscription.isPaused) "Start receiving meals again" else "Temporarily stop all upcoming deliveries",
                onClick = onTogglePause
            )

            ManagementAction(
                icon = Icons.Default.SkipNext,
                title = if (subscription.isNextMealSkipped) "Undo next meal skip" else "Skip next meal",
                subtitle = "Useful when you are eating outside or travelling",
                onClick = onToggleSkip
            )

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AppCardBackground),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, null, tint = AppPrimaryGreen)
                        Spacer(Modifier.width(10.dp))
                        Text("Change meal preference", fontWeight = FontWeight.Bold, color = AppTextPrimary)
                    }
                    Spacer(Modifier.height(12.dp))
                    PreferenceChips(
                        options = listOf("Lunch only", "Dinner only", "Lunch & Dinner"),
                        selected = mealPreference,
                        onSelected = { mealPreference = it }
                    )
                    Spacer(Modifier.height(10.dp))
                    PreferenceChips(
                        options = listOf("Veg", "Non-Veg", "Egg"),
                        selected = foodPreference,
                        onSelected = { foodPreference = it }
                    )
                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = { onPreferenceChange(mealPreference, foodPreference) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryGreen)
                    ) {
                        Text("Save Preference")
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ManagementAction(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AppCardBackground),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppSoftGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = AppPrimaryGreen)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = AppTextPrimary)
                Text(subtitle, color = AppTextSecondary, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun PreferenceChips(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) AppPrimaryGreen else AppSoftGreen)
                    .clickable { onSelected(option) }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    option,
                    color = if (isSelected) Color.White else AppTextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CancelSubscriptionDialog(
    messName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedReason by remember { mutableStateOf("Going home / travelling") }
    val reasons = listOf(
        "Going home / travelling",
        "Too expensive",
        "Taste did not match",
        "Delivery timing issue",
        "Found another mess"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancel subscription?", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Tell us why you want to cancel $messName.", color = AppTextSecondary)
                Spacer(Modifier.height(12.dp))
                reasons.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedReason = reason }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedReason == reason,
                            onClick = { selectedReason = reason },
                            colors = RadioButtonDefaults.colors(selectedColor = AppPrimaryGreen)
                        )
                        Text(reason, color = AppTextPrimary)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedReason) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel Plan")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Keep Plan")
            }
        }
    )
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = AppTextSecondary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = AppTextSecondary, fontSize = 14.sp)
    }
}
