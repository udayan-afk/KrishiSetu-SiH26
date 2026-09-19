package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun PaymentsScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onNavigateToRaiseDispute: (transactionId: String, issueType: String) -> Unit = { _, _ -> }
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val transactions by viewModel.transactions.collectAsState()

    val paymentStatuses = listOf(
        strings.statusDealConfirmed,
        strings.statusPickupScheduled,
        strings.statusInTransit,
        strings.statusDelivered,
        strings.statusPaymentInitiated,
        strings.statusPaymentReceived
    )

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.paymentsTitle,
                subtitle = "KrishiSetu Escrow Protected Payments",
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
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmerRoleGreen)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = strings.settlementEarningsHeader,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "₹1,11,500",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 32.sp
                            )
                        )
                        Text(
                            text = "🛡️ 100% Escrow Protected by KrishiSetu Trust",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentGold
                        )
                    }
                }
            }

            item {
                Text(
                    text = strings.paymentsTitle,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            items(transactions) { tx ->
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
                                    text = "${tx.cropName} (${tx.quantityKg.toInt()} kg)",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Buyer: ${tx.buyerName} · Date: ${tx.date}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                    color = TextMuted
                                )
                            }
                            StatusBadge(status = tx.status)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${strings.netRealizationTitle}:", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            Text("₹${tx.netRealization.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = FarmerRoleGreen))
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)

                        // Payment Status Timeline
                        Text(
                            text = strings.statusTimelineHeader,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val idx = paymentStatuses.indexOf(tx.status)
                        val currentStepIndex = if (idx == -1) 0 else idx

                        paymentStatuses.forEachIndexed { index, stepName ->
                            val isCompleted = index <= currentStepIndex
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(if (isCompleted) FarmerRoleGreen else BorderColor)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stepName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 12.sp,
                                        fontWeight = if (index == currentStepIndex) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCompleted) TextPrimary else TextMuted
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (tx.status != "Payment Received") {
                            PrimaryKrishiButton(
                                text = "Simulate Advance Status: Payment Received",
                                containerColor = PrimaryGreenDark,
                                onClick = {
                                    viewModel.updateTransactionStatus(tx.id, "Payment Received")
                                }
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(FarmerRoleBg)
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎉 Bank Settlement Complete (Ref: TXN-884920)", color = FarmerRoleGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        TextButton(
                            onClick = { onNavigateToRaiseDispute(tx.id, strings.issuePayment) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = "⚠️ ${strings.reportIssueBtn} (${strings.issuePayment})",
                                color = StatusError,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
