package com.krishisetu.app.ui.screens.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.models.BuyerDeliveryRecord
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.theme.*

@Composable
fun BuyerDeliveryTrackingScreen(
    delivery: BuyerDeliveryRecord? = null,
    onBack: () -> Unit
) {
    val cropName = delivery?.cropName ?: "Onion"
    val qtyKg = delivery?.quantityKg?.toInt() ?: 600
    val farmerName = delivery?.farmerName ?: "Priya More"
    val transporter = delivery?.transporter ?: "Santosh Transport"
    val deliveryId = delivery?.deliveryCode ?: "DEL-0089"
    val eta = delivery?.eta ?: "Today, 4 PM"
    val statusText = delivery?.status ?: "IN TRANSIT"

    val timelineSteps = listOf(
        Pair("Order Confirmed", true),
        Pair("Offer Accepted", true),
        Pair("Pickup Scheduled", true),
        Pair("In Transit", true), // Current Active
        Pair("Out for Delivery", false),
        Pair("Delivered", false)
    )

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = "Track Delivery",
                subtitle = "$deliveryId · $cropName",
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
            // Prominent Status Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FarmerRoleBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmerRoleGreen.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(FarmerRoleGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "🚚 $statusText",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = FarmerRoleGreen)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Estimated Arrival: $eta",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, color = TextPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Delivery Details Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Shipment Details",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    TrackingDetailRow("Crop & Quantity", "$cropName · $qtyKg kg")
                    TrackingDetailRow("Farmer Seller", farmerName)
                    TrackingDetailRow("Transport Provider", transporter)
                    TrackingDetailRow("Delivery ID", deliveryId)
                    TrackingDetailRow("ETA", eta)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Visual Delivery Timeline Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Delivery Timeline",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    timelineSteps.forEachIndexed { index, (stepTitle, isDone) ->
                        val isCurrentStep = stepTitle == "In Transit" && statusText == "IN TRANSIT"

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Timeline Step Icon
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isCurrentStep -> FarmerRoleGreen
                                            isDone -> PrimaryGreen
                                            else -> Color(0xFFE0E0E0)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCurrentStep) {
                                    Text("🚚", fontSize = 14.sp)
                                } else if (isDone) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = stepTitle,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isCurrentStep) FontWeight.ExtraBold else if (isDone) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCurrentStep) FarmerRoleGreen else if (isDone) TextPrimary else TextMuted
                                    )
                                )
                                if (isCurrentStep) {
                                    Text("Active · Live tracking in progress", style = MaterialTheme.typography.labelSmall.copy(color = FarmerRoleGreen, fontSize = 11.sp))
                                }
                            }
                        }

                        // Connecting Vertical Line between timeline nodes
                        if (index < timelineSteps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 15.dp, top = 2.dp, bottom = 2.dp)
                                    .width(2.dp)
                                    .height(24.dp)
                                    .background(if (isDone) PrimaryGreen else Color(0xFFE0E0E0))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Back Button
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmerRoleGreen)
            ) {
                Text("← Back to Logistics & Deliveries", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TrackingDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
    }
}
