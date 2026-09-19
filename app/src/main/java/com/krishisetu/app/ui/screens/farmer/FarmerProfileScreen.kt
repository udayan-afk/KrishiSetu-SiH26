package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.AppLanguage
import com.krishisetu.app.models.DocStatus
import com.krishisetu.app.models.EkycStatus
import com.krishisetu.app.models.UserRole
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.components.SubmissionSuccessDialog
import com.krishisetu.app.ui.components.VerifiedBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun FarmerProfileScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToCustomerSupport: () -> Unit = {},
    onNavigateToRaiseDispute: () -> Unit = {},
    onNavigateToMyComplaints: () -> Unit = {}
) {

    val farmer by viewModel.currentFarmer.collectAsState()
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    var showSuccessPopup by remember { mutableStateOf(false) }

    // Dialogs for mock uploading
    var editingGatDialog by remember { mutableStateOf(false) }
    var tempGatNumber by remember { mutableStateOf(farmer.gatNumberValue.ifEmpty { "GAT-782/A" }) }

    var editingBankDialog by remember { mutableStateOf(false) }
    var tempBankInfo by remember { mutableStateOf(farmer.bankAccountValue.ifEmpty { "State Bank of India (A/C: ****4892)" }) }

    val languages = listOf(
        AppLanguage.MARATHI,
        AppLanguage.HINDI,
        AppLanguage.ENGLISH
    )

    if (showSuccessPopup) {
        SubmissionSuccessDialog(
            title = strings.docsSubmittedTitle,
            message = strings.docsSubmittedMsg,
            buttonText = strings.gotItBtn,
            onDismiss = {
                showSuccessPopup = false
            }
        )
    }

    if (editingGatDialog) {
        AlertDialog(
            onDismissRequest = { editingGatDialog = false },
            title = { Text(strings.gatNumberLabel, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = tempGatNumber,
                    onValueChange = { tempGatNumber = it },
                    label = { Text(strings.gatNumberLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateGatNumber(tempGatNumber)
                        editingGatDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text(strings.submit)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingGatDialog = false }) {
                    Text(strings.cancel)
                }
            }
        )
    }

    if (editingBankDialog) {
        AlertDialog(
            onDismissRequest = { editingBankDialog = false },
            title = { Text(strings.bankDetailsLabel, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = tempBankInfo,
                    onValueChange = { tempBankInfo = it },
                    label = { Text(strings.bankDetailsLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateBankDetails(tempBankInfo)
                        editingBankDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text(strings.submit)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingBankDialog = false }) {
                    Text(strings.cancel)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.profileTitle,
                subtitle = "KrishiSetu Account & Document Verification",
                onBackClick = onBack
            )
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            // 1. Profile Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(FarmerRoleBg, shape = RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👨‍🌾", fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = farmer.name,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        if (farmer.ekycStatus == EkycStatus.VERIFIED) {
                            VerifiedBadge(text = "Verified Farmer ✓")
                        } else if (farmer.ekycStatus == EkycStatus.UNDER_REVIEW) {
                            StatusBadge(status = "UNDER REVIEW")
                        } else {
                            StatusBadge(status = "NOT VERIFIED")
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Mobile: +91 ${farmer.mobile}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = TextPrimary
                        )
                        Text(
                            text = "${farmer.village}, ${farmer.taluka}, ${farmer.district}, ${farmer.state}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            // 2. UPDATE 5 — Overall eKYC Verification Status Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (farmer.ekycStatus) {
                            EkycStatus.NOT_VERIFIED -> Color(0xFFFFF3E0)
                            EkycStatus.UNDER_REVIEW -> Color(0xFFE3F2FD)
                            EkycStatus.VERIFIED -> Color(0xFFE8F5E9)
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = when (farmer.ekycStatus) {
                                        EkycStatus.NOT_VERIFIED -> Icons.Default.Info
                                        EkycStatus.UNDER_REVIEW -> Icons.Default.HourglassTop
                                        EkycStatus.VERIFIED -> Icons.Default.CheckCircle
                                    },
                                    contentDescription = "Status",
                                    tint = FarmerRoleGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Overall eKYC Status",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = TextPrimary
                                )
                            }

                            val badgeLabel = when (farmer.ekycStatus) {
                                EkycStatus.NOT_VERIFIED -> "NOT VERIFIED"
                                EkycStatus.UNDER_REVIEW -> "UNDER REVIEW"
                                EkycStatus.VERIFIED -> "VERIFIED ✓"
                            }
                            StatusBadge(status = badgeLabel)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val statusSubtitle = when (farmer.ekycStatus) {
                            EkycStatus.NOT_VERIFIED -> "Complete your eKYC — Submit required documents below"
                            EkycStatus.UNDER_REVIEW -> "Documents submitted — verification in progress (within 24h)"
                            EkycStatus.VERIFIED -> "Your documents are verified ✓ Account 100% Trusted"
                        }

                        Text(
                            text = statusSubtitle,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = TextPrimary
                        )

                        farmer.submissionTimestamp?.let { ts ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Submission time: $ts",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }

                        // Simulated Mock State Selector for Demo
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Prototype Mock State Switcher:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextMuted)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            EkycStatus.values().forEach { status ->
                                val isSel = farmer.ekycStatus == status
                                FilterChip(
                                    selected = isSel,
                                    onClick = { viewModel.setMockEkycStatus(status) },
                                    label = { Text(status.name.replace("_", " "), fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = FarmerRoleGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3. UPDATE 3 — DOCUMENT VERIFICATION SECTION
            item {
                Text(
                    text = strings.docVerificationHeader,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            // Document Item 1: Aadhaar Card
            item {
                DocumentCard(
                    title = strings.aadhaarCardLabel,
                    status = farmer.aadhaarStatus,
                    fileName = farmer.aadhaarFileName ?: "Aadhaar_Scan.pdf",
                    strings = strings,
                    onUpload = { viewModel.uploadAadhaar("aadhaar_patil_verified.pdf") }
                )
            }

            // Document Item 2: 7/12 Extract
            item {
                DocumentCard(
                    title = strings.sevenTwelveLabel,
                    status = farmer.sevenTwelveStatus,
                    fileName = farmer.sevenTwelveFileName ?: "7_12_Extract_Satara.pdf",
                    strings = strings,
                    onUpload = { viewModel.uploadSevenTwelve("7_12_extract_satara_khed.pdf") }
                )
            }

            // Document Item 3: GAT Number
            item {
                DocumentCard(
                    title = strings.gatNumberLabel,
                    status = farmer.gatNumberStatus,
                    fileName = if (farmer.gatNumberValue.isNotEmpty()) "GAT No: ${farmer.gatNumberValue}" else "GAT-782/A (Satara)",
                    strings = strings,
                    onUpload = { editingGatDialog = true }
                )
            }

            // Document Item 4: Bank Details
            item {
                DocumentCard(
                    title = strings.bankDetailsLabel,
                    status = farmer.bankDetailsStatus,
                    fileName = if (farmer.bankAccountValue.isNotEmpty()) farmer.bankAccountValue else "SBI (A/C: ****4892)",
                    strings = strings,
                    onUpload = { editingBankDialog = true }
                )
            }

            // Document Item 5: Other Required Documents
            item {
                DocumentCard(
                    title = strings.otherDocsLabel,
                    status = farmer.otherDocsStatus,
                    fileName = farmer.otherDocsFileName ?: "Land_Declaration_Satara.pdf",
                    strings = strings,
                    onUpload = { viewModel.uploadOtherDoc("land_declaration_satara_khed.pdf") }
                )
            }

            // Submit for Verification Button & Popup Trigger
            item {
                PrimaryKrishiButton(
                    text = strings.submitDocsBtn,
                    containerColor = FarmerRoleGreen,
                    icon = Icons.Default.CloudUpload,
                    onClick = {
                        viewModel.submitDocumentsForVerification()
                        showSuccessPopup = true
                    }
                )
            }

            // 3.5. HELP & SUPPORT SECTION
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFFFEBEE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SupportAgent,
                                    contentDescription = "Help & Support",
                                    tint = StatusError,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = strings.helpAndSupportTitle,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = strings.needHelpSub,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                                    color = TextSecondary
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)

                        // Option 1: Customer Support
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onNavigateToCustomerSupport),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = BackgroundWarm)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.SupportAgent, contentDescription = null, tint = FarmerRoleGreen, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = strings.customerSupportBtn,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                        color = TextPrimary
                                    )
                                }
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Option 2: Raise a Dispute
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onNavigateToRaiseDispute),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.ReportProblem, contentDescription = null, tint = StatusError, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = strings.raiseADisputeBtn,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                        color = StatusError
                                    )
                                }
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = StatusError)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Option 3: My Complaints
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onNavigateToMyComplaints),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = BackgroundWarm)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, tint = FarmerRoleGreen, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = strings.myComplaintsBtn,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                        color = TextPrimary
                                    )
                                }
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
                            }
                        }
                    }
                }
            }

            // 4. Interactive Language Selection Card in Settings

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "🌐 ${strings.selectLanguageSetting}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderColor)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            languages.forEach { lang ->
                                val isSelected = currentLang == lang
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) FarmerRoleGreen else BorderColor,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.setLanguage(lang) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) FarmerRoleBg else BackgroundWarm
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = lang.nativeName,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 14.sp
                                            ),
                                            color = if (isSelected) FarmerRoleGreen else TextPrimary
                                        )
                                        Text(
                                            text = lang.displayName,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Logout Button
            item {
                PrimaryKrishiButton(
                    text = strings.logoutBtn,
                    containerColor = StatusError,
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    onClick = {
                        viewModel.selectRole(UserRole.NONE)
                        onLogout()
                    }
                )
            }
        }
    }
}

@Composable
private fun DocumentCard(
    title: String,
    status: DocStatus,
    fileName: String,
    strings: FarmerStrings,
    onUpload: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                val (badgeStatusText, badgeColor) = when (status) {
                    DocStatus.NOT_SUBMITTED -> Pair(strings.notSubmittedStatus, TextMuted)
                    DocStatus.SUBMITTED -> Pair(strings.submittedStatus, FarmerRoleGreen)
                    DocStatus.UNDER_VERIFICATION -> Pair(strings.underVerificationStatus, StatusWarning)
                    DocStatus.VERIFIED -> Pair(strings.verifiedStatus, StatusSuccess)
                }

                StatusBadge(status = badgeStatusText)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (status != DocStatus.NOT_SUBMITTED) fileName else "No document attached",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 12.sp,
                        color = if (status != DocStatus.NOT_SUBMITTED) TextPrimary else TextMuted
                    )
                )

                OutlinedButton(
                    onClick = onUpload,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FarmerRoleGreen)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (status == DocStatus.NOT_SUBMITTED) strings.uploadBtn else "Update",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
