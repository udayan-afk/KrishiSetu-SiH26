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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerRegisterScreen(
    viewModel: KrishiSetuViewModel,
    onProfileCreated: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var companyName by remember { mutableStateOf("FreshFoods Pvt Ltd") }
    var selectedBusinessType by remember { mutableStateOf("Food Processor & Trader") }
    var contactPerson by remember { mutableStateOf("Rajesh Mehta") }
    var email by remember { mutableStateOf("rajesh@freshfoods.in") }
    var phone by remember { mutableStateOf("9876543210") }
    var state by remember { mutableStateOf("Maharashtra") }
    var district by remember { mutableStateOf("Pune") }
    var city by remember { mutableStateOf("Pune") }
    var gstNumber by remember { mutableStateOf("27AAACFI234M1ZX") }
    var panNumber by remember { mutableStateOf("AAACFI234M") }
    var fssaiLicense by remember { mutableStateOf("11512345000123") }

    val businessTypeOptions = listOf(
        "Food Processor & Trader",
        "Agricultural Commodity Trader",
        "Retail Chain / Supermarket",
        "Exporter & International Buyer",
        "Farmer Producer Organisation (FPO)"
    )
    var expandedDropdown by remember { mutableStateOf(false) }

    val isValid = companyName.isNotBlank() &&
            contactPerson.isNotBlank() &&
            email.isNotBlank() &&
            phone.isNotBlank() &&
            gstNumber.isNotBlank() &&
            panNumber.isNotBlank()

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = "Buyer Registration",
                subtitle = "Apply for Verified Buyer Access",
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
                text = "Organisation Profile Creation",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = TextPrimary
            )
            Text(
                text = "Enter business and tax details to establish your KrishiSetu buyer identity.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Company Name
            Text("Company / Organisation Name", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = companyName,
                onValueChange = { companyName = it },
                placeholder = { Text("e.g. FreshFoods Pvt Ltd") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedBorderColor = FarmerRoleGreen, unfocusedBorderColor = BorderColor)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Business Type Dropdown
            Text("Business Type", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            Spacer(modifier = Modifier.height(6.dp))
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown }
            ) {
                OutlinedTextField(
                    value = selectedBusinessType,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedBorderColor = FarmerRoleGreen, unfocusedBorderColor = BorderColor)
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    businessTypeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedBusinessType = option
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Contact Person
            Text("Contact Person Name", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = contactPerson,
                onValueChange = { contactPerson = it },
                placeholder = { Text("e.g. Rajesh Mehta") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedBorderColor = FarmerRoleGreen, unfocusedBorderColor = BorderColor)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Email & Phone Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Company Email", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedBorderColor = FarmerRoleGreen, unfocusedBorderColor = BorderColor)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Phone Number", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedBorderColor = FarmerRoleGreen, unfocusedBorderColor = BorderColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Location Row: State, District, City
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("State", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = state, onValueChange = { state = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("District", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = district, onValueChange = { district = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("City / Location", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = city, onValueChange = { city = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. GST Number
            Text("GST Number", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = gstNumber,
                onValueChange = { gstNumber = it },
                placeholder = { Text("e.g. 27AAACFI234M1ZX") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedBorderColor = FarmerRoleGreen, unfocusedBorderColor = BorderColor)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 7. PAN Number
            Text("PAN Number", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = panNumber,
                onValueChange = { panNumber = it },
                placeholder = { Text("e.g. AAACFI234M") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedBorderColor = FarmerRoleGreen, unfocusedBorderColor = BorderColor)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 8. FSSAI License
            Text("FSSAI License (Optional / Applicable)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = fssaiLicense,
                onValueChange = { fssaiLicense = it },
                placeholder = { Text("e.g. 11512345000123") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedBorderColor = FarmerRoleGreen, unfocusedBorderColor = BorderColor)
            )

            Spacer(modifier = Modifier.height(28.dp))

            PrimaryKrishiButton(
                text = "Create Buyer Profile →",
                containerColor = FarmerRoleGreen,
                enabled = isValid,
                onClick = {
                    viewModel.registerBuyerProfile(
                        companyName = companyName,
                        businessType = selectedBusinessType,
                        contactPerson = contactPerson,
                        companyEmail = email,
                        phone = phone,
                        state = state,
                        district = district,
                        city = city,
                        gstNumber = gstNumber,
                        pan = panNumber,
                        fssaiLicense = fssaiLicense
                    )
                    onProfileCreated()
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
