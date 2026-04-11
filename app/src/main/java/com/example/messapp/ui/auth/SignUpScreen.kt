package com.example.messapp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val greenPrimary = Color(0xFF8BC34A)
    val screenBg = Color(0xFFF6F6F6)

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onCreateAccountClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Create Account",
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 12.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Start Eating Better, Every Day",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Sign up to discover nearby messes and daily meal plans.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 20.sp
        )

        Spacer(Modifier.height(20.dp))

        if (uiState.errorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = uiState.errorMessage!!,
                    color = Color(0xFFB71C1C),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        FormLabel("Name")
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; authViewModel.clearError() },
            placeholder = { Text("Enter your name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = fieldColors()
        )

        Spacer(Modifier.height(16.dp))

        FormLabel("Email Address")
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; authViewModel.clearError() },
            placeholder = { Text("name@example.com") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = fieldColors()
        )

        Spacer(Modifier.height(16.dp))

        FormLabel("Phone Number")
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            placeholder = { Text("9884865247") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = fieldColors()
        )

        Spacer(Modifier.height(16.dp))

        FormLabel("Password")
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; authViewModel.clearError() },
            placeholder = { Text("Create a strong password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = fieldColors()
        )

        Spacer(Modifier.height(16.dp))

        FormLabel("Confirm Password")
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it; authViewModel.clearError() },
            placeholder = { Text("Re-enter your password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = fieldColors()
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "By signing up, you agree to our Terms of Service and Privacy Policy.",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            lineHeight = 16.sp
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.signUp(email, password, confirmPassword) },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = greenPrimary),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Create Account", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account? ")
            TextButton(onClick = onLoginClick) {
                Text("Log In", color = greenPrimary, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(24.dp))
    }
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

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color.Black.copy(alpha = 0.6f),
    unfocusedBorderColor = Color.Black.copy(alpha = 0.35f),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = Color.Black
)