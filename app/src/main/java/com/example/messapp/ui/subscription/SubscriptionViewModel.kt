package com.example.messapp.ui.subscription

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class SubscriptionViewModel : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<ActiveSubscription>>(emptyList())
    val subscriptions: StateFlow<List<ActiveSubscription>> = _subscriptions

    private val _pendingSubscription = MutableStateFlow<ActiveSubscription?>(null)
    val pendingSubscription: StateFlow<ActiveSubscription?> = _pendingSubscription

    fun hasActiveSubscription(): Boolean = _subscriptions.value.isNotEmpty()

    fun isSubscribedTo(messId: Int): Boolean =
        _subscriptions.value.any { it.messId == messId }

    fun requestSubscribe(subscription: ActiveSubscription) {
        if (hasActiveSubscription() && !isSubscribedTo(subscription.messId)) {
            _pendingSubscription.value = subscription
        } else if (!isSubscribedTo(subscription.messId)) {
            _subscriptions.value = _subscriptions.value + subscription
        }
    }

    fun confirmAddSubscription() {
        val pending = _pendingSubscription.value ?: return
        _subscriptions.value = _subscriptions.value + pending
        _pendingSubscription.value = null
    }

    fun cancelPendingSubscription() {
        _pendingSubscription.value = null
    }

    fun cancelSubscription(messId: Int) {
        _subscriptions.value = _subscriptions.value.filter { it.messId != messId }
    }
}