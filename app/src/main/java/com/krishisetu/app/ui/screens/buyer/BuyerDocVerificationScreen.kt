package com.krishisetu.app.ui.screens.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.models.DocStatus
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.components.SubmissionSuccessDialog
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun BuyerDocVerificationScreen(
    viewModel: KrishiSetuViewModel,
    onVerificationCompleted: () -> Unit,
    onBack: () -> Unit
) {
    val buyerProfile by viewModel.currentBuyerProfile.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }

    val docItems = listOf(
        Triple("GST Certificate", buyerProfile.gstStatus, "Upload GST Certificate (PDF/JPG)"),
        Triple("PAN Card", buyerProfile.panStatus, "Upload Business PAN Card"),
        Triple("Business Registration / Incorporation", buyerProfile.businessRegStatus, "Upload Certificate of Incorporation / Udhyam"),
        Triple("FSSAI License", buyerProfile.fssaiStatus, "Upload FSSAI License Document"),
        Triple("Business Address Proof", buyerProfile.addressProofStatus, "Upload Electricity Bill / Lease Agreement")
    )

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = "Verify Your Organisation",
                subtitle = "Submit documents to become a Verified Buyer",
                onBackClick = onBack
            )
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Verify Your Organisation",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = TextPrimary
            )
            Text(
                text = "Submit your business documents to become a Verified Buyer.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Why Verification Banner Card matching spec
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FarmerRoleBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmerRoleGreen.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = FarmerRoleGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Why verification?",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen, fontSize = 14.sp)
                        )
                        Text(
                            text = "Verification helps protect farmers and buyers from fraudulent transactions.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontSize = 12.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5 Documents List
            docItems.forEach { (docName, status, helperText) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = docName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                                    color = TextPrimary
                                )
                            }

                            StatusBadge(
                                status = when (status) {
                                    DocStatus.NOT_SUBMITTED -> "Not Submitted"
                                    DocStatus.SUBMITTED -> "Submitted"
                                    DocStatus.UNDER_VERIFICATION -> "Under Verification"
                                    DocStatus.VERIFIED -> "Verified"
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = helperText, style = MaterialTheme.typography.bodySmall, color = TextMuted)

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { viewModel.uploadBuyerDocument(docName) },
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, FarmerRoleGreen)
                        ) {
                            Text(
                                text = if (status == DocStatus.VERIFIED) "✓ Document Uploaded" else if (status == DocStatus.SUBMITTED) "Replace File 📄" else "Upload / Submit 📤",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryKrishiButton(
                text = "Submit Documents for Verification",
                containerColor = FarmerRoleGreen,
                onClick = {
                    viewModel.submitBuyerDocsForVerification()
                    showSuccessDialog = true
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showSuccessDialog) {
        SubmissionSuccessDialog(
            title = "Documents Submitted Successfully",
            message = "Your organisation documents have been submitted for verification. They will be reviewed and verified by KrishiSetu.",
            buttonText = "Continue →",
            onDismiss = {
                showSuccessDialog = false
                onVerificationCompleted()
            }
        )
    }
}
