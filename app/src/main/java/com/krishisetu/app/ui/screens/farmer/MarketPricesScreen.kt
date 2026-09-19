package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.AppLanguage
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.MetricStatCard
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun MarketPricesScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onNavigateToComparison: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val marketPrices by viewModel.marketPrices.collectAsState()
    var selectedCrop by remember { mutableStateOf("Tomato") }
    val crops = listOf("Tomato", "Onion", "Wheat", "Soybean")

    val filteredPrices = marketPrices.filter { it.cropName == selectedCrop }
    val bestPriceMarket = filteredPrices.maxByOrNull { it.currentPricePerKg } ?: filteredPrices.firstOrNull()

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.marketPricesTitle,
                subtitle = strings.marketPricesSubtitle,
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
            // 1. Crop Selector Tabs
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(crops) { crop ->
                        val isSelected = crop == selectedCrop
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCrop = crop },
                            label = { Text(crop, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FarmerRoleGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // 2. Selected Crop Overview Card
            item {
                bestPriceMarket?.let { market ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "$selectedCrop - ${strings.highestRateLabel}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${market.marketName} (${market.district})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                                StatusBadge(status = "TOP PRICE")
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MetricStatCard(
                                    label = strings.currentRateLabel,
                                    value = "₹${market.currentPricePerKg.toInt()}/kg",
                                    valueColor = FarmerRoleGreen,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricStatCard(
                                    label = strings.modalPriceLabel,
                                    value = "₹${market.modalPricePerKg.toInt()}/kg",
                                    valueColor = AccentGold,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricStatCard(
                                    label = strings.maxRateLabel,
                                    value = "₹${market.maxPricePerKg.toInt()}/kg",
                                    valueColor = BuyerRoleBlue,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 7-Day Trend Chart Simulation
                            Text(
                                text = strings.priceTrend7Days,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BackgroundWarm)
                                    .padding(8.dp)
                            ) {
                                val points = market.priceTrend7Days
                                val maxX = points.size - 1
                                val minY = points.minOrNull() ?: 20.0
                                val maxY = points.maxOrNull() ?: 30.0

                                val path = Path()
                                points.forEachIndexed { index, valY ->
                                    val x = (index.toFloat() / maxX) * size.width
                                    val y = size.height - (((valY - minY) / (maxY - minY + 0.1)).toFloat() * size.height)
                                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                                }
                                drawPath(
                                    path = path,
                                    color = PrimaryGreen,
                                    style = Stroke(width = 3.dp.toPx())
                                )
                            }
                        }
                    }
                }
            }

            // 3. Comparison Banner Button
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
                        Column {
                            Text(
                                text = strings.compareMarketCostsBtn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            )
                            Text(
                                text = strings.compareMarketSub,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                color = TextSecondary
                            )
                        }
                        Text("Compare →", fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                    }
                }
            }

            // 4. Mandi List
            item {
                Text(
                    text = "All Markets for $selectedCrop",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            items(filteredPrices) { data ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = data.marketName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "Distance: ${data.distanceKm} km · Est. Transport: ₹${data.transportCostEst.toInt()}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                color = TextMuted
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${data.currentPricePerKg.toInt()}/kg",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = FarmerRoleGreen
                                )
                            )
                            Text(
                                text = "Modal: ₹${data.modalPricePerKg.toInt()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}
