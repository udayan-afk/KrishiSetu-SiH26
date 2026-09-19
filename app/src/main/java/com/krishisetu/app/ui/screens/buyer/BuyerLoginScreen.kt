package com.krishisetu.app.ui.screens.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.models.UserRole
import com.krishisetu.app.ui.components.KrishiSetuLogoIcon
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun BuyerLoginScreen(
    viewModel: KrishiSetuViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onBackToRoleSelection: () -> Unit
) {
    var email by remember { mutableStateOf("rajesh@freshfoods.in") }
    var password by remember { mutableStateOf("FreshFoods@123") }
    var showPassword by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onBackToRoleSelection)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryGreen)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("All Roles", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryGreen))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    KrishiSetuLogoIcon(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("KrishiSetu", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = PrimaryGreen))
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Buyer Role Icon Badge
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(FarmerRoleBg),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏢", fontSize = 38.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Buyer / Organisation",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sign in to your account",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Company Email Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Company Email",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("e.g. rajesh@freshfoods.in") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface,
                        focusedBorderColor = FarmerRoleGreen,
                        unfocusedBorderColor = BorderColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Password Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Enter password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface,
                        focusedBorderColor = FarmerRoleGreen,
                        unfocusedBorderColor = BorderColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Show password checkbox & Forgot password row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showPassword,
                        onCheckedChange = { showPassword = it },
                        colors = CheckboxDefaults.colors(checkedColor = FarmerRoleGreen)
                    )
                    Text("Show password", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp), color = TextSecondary)
                }

                Text(
                    text = "Forgot password?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 13.sp),
                    modifier = Modifier.clickable { showForgotPasswordDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Sign In Button
            PrimaryKrishiButton(
                text = "Sign In →",
                containerColor = FarmerRoleGreen,
                onClick = {
                    viewModel.selectRole(UserRole.BUYER)
                    onLoginSuccess()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Demo text hint
            Text(
                text = "Demo: click Sign In to enter the Buyer Dashboard",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Bottom Section: Apply for Verified Buyer access
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("New organisation?", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Apply for Verified Buyer access →",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = FarmerRoleGreen),
                    modifier = Modifier.clickable(onClick = onNavigateToRegister)
                )
            }
        }
    }

    // Forgot Password Info Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = { Text("Reset Password", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "For prototype mode, simply click 'Sign In →' to enter the Buyer Dashboard. In production, password reset instructions will be sent to your registered company email.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { showForgotPasswordDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }
}
