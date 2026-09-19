package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.BuyerUser
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.VerifiedBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun BuyerMarketplaceScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onSelectBuyer: (BuyerUser) -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val buyers by viewModel.buyers.collectAsState()
    val requirements by viewModel.buyerRequirements.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredBuyers = buyers.filter {
        it.companyName.contains(searchQuery, ignoreCase = true) ||
                it.location.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.buyerMarketplaceTitle,
                subtitle = "Verified Processors, Traders & FPOs",
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
            // Search Input
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(strings.searchBuyersPlaceholder) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface,
                        focusedBorderColor = FarmerRoleGreen,
                        unfocusedBorderColor = BorderColor
                    )
                )
            }

            item {
                Text(
                    text = "${strings.actionFindBuyers} (${filteredBuyers.size})",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            items(filteredBuyers) { buyer ->
                val buyerReq = requirements.find { it.buyerId == buyer.id }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectBuyer(buyer) },
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = buyer.companyName,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${buyer.buyerType} · ${buyer.location} (${buyer.distanceKm} km)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                    color = TextMuted
                                )
                            }
                            if (buyer.verified) {
                                VerifiedBadge()
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)

                        buyerReq?.let { req ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Requirement: ${req.cropName} (${req.requiredQuantityKg.toInt()} kg)",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = buyer.onTimeHistory,
                                        style = MaterialTheme.typography.labelSmall.copy(color = FarmerRoleGreen)
                                    )
                                }
                                Text(
                                    text = "₹${req.pricePerKg.toInt()}/kg",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = FarmerRoleGreen
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { onSelectBuyer(buyer) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                            ) {
                                Text(strings.makeOfferBtn, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
