package com.krishisetu.app.ui.screens.admin

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
import com.krishisetu.app.models.AdminBuyerRecord
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBuyerVerificationDetailScreen(
    viewModel: KrishiSetuViewModel,
    buyer: AdminBuyerRecord,
    onBack: () -> Unit
) {
    val adminBuyers by viewModel.adminBuyers.collectAsState()
    val currentBuyerState = adminBuyers.find { it.id == buyer.id } ?: buyer

    var showDocPreviewDialog by remember { mutableStateOf<String?>(null) }
    var showRejectReasonDialog by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("Invalid document") }
    var showSuccessToast by remember { mutableStateOf(false) }

    val rejectionReasons = listOf(
        "Invalid document",
        "Document unclear",
        "Information mismatch",
        "Missing required document",
        "Other"
    )

    val docs = listOf(
        Pair("GST Certificate", currentBuyerState.gstStatus),
        Pair("PAN Card", currentBuyerState.panStatus),
        Pair("Business Registration Certificate", currentBuyerState.busRegStatus),
        Pair("FSSAI License", currentBuyerState.fssaiStatus),
        Pair("Business Address Proof", currentBuyerState.addressProofStatus)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Buyer Verification", fontWeight = FontWeight.ExtraBold)
                        Text(buyer.organisationName, style = MaterialTheme.typography.bodySmall, color = TextMuted)
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
                // Organisation Info Card
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
                                        text = buyer.organisationName,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                                    )
                                    Text(text = "${buyer.businessType} · ${buyer.location}", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                                }
                                StatusBadge(status = currentBuyerState.status)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Applied: ${buyer.appliedDate}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text("Risk Level: ${buyer.riskLevel}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BuyerRoleBlue))
                            }
                        }
                    }
                }

                // Documents Section
                item {
                    Text(
                        text = "Organisation Registration Documents",
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
                                    Icon(Icons.Default.Description, contentDescription = null, tint = BuyerRoleBlue)
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
                                Text("📄 View Document Preview", fontSize = 13.sp, color = BuyerRoleBlue, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Actions
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
                                viewModel.approveAdminBuyer(buyer.id)
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
            title = { Text("Buyer Document: $docName", fontWeight = FontWeight.Bold) },
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
                                Icon(Icons.Default.Description, contentDescription = null, tint = BuyerRoleBlue, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("${docName.lowercase().replace(" ", "_")}_verified.pdf", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                                Text("GSTIN Verification: ACTIVE & VALID", style = MaterialTheme.typography.labelSmall, color = FarmerRoleGreen)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDocPreviewDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = BuyerRoleBlue)) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }

    // Rejection Reason Selection Dialog
    if (showRejectReasonDialog) {
        AlertDialog(
            onDismissRequest = { showRejectReasonDialog = false },
            title = { Text("Buyer Verification Rejected", fontWeight = FontWeight.Bold, color = StatusError) },
            text = {
                Column {
                    Text("Select reason for rejecting ${buyer.organisationName}:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    rejectionReasons.forEach { reason ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedReason == reason),
                                onClick = { selectedReason = reason }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(reason, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.rejectAdminBuyer(buyer.id, selectedReason)
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

    // Success Toast
    if (showSuccessToast) {
        AlertDialog(
            onDismissRequest = {
                showSuccessToast = false
                onBack()
            },
            title = { Text("Buyer Verified Successfully", fontWeight = FontWeight.Bold, color = FarmerRoleGreen) },
            text = { Text("${buyer.organisationName} has been approved as a Verified Buyer.", color = TextSecondary) },
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
