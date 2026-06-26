package com.example.messapp.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import kotlinx.coroutines.launch

data class SavedAddress(
    val id: Int,
    val label: String,
    val line: String,
    val icon: ImageVector,
    val iconBackground: Color,
    val iconTint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAddressesScreen(
    onBackClick: () -> Unit
) {
    val addresses = remember {
        mutableStateListOf(
            SavedAddress(
                id = 1,
                label = "Home",
                line = "221B Baker St, Marylebone, Pune",
                icon = Icons.Default.Home,
                iconBackground = Color(0xFFFFEDE5),
                iconTint = Color(0xFFFF5722)
            ),
            SavedAddress(
                id = 2,
                label = "Work",
                line = "42 Wallaby Way, Hinjewadi, Pune",
                icon = Icons.Default.Work,
                iconBackground = Color(0xFFE8F0FF),
                iconTint = Color(0xFF2962FF)
            )
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showComingSoon(action: String) {
        scope.launch { snackbarHostState.showSnackbar(action) }
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Delivery Addresses",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AppBackground)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(addresses, key = { it.id }) { address ->
                    AddressCard(
                        address = address,
                        onEdit = { showComingSoon("Editing ${address.label} address") },
                        onDelete = {
                            addresses.remove(address)
                            showComingSoon("${address.label} address removed")
                        }
                    )
                }
            }

            Button(
                onClick = { showComingSoon("Add new address is coming soon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryGreen)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Add New Address",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun AddressCard(
    address: SavedAddress,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFEDEDED))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(address.iconBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = address.icon,
                    contentDescription = null,
                    tint = address.iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = address.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = address.line,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF667085),
                    lineHeight = 18.sp
                )
            }

            IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit ${address.label}",
                    tint = Color(0xFF9AA0A6),
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete ${address.label}",
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
