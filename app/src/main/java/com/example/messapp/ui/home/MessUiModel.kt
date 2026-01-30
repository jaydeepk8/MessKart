package com.example.messapp.ui.home

import androidx.annotation.DrawableRes

data class MessUiModel(
    val id: Int,
    val name: String,
    val cuisine: String,
    val distanceKm: String,
    val timeMin: String,
    val rating: Double,
    val tags: List<String>,
    @DrawableRes val imageRes: Int,
    val isFavourite: Boolean = false
)
