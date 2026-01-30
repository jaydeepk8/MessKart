package com.example.messapp.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun OfferBannerImage(
    imageRes: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2.4f),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Offer banner",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

