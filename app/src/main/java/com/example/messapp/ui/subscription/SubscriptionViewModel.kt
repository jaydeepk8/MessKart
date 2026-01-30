package com.example.messapp.ui.subscription

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SubscriptionViewModel : ViewModel() {

    private val _activeSubscription =
        MutableStateFlow<ActiveSubscription?>(null)

    val activeSubscription: StateFlow<ActiveSubscription?> =
        _activeSubscription

    fun subscribe(subscription: ActiveSubscription) {
        _activeSubscription.value = subscription
    }

    fun cancelSubscription() {
        _activeSubscription.value = null
    }
}
