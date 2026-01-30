package com.example.messapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp

@Composable
fun StickyTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onViewMapClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .zIndex(1f)
    ) {

        SearchBarWithFilter(
            query = query,
            onQueryChange = onQueryChange,
            onFilterClick = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        MessNearMeHeader(
            onViewMapClick = onViewMapClick
        )
    }
}
