package com.example.messapp.ui.subscription

data class ActiveSubscription(
    val messName: String,
    val messImageRes: Int,
    val pricePerWeek: Int,
    val nextDelivery: String
)
