package com.example.messapp.data.model

data class Mess(
    val id: Int,
    val name: String,
    val imageRes: Int,
    val rating: Double,
    val cuisine: String,
    val distanceKm: String,
    val timeMin: String,
    val tags: List<String>
)
