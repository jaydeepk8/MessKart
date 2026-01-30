package com.example.messapp.data.source

import com.example.messapp.R
import com.example.messapp.data.model.Mess

object MessDataSource {

    private val messList = listOf(
        Mess(
            id = 1,
            name = "Annapurna Mess",
            imageRes = R.drawable.tiffin1,
            rating = 4.5,
            cuisine = "North Indian • Thali",
            distanceKm = "1.2 km",
            timeMin = "25 mins",
            tags = listOf("Pure Veg", "Home Delivery", "Dine-in")
        ),
        Mess(
            id = 2,
            name = "Green Leaf Tiffins",
            imageRes = R.drawable.tiffin1,
            rating = 4.8,
            cuisine = "Healthy • Homemade",
            distanceKm = "0.5 km",
            timeMin = "15 mins",
            tags = listOf("Pure Veg", "Dine-in")
        ),
        Mess(
            id = 3,
            name = "Spicy Kitchen",
            imageRes = R.drawable.tiffin1,
            rating = 4.6,
            cuisine = "Maharashtrian • Homemade",
            distanceKm = "0.8 km",
            timeMin = "20 mins",
            tags = listOf("Veg / Non-Veg", "Dine-in")
        ),
        Mess(
            id = 4,
            name = "Shri Krishna Tiffins",
            imageRes = R.drawable.tiffin1,
            rating = 4.7,
            cuisine = "South Indian • Homemade",
            distanceKm = "0.6 km",
            timeMin = "15 mins",
            tags = listOf("Pure Veg", "Dine-in")
        ),
        Mess(
            id = 5,
            name = "Gharkul Mess",
            imageRes = R.drawable.tiffin1,
            rating = 4.7,
            cuisine = "South Indian • Homemade",
            distanceKm = "0.6 km",
            timeMin = "15 mins",
            tags = listOf("Pure Veg", "Dine-in")
        )
    )

    fun getAllMesses(): List<Mess> = messList

    fun getMessById(id: Int): Mess {
        return messList.first { it.id == id }
    }
}
