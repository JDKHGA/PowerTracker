package com.example.powertracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.powertracker.auth.supabase
import com.example.powertracker.textfield.CustomTextField
import com.example.powertracker.ui.theme.IndigoGradient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun RegistrationScreen(navController: NavController? = null) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(IndigoGradient)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier.size(70.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Join Us",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Start your energy saving journey",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // Registration Form Section
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 240.dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(32.dp))

                CustomTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name",
                    placeholder = "John Doe",
                    trailingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    placeholder = "john@example.com",
                    trailingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "••••••••",
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, "toggle password visibility", tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    placeholder = "••••••••",
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(image, "toggle password visibility", tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                        }
                    }
                )

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                successMessage?.let {
                    Text(
                        text = it,
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onClick = {
                        if (password != confirmPassword) {
                            error = "Passwords do not match"
                            return@Button
                        }
                        scope.launch {
                            try {
                                error = null
                                successMessage = null
                                supabase.auth.signUpWith(Email) {
                                    this.email = email
                                    this.password = password
                                    data = buildJsonObject {
                                        put("full_name", fullName)
                                    }
                                }

                                if (supabase.auth.currentSessionOrNull() != null) {
                                    navController?.navigate("home") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                } else {
                                    successMessage = "Registration successful! Please check your email to confirm your account."
                                }
                            } catch (e: Exception) {
                                error = "Registration failed: ${e.message}"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(IndigoGradient, RoundedCornerShape(16.dp))
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Register",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Text(
                        text = "Already have an account? Login",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
