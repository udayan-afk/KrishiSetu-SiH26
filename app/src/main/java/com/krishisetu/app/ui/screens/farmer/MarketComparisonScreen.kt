package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*

@Composable
fun MarketComparisonScreen(
    onBack: () -> Unit,
    onNavigateToBestPlaceToSell: () -> Unit
) {
    val strings = FarmerStrings.get()

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.marketComparisonTitle,
                subtitle = "800 kg Tomato · Satara Base",
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
                    colors = CardDefaults.cardColors(containerColor = FarmerRoleBg)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "💡 Net Realization Formula",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.netRealizationFormula,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = TextSecondary
                        )
                    }
                }
            }

            // Comparison Option 1: Satara (Local)
            item {
                MarketComparisonCard(
                    marketName = "Satara Mandi (Local)",
                    distanceKm = 8,
                    pricePerKg = 22.0,
                    grossRevenue = 17600.0,
                    transportCost = 400.0,
                    commissionPercent = "8% APMC Fee (₹1,408)",
                    estimatedNet = 15792.0,
                    isBestOption = false,
                    strings = strings
                )
            }

            // Comparison Option 2: Karad
            item {
                MarketComparisonCard(
                    marketName = "Karad APMC",
                    distanceKm = 55,
                    pricePerKg = 24.0,
                    grossRevenue = 19200.0,
                    transportCost = 1500.0,
                    commissionPercent = "5% Fee (₹960)",
                    estimatedNet = 16740.0,
                    isBestOption = false,
                    strings = strings
                )
            }

            // Comparison Option 3: Pune (BEST OPTION)
            item {
                MarketComparisonCard(
                    marketName = "Pune Food Processor / Mandi",
                    distanceKm = 115,
                    pricePerKg = 26.0,
                    grossRevenue = 20800.0,
                    transportCost = 1800.0,
                    commissionPercent = "0% Direct KrishiSetu Fee",
                    estimatedNet = 19000.0,
                    isBestOption = true,
                    strings = strings
                )
            }

            item {
                PrimaryKrishiButton(
                    text = strings.viewRecommendedDealBtn,
                    containerColor = FarmerRoleGreen,
                    onClick = onNavigateToBestPlaceToSell
                )
            }
        }
    }
}

@Composable
private fun MarketComparisonCard(
    marketName: String,
    distanceKm: Int,
    pricePerKg: Double,
    grossRevenue: Double,
    transportCost: Double,
    commissionPercent: String,
    estimatedNet: Double,
    isBestOption: Boolean,
    strings: FarmerStrings
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isBestOption) Modifier.border(2.dp, FarmerRoleGreen, RoundedCornerShape(20.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isBestOption) Color(0xFFF1F8E9) else CardSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isBestOption) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = marketName,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Distance: $distanceKm km from farm",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = TextMuted
                    )
                }
                if (isBestOption) {
                    StatusBadge(status = strings.bestValueBadge)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Price per kg", color = TextSecondary)
                Text("₹${pricePerKg.toInt()}/kg", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(strings.grossAmountLabel, color = TextSecondary)
                Text("₹${grossRevenue.toInt()}", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(strings.transportCostLabel, color = TextSecondary)
                Text("₹${transportCost.toInt()}", color = StatusError)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(strings.commissionFeeLabel, color = TextSecondary)
                Text(commissionPercent, color = TextMuted)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderColor)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.estNetLabel,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "₹${estimatedNet.toInt()}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isBestOption) FarmerRoleGreen else TextPrimary
                    )
                )
            }
        }
    }
}
