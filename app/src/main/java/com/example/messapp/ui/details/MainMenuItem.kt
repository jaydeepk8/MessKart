package com.example.messapp.ui.details

data class MainMenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val imageRes: Int,
    val available: Boolean = true
)