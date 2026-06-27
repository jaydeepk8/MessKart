@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.example.messapp.ui.subscription

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.messapp.data.source.MessDataSource
import com.example.messapp.navigation.Routes
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppCardBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import com.example.messapp.ui.theme.AppSuccessGreen
import com.example.messapp.ui.theme.AppTextPrimary
import com.example.messapp.ui.theme.AppTextSecondary

private data class PlanChoice(
    val title: String,
    val subtitle: String,
    val meals: Int,
    val price: Int,
    val deliveryFee: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionFlowScreen(
    messId: Int,
    navController: NavController,
    subscriptionViewModel: SubscriptionViewModel
) {
    val mess = remember(messId) { MessDataSource.getMessById(messId) }
    val plans = remember {
        listOf(
            PlanChoice("Weekly", "14 meals, ideal for PG and hostel stays", 14, 1400, 30),
            PlanChoice("Monthly", "60 meals with the best savings", 60, 5200, 0)
        )
    }
    val steps = listOf("Plan", "Meal", "Schedule", "Address", "Review")

    val homeDeliveryAvailable = remember(messId) {
        mess.tags.any { it.contains("Home Delivery", ignoreCase = true) }
    }

    var step by rememberSaveable { mutableIntStateOf(0) }
    var selectedPlan by rememberSaveable { mutableStateOf("Weekly") }
    var serviceType by rememberSaveable {
        mutableStateOf(if (homeDeliveryAvailable) "Delivery" else "Dine in")
    }
    var mealTime by rememberSaveable { mutableStateOf("Lunch & Dinner") }
    var foodType by rememberSaveable { mutableStateOf("Veg") }
    var specialRequest by rememberSaveable { mutableStateOf("") }
    var startDate by rememberSaveable { mutableStateOf("Tomorrow") }
    var timeSlot by rememberSaveable { mutableStateOf("Lunch (12:30 - 1:30 PM)") }
    var addressType by rememberSaveable { mutableStateOf("Use saved address") }
    var address by rememberSaveable { mutableStateOf("123 Main Street, Apt 4B") }
    var paymentMethod by rememberSaveable { mutableStateOf("UPI") }

    val plan = plans.first { it.title == selectedPlan }
    val tax = ((plan.price + plan.deliveryFee) * 0.05).toInt()
    val total = plan.price + plan.deliveryFee + tax
    val subscriptionId = remember(messId, selectedPlan) {
        "MSK-${messId}${selectedPlan.take(1).uppercase()}-${System.currentTimeMillis().toString().takeLast(4)}"
    }

    if (step == steps.size) {
        SubscriptionSuccessScreen(
            subscriptionId = subscriptionId,
            messName = mess.name,
            onViewSubscription = {
                navController.navigate(Routes.SUBSCRIPTION) {
                    popUpTo(Routes.HOME)
                }
            },
            onDone = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.HOME) { inclusive = true }
                }
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meal Plan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (step == 0) navController.popBackStack() else step--
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        },
        bottomBar = {
            FlowBottomBar(
                isFirstStep = step == 0,
                onPrevious = { if (step > 0) step-- else navController.popBackStack() },
                onContinue = {
                    if (step == steps.lastIndex) {
                        subscriptionViewModel.addConfirmedSubscription(
                            ActiveSubscription(
                                messName = mess.name,
                                messImageRes = mess.imageRes,
                                pricePerWeek = if (selectedPlan == "Monthly") plan.price / 4 else plan.price,
                                nextDelivery = "$startDate, $timeSlot",
                                messId = mess.id,
                                mealPreference = mealTime,
                                foodPreference = foodType
                            )
                        )
                    }
                    step++
                },
                continueText = if (step == steps.lastIndex) "Confirm" else "Continue"
            )
        },
        containerColor = AppBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                StepHeader(
                    step = step,
                    totalSteps = steps.size,
                    label = steps[step],
                    title = when (step) {
                        0 -> "Choose Your Plan"
                        1 -> "Your Meal Preferences"
                        2 -> "Delivery Schedule"
                        3 -> "Confirm Address"
                        else -> "Review Subscription"
                    },
                    subtitle = when (step) {
                        0 -> "Pick the commitment that matches your routine."
                        1 -> "We will customize your tiffin around your daily meals."
                        2 -> "Choose when your subscription should start."
                        3 -> "Use your saved address or add a new PG/rental address."
                        else -> "Check your plan, price, and payment method."
                    }
                )
            }

            item {
                when (step) {
                    0 -> PlanStep(plans, selectedPlan) { selectedPlan = it }
                    1 -> PreferenceStep(
                        serviceType = serviceType,
                        homeDeliveryAvailable = homeDeliveryAvailable,
                        mealTime = mealTime,
                        foodType = foodType,
                        specialRequest = specialRequest,
                        onServiceTypeChange = { serviceType = it },
                        onMealTimeChange = { mealTime = it },
                        onFoodTypeChange = { foodType = it },
                        onSpecialRequestChange = { specialRequest = it }
                    )
                    2 -> ScheduleStep(
                        startDate = startDate,
                        timeSlot = timeSlot,
                        onStartDateChange = { startDate = it },
                        onTimeSlotChange = { timeSlot = it }
                    )
                    3 -> AddressStep(
                        addressType = addressType,
                        address = address,
                        onAddressTypeChange = { addressType = it },
                        onAddressChange = { address = it }
                    )
                    else -> ReviewStep(
                        messName = mess.name,
                        messImageRes = mess.imageRes,
                        plan = plan,
                        serviceType = serviceType,
                        mealTime = mealTime,
                        foodType = foodType,
                        startDate = startDate,
                        timeSlot = timeSlot,
                        address = address,
                        paymentMethod = paymentMethod,
                        tax = tax,
                        total = total,
                        onPaymentChange = { paymentMethod = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun StepHeader(
    step: Int,
    totalSteps: Int,
    label: String,
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Step ${step + 1} of $totalSteps", color = AppTextSecondary)
            Text(label, color = AppPrimaryGreen, fontWeight = FontWeight.SemiBold)
        }
        LinearProgressIndicator(
            progress = { (step + 1).toFloat() / totalSteps },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = AppPrimaryGreen,
            trackColor = AppSoftGreen
        )
        Spacer(Modifier.height(10.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = AppTextPrimary)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = AppTextSecondary)
    }
}

@Composable
private fun PlanStep(
    plans: List<PlanChoice>,
    selectedPlan: String,
    onPlanChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        plans.forEach { plan ->
            SelectableCard(
                selected = selectedPlan == plan.title,
                onClick = { onPlanChange(plan.title) }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PlanIcon(Icons.Default.Restaurant, selectedPlan == plan.title)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(plan.title, fontWeight = FontWeight.Bold, color = AppTextPrimary)
                        Text(plan.subtitle, style = MaterialTheme.typography.bodySmall, color = AppTextSecondary)
                    }
                    Text("Rs.${plan.price}", fontWeight = FontWeight.Bold, color = AppPrimaryGreen)
                }
            }
        }
    }
}

