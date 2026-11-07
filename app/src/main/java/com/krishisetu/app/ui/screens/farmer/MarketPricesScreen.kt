package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.*
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.MetricStatCard
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel
import java.util.Locale
import kotlin.math.abs

private enum class MarketSortOption {
    HIGHEST_PRICE,
    LOWEST_PRICE,
    NEAREST_DISTANCE,
    MARKET_NAME
}

private fun formatRate(price: Double): String {
    return if (price % 1.0 == 0.0) {
        price.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", price)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketPricesScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onNavigateToComparison: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val marketPrices by viewModel.marketPrices.collectAsState()
    val selectedCrop by viewModel.selectedCommodity.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedMarketId by viewModel.selectedMarketId.collectAsState()
    val allCommodities = viewModel.commodities

    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf(MarketSortOption.HIGHEST_PRICE) }
    var marketDropdownExpanded by remember { mutableStateOf(false) }

    // Filter commodities by category; also allow search query to match commodity or market
    val matchingCommodities = remember(allCommodities, selectedCategory, searchQuery, currentLang) {
        val trimmed = searchQuery.trim()
        val byCategory = allCommodities.filter { commodity ->
            selectedCategory == CommodityCategory.ALL || commodity.category == selectedCategory
        }
        val filteredByCropName = if (trimmed.isBlank()) {
            byCategory
        } else {
            byCategory.filter { commodity ->
                commodity.name.contains(trimmed, ignoreCase = true) ||
                        strings.commodityName(commodity.name).contains(trimmed, ignoreCase = true) ||
                        commodity.category.displayName.contains(trimmed, ignoreCase = true)
            }
        }
        // If user typed a market name (e.g., "Pune", "Lasalgaon") rather than a crop name, keep category commodities visible
        if (filteredByCropName.isEmpty() && trimmed.isNotBlank() &&
            viewModel.marketYards.any {
                it.name.contains(trimmed, ignoreCase = true) || it.district.contains(trimmed, ignoreCase = true)
            }
        ) {
            byCategory
        } else {
            filteredByCropName
        }
    }

    LaunchedEffect(matchingCommodities, selectedCrop) {
        if (matchingCommodities.isNotEmpty() && matchingCommodities.none { it.name == selectedCrop }) {
            viewModel.selectCommodity(matchingCommodities.first().name)
        }
    }

    val selectedCommodityInfo = remember(allCommodities, selectedCrop) {
        allCommodities.find { it.name == selectedCrop }
    }

    val allSelectedCropMarkets = remember(marketPrices, selectedCrop) {
        marketPrices.filter { it.cropName == selectedCrop }
    }

    val commodityPrices = remember(allSelectedCropMarkets, sortOption, searchQuery) {
        val trimmed = searchQuery.trim()
        val marketFiltered = if (trimmed.isNotBlank() &&
            allCommodities.none {
                it.name.contains(trimmed, ignoreCase = true) ||
                        strings.commodityName(it.name).contains(trimmed, ignoreCase = true)
            }
        ) {
            allSelectedCropMarkets.filter {
                it.marketName.contains(trimmed, ignoreCase = true) ||
                        it.district.contains(trimmed, ignoreCase = true)
            }
        } else {
            allSelectedCropMarkets
        }
        when (sortOption) {
            MarketSortOption.HIGHEST_PRICE -> marketFiltered.sortedByDescending { it.currentPricePerKg }
            MarketSortOption.LOWEST_PRICE -> marketFiltered.sortedBy { it.currentPricePerKg }
            MarketSortOption.NEAREST_DISTANCE -> marketFiltered.sortedBy { it.distanceKm }
            MarketSortOption.MARKET_NAME -> marketFiltered.sortedBy { it.marketName }
        }
    }

    val inspectedMarketRecord = remember(allSelectedCropMarkets, selectedMarketId) {
        allSelectedCropMarkets.find { it.marketId == selectedMarketId }
            ?: allSelectedCropMarkets.firstOrNull()
    }

    val bestPriceMarket = remember(allSelectedCropMarkets) {
        allSelectedCropMarkets.maxByOrNull { it.currentPricePerKg }
    }
    val lowestPriceMarket = remember(allSelectedCropMarkets) {
        allSelectedCropMarkets.minByOrNull { it.currentPricePerKg }
    }
    val avgModalPrice = remember(allSelectedCropMarkets) {
        if (allSelectedCropMarkets.isEmpty()) 0.0
        else allSelectedCropMarkets.map { it.modalPricePerKg }.average()
    }

    val localizedSelectedCrop = strings.commodityName(selectedCrop)

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.marketPricesTitle,
                subtitle = "$localizedSelectedCrop · ${allCommodities.size} Crops × ${viewModel.marketYards.size} ${strings.marketsCountLabel}",
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
            // 1. DEMO DATA — NOT LIVE PRICES Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "⚠️ ${strings.demoDataBadge}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFE65100)
                                )
                            )
                            Text(
                                text = strings.demoDataSubtitle,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = Color(0xFFBF360C)
                            )
                        }
                    }
                }
            }

            // 2. Search Bar by Commodity or Market
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(strings.searchCommodityOrMarketHint) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = FarmerRoleGreen
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = TextMuted
                                )
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

            // 3. Category Filter Chips: All, Vegetables, Fruits, Grains and Pulses
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CommodityCategory.values().toList()) { category ->
                        val isSelected = selectedCategory == category
                        val count = if (category == CommodityCategory.ALL) {
                            allCommodities.size
                        } else {
                            allCommodities.count { it.category == category }
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectCategory(category) },
                            label = {
                                Text(
                                    text = "${strings.categoryName(category)} ($count)",
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

            // 4. Empty state if search matches no commodity or market
            if (matchingCommodities.isEmpty() || commodityPrices.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔍", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = strings.noMatchingMarketsMsg,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = {
                                    searchQuery = ""
                                    viewModel.selectCategory(CommodityCategory.ALL)
                                }
                            ) {
                                Text(strings.cancel, color = FarmerRoleGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // 5. Commodity Selector Row (All matching commodities from the 35-item catalog)
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${strings.selectCropLabel} (${matchingCommodities.size})",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                            )
                            selectedCommodityInfo?.let { info ->
                                Text(
                                    text = "${info.emoji} ${strings.categoryName(info.category)} · ${info.unit}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = FarmerRoleGreen
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(matchingCommodities, key = { it.id }) { commodity ->
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
                }

                // 6. Top Summary Strip: Highest, Lowest, Average Modal, Markets Count
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricStatCard(
                            label = strings.highestRateLabel,
                            value = "₹${formatRate(bestPriceMarket?.currentPricePerKg ?: 0.0)}/kg",
                            valueColor = FarmerRoleGreen,
                            modifier = Modifier.weight(1f)
                        )
                        MetricStatCard(
                            label = strings.lowestRateLabel,
                            value = "₹${formatRate(lowestPriceMarket?.currentPricePerKg ?: 0.0)}/kg",
                            valueColor = StatusError,
                            modifier = Modifier.weight(1f)
                        )
                        MetricStatCard(
                            label = strings.avgModalLabel,
                            value = "₹${formatRate(avgModalPrice)}/kg",
                            valueColor = BuyerRoleBlue,
                            modifier = Modifier.weight(1f)
                        )
                        MetricStatCard(
                            label = strings.marketsCountLabel,
                            value = "${allSelectedCropMarkets.size}",
                            valueColor = AccentGold,
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                }

                // 7. Phase 2: Intraday Price Timeline Card (with Market Yard Selector)
                item {
                    inspectedMarketRecord?.let { record ->
                        IntradayPriceTimelineCard(
                            record = record,
                            allCropMarkets = allSelectedCropMarkets,
                            commodityEmoji = selectedCommodityInfo?.emoji ?: "🌾",
                            localizedCropName = localizedSelectedCrop,
                            marketDropdownExpanded = marketDropdownExpanded,
                            onExpandedChange = { marketDropdownExpanded = it },
                            onSelectMarket = { mktId ->
                                viewModel.selectMarket(mktId)
                                marketDropdownExpanded = false
                            },
                            strings = strings
                        )
                    }
                }

                // 8. Phase 3: Simulated Auction Summary Card (only when inspected market is AUCTION_BIDDING)
                item {
                    inspectedMarketRecord?.auctionSummary?.let { auction ->
                        AuctionSummaryCard(
                            record = inspectedMarketRecord,
                            auction = auction,
                            localizedCropName = localizedSelectedCrop,
                            strings = strings
                        )
                    }
                }

                // 9. Comparison Banner Button
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigateToComparison),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${strings.compareMarketCostsBtn} ($localizedSelectedCrop)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                                )
                                Text(
                                    text = strings.compareMarketSub,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                    color = TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("→", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = FarmerRoleGreen)
                        }
                    }
                }

                // 10. Sort Controls + Mandi List Header
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${strings.marketsCountLabel} (${commodityPrices.size}) — $localizedSelectedCrop",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "₹/kg",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FarmerRoleGreen
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.tapCardForTimelineHint,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val sortModes = listOf(
                            MarketSortOption.HIGHEST_PRICE to strings.sortHighestPrice,
                            MarketSortOption.LOWEST_PRICE to strings.lowestRateLabel,
                            MarketSortOption.NEAREST_DISTANCE to strings.sortNearestDistance,
                            MarketSortOption.MARKET_NAME to "A–Z"
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(sortModes) { (option, label) ->
                                val isSelected = sortOption == option
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { sortOption = option },
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

                // 11. Mandi List Items (30 Maharashtra APMCs)
                items(commodityPrices, key = { it.id }) { data ->
                    val isSelectedMarket = data.marketId == inspectedMarketRecord?.marketId
                    val latestObs = data.observations.lastOrNull()
                    val changeRs = latestObs?.changeRs ?: (data.currentPricePerKg - data.previousPricePerKg)
                    val changePct = latestObs?.changePercent

                    val trendBadgeText = when {
                        changeRs > 0 -> "▲ +₹${formatRate(changeRs)}/kg" + (changePct?.let { " (+${formatRate(it)}%)" } ?: "")
                        changeRs < 0 -> "▼ -₹${formatRate(abs(changeRs))}/kg" + (changePct?.let { " (${formatRate(it)}%)" } ?: "")
                        else -> "• 0.0%"
                    }
                    val trendColor = when {
                        changeRs > 0 -> FarmerRoleGreen
                        changeRs < 0 -> StatusError
                        else -> TextMuted
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (isSelectedMarket) {
                                    Modifier.border(2.dp, FarmerRoleGreen, RoundedCornerShape(16.dp))
                                } else {
                                    Modifier
                                }
                            )
                            .clickable { viewModel.selectMarket(data.marketId) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelectedMarket) Color(0xFFF1F8E9) else CardSurface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelectedMarket) 3.dp else 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = strings.marketName(data.marketName),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${strings.districtLabel}: ${strings.districtName(data.district)} · ${data.distanceKm} ${strings.kmUnit}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        MarketTypePill(marketType = data.marketType, strings = strings)
                                        DataStatusPill(status = data.dataStatus, strings = strings)
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "₹${formatRate(data.currentPricePerKg)}/kg",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = FarmerRoleGreen
                                        )
                                    )
                                    Text(
                                        text = trendBadgeText,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = trendColor
                                        )
                                    )
                                    Text(
                                        text = data.lastUpdated,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = TextMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Min ₹${formatRate(data.minPricePerKg)} · ${strings.modalPriceLabel} ₹${formatRate(data.modalPricePerKg)} · Max ₹${formatRate(data.maxPricePerKg)}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = TextSecondary
                                )
                                Text(
                                    text = "${data.observations.size} obs",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (data.observations.size > 1) FarmerRoleGreen else AccentGold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntradayPriceTimelineCard(
    record: MarketPriceData,
    allCropMarkets: List<MarketPriceData>,
    commodityEmoji: String,
    localizedCropName: String,
    marketDropdownExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelectMarket: (String) -> Unit,
    strings: FarmerStrings
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "⏱️ ${strings.intradayTimelineTitle}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = TextPrimary
                    )
                    Text(
                        text = "$commodityEmoji $localizedCropName · ${strings.intradayTimelineSubtitle}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                DataStatusPill(status = record.dataStatus, strings = strings)
            }

            // Market Yard Selector Dropdown + Quick Market Type Presets
            ExposedDropdownMenuBox(
                expanded = marketDropdownExpanded,
                onExpandedChange = { onExpandedChange(!marketDropdownExpanded) }
            ) {
                OutlinedTextField(
                    value = "${strings.marketName(record.marketName)} (${strings.marketTypeLabel(record.marketType)})",
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text(strings.selectMarketTimelineLabel) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = marketDropdownExpanded)
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
                    expanded = marketDropdownExpanded,
                    onDismissRequest = { onExpandedChange(false) }
                ) {
                    allCropMarkets.forEach { mkt ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = "${strings.marketName(mkt.marketName)} — ₹${formatRate(mkt.currentPricePerKg)}/${strings.kgUnit}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "${strings.marketTypeLabel(mkt.marketType)} · ${mkt.observations.size} obs",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            },
                            onClick = { onSelectMarket(mkt.marketId) }
                        )
                    }
                }
            }

            // Quick market type switcher chips so evaluators can test Intraday (Pune), Open Auction (Nashik), Closed Auction (Lasalgaon), Single Reported (Rahuri) in 1 tap
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val quickMarketIds = listOf("MKT-01", "MKT-03", "MKT-04", "MKT-12", "MKT-14")
                val quickMarkets = quickMarketIds.mapNotNull { id -> allCropMarkets.find { it.marketId == id } }
                items(quickMarkets, key = { it.marketId }) { mkt ->
                    val isSelected = mkt.marketId == record.marketId
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectMarket(mkt.marketId) },
                        label = {
                            Text(
                                text = mkt.marketName.substringBefore(" —").substringBefore(" APMC"),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FarmerRoleGreen,
                            selectedLabelColor = Color.White,
                            containerColor = BackgroundWarm
                        )
                    )
                }
            }

            // Market Type + Freshness Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MarketTypePill(marketType = record.marketType, strings = strings)
                Text(
                    text = record.freshnessLabel,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = if (record.isStale) StatusError else TextMuted
                )
            }

            // Stale data warning if applicable
            if (record.isStale) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFEBEE),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠️ ${strings.staleDataWarning}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StatusError
                        ),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            // Observations List (Morning, Midday, Afternoon OR Single Daily Bulletin)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                record.observations.forEachIndexed { index, obs ->
                    val isLatest = index == record.observations.lastIndex
                    val changeRs = obs.changeRs
                    val changePct = obs.changePercent

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isLatest) FarmerRoleBg else BackgroundWarm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (isLatest) Modifier.border(1.dp, FarmerRoleGreen, RoundedCornerShape(12.dp))
                                else Modifier
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (isLatest) FarmerRoleGreen else BuyerRoleBlue)
                                )
                                Column {
                                    Text(
                                        text = strings.sessionLabel(obs.sessionKey),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "${obs.reportedDate} · ${obs.reportedTime} · ${obs.sourceProvider}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = TextMuted
                                    )
                                    if (obs.minPricePerKg != null && obs.maxPricePerKg != null) {
                                        Text(
                                            text = "Min ₹${formatRate(obs.minPricePerKg)} · Modal ₹${formatRate(obs.modalPricePerKg ?: obs.pricePerKg)} · Max ₹${formatRate(obs.maxPricePerKg)}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${formatRate(obs.pricePerKg)}/kg",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isLatest) FarmerRoleGreen else TextPrimary
                                    )
                                )
                                if (changeRs != null && changePct != null) {
                                    val sign = if (changeRs > 0) "+" else if (changeRs < 0) "-" else ""
                                    val pctSign = if (changePct > 0) "+" else ""
                                    val color = when {
                                        changeRs > 0 -> FarmerRoleGreen
                                        changeRs < 0 -> StatusError
                                        else -> TextMuted
                                    }
                                    Text(
                                        text = "${sign}₹${formatRate(abs(changeRs))}/kg (${pctSign}${formatRate(changePct)}%)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = color
                                        )
                                    )
                                } else {
                                    Text(
                                        text = strings.openingObservationLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // If multiple observations exist, render an intraday session line chart; if only 1 exists, show single-observation notice
            if (record.observations.size > 1) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BackgroundWarm)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    val prices = record.observations.map { it.pricePerKg }
                    val maxIdx = (prices.size - 1).coerceAtLeast(1)
                    val minVal = (prices.minOrNull() ?: 10.0) - 1.0
                    val maxVal = (prices.maxOrNull() ?: 30.0) + 1.0

                    val path = Path()
                    val points = prices.mapIndexed { idx, value ->
                        val x = (idx.toFloat() / maxIdx) * size.width
                        val y = size.height - (((value - minVal) / (maxVal - minVal + 0.1)).toFloat() * size.height)
                        Offset(x, y)
                    }
                    points.forEachIndexed { idx, pt ->
                        if (idx == 0) path.moveTo(pt.x, pt.y) else path.lineTo(pt.x, pt.y)
                    }
                    drawPath(
                        path = path,
                        color = FarmerRoleGreen,
                        style = Stroke(width = 3.dp.toPx())
                    )
                    points.forEach { pt ->
                        drawCircle(
                            color = FarmerRoleGreen,
                            radius = 5.dp.toPx(),
                            center = pt
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFF8E1),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ℹ️ ${strings.singleObservationNotice}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF795548),
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AuctionSummaryCard(
    record: MarketPriceData,
    auction: AuctionSummary,
    localizedCropName: String,
    strings: FarmerStrings
) {
    val statusColor = when (auction.status) {
        AuctionStatus.OPEN -> FarmerRoleGreen
        AuctionStatus.CLOSED -> BuyerRoleBlue
        AuctionStatus.NOT_STARTED -> AccentGold
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🔨 ${strings.auctionSummaryTitle}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = TextPrimary
                    )
                    Text(
                        text = "${record.marketName} · $localizedCropName",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF3E0)
                ) {
                    Text(
                        text = strings.sampleAuctionBadge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100),
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(color = BorderColor.copy(alpha = 0.6f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(strings.auctionStatusLabel, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(
                        text = strings.auctionStatusLabel(auction.status),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = statusColor
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(strings.auctionWindowLabel, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(
                        text = "${auction.startTime} – ${auction.closeTime}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                    )
                    Text(
                        text = auction.bidTimestamp,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextMuted
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BackgroundWarm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(strings.validBidsLabel, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text(
                            text = "${auction.validBidsCount}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                        )
                    }

                    when (auction.status) {
                        AuctionStatus.NOT_STARTED -> {
                            Text(
                                text = strings.noBidsYetLabel,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = AccentGold
                                )
                            )
                        }
                        AuctionStatus.OPEN -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(strings.currentHighestBidLabel, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text(
                                    text = "₹${formatRate(auction.currentHighestBidPerKg ?: 0.0)}/kg",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = FarmerRoleGreen
                                    )
                                )
                            }
                        }
                        AuctionStatus.CLOSED -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(strings.winningBidLabel, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text(
                                    text = "₹${formatRate(auction.winningBidPerKg ?: 0.0)}/kg",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = BuyerRoleBlue
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "ℹ️ ${strings.auctionDisclaimerText}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = TextMuted
            )
        }
    }
}

@Composable
private fun MarketTypePill(
    marketType: MarketType,
    strings: FarmerStrings
) {
    val (bg, fg) = when (marketType) {
        MarketType.INTRADAY_UPDATED -> FarmerRoleBg to FarmerRoleGreen
        MarketType.AUCTION_BIDDING -> Color(0xFFE3F2FD) to BuyerRoleBlue
        MarketType.REPORTED_PRICE -> Color(0xFFFFF8E1) to Color(0xFF795548)
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bg
    ) {
        Text(
            text = strings.marketTypeLabel(marketType),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = fg
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun DataStatusPill(
    status: MarketDataStatus,
    strings: FarmerStrings
) {
    val (bg, fg) = when (status) {
        MarketDataStatus.SAMPLE_DATA -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        MarketDataStatus.REPORTED -> Color(0xFFE8EAF6) to Color(0xFF283593)
        MarketDataStatus.LIVE -> FarmerRoleBg to FarmerRoleGreen
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bg
    ) {
        Text(
            text = strings.dataStatusLabel(status),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = fg
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
