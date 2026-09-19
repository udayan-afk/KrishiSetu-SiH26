package com.krishisetu.app.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.models.AdminFarmerRecord
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFarmerVerificationScreen(
    viewModel: KrishiSetuViewModel,
    farmer: AdminFarmerRecord,
    onBack: () -> Unit
) {
    val adminFarmers by viewModel.adminFarmers.collectAsState()
    val currentFarmerState = adminFarmers.find { it.id == farmer.id } ?: farmer

    var showDocPreviewDialog by remember { mutableStateOf<String?>(null) }
    var showRejectReasonDialog by remember { mutableStateOf(false) }
    var rejectionReasonText by remember { mutableStateOf("Document is unclear or invalid.") }
    var showSuccessToast by remember { mutableStateOf(false) }

    val docs = listOf(
        Pair("Aadhaar Card", currentFarmerState.aadhaarStatus),
        Pair("7/12 Extract", currentFarmerState.sevenTwelveStatus),
        Pair("GAT Number (Land)", currentFarmerState.gatNumberStatus),
        Pair("Bank Details & IFSC", currentFarmerState.bankDetailsStatus)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Farmer Verification", fontWeight = FontWeight.ExtraBold)
                        Text(farmer.name, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWarm)
            )
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 88.dp)
            ) {
                // Farmer Summary Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = farmer.name,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                                    )
                                    Text(text = "${farmer.location} · ${farmer.activeLotsCount} Active Lots", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                                }
                                StatusBadge(status = currentFarmerState.status)
                            }
                        }
                    }
                }

                // Documents List Header
                item {
                    Text(
                        text = "Submitted Verification Documents",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = TextPrimary
                    )
                }

                items(docs) { (docTitle, docStatus) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Description, contentDescription = null, tint = AdminRoleDark)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = docTitle, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                }
                                StatusBadge(status = docStatus)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = { showDocPreviewDialog = docTitle },
                                modifier = Modifier.fillMaxWidth().height(38.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("📄 View Document Preview", fontSize = 13.sp, color = AdminRoleDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Action Buttons (Approve & Reject)
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showRejectReasonDialog = true },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusError)
                        ) {
                            Text("Reject", fontWeight = FontWeight.Bold, color = StatusError)
                        }

                        Button(
                            onClick = {
                                viewModel.approveAdminFarmer(farmer.id)
                                showSuccessToast = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen),
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Approve ✓", fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Document Preview Dialog
    showDocPreviewDialog?.let { docName ->
        AlertDialog(
            onDismissRequest = { showDocPreviewDialog = null },
            title = { Text("Document Preview: $docName", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = BackgroundWarm
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = FarmerRoleGreen, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("official_${docName.lowercase().replace(" ", "_")}.pdf", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                                Text("Verified Stamp: KS-STATE-GOV-APPROVED", style = MaterialTheme.typography.labelSmall, color = FarmerRoleGreen)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDocPreviewDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }

    // Rejection Dialog
    if (showRejectReasonDialog) {
        AlertDialog(
            onDismissRequest = { showRejectReasonDialog = false },
            title = { Text("Reject Farmer Document", fontWeight = FontWeight.Bold, color = StatusError) },
            text = {
                Column {
                    Text("Specify reason for rejecting ${farmer.name}'s document:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = rejectionReasonText,
                        onValueChange = { rejectionReasonText = it },
                        label = { Text("Rejection Reason") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.rejectAdminFarmer(farmer.id, rejectionReasonText)
                        showRejectReasonDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                ) {
                    Text("Confirm Rejection")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectReasonDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }

    // Onboarding Confirmation Toast
    if (showSuccessToast) {
        AlertDialog(
            onDismissRequest = {
                showSuccessToast = false
                onBack()
            },
            title = { Text("Farmer Verified Successfully", fontWeight = FontWeight.Bold, color = FarmerRoleGreen) },
            text = { Text("${farmer.name} has been verified and onboarded to KrishiSetu platform.", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessToast = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text("Done")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }
}
