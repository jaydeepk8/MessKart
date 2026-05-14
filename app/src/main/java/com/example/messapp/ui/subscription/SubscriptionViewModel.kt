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

    fun addConfirmedSubscription(subscription: ActiveSubscription) {
        _subscriptions.value = _subscriptions.value
            .filterNot { it.messId == subscription.messId } + subscription
        _pendingSubscription.value = null
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

    fun cancelSubscription(messId: Int, reason: String) {
        _subscriptions.value = _subscriptions.value
            .map {
                if (it.messId == messId) it.copy(cancelReason = reason) else it
            }
            .filter { it.messId != messId }
    }

    fun togglePause(messId: Int) {
        updateSubscription(messId) { it.copy(isPaused = !it.isPaused) }
    }

    fun toggleSkipNextMeal(messId: Int) {
        updateSubscription(messId) { it.copy(isNextMealSkipped = !it.isNextMealSkipped) }
    }

    fun updateMealPreference(messId: Int, mealPreference: String, foodPreference: String) {
        updateSubscription(messId) {
            it.copy(
                mealPreference = mealPreference,
                foodPreference = foodPreference
            )
        }
    }

    private fun updateSubscription(
        messId: Int,
        transform: (ActiveSubscription) -> ActiveSubscription
    ) {
        _subscriptions.value = _subscriptions.value.map {
            if (it.messId == messId) transform(it) else it
        }
    }
}
