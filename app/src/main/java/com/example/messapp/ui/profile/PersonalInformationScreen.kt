package com.example.messapp.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppSoftGreen
import kotlinx.coroutines.launch

private val LabelGray = Color(0xFF9AA0A6)
private val CardBorder = Color(0xFFEDEDED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInformationScreen(
    initialName: String = "",
    initialPhone: String = "",
    onBackClick: () -> Unit
) {
    var fullName by remember { mutableStateOf(initialName) }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("jaydeep.kulkarni@example.com") }
    var phone by remember { mutableStateOf(initialPhone) }
    var foodPreference by remember { mutableStateOf("Veg") }
    var mealPreference by remember { mutableStateOf("Both") }

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
                .padding(horizontal = 16.dp)
        ) {

            Spacer(Modifier.height(8.dp))

            SectionHeader("Personal Details")

            EditableInfoRow(
                icon = Icons.Default.Person,
                iconBackground = Color(0xFFFFEDE5),
                iconTint = Color(0xFFFF5722),
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Add your full name",
                capitalization = KeyboardCapitalization.Words
            )

            EditableInfoRow(
                icon = Icons.Default.Cake,
                iconBackground = Color(0xFFFCE4EC),
                iconTint = Color(0xFFE91E63),
                label = "Date of Birth",
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                placeholder = "DD / MM / YYYY"
            )

            ChoiceInfoCard(
                icon = Icons.Default.Wc,
                iconBackground = Color(0xFFF3E8FF),
                iconTint = Color(0xFF7C4DFF),
                label = "Gender",
                options = listOf("Male", "Female", "Other"),
                selected = gender,
                onSelect = { gender = it }
            )

            Spacer(Modifier.height(20.dp))

            SectionHeader("Contact Details")

            EditableInfoRow(
                icon = Icons.Default.Email,
                iconBackground = Color(0xFFE8F0FF),
                iconTint = Color(0xFF2962FF),
                label = "Primary Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "name@example.com",
                keyboardType = KeyboardType.Email,
                verified = true
            )

            EditableInfoRow(
                icon = Icons.Default.Phone,
                iconBackground = Color(0xFFE0F2F1),
                iconTint = Color(0xFF00897B),
                label = "Phone Number",
                value = phone,
                onValueChange = { input -> phone = input.filter { it.isDigit() }.take(10) },
                placeholder = "10-digit mobile number",
                keyboardType = KeyboardType.Phone
            )

            Spacer(Modifier.height(20.dp))

            SectionHeader("Food Preferences")

            ChoiceInfoCard(
                icon = Icons.Default.RestaurantMenu,
                iconBackground = Color(0xFFFFF1E6),
                iconTint = Color(0xFFFF6D00),
                label = "Food Preference",
                options = listOf("Veg", "Non-Veg", "Both"),
                selected = foodPreference,
                onSelect = { foodPreference = it }
            )

            ChoiceInfoCard(
                icon = Icons.Default.Schedule,
                iconBackground = Color(0xFFEDE7F6),
                iconTint = Color(0xFF5E35B1),
                label = "Meal Preference",
                options = listOf("Lunch", "Dinner", "Both"),
                selected = mealPreference,
                onSelect = { mealPreference = it }
            )

            Spacer(Modifier.height(24.dp))

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
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditableInfoRow(
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    verified: Boolean = false,
    singleLine: Boolean = true
) {
    var editing by remember { mutableStateOf(false) }

    InfoCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {

            IconTile(icon, iconBackground, iconTint)

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                FieldLabel(label)

                Spacer(Modifier.height(4.dp))

                if (editing) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        placeholder = { Text(placeholder) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = singleLine,
                        minLines = if (singleLine) 1 else 2,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = keyboardType,
                            capitalization = capitalization
                        ),
                        colors = fieldColors()
                    )
                } else {
                    Text(
                        text = value.ifBlank { placeholder },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (value.isBlank()) LabelGray else Color(0xFF202124),
                        fontWeight = if (value.isBlank()) FontWeight.Normal else FontWeight.Medium
                    )

                    if (verified && value.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        VerifiedBadge()
                    }
                }
            }

            IconButton(
                onClick = { editing = !editing },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (editing) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (editing) "Done" else "Edit $label",
                    tint = if (editing) AppPrimaryGreen else Color(0xFF9AA0A6),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ChoiceInfoCard(
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    InfoCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconTile(icon, iconBackground, iconTint)
                Spacer(Modifier.width(12.dp))
                FieldLabel(label)
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                options.forEach { option ->
                    val isSelected = option == selected
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
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
    }
}

@Composable
private fun InfoCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        content()
    }
}

@Composable
private fun IconTile(
    icon: ImageVector,
    background: Color,
    tint: Color
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(background, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = LabelGray,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.6.sp
    )
}

@Composable
private fun VerifiedBadge() {
    Row(
        modifier = Modifier
            .background(AppSoftGreen, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = AppPrimaryGreen,
            modifier = Modifier.size(12.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "VERIFIED",
            style = MaterialTheme.typography.labelSmall,
            color = AppPrimaryGreen,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppPrimaryGreen,
    unfocusedBorderColor = Color.Black.copy(alpha = 0.25f),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = AppPrimaryGreen
)
