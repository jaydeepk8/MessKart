package com.example.messapp.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.R

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
) {

    val greenPrimary = Color(0xFF8BC34A)
    val screenBg = Color(0xFFF6F6F6)

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.login_food),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 300f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Hungry again?",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Sign in to your account",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Email or Phone",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(6.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Enter your email or phone") },
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

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Password",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(6.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Enter your password") },
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

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password?", color = greenPrimary)
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenPrimary)
            ) {
                Text("Login", color = Color.White, fontSize = 16.sp ,fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    "  Or continue with  ",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SocialButton(
                    iconRes = R.drawable.ic_google,
                    text = "Google",
                    onClick = onGoogleClick,
                    modifier = Modifier.weight(1f)
                )
                SocialButton(
                    iconRes = R.drawable.ic_facebook,
                    text = "Facebook",
                    onClick = onFacebookClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                thickness = 0.6.dp,
                color = Color.LightGray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account? ")
                TextButton(onClick = onSignUpClick) {
                    Text("Sign Up", color = greenPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SocialButton(
    iconRes: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            0.8.dp,
            Color.Black.copy(alpha = 0.35f)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}
