package com.krishisetu.app.ui.screens.buyer

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
import com.krishisetu.app.models.BuyerOfferRecord
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun BuyerOffersScreen(
    viewModel: KrishiSetuViewModel,
    onActionGated: (() -> Unit) -> Unit
) {
    val offers by viewModel.buyerOffersList.collectAsState()

    var showCounterDialogForOffer by remember { mutableStateOf<BuyerOfferRecord?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Offers & Negotiations",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            items(offers) { offer ->
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
                            Column {
                                Text(
                                    text = offer.farmerName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = offer.offerCode,
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )
                            }
                            StatusBadge(status = offer.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${offer.cropName} · ${offer.quantityKg.toInt()} kg",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                text = "₹${offer.pricePerKg.toInt()}/kg",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = FarmerRoleGreen)
                            )
                        }

                        if (offer.status == "Pending") {
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onActionGated {
                                            viewModel.updateBuyerOfferRecordStatus(offer.id, "Accepted")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                                ) {
                                    Text("Accept", fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                                }

                                OutlinedButton(
                                    onClick = {
                                        onActionGated {
                                            showCounterDialogForOffer = offer
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, FarmerRoleGreen),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                                ) {
                                    Text("Counter", fontWeight = FontWeight.Bold, color = FarmerRoleGreen, fontSize = 13.sp, maxLines = 1)
                                }

                                Button(
                                    onClick = {
                                        onActionGated {
                                            viewModel.updateBuyerOfferRecordStatus(offer.id, "Rejected")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = StatusError),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                                ) {
                                    Text("Reject", fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Counter Offer Modal Dialog
    showCounterDialogForOffer?.let { targetOffer ->
        var counterRate by remember { mutableStateOf("${targetOffer.pricePerKg.toInt() - 1}") }

        AlertDialog(
            onDismissRequest = { showCounterDialogForOffer = null },
            title = { Text("Submit Counter Offer", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Counter rate for ${targetOffer.cropName} (${targetOffer.quantityKg.toInt()} kg) from ${targetOffer.farmerName}:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = counterRate,
                        onValueChange = { counterRate = it },
                        label = { Text("Counter Rate (₹/kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateBuyerOfferRecordStatus(targetOffer.id, "Counter Sent")
                        showCounterDialogForOffer = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text("Send Counter Offer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCounterDialogForOffer = null }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }
}
