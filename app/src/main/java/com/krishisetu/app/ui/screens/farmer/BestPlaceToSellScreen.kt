package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.krishisetu.app.models.DemoMarketDataCatalog
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.components.VerifiedBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel
import java.util.Locale

private fun formatBestPlaceRate(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", value)
    }
}

@Composable
fun BestPlaceToSellScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onNavigateToDirectBuyer: () -> Unit,
    onNavigateToSellOrWait: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val marketPrices by viewModel.marketPrices.collectAsState()
    val selectedCrop by viewModel.selectedCommodity.collectAsState()
    val quantityKg by viewModel.comparisonQuantityKg.collectAsState()
    val allCommodities = viewModel.commodities

    val cropMarkets = remember(marketPrices, selectedCrop) {
        marketPrices.filter { it.cropName == selectedCrop }
    }

    val bestNetMarket = remember(cropMarkets, quantityKg) {
        cropMarkets.maxByOrNull { record ->
            val gross = record.currentPricePerKg * quantityKg
            val transport = DemoMarketDataCatalog.estimateTransportCost(record.distanceKm, quantityKg)
            gross - transport
        }
    }

    val localSataraMarket = remember(cropMarkets) {
        cropMarkets.find { it.marketId == "MKT-10" || it.district.equals("Satara", ignoreCase = true) }
            ?: cropMarkets.minByOrNull { it.distanceKm }
    }

    val localizedSelectedCrop = strings.commodityName(selectedCrop)

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.bestPlaceTitle,
                subtitle = String.format(strings.bestPlaceSubtitle, localizedSelectedCrop),
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
            // Commodity Switcher Row
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(allCommodities, key = { it.id }) { commodity ->
                        val isSelected = commodity.name == selectedCrop
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectCommodity(commodity.name) },
                            label = {
                                Text(
                                    text = "${commodity.emoji} ${strings.commodityName(commodity.name)}",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
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

            // Recommendation Alert Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⭐", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = String.format(strings.bestNetMarketIdentifiedTitle, localizedSelectedCrop),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            )
                            Text(
                                text = String.format(
                                    strings.bestNetMarketIdentifiedSub,
                                    cropMarkets.size,
                                    quantityKg.toInt(),
                                    strings.kgUnit
                                ),
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Visually Prominent Recommendation Card
            item {
                if (bestNetMarket == null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Text(
                            text = strings.noMarketsAvailableForCrop,
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                } else {
                    val best = bestNetMarket
                    val bestGross = best.currentPricePerKg * quantityKg
                    val bestTransport = DemoMarketDataCatalog.estimateTransportCost(best.distanceKm, quantityKg)
                    val bestNet = bestGross - bestTransport

                    val localNet = localSataraMarket?.let { loc ->
                        (loc.currentPricePerKg * quantityKg) -
                                DemoMarketDataCatalog.estimateTransportCost(loc.distanceKm, quantityKg)
                    } ?: bestNet
                    val netGainVsLocal = (bestNet - localNet).coerceAtLeast(0.0)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, FarmerRoleGreen, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(status = strings.topRecommendationBadge)
                                VerifiedBadge(text = strings.verifiedMarketBadge)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = strings.marketName(best.marketName),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                ),
                                color = TextPrimary
                            )

                            Text(
                                text = String.format(
                                    strings.bestPlaceCropQtyDistrictLine,
                                    localizedSelectedCrop,
                                    quantityKg.toInt(),
                                    strings.kgUnit,
                                    strings.districtName(best.district),
                                    best.distanceKm,
                                    strings.kmUnit
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BorderColor)

                            // Financial breakdown
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    "${strings.grossAmountLabel} (${quantityKg.toInt()} ${strings.kgUnit} @ ₹${formatBestPlaceRate(best.currentPricePerKg)}/${strings.kgUnit})",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextSecondary
                                )
                                Text("₹${bestGross.toInt()}", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(strings.estimatedTransportCostLabel, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                                Text("-₹${bestTransport.toInt()}", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = StatusError))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(strings.dataSourceLabel, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                                Text(
                                    "${strings.dataStatusLabel(best.dataStatus)} · ${strings.marketTypeLabel(best.marketType)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                                )
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = BorderColor)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(FarmerRoleBg)
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = strings.netRealizationTitle,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                                        )
                                        Text(
                                            text = "₹${bestNet.toInt()}",
                                            style = MaterialTheme.typography.headlineLarge.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = FarmerRoleGreen,
                                                fontSize = 32.sp
                                            )
                                        )
                                    }
                                    if (netGainVsLocal > 0) {
                                        Text(
                                            text = String.format(strings.gainVsLocalMandi, netGainVsLocal.toInt()),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = AccentGold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Comparison against Local Mandi Card
            item {
                if (bestNetMarket != null && localSataraMarket != null) {
                    val localGross = localSataraMarket.currentPricePerKg * quantityKg
                    val localTransport = DemoMarketDataCatalog.estimateTransportCost(localSataraMarket.distanceKm, quantityKg)
                    val localNet = localGross - localTransport

                    val bestNet = (bestNetMarket.currentPricePerKg * quantityKg) -
                            DemoMarketDataCatalog.estimateTransportCost(bestNetMarket.distanceKm, quantityKg)
                    val diff = (bestNet - localNet).coerceAtLeast(0.0)
                    val localizedLocalMarket = strings.marketName(localSataraMarket.marketName)
                    val localizedBestMarket = strings.marketName(bestNetMarket.marketName)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = String.format(strings.comparisonWithLocalTitle, localizedLocalMarket),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = String.format(
                                    strings.localMarketMetricsLine,
                                    formatBestPlaceRate(localSataraMarket.currentPricePerKg),
                                    strings.kgUnit,
                                    localTransport.toInt(),
                                    localNet.toInt()
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (diff > 0) {
                                    String.format(strings.higherNetInBestMarketMsg, localizedBestMarket, diff.toInt())
                                } else {
                                    String.format(strings.localMarketIsBestMsg, localizedLocalMarket)
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = strings.bestPlaceWarningNote,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            // Actions
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PrimaryKrishiButton(
                        text = "🏢 ${strings.actionFindBuyers}",
                        containerColor = FarmerRoleGreen,
                        onClick = onNavigateToDirectBuyer
                    )
                    OutlinedButton(
                        onClick = onNavigateToSellOrWait,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "⏳ ${strings.sellNowOrWaitTitle} ($localizedSelectedCrop)",
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                    }
                }
            }
        }
    }
}
