package com.example.messapp.ui.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddressBottomSheet(
    onSave: () -> Unit
) {
    Column(Modifier.padding(20.dp)) {
        Text("Edit Address", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = "123 Main Street, Apt 4B",
            onValueChange = {},
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Address")
        }
    }
}
