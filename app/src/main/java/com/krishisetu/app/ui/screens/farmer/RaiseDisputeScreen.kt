package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ReportProblem
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
import com.krishisetu.app.models.DisputeRecord
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun RaiseDisputeScreen(
    viewModel: KrishiSetuViewModel,
    initialTransactionId: String? = null,
    initialIssueType: String? = null,
    onBack: () -> Unit,
    onNavigateToMyComplaints: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val transactions by viewModel.transactions.collectAsState()

    val issueTypes = listOf(
        strings.issuePayment,
        strings.issueQuality,
        strings.issueDelivery,
        strings.issueBuyer,
        strings.issueOther
    )

    var selectedIssueType by remember {
        mutableStateOf(
            if (!initialIssueType.isNullOrEmpty()) {
                issueTypes.firstOrNull { it.contains(initialIssueType, ignoreCase = true) } ?: issueTypes[0]
            } else {
                issueTypes[0]
            }
        )
    }

    var selectedTxId by remember {
        mutableStateOf(
            if (!initialTransactionId.isNullOrEmpty()) {
                transactions.firstOrNull { it.id == initialTransactionId || it.lotId == initialTransactionId }?.id ?: (transactions.firstOrNull()?.id ?: "TX-9001")
            } else {
                transactions.firstOrNull()?.id ?: "TX-9001"
            }
        )
    }

    var issueDescription by remember { mutableStateOf("") }
    var isPhotoAttached by remember { mutableStateOf(false) }

    var submittedDisputeRecord by remember { mutableStateOf<DisputeRecord?>(null) }

    // Dispute Submission Success Dialog
    submittedDisputeRecord?.let { record ->
        AlertDialog(
            onDismissRequest = {
                submittedDisputeRecord = null
                onNavigateToMyComplaints()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✅", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = strings.disputeSubmittedTitle,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = strings.disputeSubmittedMsg,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Ticket ID: ${record.ticketId}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = FarmerRoleGreen
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = strings.expectedResponseTime,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                                color = TextPrimary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        submittedDisputeRecord = null
                        onNavigateToMyComplaints()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = strings.trackDisputeBtn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = CardSurface
        )
    }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.raiseDisputeTitle,
                subtitle = "KrishiSetu Grievance Redressal Mechanism",
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
            // 1. Issue Category Selection
            item {
                Text(
                    text = strings.issueAboutPrompt,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    issueTypes.chunked(2).forEach { chunk ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            chunk.forEach { issue ->
                                val isSelected = selectedIssueType == issue
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) StatusError else BorderColor,
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .clickable { selectedIssueType = issue },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) Color(0xFFFFEBEE) else CardSurface
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { selectedIssueType = issue },
                                            colors = RadioButtonDefaults.colors(selectedColor = StatusError)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = issue,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 13.sp
                                            ),
                                            color = if (isSelected) StatusError else TextPrimary
                                        )
                                    }
                                }
                            }
                            if (chunk.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // 2. Select Transaction
            item {
                Text(
                    text = strings.selectTransactionPrompt,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    transactions.forEach { tx ->
                        val isSelected = selectedTxId == tx.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) FarmerRoleGreen else BorderColor,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedTxId = tx.id },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) FarmerRoleBg else CardSurface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tx.buyerName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                    StatusBadge(status = tx.status)
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${tx.cropName} – ${tx.quantityKg.toInt()} kg",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Amount: ₹${tx.netRealization.toInt()}",
                                        style = MaterialTheme.typography.titleMedium.copy(color = FarmerRoleGreen, fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Lot ID: ${tx.lotId}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Describe Issue Text Field
            item {
                Text(
                    text = strings.describeIssuePrompt,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = issueDescription,
                    onValueChange = { issueDescription = it },
                    placeholder = { Text(strings.describeIssuePlaceholder) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FarmerRoleGreen,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface
                    )
                )
            }

            // 4. Add Photo / Document UI
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isPhotoAttached = !isPhotoAttached },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPhotoAttached) Color(0xFFE8F5E9) else CardSurface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isPhotoAttached) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                                contentDescription = "Attach",
                                tint = if (isPhotoAttached) StatusSuccess else FarmerRoleGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isPhotoAttached) strings.photoAttached else strings.addPhotoDoc,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPhotoAttached) StatusSuccess else TextPrimary
                                    )
                                    )
                                Text(
                                    text = if (isPhotoAttached) "crop_damage_photo_01.jpg attached" else "Upload crop photo or receipt (Optional)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                    color = TextSecondary
                                )
                            }
                        }
                        TextButton(onClick = { isPhotoAttached = !isPhotoAttached }) {
                            Text(
                                text = if (isPhotoAttached) "Change" else "Upload",
                                color = FarmerRoleGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 5. Submit Dispute Button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                val targetTx = transactions.firstOrNull { it.id == selectedTxId } ?: transactions.firstOrNull()
                PrimaryKrishiButton(
                    text = strings.submitDisputeBtn,
                    containerColor = StatusError,
                    icon = Icons.Default.ReportProblem,
                    onClick = {
                        val record = viewModel.createDispute(
                            transactionId = selectedTxId,
                            disputeType = selectedIssueType,
                            description = issueDescription.ifBlank { "Issue regarding $selectedIssueType for lot ${targetTx?.lotId ?: "LOT-8841"}" },
                            photoAttached = isPhotoAttached,
                            buyerName = targetTx?.buyerName ?: "FreshFoods Pvt Ltd",
                            cropInfo = "${targetTx?.cropName ?: "Tomato"} – ${targetTx?.quantityKg?.toInt() ?: 800} kg"
                        )
                        submittedDisputeRecord = record
                    }
                )
            }
        }
    }
}
