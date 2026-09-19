package com.krishisetu.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.models.DisputeRecord
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDisputesScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit
) {
    val disputes by viewModel.disputes.collectAsState()

    var selectedDisputeForDetail by remember { mutableStateOf<DisputeRecord?>(null) }
    var selectedDisputeForResolve by remember { mutableStateOf<DisputeRecord?>(null) }
    var resolutionNoteText by remember { mutableStateOf("Verified Bank Transaction. Escrow funds released to seller.") }
    var showResolvedToast by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dispute Management", fontWeight = FontWeight.ExtraBold) },
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
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 88.dp)
            ) {
                items(disputes) { dispute ->
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Gavel, contentDescription = null, tint = AdminRoleDark)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = dispute.id, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold))
                                }
                                StatusBadge(status = dispute.status)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${dispute.farmerName} ↔ ${dispute.buyerName}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(text = dispute.cropInfo, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = FarmerRoleGreen))

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "\"${dispute.description}\"", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { selectedDisputeForDetail = dispute },
                                    modifier = Modifier.weight(1f).height(40.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Details", fontWeight = FontWeight.Bold, color = AdminRoleDark)
                                }

                                if (dispute.status != "Resolved") {
                                    Button(
                                        onClick = { selectedDisputeForResolve = dispute },
                                        colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen),
                                        modifier = Modifier.weight(1f).height(40.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Resolve ✓", fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Detail Dialog
    selectedDisputeForDetail?.let { dispute ->
        AlertDialog(
            onDismissRequest = { selectedDisputeForDetail = null },
            title = { Text("Dispute Details (${dispute.id})", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Farmer: ${dispute.farmerName}", fontWeight = FontWeight.SemiBold)
                    Text("Buyer: ${dispute.buyerName}", fontWeight = FontWeight.SemiBold)
                    Text("Transaction ID: ${dispute.transactionId}", color = TextMuted)
                    Text("Crop & Quantity: ${dispute.cropInfo}")
                    Text("Issue Type: ${dispute.disputeType}", color = StatusError, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Description:\n${dispute.description}", color = TextSecondary)
                }
            },
            confirmButton = {
                Button(onClick = { selectedDisputeForDetail = null }, colors = ButtonDefaults.buttonColors(containerColor = AdminRoleDark)) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }

    // Resolve Dialog
    selectedDisputeForResolve?.let { dispute ->
        AlertDialog(
            onDismissRequest = { selectedDisputeForResolve = null },
            title = { Text("Resolve Dispute ${dispute.id}", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Enter resolution note and final decision:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = resolutionNoteText,
                        onValueChange = { resolutionNoteText = it },
                        label = { Text("Resolution Note") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resolveDisputeWithNote(dispute.id, resolutionNoteText)
                        selectedDisputeForResolve = null
                        showResolvedToast = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text("Mark as Resolved ✓")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedDisputeForResolve = null }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }

    if (showResolvedToast) {
        AlertDialog(
            onDismissRequest = { showResolvedToast = false },
            title = { Text("Dispute Resolved", fontWeight = FontWeight.Bold, color = FarmerRoleGreen) },
            text = { Text("Case has been marked as resolved and updated across the platform.", color = TextSecondary) },
            confirmButton = {
                Button(onClick = { showResolvedToast = false }, colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }
}
