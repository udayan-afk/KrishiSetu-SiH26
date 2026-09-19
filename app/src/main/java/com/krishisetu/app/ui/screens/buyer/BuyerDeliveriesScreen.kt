package com.krishisetu.app.ui.screens.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.models.BuyerDeliveryRecord
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun BuyerDeliveriesScreen(
    viewModel: KrishiSetuViewModel,
    onTrackLive: (BuyerDeliveryRecord) -> Unit
) {
    val deliveries by viewModel.buyerDeliveriesList.collectAsState()
    var showDeliveryConfirmedToast by remember { mutableStateOf(false) }

    val inTransit = deliveries.filter { it.status == "IN TRANSIT" }
    val delivered = deliveries.filter { it.status == "DELIVERED" }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Logistics & Deliveries",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // IN TRANSIT SECTION
            item {
                Text(
                    text = "IN TRANSIT",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 13.sp)
                )
            }

            if (inTransit.isEmpty()) {
                item {
                    Text("No active shipments in transit.", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                }
            } else {
                items(inTransit) { item ->
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
                                    Icon(Icons.Default.LocalShipping, contentDescription = null, tint = PrimaryGreen)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${item.cropName} · ${item.quantityKg.toInt()} kg",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                                    )
                                }
                                StatusBadge(status = "IN TRANSIT")
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Farmer: ${item.farmerName} · ID: ${item.deliveryCode}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            Text(text = "via ${item.transporter} · ETA: ${item.eta}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onTrackLive(item) },
                                    modifier = Modifier.weight(1f).height(42.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryGreen)
                                ) {
                                    Text("Track Live 📍", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                                }

                                Button(
                                    onClick = {
                                        viewModel.confirmBuyerDelivery(item.id)
                                        showDeliveryConfirmedToast = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen),
                                    modifier = Modifier.weight(1f).height(42.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Confirm Delivery ✓", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // DELIVERED SECTION
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "DELIVERED",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextMuted, fontSize = 13.sp)
                )
            }

            items(delivered) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.cropName} · ${item.quantityKg.toInt()} kg",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            StatusBadge(status = "DELIVERED")
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Farmer: ${item.farmerName} · ID: ${item.deliveryCode}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        Text(text = "via ${item.transporter} · Delivered: ${item.eta}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                }
            }
        }
    }

    if (showDeliveryConfirmedToast) {
        AlertDialog(
            onDismissRequest = { showDeliveryConfirmedToast = false },
            title = { Text("Delivery Confirmed", fontWeight = FontWeight.Bold) },
            text = { Text("Shipment delivery has been confirmed. Escrow funds released to farmer.", color = TextSecondary) },
            confirmButton = {
                Button(onClick = { showDeliveryConfirmedToast = false }, colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }
}
