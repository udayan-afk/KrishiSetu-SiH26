package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun FpoAggregationScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val fpoGroups by viewModel.fpoGroups.collectAsState()

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.fpoAggregationTitle,
                subtitle = "Aggregate Quantity for Bulk Price Power",
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
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🤝", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Why Join an FPO Lot?",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            )
                            Text(
                                text = "Bulk supply deals get ₹2-₹3 higher per kg from large corporate processors!",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            items(fpoGroups) { fpo ->
                val progress = (fpo.collectedQuantityKg / fpo.targetQuantityKg).toFloat().coerceIn(0f, 1f)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = fpo.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Crop Target: ${fpo.cropTarget} · Members Joined: ${fpo.memberCount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Progress: ${fpo.collectedQuantityKg.toInt()} / ${fpo.targetQuantityKg.toInt()} kg", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text("₹${fpo.offeredPricePerKg}/kg", style = MaterialTheme.typography.titleMedium.copy(color = FarmerRoleGreen, fontWeight = FontWeight.ExtraBold))
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = FarmerRoleGreen,
                            trackColor = BorderColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PrimaryKrishiButton(
                            text = strings.joinFpoBtn,
                            containerColor = FarmerRoleGreen,
                            onClick = {
                                viewModel.joinFpo(fpo.id, 800.0)
                            }
                        )
                    }
                }
            }
        }
    }
}
