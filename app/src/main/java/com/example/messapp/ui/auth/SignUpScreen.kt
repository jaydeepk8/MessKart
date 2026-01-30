package com.example.messapp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val greenPrimary = Color(0xFF8BC34A)
    val screenBg = Color(0xFFF6F6F6)

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Create Account",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(18.dp))

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

        Spacer(Modifier.height(28.dp))

        FormLabel("Name")
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Enter your name") },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black.copy(alpha = 0.6f),
                unfocusedBorderColor = Color.Black.copy(alpha = 0.35f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Black
            )
        )

        Spacer(Modifier.height(18.dp))

        FormLabel("Email Address")
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("name@example.com") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black.copy(alpha = 0.6f),
                unfocusedBorderColor = Color.Black.copy(alpha = 0.35f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Black
            )
        )

        Spacer(Modifier.height(18.dp))

        FormLabel("Phone Number")
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("9884865247") },
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black.copy(alpha = 0.6f),
                unfocusedBorderColor = Color.Black.copy(alpha = 0.35f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Black
            )
        )

        Spacer(Modifier.height(18.dp))

        FormLabel("Password")
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Create a strong password") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black.copy(alpha = 0.6f),
                unfocusedBorderColor = Color.Black.copy(alpha = 0.35f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Black
            )
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = "By signing up, you agree to our Terms of Service and Privacy Policy.",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            lineHeight = 16.sp
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onCreateAccountClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = greenPrimary)
        ) {
            Text(
                text = "Create Account",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account? ")
            TextButton(onClick = onLoginClick) {
                Text(
                    text = "Log In",
                    color = greenPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
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
private fun OutlinedInputField(
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false
) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        trailingIcon = trailingIcon,
        visualTransformation =
            if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}
