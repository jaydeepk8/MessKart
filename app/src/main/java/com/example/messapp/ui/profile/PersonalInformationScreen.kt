package com.example.messapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInformationScreen(
    initialName: String = "",
    initialPhone: String = "",
    onBackClick: () -> Unit
) {
    var fullName by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf(initialPhone) }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var foodPreference by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Personal Information",
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Keep your details up to date so your tiffin reaches the right place, on time.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(20.dp))

            SectionTitle("Basic Details")

            FormLabel("Full Name")
            ProfileField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Enter your full name",
                icon = Icons.Default.Person,
                capitalization = KeyboardCapitalization.Words
            )

            Spacer(Modifier.height(16.dp))

            FormLabel("Email Address")
            ProfileField(
                value = email,
                onValueChange = { email = it },
                placeholder = "name@example.com",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(Modifier.height(16.dp))

            FormLabel("Phone Number")
            ProfileField(
                value = phone,
                onValueChange = { input -> phone = input.filter { it.isDigit() }.take(10) },
                placeholder = "10-digit mobile number",
                icon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(Modifier.height(16.dp))

            FormLabel("Date of Birth")
            ProfileField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                placeholder = "DD / MM / YYYY",
                icon = Icons.Default.Cake,
                keyboardType = KeyboardType.Number
            )

            Spacer(Modifier.height(16.dp))

            FormLabel("Gender")
            ChoiceRow(
                options = listOf("Male", "Female", "Other"),
                selected = gender,
                onSelect = { gender = it }
            )

            Spacer(Modifier.height(24.dp))

            SectionTitle("Food Preference")

            FormLabel("What do you usually eat?")
            ChoiceRow(
                options = listOf("Veg", "Non-Veg", "Both"),
                selected = foodPreference,
                onSelect = { foodPreference = it }
            )

            Spacer(Modifier.height(24.dp))

            SectionTitle("Delivery Address")

            FormLabel("Address")
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                placeholder = { Text("Flat / House no, Street, Area") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 96.dp),
                shape = RoundedCornerShape(12.dp),
                minLines = 2,
                colors = fieldColors()
            )

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    FormLabel("City")
                    ProfileField(
                        value = city,
                        onValueChange = { city = it },
                        placeholder = "City",
                        icon = null,
                        capitalization = KeyboardCapitalization.Words
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    FormLabel("Pincode")
                    ProfileField(
                        value = pincode,
                        onValueChange = { input -> pincode = input.filter { it.isDigit() }.take(6) },
                        placeholder = "Pincode",
                        icon = null,
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Your details have been saved")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryGreen)
            ) {
                Text(
                    "Save Changes",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        leadingIcon = icon?.let { { Icon(it, contentDescription = null) } },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = capitalization
        ),
        colors = fieldColors()
    )
}

@Composable
private fun ChoiceRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEach { option ->
            val isSelected = option == selected
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
                    .clickable { onSelect(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) AppPrimaryGreen else Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppPrimaryGreen,
    unfocusedBorderColor = Color.Black.copy(alpha = 0.35f),
    focusedLeadingIconColor = AppPrimaryGreen,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = AppPrimaryGreen
)
