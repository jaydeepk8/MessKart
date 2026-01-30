package com.example.messapp.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messapp.ui.details.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    // Expose totals as StateFlow so Compose can observe them
    val totalItems: StateFlow<Int> = _cartItems.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalPrice: StateFlow<Int> = _cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun addItem(menuItem: MenuItem) {
        val current = _cartItems.value
        val existingIndex = current.indexOfFirst { it.id == menuItem.id }

        _cartItems.value = if (existingIndex >= 0) {
            current.toMutableList().apply {
                this[existingIndex] = this[existingIndex].copy(
                    quantity = this[existingIndex].quantity + 1
                )
            }
        } else {
            current + CartItem(
                id = menuItem.id,
                name = menuItem.name,
                price = menuItem.price,
                imageRes = menuItem.imageRes,
                quantity = 1
            )
        }
    }

    fun removeItem(menuItemId: Int) {
        val current = _cartItems.value
        val existingIndex = current.indexOfFirst { it.id == menuItemId }

        if (existingIndex < 0) return

        val existing = current[existingIndex]

        _cartItems.value = if (existing.quantity <= 1) {
            current.filterIndexed { index, _ -> index != existingIndex }
        } else {
            current.toMutableList().apply {
                this[existingIndex] = existing.copy(quantity = existing.quantity - 1)
            }
        }
    }

    fun increaseQuantity(itemId: Int) {
        val current = _cartItems.value
        val existingIndex = current.indexOfFirst { it.id == itemId }

        if (existingIndex < 0) return

        _cartItems.value = current.toMutableList().apply {
            this[existingIndex] = this[existingIndex].copy(
                quantity = this[existingIndex].quantity + 1
            )
        }
    }

    fun decreaseQuantity(itemId: Int) {
        val current = _cartItems.value
        val existingIndex = current.indexOfFirst { it.id == itemId }

        if (existingIndex < 0) return

        val existing = current[existingIndex]

        _cartItems.value = if (existing.quantity <= 1) {
            current.filterIndexed { index, _ -> index != existingIndex }
        } else {
            current.toMutableList().apply {
                this[existingIndex] = existing.copy(quantity = existing.quantity - 1)
            }
        }
    }
}