@Composable
private fun PreferenceStep(
    serviceType: String,
    homeDeliveryAvailable: Boolean,
    mealTime: String,
    foodType: String,
    specialRequest: String,
    onServiceTypeChange: (String) -> Unit,
    onMealTimeChange: (String) -> Unit,
    onFoodTypeChange: (String) -> Unit,
    onSpecialRequestChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        ChoiceSection("Service Type", Icons.Default.Storefront) {
            ChoiceGroup(
                options = if (homeDeliveryAvailable) listOf("Delivery", "Dine in") else listOf("Dine in"),
                selected = serviceType,
                onSelected = onServiceTypeChange
            )
            if (!homeDeliveryAvailable) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "This mess offers dine-in only.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTextSecondary
                )
            }
        }
        ChoiceSection("Meal Time", Icons.Default.Schedule) {
            ChoiceGroup(
                options = listOf("Lunch only", "Dinner only", "Lunch & Dinner"),
                selected = mealTime,
                onSelected = onMealTimeChange
            )
        }
        ChoiceSection("Food Type", Icons.Default.Restaurant) {
            ChoiceGroup(
                options = listOf("Veg", "Non-Veg"),
                selected = foodType,
                onSelected = onFoodTypeChange
            )
        }
        Column {
            Text("Any special requests? (optional)", fontWeight = FontWeight.Bold, color = AppTextPrimary)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = specialRequest,
                onValueChange = onSpecialRequestChange,
                placeholder = { Text("e.g. Less spicy, no peanuts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
private fun ScheduleStep(
    startDate: String,
    timeSlot: String,
    onStartDateChange: (String) -> Unit,
    onTimeSlotChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        ChoiceSection("Start Date", Icons.Default.CalendarMonth) {
            StartDateSelector(
                startDate = startDate,
                onStartDateChange = onStartDateChange
            )
        }
        ChoiceSection("Preferred Meal Time", Icons.Default.Schedule) {
            ChoiceGroup(
                options = listOf(
                    "Lunch (12:30 - 1:30 PM)",
                    "Dinner (8:00 - 9:00 PM)",
                    "Flexible timing"
                ),
                selected = timeSlot,
                onSelected = onTimeSlotChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StartDateSelector(
    startDate: String,
    onStartDateChange: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val isPreset = startDate == "Today" || startDate == "Tomorrow"

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OptionPill("Today", startDate == "Today") { onStartDateChange("Today") }
        OptionPill("Tomorrow", startDate == "Tomorrow") { onStartDateChange("Tomorrow") }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(if (!isPreset) AppPrimaryGreen else AppSoftGreen.copy(alpha = 0.7f))
                .clickable { showPicker = true }
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (!isPreset) Color.White else AppPrimaryGreen
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (!isPreset) startDate else "Pick a date",
                color = if (!isPreset) Color.White else AppTextPrimary,
                fontWeight = if (!isPreset) FontWeight.Bold else FontWeight.Medium
            )
        }
    }

    if (showPicker) {
        val todayStart = remember { startOfTodayMillis() }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = todayStart,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                    utcTimeMillis >= todayStart
            }
        )

        val pickerColors = DatePickerDefaults.colors(
            containerColor = AppBackground,
            selectedDayContainerColor = AppPrimaryGreen,
            selectedDayContentColor = Color.White,
            todayDateBorderColor = AppPrimaryGreen,
            todayContentColor = AppPrimaryGreen,
            selectedYearContainerColor = AppPrimaryGreen,
            selectedYearContentColor = Color.White
        )

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onStartDateChange(formatStartDate(it)) }
                    showPicker = false
                }) {
                    Text("Confirm", color = AppPrimaryGreen, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Cancel", color = AppTextSecondary)
                }
            },
            colors = pickerColors
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = {
                    Text(
                        "Select start date",
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp),
                        color = AppTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = pickerColors
            )
        }
    }
}

