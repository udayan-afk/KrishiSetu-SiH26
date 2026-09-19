package com.krishisetu.app.ui.screens.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.models.UserRole
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.MetricStatCard
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.VerifiedBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun BuyerProfileScreen(
    viewModel: KrishiSetuViewModel,
    onNavigateToVerification: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val buyerProfile by viewModel.currentBuyerProfile.collectAsState()

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = "My Profile",
                subtitle = "KrishiSetu Buyer Identity",
                onBackClick = onBack
            )
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            // Company Card Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(FarmerRoleBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("FF", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = FarmerRoleGreen)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = buyerProfile.companyName,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (buyerProfile.isVerified) {
                                    VerifiedBadge(text = "✓ Verified Buyer")
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFFFF3E0)
                                    ) {
                                        Text(
                                            text = "Unverified Buyer",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = StatusWarning),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("• ${buyerProfile.city}, ${buyerProfile.state}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Row matching Figma (2 Active Reqs, 47 Transactions, ₹4.28L Total Spent)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Active Reqs", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text("${buyerProfile.activeRequirementsCount}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = PrimaryGreen))
                        }
                        Column {
                            Text("Transactions", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text("${buyerProfile.totalTransactionsCount}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = PrimaryGreen))
                        }
                        Column {
                            Text("Total Spent", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text("₹4.28L", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dedicated Document Verification Section
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
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = FarmerRoleGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Document Verification", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }

                        TextButton(onClick = onNavigateToVerification) {
                            Text(if (buyerProfile.isVerified) "View Status →" else "Verify Now →", color = FarmerRoleGreen, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (buyerProfile.isVerified) "All 5 business documents are verified." else "Submit business documents to participate in marketplace transactions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Prototype Testing Verification Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Prototype Gating Test Switch", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                            Text("Toggle to test unverified vs verified buyer gating", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                        }
                        Switch(
                            checked = buyerProfile.isVerified,
                            onCheckedChange = { isChecked ->
                                viewModel.setBuyerVerificationStatus(isChecked)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = FarmerRoleGreen, checkedTrackColor = FarmerRoleBg)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Company Information Section matching Figma
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Company Information", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileDetailRow("Business Type", buyerProfile.businessType)
                    ProfileDetailRow("GST Number", buyerProfile.gstNumber)
                    ProfileDetailRow("PAN", buyerProfile.pan)
                    ProfileDetailRow("FSSAI License", buyerProfile.fssaiLicense)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Person Section matching Figma
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Contact Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileDetailRow("Contact Person", buyerProfile.contactPerson)
                    ProfileDetailRow("Email", buyerProfile.companyEmail)
                    ProfileDetailRow("Phone", buyerProfile.phone)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Logout / Switch Account Button
            OutlinedButton(
                onClick = {
                    viewModel.selectRole(UserRole.NONE)
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StatusError)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = StatusError)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout / Switch Account", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = StatusError))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileDetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = TextPrimary))
    }
}
