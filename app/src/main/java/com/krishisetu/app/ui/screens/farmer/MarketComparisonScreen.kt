package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.CommodityCategory
import com.krishisetu.app.models.DemoMarketDataCatalog
import com.krishisetu.app.models.MarketPriceData
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel
import java.text.NumberFormat
import java.util.Locale

private enum class ComparisonSortMode {
    NET_HIGH_TO_LOW,
    PRICE_HIGH_TO_LOW,
    NEAREST_DISTANCE,
    LOWEST_TRANSPORT_COST
}

private data class EvaluatedMarketComparison(
    val record: MarketPriceData,
    val grossRevenue: Double,
    val estimatedTransportCost: Double,
    val netRealization: Double
)

private fun formatPriceValue(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", value)
    }
}

private fun formatQuantityWithCommas(kg: Int): String {
    return NumberFormat.getIntegerInstance(Locale.US).format(kg)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketComparisonScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onNavigateToBestPlaceToSell: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val marketPrices by viewModel.marketPrices.collectAsState()
    val selectedCrop by viewModel.selectedCommodity.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val sharedQuantityKg by viewModel.comparisonQuantityKg.collectAsState()
    val allCommodities = viewModel.commodities

    var quantityInput by remember(sharedQuantityKg) {
        mutableStateOf(sharedQuantityKg.toInt().toString())
    }
    val effectiveQuantityKg = (quantityInput.toDoubleOrNull() ?: sharedQuantityKg).coerceAtLeast(1.0)

    var sortMode by remember { mutableStateOf(ComparisonSortMode.NET_HIGH_TO_LOW) }
    var commodityDropdownExpanded by remember { mutableStateOf(false) }

    val categoryCommodities = remember(allCommodities, selectedCategory) {
        if (selectedCategory == CommodityCategory.ALL) {
            allCommodities
        } else {
            allCommodities.filter { it.category == selectedCategory }
        }
    }

    val selectedCommodityInfo = remember(allCommodities, selectedCrop) {
        allCommodities.find { it.name == selectedCrop }
    }

    val evaluatedMarkets = remember(marketPrices, selectedCrop, effectiveQuantityKg, sortMode) {
        val raw = marketPrices.filter { it.cropName == selectedCrop }.map { record ->
            val gross = record.currentPricePerKg * effectiveQuantityKg
            val transport = DemoMarketDataCatalog.estimateTransportCost(
                distanceKm = record.distanceKm,
                quantityKg = effectiveQuantityKg
            )
            val net = gross - transport
            EvaluatedMarketComparison(
                record = record,
                grossRevenue = gross,
                estimatedTransportCost = transport,
                netRealization = net
            )
        }
        when (sortMode) {
            ComparisonSortMode.NET_HIGH_TO_LOW -> raw.sortedByDescending { it.netRealization }
            ComparisonSortMode.PRICE_HIGH_TO_LOW -> raw.sortedByDescending { it.record.currentPricePerKg }
            ComparisonSortMode.NEAREST_DISTANCE -> raw.sortedBy { it.record.distanceKm }
            ComparisonSortMode.LOWEST_TRANSPORT_COST -> raw.sortedBy { it.estimatedTransportCost }
        }
    }

    val highestPriceItem = remember(evaluatedMarkets) {
        evaluatedMarkets.maxByOrNull { it.record.currentPricePerKg }
    }
    val lowestPriceItem = remember(evaluatedMarkets) {
        evaluatedMarkets.minByOrNull { it.record.currentPricePerKg }
    }
    val bestNetItem = remember(evaluatedMarkets) {
        evaluatedMarkets.maxByOrNull { it.netRealization }
    }
    val lowestNetItem = remember(evaluatedMarkets) {
        evaluatedMarkets.minByOrNull { it.netRealization }
    }

    val localizedSelectedCrop = strings.commodityName(selectedCrop)

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.marketComparisonTitle,
                subtitle = "${formatQuantityWithCommas(effectiveQuantityKg.toInt())} kg · $localizedSelectedCrop · ${evaluatedMarkets.size} ${strings.marketsCountLabel}",
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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
        ) {
            // 1. DEMO & Transport Warning Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "⚠️ ${strings.demoDataBadge}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = Color(0xFFE65100)
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.demoDataSubtitle,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = Color(0xFFBF360C)
                        )
                    }
                }
            }

            // 2. Category Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CommodityCategory.values().toList()) { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectCategory(category) },
                            label = {
                                Text(
                                    text = strings.categoryName(category),
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

            // 3. Commodity & Load Configuration Card (Phase 1 Fixed Layout)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = strings.commodityLoadConfigTitle,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )

                        // Clean full-width Selected Commodity Dropdown
                        ExposedDropdownMenuBox(
                            expanded = commodityDropdownExpanded,
                            onExpandedChange = { commodityDropdownExpanded = !commodityDropdownExpanded }
                        ) {
                            val catLabel = selectedCommodityInfo?.category?.let { strings.categoryName(it) } ?: ""
                            OutlinedTextField(
                                value = "${selectedCommodityInfo?.emoji ?: ""} $localizedSelectedCrop ($catLabel)",
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                label = { Text(strings.selectedCommodityLabel) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = commodityDropdownExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = BackgroundWarm,
                                    unfocusedContainerColor = BackgroundWarm,
                                    focusedBorderColor = FarmerRoleGreen,
                                    unfocusedBorderColor = BorderColor
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = commodityDropdownExpanded,
                                onDismissRequest = { commodityDropdownExpanded = false }
                            ) {
                                categoryCommodities.forEach { commodity ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "${commodity.emoji} ${strings.commodityName(commodity.name)} (${strings.categoryName(commodity.category)})"
                                            )
                                        },
                                        onClick = {
                                            viewModel.selectCommodity(commodity.name)
                                            commodityDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Horizontally scrollable quick-select commodity chips
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = strings.quickSelectCommodityLabel,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = TextSecondary
                            )
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(categoryCommodities, key = { it.id }) { commodity ->
                                    val isSelected = commodity.name == selectedCrop
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.selectCommodity(commodity.name) },
                                        label = {
                                            Text(
                                                text = "${commodity.emoji} ${strings.commodityName(commodity.name)}",
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = FarmerRoleGreen,
                                            selectedLabelColor = Color.White,
                                            containerColor = BackgroundWarm,
                                            labelColor = TextPrimary
                                        )
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = BorderColor.copy(alpha = 0.6f))

                        // Full-width Quantity Input on its own row (eliminates narrow vertical squeeze)
                        OutlinedTextField(
                            value = quantityInput,
                            onValueChange = { newVal ->
                                val digitsOnly = newVal.filter { it.isDigit() }.take(6)
                                quantityInput = digitsOnly
                                val parsed = digitsOnly.toDoubleOrNull()
                                if (parsed != null && parsed > 0) {
                                    viewModel.updateComparisonQuantityKg(parsed)
                                }
                            },
                            label = { Text(strings.customQuantityInputLabel) },
                            suffix = {
                                Text(
                                    text = "kg",
                                    fontWeight = FontWeight.Bold,
                                    color = FarmerRoleGreen
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = BackgroundWarm,
                                unfocusedContainerColor = BackgroundWarm,
                                focusedBorderColor = FarmerRoleGreen,
                                unfocusedBorderColor = BorderColor
                            )
                        )

                        // Responsive 4-button row for preset quantities: 500 kg, 800 kg, 1,500 kg, 3,000 kg
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = strings.presetQuantityLabel,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = TextSecondary
                            )
                            val presets = listOf(500, 800, 1500, 3000)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                presets.forEach { presetKg ->
                                    val isPresetSelected = effectiveQuantityKg.toInt() == presetKg
                                    val formattedPreset = "${formatQuantityWithCommas(presetKg)} kg"
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(
                                                width = if (isPresetSelected) 1.5.dp else 1.dp,
                                                color = if (isPresetSelected) FarmerRoleGreen else BorderColor,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .clickable {
                                                quantityInput = presetKg.toString()
                                                viewModel.updateComparisonQuantityKg(presetKg.toDouble())
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isPresetSelected) FarmerRoleGreen else BackgroundWarm
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 4.dp)
                                        ) {
                                            Text(
                                                text = formattedPreset,
                                                fontSize = 12.sp,
                                                fontWeight = if (isPresetSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                                color = if (isPresetSelected) Color.White else TextPrimary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Dynamic Top Summary Card: Best Market for Selected Load + Formula Breakdown
            item {
                if (highestPriceItem != null && lowestPriceItem != null && bestNetItem != null && lowestNetItem != null) {
                    val netAdvantage = bestNetItem.netRealization - lowestNetItem.netRealization

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, FarmerRoleGreen.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = FarmerRoleBg)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = FarmerRoleGreen
                                ) {
                                    Text(
                                        text = "🏆 ${strings.bestMarketForLoadBadge} ${formatQuantityWithCommas(effectiveQuantityKg.toInt())} KG",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            fontSize = 11.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Text(
                                    text = "₹${formatPriceValue(bestNetItem.record.currentPricePerKg)}/kg",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = FarmerRoleGreen
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = strings.marketName(bestNetItem.record.marketName),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "${strings.districtLabel}: ${strings.districtName(bestNetItem.record.district)} · ${bestNetItem.record.distanceKm} ${strings.kmUnit} · ${strings.marketTypeLabel(bestNetItem.record.marketType)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Dynamic Formula Pill
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = CardSurface,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "(₹${formatPriceValue(bestNetItem.record.currentPricePerKg)}/kg × ${formatQuantityWithCommas(effectiveQuantityKg.toInt())} kg) − ₹${bestNetItem.estimatedTransportCost.toInt()} ${strings.transportCostShort} = ₹${bestNetItem.netRealization.toInt()}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = FarmerRoleGreen
                                        )
                                    )
                                    HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(strings.grossRevenueShort, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                            Text(
                                                "₹${bestNetItem.grossRevenue.toInt()}",
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(strings.transportCostShort, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                            Text(
                                                "-₹${bestNetItem.estimatedTransportCost.toInt()}",
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = StatusError)
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(strings.netRealizationShort, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                            Text(
                                                "₹${bestNetItem.netRealization.toInt()}",
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = FarmerRoleGreen)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = strings.gainVsLowestLabel,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = "+₹${netAdvantage.toInt()}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
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

            // 5. Sort Controls + Action Button
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PrimaryKrishiButton(
                        text = "${strings.viewRecommendedDealBtn} ($localizedSelectedCrop)",
                        containerColor = FarmerRoleGreen,
                        onClick = onNavigateToBestPlaceToSell
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${strings.sortMarketsByLabel} (${evaluatedMarkets.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    val sortOptions = listOf(
                        ComparisonSortMode.NET_HIGH_TO_LOW to strings.sortBestNet,
                        ComparisonSortMode.PRICE_HIGH_TO_LOW to strings.sortHighestPrice,
                        ComparisonSortMode.NEAREST_DISTANCE to strings.sortNearestDistance,
                        ComparisonSortMode.LOWEST_TRANSPORT_COST to strings.sortLowestTransport
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(sortOptions) { (mode, label) ->
                            val isSelected = sortMode == mode
                            FilterChip(
                                selected = isSelected,
                                onClick = { sortMode = mode },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryGreen,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // 6. All 30 Market Comparison Cards
            items(evaluatedMarkets, key = { it.record.id }) { item ->
                val isBestNet = bestNetItem?.record?.id == item.record.id
                val isHighestRawPrice = highestPriceItem?.record?.id == item.record.id
                val isLowestRawPrice = lowestPriceItem?.record?.id == item.record.id

                MarketComparisonCard(
                    item = item,
                    quantityKg = effectiveQuantityKg,
                    isBestNet = isBestNet,
                    isHighestRawPrice = isHighestRawPrice,
                    isLowestRawPrice = isLowestRawPrice,
                    bestNetRealization = bestNetItem?.netRealization ?: item.netRealization,
                    strings = strings
                )
            }
        }
    }
}

@Composable
private fun MarketComparisonCard(
    item: EvaluatedMarketComparison,
    quantityKg: Double,
    isBestNet: Boolean,
    isHighestRawPrice: Boolean,
    isLowestRawPrice: Boolean,
    bestNetRealization: Double,
    strings: FarmerStrings
) {
    val record = item.record
    val netDiffFromBest = item.netRealization - bestNetRealization

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                when {
                    isBestNet -> Modifier.border(2.dp, FarmerRoleGreen, RoundedCornerShape(20.dp))
                    isHighestRawPrice -> Modifier.border(1.5.dp, BuyerRoleBlue, RoundedCornerShape(20.dp))
                    isLowestRawPrice -> Modifier.border(1.5.dp, StatusError, RoundedCornerShape(20.dp))
                    else -> Modifier
                }
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isBestNet) Color(0xFFF1F8E9) else CardSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isBestNet) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strings.marketName(record.marketName),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "${strings.districtLabel}: ${strings.districtName(record.district)} · ${record.distanceKm} ${strings.kmUnit} · ${strings.marketTypeLabel(record.marketType)}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = TextMuted
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isBestNet) {
                        StatusBadge(status = strings.bestValueBadge)
                    }
                    if (isHighestRawPrice) {
                        StatusBadge(status = strings.highestRateLabel)
                    }
                    if (isLowestRawPrice) {
                        StatusBadge(status = strings.lowestRateLabel)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${strings.currentRateLabel} (${record.unit})", color = TextSecondary)
                Text("₹${formatPriceValue(record.currentPricePerKg)}/kg", fontWeight = FontWeight.Bold, color = TextPrimary)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${strings.grossAmountLabel} (${formatQuantityWithCommas(quantityKg.toInt())} kg)", color = TextSecondary)
                Text("₹${item.grossRevenue.toInt()}", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${strings.transportCostLabel} (${record.distanceKm} km)", color = TextSecondary)
                Text("-₹${item.estimatedTransportCost.toInt()}", color = StatusError, fontWeight = FontWeight.SemiBold)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderColor)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strings.estNetLabel,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (isBestNet) {
                            "★ ${strings.sortBestNet}"
                        } else {
                            "-₹${(-netDiffFromBest).toInt()} vs #1"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isBestNet) FarmerRoleGreen else TextMuted
                    )
                }
                Text(
                    text = "₹${item.netRealization.toInt()}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isBestNet) FarmerRoleGreen else TextPrimary
                    )
                )
            }
        }
    }
}
