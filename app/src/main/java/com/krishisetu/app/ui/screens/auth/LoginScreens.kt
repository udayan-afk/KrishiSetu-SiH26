package com.krishisetu.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.krishisetu.app.R
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.UserRole
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.theme.*

@Composable
fun RoleLoginScreen(
    role: UserRole,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onBackToRoleSelection: () -> Unit
) {
    val strings = FarmerStrings.get()
    var mobileOrEmail by remember { mutableStateOf(if (role == UserRole.FARMER) "98765 43210" else "buyer@freshfoods.com") }
    var otpOrPassword by remember { mutableStateOf("123456") }
    var isOtpSent by remember { mutableStateOf(false) }

    val cleanedMobileOrEmail = mobileOrEmail.trim()
    val farmerDigits = cleanedMobileOrEmail.filter { it.isDigit() }
    val isMobileOrEmailValid = if (role == UserRole.FARMER) {
        cleanedMobileOrEmail.isNotEmpty() && farmerDigits.length >= 10
    } else {
        cleanedMobileOrEmail.isNotEmpty()
    }
    val cleanedOtp = otpOrPassword.trim()
    val isOtpValid = cleanedOtp.length == 6 && cleanedOtp.all { it.isDigit() }
    val isActionEnabled = if (role == UserRole.FARMER && !isOtpSent) {
        isMobileOrEmailValid
    } else if (isOtpSent) {
        isMobileOrEmailValid && isOtpValid
    } else {
        isMobileOrEmailValid
    }

    val roleTitle = when (role) {
        UserRole.FARMER -> strings.farmerLoginTitle
        UserRole.BUYER -> stringResource(R.string.buyer_role_title)
        UserRole.ADMIN -> stringResource(R.string.admin_role_title)
        UserRole.NONE -> "Login"
    }

    val roleEmoji = when (role) {
        UserRole.FARMER -> "👨‍🌾"
        UserRole.BUYER -> "🏢"
        UserRole.ADMIN -> "🛠️"
        UserRole.NONE -> "🔐"
    }

    val roleThemeColor = when (role) {
        UserRole.FARMER -> FarmerRoleGreen
        UserRole.BUYER -> BuyerRoleBlue
        UserRole.ADMIN -> AdminRoleDark
        UserRole.NONE -> PrimaryGreen
    }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = roleTitle,
                onBackClick = onBackToRoleSelection
            )
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Role Avatar Circle
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(roleThemeColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = roleEmoji, fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = roleTitle,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (role == UserRole.FARMER) strings.enterMobilePrompt else "Enter credentials to access dashboard",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Mobile / Email Input Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (role == UserRole.FARMER) strings.mobileNumberLabel else "Mobile Number / Email",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = mobileOrEmail,
                    onValueChange = { mobileOrEmail = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = !isMobileOrEmailValid,
                    leadingIcon = {
                        if (role == UserRole.FARMER) {
                            Text(
                                text = "IN +91 | ",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextMuted),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (role == UserRole.FARMER) KeyboardType.Phone else KeyboardType.Email
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface,
                        focusedBorderColor = roleThemeColor,
                        unfocusedBorderColor = BorderColor
                    )
                )
                if (!isMobileOrEmailValid) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (role == UserRole.FARMER) "Please enter a valid 10-digit mobile number" else "Mobile number or email is required",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (isOtpSent) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Enter 6-Digit Demo OTP",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = otpOrPassword,
                        onValueChange = { otpOrPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = !isOtpValid,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardSurface,
                            unfocusedContainerColor = CardSurface,
                            focusedBorderColor = roleThemeColor,
                            unfocusedBorderColor = BorderColor
                        )
                    )
                    if (!isOtpValid) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Please enter a valid 6-digit numeric OTP for demo verification",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login / Send OTP Button
            PrimaryKrishiButton(
                text = if (!isOtpSent && role == UserRole.FARMER) strings.sendOtpBtn else strings.verifyLoginBtn,
                containerColor = roleThemeColor,
                enabled = isActionEnabled,
                onClick = {
                    if (role == UserRole.FARMER && !isOtpSent) {
                        if (isMobileOrEmailValid) {
                            isOtpSent = true
                        }
                    } else if (isActionEnabled) {
                        onLoginSuccess()
                    }
                }
            )

            if (role == UserRole.FARMER) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)
                    Text(
                        text = "  or  ",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // New Farmer Register Button
                OutlinedButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmerRoleGreen)
                ) {
                    Text(
                        text = strings.newFarmerRegisterBtn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = FarmerRoleGreen
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "नोंदणी करा · पंजीकरण करें · Registration",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = TextMuted
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDropdownMenu(
    label: String,
    prompt: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
            color = if (enabled) TextPrimary else TextMuted
        )
        Spacer(modifier = Modifier.height(6.dp))

        ExposedDropdownMenuBox(
            expanded = expanded && enabled,
            onExpandedChange = { if (enabled) expanded = !expanded }
        ) {
            OutlinedTextField(
                value = if (selectedOption.isEmpty()) prompt else selectedOption,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded && enabled) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface,
                    disabledContainerColor = Color(0xFFF0F0F0),
                    focusedBorderColor = FarmerRoleGreen,
                    unfocusedBorderColor = BorderColor
                )
            )
            ExposedDropdownMenu(
                expanded = expanded && enabled,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FarmerRegisterScreen(
    viewModel: com.krishisetu.app.viewmodel.KrishiSetuViewModel,
    onRegistrationSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    var fullName by remember { mutableStateOf("Vikram Jadhav") }
    var mobileNumber by remember { mutableStateOf("9876543210") }
    var selectedState by remember { mutableStateOf("Maharashtra") }
    var selectedDistrict by remember { mutableStateOf("Satara") }
    var selectedTaluka by remember { mutableStateOf("Satara") }
    var selectedVillage by remember { mutableStateOf("Khed") }

    val locations = com.krishisetu.app.models.MockLocationRepository.locations

    val stateOptions = locations.map { it.state }
    val districtOptions = remember(selectedState) {
        locations.find { it.state == selectedState }?.districts?.map { it.name } ?: emptyList()
    }
    val talukaOptions = remember(selectedState, selectedDistrict) {
        locations.find { it.state == selectedState }
            ?.districts?.find { it.name == selectedDistrict }
            ?.talukas?.map { it.name } ?: emptyList()
    }
    val villageOptions = remember(selectedState, selectedDistrict, selectedTaluka) {
        locations.find { it.state == selectedState }
            ?.districts?.find { it.name == selectedDistrict }
            ?.talukas?.find { it.name == selectedTaluka }
            ?.villages ?: emptyList()
    }

    val isValid = fullName.trim().isNotEmpty() &&
            mobileNumber.trim().length >= 10 &&
            selectedState.isNotEmpty() &&
            selectedDistrict.isNotEmpty() &&
            selectedTaluka.isNotEmpty() &&
            selectedVillage.isNotEmpty()

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.registerTitle,
                subtitle = strings.registerSubtitle,
                onBackClick = onBackToLogin
            )
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = strings.backToLogin,
                style = MaterialTheme.typography.labelLarge.copy(color = FarmerRoleGreen, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .clickable(onClick = onBackToLogin)
                    .padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = strings.registerTitle,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = TextPrimary
            )
            Text(
                text = strings.registerSubtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Full Name Input
            Text(
                text = strings.fullNameLabel,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("e.g. Vikram Jadhav") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface,
                    focusedBorderColor = FarmerRoleGreen,
                    unfocusedBorderColor = BorderColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Mobile Number Input
            Text(
                text = strings.mobileNumberLabel,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                placeholder = { Text("10-digit number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface,
                    focusedBorderColor = FarmerRoleGreen,
                    unfocusedBorderColor = BorderColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. State Dropdown
            LocationDropdownMenu(
                label = strings.stateLabel,
                prompt = strings.selectStatePrompt,
                options = stateOptions,
                selectedOption = selectedState,
                onOptionSelected = { newState ->
                    selectedState = newState
                    selectedDistrict = ""
                    selectedTaluka = ""
                    selectedVillage = ""
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. District Dropdown
            LocationDropdownMenu(
                label = strings.districtLabel,
                prompt = strings.selectDistrictPrompt,
                options = districtOptions,
                selectedOption = selectedDistrict,
                enabled = selectedState.isNotEmpty(),
                onOptionSelected = { newDistrict ->
                    selectedDistrict = newDistrict
                    selectedTaluka = ""
                    selectedVillage = ""
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Taluka Dropdown
            LocationDropdownMenu(
                label = strings.talukaLabel,
                prompt = strings.selectTalukaPrompt,
                options = talukaOptions,
                selectedOption = selectedTaluka,
                enabled = selectedDistrict.isNotEmpty(),
                onOptionSelected = { newTaluka ->
                    selectedTaluka = newTaluka
                    selectedVillage = ""
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Village Dropdown
            LocationDropdownMenu(
                label = strings.villageLabel,
                prompt = strings.selectVillagePrompt,
                options = villageOptions,
                selectedOption = selectedVillage,
                enabled = selectedTaluka.isNotEmpty(),
                onOptionSelected = { newVillage ->
                    selectedVillage = newVillage
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            PrimaryKrishiButton(
                text = strings.createAccountBtn,
                containerColor = FarmerRoleGreen,
                enabled = isValid,
                onClick = {
                    viewModel.registerFarmer(
                        name = fullName.trim(),
                        mobile = mobileNumber.trim(),
                        state = selectedState,
                        district = selectedDistrict,
                        taluka = selectedTaluka,
                        village = selectedVillage
                    )
                    onRegistrationSuccess()
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
