package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.CommodityCategory
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel
import java.util.Locale

private fun formatTrendRate(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", value)
    }
}

@Composable
fun SellNowOrWaitScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onSellNowClick: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val marketPrices by viewModel.marketPrices.collectAsState()
    val selectedCrop by viewModel.selectedCommodity.collectAsState()
    val allCommodities = viewModel.commodities

    val cropMarkets = remember(marketPrices, selectedCrop) {
        marketPrices.filter { it.cropName == selectedCrop }
    }
    val topMarket = remember(cropMarkets) {
        cropMarkets.maxByOrNull { it.currentPricePerKg }
    }
    val minMarket = remember(cropMarkets) {
        cropMarkets.minByOrNull { it.currentPricePerKg }
    }
    val avgRate = remember(cropMarkets) {
        if (cropMarkets.isEmpty()) 0.0 else cropMarkets.map { it.currentPricePerKg }.average()
    }
    val commodityInfo = remember(allCommodities, selectedCrop) {
        allCommodities.find { it.name == selectedCrop }
    }

    val day1Price = topMarket?.priceTrend7Days?.firstOrNull() ?: avgRate
    val day7Price = topMarket?.priceTrend7Days?.lastOrNull() ?: avgRate
    val sevenDayChange = day7Price - day1Price

    val shelfLifeNote = when (commodityInfo?.category) {
        CommodityCategory.VEGETABLES -> strings.shelfLifeVegetables
        CommodityCategory.FRUITS -> strings.shelfLifeFruits
        CommodityCategory.GRAINS_AND_PULSES -> strings.shelfLifeGrains
        else -> strings.shelfLifeVegetables
    }
    val localizedSelectedCrop = strings.commodityName(selectedCrop)

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.sellNowOrWaitTitle,
                subtitle = String.format(strings.sellOrWaitSubtitle, localizedSelectedCrop),
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
            // Commodity Selector
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

            // Main Demo Trend Summary Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmerRoleBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        StatusBadge(
                            status = if (sevenDayChange >= 0) strings.sellNowHeuristicBadge else strings.waitMonitorHeuristicBadge
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${commodityInfo?.emoji ?: ""} ${String.format(strings.trendSummaryHeading, localizedSelectedCrop)}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = FarmerRoleGreen,
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = String.format(
                                strings.trendSummaryDescription,
                                localizedSelectedCrop,
                                formatTrendRate(topMarket?.currentPricePerKg ?: avgRate),
                                strings.marketName(topMarket?.marketName ?: "Pune — Gultekdi Market Yard"),
                                formatTrendRate(avgRate)
                            ),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                            color = TextSecondary
                        )
                    }
                }
            }

            // Market Indicators Card (Derived from Repository Data)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "${strings.marketIndicatorsTitle} ($localizedSelectedCrop)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderColor)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(strings.highestMarketRateRow, color = TextSecondary)
                            Text(
                                "₹${formatTrendRate(topMarket?.currentPricePerKg ?: 0.0)}/${strings.kgUnit} (${strings.districtName(topMarket?.district ?: "")})",
                                fontWeight = FontWeight.Bold,
                                color = FarmerRoleGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(strings.lowestMarketRateRow, color = TextSecondary)
                            Text(
                                "₹${formatTrendRate(minMarket?.currentPricePerKg ?: 0.0)}/${strings.kgUnit} (${strings.districtName(minMarket?.district ?: "")})",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(strings.thirtyMarketAvgRateRow, color = TextSecondary)
                            Text("₹${formatTrendRate(avgRate)}/${strings.kgUnit}", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(strings.sevenDaySampleTrendRow, color = TextSecondary)
                            val sign = if (sevenDayChange > 0) "+" else ""
                            Text(
                                "₹${formatTrendRate(day1Price)} → ₹${formatTrendRate(day7Price)} (${sign}₹${formatTrendRate(sevenDayChange)}/${strings.kgUnit})",
                                fontWeight = FontWeight.Bold,
                                color = if (sevenDayChange >= 0) StatusSuccess else StatusWarning
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(strings.storagePerishabilityRow, color = TextSecondary, modifier = Modifier.weight(1f))
                            Text(shelfLifeNote, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Prototype Disclaimer Notice
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("ℹ️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.trendDisclaimerNotice,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = Color(0xFFE65100)
                        )
                    }
                }
            }

            item {
                PrimaryKrishiButton(
                    text = strings.proceedToSellBtn,
                    containerColor = FarmerRoleGreen,
                    onClick = onSellNowClick
                )
            }
        }
    }
}