private fun startOfTodayMillis(): Long {
    val cal = java.util.Calendar.getInstance()
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
    cal.set(java.util.Calendar.MINUTE, 0)
    cal.set(java.util.Calendar.SECOND, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

private fun formatStartDate(millis: Long): String {
    val formatter = java.text.SimpleDateFormat("d MMM yyyy", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(millis))
}

@Composable
private fun AddressStep(
    addressType: String,
    address: String,
    onAddressTypeChange: (String) -> Unit,
    onAddressChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        ChoiceSection("Delivery Address", Icons.Default.Home) {
            ChoiceGroup(
                options = listOf("Use saved address", "Add new address"),
                selected = addressType,
                onSelected = onAddressTypeChange
            )
            Spacer(Modifier.height(12.dp))
            AnimatedVisibility(visible = addressType == "Use saved address") {
                Text(address, color = AppTextSecondary)
            }
            AnimatedVisibility(visible = addressType == "Add new address") {
                OutlinedTextField(
                    value = address,
                    onValueChange = onAddressChange,
                    placeholder = { Text("Flat, PG name, street, landmark") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ReviewStep(
    messName: String,
    messImageRes: Int,
    plan: PlanChoice,
    serviceType: String,
    mealTime: String,
    foodType: String,
    startDate: String,
    timeSlot: String,
    address: String,
    paymentMethod: String,
    tax: Int,
    total: Int,
    onPaymentChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        CardBlock {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(messImageRes),
                    contentDescription = messName,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(messName, fontWeight = FontWeight.Bold, color = AppTextPrimary)
                    Text("$foodType • $mealTime", color = AppTextSecondary)
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
            ReviewGrid(
                items = buildList {
                    add("Plan" to "${plan.title} Subscription")
                    add("Meals" to "${plan.meals} meals")
                    add("Service" to serviceType)
                    add("Starts" to "$startDate, $timeSlot")
                    if (serviceType == "Delivery") add("Address" to address)
                }
            )
        }

        CardBlock(background = AppSoftGreen.copy(alpha = 0.5f)) {
            Text("Price Summary", fontWeight = FontWeight.Bold, color = AppTextPrimary)
            Spacer(Modifier.height(14.dp))
            PriceRow("${plan.title} Plan (${plan.meals} meals)", plan.price)
            PriceRow("Delivery Fee", plan.deliveryFee)
            PriceRow("Taxes (5%)", tax)
            HorizontalDivider(Modifier.padding(vertical = 12.dp))
            PriceRow("Total Amount", total, bold = true)
        }

        Column {
            Text("Payment Method", fontWeight = FontWeight.Bold, color = AppTextPrimary)
            Spacer(Modifier.height(10.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PaymentChoice("UPI", Icons.Default.Payments, paymentMethod, onPaymentChange)
                PaymentChoice("Card", Icons.Default.CreditCard, paymentMethod, onPaymentChange)
                PaymentChoice("Cash", Icons.Default.Money, paymentMethod, onPaymentChange)
            }
        }
    }
}

@Composable
private fun SubscriptionSuccessScreen(
    subscriptionId: String,
    messName: String,
    onViewSubscription: () -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(AppSoftGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppSuccessGreen, modifier = Modifier.size(64.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("Subscription Confirmed", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = AppTextPrimary)
        Spacer(Modifier.height(8.dp))
        Text(
            "Your $messName meal plan is active. We will remind you before your first delivery.",
            color = AppTextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text("Subscription ID: $subscriptionId", color = AppPrimaryGreen, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onViewSubscription,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryGreen)
        ) {
            Text("View Subscription")
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Back to Home", color = AppTextPrimary)
        }
    }
}

@Composable
private fun ChoiceSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PlanIcon(icon, selected = true)
            Spacer(Modifier.width(10.dp))
            Text(title, fontWeight = FontWeight.Bold, color = AppTextPrimary)
        }
        Spacer(Modifier.height(10.dp))
        CardBlock(content = content)
    }
}

@Composable
private fun ChoiceGroup(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        options.forEach { option ->
            OptionPill(
                text = option,
                selected = option == selected,
                onClick = { onSelected(option) }
            )
        }
    }
}

@Composable
private fun SelectableCard(
    selected: Boolean,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (selected) 1.5.dp else 0.dp,
                color = if (selected) AppPrimaryGreen else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) AppSoftGreen else AppCardBackground),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun CardBlock(
    background: Color = AppCardBackground,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(18.dp), content = content)
    }
}

@Composable
private fun OptionPill(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) AppPrimaryGreen else AppSoftGreen.copy(alpha = 0.7f))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else AppTextPrimary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun PaymentChoice(
    label: String,
    icon: ImageVector,
    selected: String,
    onSelected: (String) -> Unit
) {
    val isSelected = selected == label
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) AppPrimaryGreen else AppCardBackground)
            .clickable { onSelected(label) }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(17.dp), tint = if (isSelected) Color.White else AppTextSecondary)
        Spacer(Modifier.width(6.dp))
        Text(label, color = if (isSelected) Color.White else AppTextPrimary, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun FlowBottomBar(
    isFirstStep: Boolean,
    onPrevious: () -> Unit,
    onContinue: () -> Unit,
    continueText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onPrevious,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (isFirstStep) "Cancel" else "Previous", color = AppTextPrimary)
        }
        Button(
            onClick = onContinue,
            modifier = Modifier.weight(1.3f),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryGreen)
        ) {
            Text(continueText)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun PlanIcon(icon: ImageVector, selected: Boolean) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(if (selected) AppSoftGreen else AppCardBackground, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = AppPrimaryGreen, modifier = Modifier.size(21.dp))
    }
}

@Composable
private fun ReviewGrid(items: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items.forEach { (label, value) ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(label.uppercase(), color = AppTextSecondary, style = MaterialTheme.typography.labelSmall)
                Text(value, color = AppTextPrimary, fontWeight = FontWeight.Medium, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, amount: Int, bold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = if (bold) AppTextPrimary else AppTextSecondary, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text("Rs.$amount", color = if (bold) AppPrimaryGreen else AppTextPrimary, fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium)
    }
}
