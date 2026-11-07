package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
    var selectedCropFilter by remember { mutableStateOf("ALL") }
    var contactDialogBuyer by remember { mutableStateOf<BuyerUser?>(null) }

    val availableCropFilters = remember(requirements) {
        listOf("ALL") + requirements.map { it.cropName }.distinct()
    }

    val filteredBuyers = remember(buyers, requirements, searchQuery, selectedCropFilter, currentLang) {
        val query = searchQuery.trim().lowercase()
        buyers.filter { buyer ->
            val buyerReq = requirements.find { it.buyerId == buyer.id }
            val cropMatches = selectedCropFilter == "ALL" || buyerReq?.cropName == selectedCropFilter
            val localizedLocation = strings.districtName(buyer.location).lowercase()
            val localizedType = strings.translateBuyerType(buyer.buyerType).lowercase()
            val localizedCrop = buyerReq?.let { strings.commodityName(it.cropName).lowercase() } ?: ""
            val queryMatches = query.isEmpty() ||
                    buyer.companyName.lowercase().contains(query) ||
                    buyer.location.lowercase().contains(query) ||
                    localizedLocation.contains(query) ||
                    localizedType.contains(query) ||
                    localizedCrop.contains(query) ||
                    (buyerReq?.cropName?.lowercase()?.contains(query) == true)
            cropMatches && queryMatches
        }
    }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.buyerMarketplaceTitle,
                subtitle = strings.buyerMarketplaceSubtitle,
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
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = strings.clearSearchBtn)
                            }
                        }
                    },
                    singleLine = true,
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

            // Commodity Filter Row
            item {
                Column {
                    Text(
                        text = strings.filterByCropLabel,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availableCropFilters) { cropKey ->
                            val isSelected = selectedCropFilter == cropKey
                            val chipLabel = if (cropKey == "ALL") {
                                strings.categoryAll
                            } else {
                                strings.commodityName(cropKey)
                            }
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCropFilter = cropKey },
                                label = {
                                    Text(
                                        text = chipLabel,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FarmerRoleGreen,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "${strings.actionFindBuyers} (${filteredBuyers.size})",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            if (filteredBuyers.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = strings.noBuyersFoundTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = strings.noBuyersFoundSub,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            TextButton(onClick = {
                                searchQuery = ""
                                selectedCropFilter = "ALL"
                            }) {
                                Text(strings.clearSearchBtn, fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            }
                        }
                    }
                }
            }

            items(filteredBuyers, key = { it.id }) { buyer ->
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
                                    text = "${strings.translateBuyerType(buyer.buyerType)} · ${strings.districtName(buyer.location)} (${buyer.distanceKm} ${strings.kmUnit})",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                    color = TextMuted
                                )
                                Text(
                                    text = String.format(strings.buyerContactLabel, buyer.contactName),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                            if (buyer.verified) {
                                VerifiedBadge(text = strings.verifiedBuyer)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)

                        buyerReq?.let { req ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = String.format(
                                            strings.buyerRequirementLine,
                                            strings.commodityName(req.cropName),
                                            req.requiredQuantityKg.toInt(),
                                            strings.kgUnit
                                        ),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = String.format(strings.onTimePaymentHistoryFormat, buyer.paymentReliability),
                                        style = MaterialTheme.typography.labelSmall.copy(color = FarmerRoleGreen)
                                    )
                                }
                                Text(
                                    text = "₹${req.pricePerKg.toInt()}/${strings.kgUnit}",
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
                        ) {
                            OutlinedButton(
                                onClick = { contactDialogBuyer = buyer },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(strings.contactBuyerBtn, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                            }
                            Button(
                                onClick = { onSelectBuyer(buyer) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                            ) {
                                Text(strings.makeOfferBtn, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    contactDialogBuyer?.let { buyer ->
        AlertDialog(
            onDismissRequest = { contactDialogBuyer = null },
            title = {
                Text(strings.contactBuyerDialogTitle, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("${strings.buyerCompanyLabel}: ${buyer.companyName}", fontWeight = FontWeight.Bold)
                    Text("${strings.buyerTypeLabel}: ${strings.translateBuyerType(buyer.buyerType)}")
                    Text("${strings.buyerLocationLabel}: ${strings.districtName(buyer.location)} (${buyer.distanceKm} ${strings.kmUnit})")
                    Text(String.format(strings.buyerContactLabel, buyer.contactName))
                    Text("${strings.buyerPhoneLabel}: ${buyer.mobile}", fontWeight = FontWeight.SemiBold, color = FarmerRoleGreen)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val selected = buyer
                        contactDialogBuyer = null
                        onSelectBuyer(selected)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text(strings.makeOfferBtn)
                }
            },
            dismissButton = {
                TextButton(onClick = { contactDialogBuyer = null }) {
                    Text(strings.cancel)
                }
            }
        )
    }
}
