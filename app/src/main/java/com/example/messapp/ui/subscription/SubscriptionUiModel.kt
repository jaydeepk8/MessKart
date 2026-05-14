package com.example.messapp.ui.subscription

data class ActiveSubscription(
    val messName: String,
    val messImageRes: Int,
    val pricePerWeek: Int,
    val nextDelivery: String,
    val messId: Int,
    val mealPreference: String = "Lunch & Dinner",
    val foodPreference: String = "Veg",
    val isPaused: Boolean = false,
    val isNextMealSkipped: Boolean = false,
    val cancelReason: String? = null
)
