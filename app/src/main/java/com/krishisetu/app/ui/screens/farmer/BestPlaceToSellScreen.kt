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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.components.VerifiedBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun BestPlaceToSellScreen(
    onBack: () -> Unit,
    onNavigateToDirectBuyer: () -> Unit,
    onNavigateToSellOrWait: () -> Unit
) {
    val strings = FarmerStrings.get()

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.bestPlaceTitle,
                subtitle = "AI & Market Linkage Recommendation",
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
                                text = "Highest Profit Opportunity Identified",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            )
                            Text(
                                text = "Direct buyer procurement saves ₹1,408 mandi commission fee!",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Visually Prominent Recommendation Card
            item {
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
                            VerifiedBadge()
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "FreshFoods Pvt Ltd / Pune Food Processor",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            ),
                            color = TextPrimary
                        )

                        Text(
                            text = "Crop: Tomato · Quantity: 800 kg · Distance: 42 km",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BorderColor)

                        // Financial breakdown
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${strings.grossAmountLabel} (800 kg @ ₹26/kg)", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                            Text("₹20,800", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(strings.transportCostLabel, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                            Text("₹1,800", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = StatusError))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(strings.commissionFeeLabel, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                            Text("₹0 (Direct Deal)", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = StatusSuccess))
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
                                        text = "₹19,000",
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = FarmerRoleGreen,
                                            fontSize = 32.sp
                                        )
                                    )
                                }
                                Text(
                                    text = "+₹2,400\nvs Mandi",
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

            // Comparison against Local Mandi Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Comparison with Satara Local Mandi",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Local Mandi Rate: ₹22/kg · Mandi Net: ₹15,792",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "By selling to Pune Processor, you gain ₹3,208 additional profit!",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                        )
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
                        Text("⏳ ${strings.sellNowOrWaitTitle}", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                    }
                }
            }
        }
    }
}
