package com.example.messapp.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.messapp.ui.home.HomeUiState

@Composable
fun LocationAndSearchSection(
    uiState: HomeUiState,
    onSearchChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        Text(
            text = "CURRENT LOCATION",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = uiState.locationName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Change location",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = uiState.address,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchBarWithFilter(
            query = uiState.searchQuery,
            onQueryChange = onSearchChange,
            onFilterClick = onFilterClick
        )
    }
}
