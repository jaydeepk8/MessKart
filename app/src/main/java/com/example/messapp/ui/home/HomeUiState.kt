package com.example.messapp.ui.home

import com.example.messapp.R

data class HomeUiState(
    val locationName: String = "Home",
    val address: String = "123, Green Park Society, Kothrud, Pune",
    val searchQuery: String = "",
    val offerBanners: List<Int> = listOf(
        R.drawable.b1,
        R.drawable.b2,
        R.drawable.b3
    )
)
