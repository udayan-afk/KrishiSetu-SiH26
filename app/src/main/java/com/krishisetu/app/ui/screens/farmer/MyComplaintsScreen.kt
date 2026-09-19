package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun MyComplaintsScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onNavigateToRaiseDispute: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val disputes by viewModel.disputes.collectAsState()

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.myComplaintsTitle,
                subtitle = "Track Ticket Status & Resolutions",
                onBackClick = onBack
            )
        },
        containerColor = BackgroundWarm,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToRaiseDispute,
                containerColor = StatusError,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text(strings.raiseADisputeBtn, fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${strings.myComplaintsTitle} (${disputes.size})",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }
            }

            if (disputes.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("✅", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = strings.noComplaintsMsg,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "All your KrishiSetu transactions are running smoothly.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            } else {
                items(disputes) { dispute ->
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
                                        text = "${strings.ticketIdLabel}: ${dispute.ticketId}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = FarmerRoleGreen
                                        )
                                    )
                                    Text(
                                        text = "Date: ${dispute.date}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                                        color = TextMuted
                                    )
                                }

                                StatusBadge(status = dispute.status)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = BorderColor)
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Issue: ${dispute.disputeType}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Transaction: ${dispute.buyerName} (${dispute.cropInfo})",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = dispute.description,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                color = TextPrimary
                            )

                            if (dispute.photoAttached) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("📷 Photo Evidence Attached ✓", style = MaterialTheme.typography.labelSmall.copy(color = StatusSuccess, fontWeight = FontWeight.Bold))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = when (dispute.status) {
                                        "Resolved" -> Color(0xFFE8F5E9)
                                        "Under Review" -> Color(0xFFFFF3E0)
                                        else -> Color(0xFFE3F2FD)
                                    }
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when (dispute.status) {
                                            "Resolved" -> Icons.Default.AssignmentTurnedIn
                                            "Under Review" -> Icons.Default.HourglassTop
                                            else -> Icons.Default.ReportProblem
                                        },
                                        contentDescription = null,
                                        tint = when (dispute.status) {
                                            "Resolved" -> StatusSuccess
                                            "Under Review" -> StatusWarning
                                            else -> StatusInfo
                                        },
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = when (dispute.status) {
                                            "Resolved" -> "Resolved: Refund / settlement issued to farmer."
                                            "Under Review" -> "Under Review: Field officer assigned (Response < 24h)."
                                            else -> "Open: Ticket registered with KrishiSetu Support Team."
                                        },
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
