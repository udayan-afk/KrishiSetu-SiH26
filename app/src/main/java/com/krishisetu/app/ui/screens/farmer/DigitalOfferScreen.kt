package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun DigitalOfferScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onOfferAcceptedNavigateToLogistics: () -> Unit,
    onNavigateToRaiseDispute: (transactionId: String, issueType: String) -> Unit = { _, _ -> }
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val offers by viewModel.offers.collectAsState()
    val activeOffer = offers.firstOrNull() ?: return

    var counterPriceText by remember { mutableStateOf("27.5") }
    var showCounterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.digitalOfferTitle,
                subtitle = "Offer ID: ${activeOffer.id}",
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
            // Main Offer Overview Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = activeOffer.buyerName,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            StatusBadge(status = activeOffer.status)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "${strings.offeredPriceLabel}: ₹${activeOffer.currentOfferedPricePerKg}/kg",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = FarmerRoleGreen,
                                fontSize = 28.sp
                            )
                        )

                        Text(
                            text = "Crop Lot: ${activeOffer.cropName} (${activeOffer.quantityKg.toInt()} kg)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )

                        val totalValue = activeOffer.currentOfferedPricePerKg * activeOffer.quantityKg
                        Text(
                            text = "${strings.grossAmountLabel}: ₹${totalValue.toInt()}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }
                }
            }

            // Negotiation Timeline / History
            item {
                Text(
                    text = strings.negotiationHistoryHeader,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            items(activeOffer.history) { historyLine ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💬", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = historyLine,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }

            // Interactive Buttons (Accept / Counter / Reject)
            item {
                if (activeOffer.status == "Accepted") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FarmerRoleBg)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🎉", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = strings.offerAcceptedSuccess,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = FarmerRoleGreen
                                )
                            )
                            Text(
                                text = "Deal confirmed with ${activeOffer.buyerName}. Proceed to select transport.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            PrimaryKrishiButton(
                                text = strings.selectLogisticsBtn,
                                containerColor = FarmerRoleGreen,
                                onClick = onOfferAcceptedNavigateToLogistics
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        PrimaryKrishiButton(
                            text = "${strings.acceptOfferBtn} (₹${activeOffer.currentOfferedPricePerKg}/kg)",
                            containerColor = FarmerRoleGreen,
                            onClick = {
                                viewModel.updateOfferStatus(activeOffer.id, "Accepted")
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showCounterDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(strings.counterOfferBtn, fontWeight = FontWeight.Bold, color = BuyerRoleBlue)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.updateOfferStatus(activeOffer.id, "Rejected")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(strings.rejectOfferBtn, fontWeight = FontWeight.Bold, color = StatusError)
                            }
                        }
                    }
                }
            }

            item {
                TextButton(
                    onClick = { onNavigateToRaiseDispute(activeOffer.lotId, strings.issueBuyer) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠️ ${strings.reportIssueBtn} (${strings.issueBuyer})",
                        color = StatusError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Counter Offer Dialog
    if (showCounterDialog) {
        AlertDialog(
            onDismissRequest = { showCounterDialog = false },
            title = { Text(strings.counterOfferBtn, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = counterPriceText,
                    onValueChange = { counterPriceText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val counterPrice = counterPriceText.toDoubleOrNull() ?: 27.5
                        viewModel.updateOfferStatus(activeOffer.id, "Countered", counterPrice)
                        showCounterDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text(strings.submit)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCounterDialog = false }) {
                    Text(strings.cancel)
                }
            }
        )
    }
}
