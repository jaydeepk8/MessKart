package com.example.messapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
import com.example.messapp.ui.theme.AppBackground

@Composable
fun StickyTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onViewMapClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground)
            .zIndex(1f)
    ) {

        SearchBarWithFilter(
            query = query,
            onQueryChange = onQueryChange,
            onFilterClick = onFilterClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        MessNearMeHeader(
            onViewMapClick = onViewMapClick
        )
    }
}
