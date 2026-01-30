package com.example.messapp.ui.cart

data class CartItem(
    val id: Int,
    val name: String,
    val price: Int,
    val imageRes: Int,
    var quantity: Int
)
