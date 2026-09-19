package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.TransportOption
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun LogisticsScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onTransportSelectedNavigateToPayments: () -> Unit,
    onNavigateToRaiseDispute: (transactionId: String, issueType: String) -> Unit = { _, _ -> }
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val transports by viewModel.transports.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val activeTx = transactions.firstOrNull()

    var selectedOptionId by remember { mutableStateOf<String?>(null) }
    var transportConfirmed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.logisticsTitle,
                subtitle = "Satara Farm → Pune Food Processor",
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
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Cargo Details",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Crop: 800 kg Tomato (Grade A) · Pickup: Khed, Satara",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            item {
                Text(
                    text = strings.availableVehiclesHeader,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            items(transports) { option ->
                val isSelected = selectedOptionId == option.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) FarmerRoleGreen else BorderColor,
                            shape = RoundedCornerShape(18.dp)
                        ),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) FarmerRoleBg else CardSurface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🚛", fontSize = 26.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = option.vehicleName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Capacity: ${option.capacityTonnes} Tonnes · Driver: ${option.driverName}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                        color = TextMuted
                                    )
                                }
                            }

                            if (isSelected) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FarmerRoleGreen)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Availability: ${option.availability}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Rating: ⭐ ${option.rating}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }

                            Text(
                                text = "₹${option.costRs.toInt()}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = FarmerRoleGreen
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { selectedOptionId = option.id },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) FarmerRoleGreen else PrimaryGreenDark
                            )
                        ) {
                            Text(if (isSelected) "✓ Vehicle Selected" else "Select Vehicle")
                        }
                    }
                }
            }

            item {
                if (transportConfirmed) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FarmerRoleBg)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "✅", fontSize = 36.sp)
                            Text(
                                text = "TRANSPORT SELECTED!",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            )
                            Text(
                                text = "Driver notified. Pickup scheduled for tomorrow morning.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            PrimaryKrishiButton(
                                text = strings.paymentsTitle,
                                containerColor = FarmerRoleGreen,
                                onClick = onTransportSelectedNavigateToPayments
                            )
                        }
                    }
                } else {
                    PrimaryKrishiButton(
                        text = strings.confirmTransportBtn,
                        enabled = selectedOptionId != null,
                        containerColor = FarmerRoleGreen,
                        onClick = {
                            val selected = transports.find { it.id == selectedOptionId }
                            if (selected != null && activeTx != null) {
                                viewModel.selectTransport(activeTx.id, selected)
                                transportConfirmed = true
                            }
                        }
                    )
                }
            }


            item {
                TextButton(
                    onClick = { onNavigateToRaiseDispute(activeTx?.id ?: "TX-9001", strings.issueDelivery) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠️ ${strings.reportIssueBtn} (${strings.issueDelivery})",
                        color = StatusError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

