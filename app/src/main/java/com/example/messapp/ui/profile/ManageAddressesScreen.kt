package com.example.messapp.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import kotlinx.coroutines.launch

data class SavedAddress(
    val id: Int,
    val label: String,
    val line: String
)

/** Type-aware visuals so every address shows a logo that matches its label. */
private data class AddressStyle(
    val icon: ImageVector,
    val background: Color,
    val tint: Color
)

private fun styleFor(label: String): AddressStyle = when (label.trim().lowercase()) {
    "home" -> AddressStyle(Icons.Default.Home, Color(0xFFFFEDE5), Color(0xFFFF5722))
    "work" -> AddressStyle(Icons.Default.Work, Color(0xFFE8F0FF), Color(0xFF2962FF))
    else -> AddressStyle(Icons.Default.Place, Color(0xFFEDE7F6), Color(0xFF5E35B1))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAddressesScreen(
    onBackClick: () -> Unit
) {
    val addresses = remember {
        mutableStateListOf(
            SavedAddress(1, "Home", "221B Baker St, Marylebone, Pune"),
            SavedAddress(2, "Work", "42 Wallaby Way, Hinjewadi, Pune")
        )
    }

    var nextId by remember { mutableStateOf(3) }
    var editingAddress by remember { mutableStateOf<SavedAddress?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun notify(message: String) {
        scope.launch { snackbarHostState.showSnackbar(message) }
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

            if (addresses.isEmpty()) {
                EmptyAddresses(modifier = Modifier.weight(1f))
            } else {
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
                            onEdit = {
                                editingAddress = address
                                showSheet = true
                            },
                            onDelete = {
                                addresses.remove(address)
                                notify("${address.label} address removed")
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    editingAddress = null
                    showSheet = true
                },
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

    if (showSheet) {
        AddressEditorSheet(
            existing = editingAddress,
            onDismiss = { showSheet = false },
            onSave = { label, line ->
                val current = editingAddress
                if (current == null) {
                    addresses.add(SavedAddress(nextId, label, line))
                    nextId += 1
                    notify("$label address added")
                } else {
                    val index = addresses.indexOfFirst { it.id == current.id }
                    if (index != -1) {
                        addresses[index] = current.copy(label = label, line = line)
                    }
                    notify("$label address updated")
                }
                showSheet = false
            }
        )
    }
}

@Composable
private fun AddressCard(
    address: SavedAddress,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val style = styleFor(address.label)

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

            AddressLogo(style = style)

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

/** Rounded gradient-feel tile with a subtle ring — a cleaner "logo" than a flat square. */
@Composable
private fun AddressLogo(style: AddressStyle) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(style.background, RoundedCornerShape(14.dp))
            .border(1.dp, style.tint.copy(alpha = 0.18f), RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = style.icon,
            contentDescription = null,
            tint = style.tint,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun EmptyAddresses(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOff,
            contentDescription = null,
            tint = Color(0xFFB0B0B0),
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "No saved addresses",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Add an address so your tiffin knows where to go.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddressEditorSheet(
    existing: SavedAddress?,
    onDismiss: () -> Unit,
    onSave: (label: String, line: String) -> Unit
) {
    val presetLabels = listOf("Home", "Work", "Other")

    val initialPreset = when {
        existing == null -> "Home"
        existing.label in presetLabels -> existing.label
        else -> "Other"
    }
    var selectedPreset by remember { mutableStateOf(initialPreset) }
    var customLabel by remember {
        mutableStateOf(if (existing != null && existing.label !in presetLabels) existing.label else "")
    }
    var line by remember { mutableStateOf(existing?.line ?: "") }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val resolvedLabel = if (selectedPreset == "Other") customLabel.trim() else selectedPreset
    val canSave = line.isNotBlank() && resolvedLabel.isNotBlank()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = if (existing == null) "Add Address" else "Edit Address",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "ADDRESS TYPE",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9AA0A6),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.6.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    presetLabels.forEach { preset ->
                        val isSelected = preset == selectedPreset
                        val style = styleFor(if (preset == "Other") "other" else preset)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .background(
                                    color = if (isSelected) AppSoftGreen else Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) AppPrimaryGreen else Color(0xFFD9D9D9),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedPreset = preset },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = style.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) AppPrimaryGreen else style.tint,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = preset,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) AppPrimaryGreen else Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }

            if (selectedPreset == "Other") {
                OutlinedTextField(
                    value = customLabel,
                    onValueChange = { customLabel = it },
                    label = { Text("Label name") },
                    placeholder = { Text("e.g. Hostel, Gym, Parents") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    colors = fieldColors()
                )
            }

            OutlinedTextField(
                value = line,
                onValueChange = { line = it },
                label = { Text("Full address") },
                placeholder = { Text("Flat / House no, Street, Area, City, Pincode") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 96.dp),
                shape = RoundedCornerShape(12.dp),
                minLines = 2,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                colors = fieldColors()
            )

            Button(
                onClick = { onSave(resolvedLabel, line.trim()) },
                enabled = canSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppPrimaryGreen,
                    disabledContainerColor = Color(0xFFCDE3AE)
                )
            ) {
                Text(
                    text = if (existing == null) "Save Address" else "Update Address",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppPrimaryGreen,
    unfocusedBorderColor = Color.Black.copy(alpha = 0.25f),
    focusedLabelColor = AppPrimaryGreen,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = AppPrimaryGreen
)
