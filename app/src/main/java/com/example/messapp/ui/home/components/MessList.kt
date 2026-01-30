package com.example.messapp.ui.home.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.messapp.data.model.Mess

@Composable
fun MessList(
    messList: List<Mess>,
    onMessClick: (Mess) -> Unit
) {
    LazyColumn {
        items(messList) { mess ->
            MessItemCard(
                mess = mess,
                onClick = onMessClick
            )
        }
    }
}
