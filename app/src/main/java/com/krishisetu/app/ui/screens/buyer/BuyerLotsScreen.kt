package com.krishisetu.app.ui.screens.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.models.BuyerLot
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun BuyerLotsScreen(
    viewModel: KrishiSetuViewModel,
    onMakeOfferGated: (BuyerLot) -> Unit
) {
    val buyerLots by viewModel.buyerLots.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCropFilter by remember { mutableStateOf("All") }

    val crops = listOf("All", "Tomato", "Onion", "Potato")

    val filteredLots = buyerLots.filter { lot ->
        (selectedCropFilter == "All" || lot.cropName.equals(selectedCropFilter, ignoreCase = true)) &&
                (lot.cropName.contains(searchQuery, ignoreCase = true) ||
                        lot.farmerName.contains(searchQuery, ignoreCase = true) ||
                        lot.lotCode.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search & Filter Header
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search farmer lots, crops, or lot ID...") },
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

        Spacer(modifier = Modifier.height(10.dp))

        // Crop Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            crops.forEach { crop ->
                val isSelected = selectedCropFilter == crop
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCropFilter = crop },
                    label = { Text(crop, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FarmerRoleGreen,
                        selectedLabelColor = Color.White,
                        containerColor = CardSurface
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            items(filteredLots) { lot ->
                BuyerLotCard(
                    lot = lot,
                    onMakeOfferClick = { onMakeOfferGated(lot) }
                )
            }
        }
    }
}

@Composable
fun BuyerLotCard(
    lot: BuyerLot,
    onMakeOfferClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Farmer Name & Lot Code & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = lot.farmerName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = TextPrimary
                    )
                    Text(
                        text = lot.lotCode,
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }
                StatusBadge(status = lot.status)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Details Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Crop & Volume", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                    Text("${lot.cropName} · ${lot.quantityKg.toInt()} kg", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Grade & Distance", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                    Text("${lot.qualityGrade} · ${lot.locationDistance}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Asking Rate", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                    Text("₹${lot.askingPricePerKg.toInt()}/kg", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = FarmerRoleGreen))
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = FarmerRoleBg
                ) {
                    Text(
                        text = "${lot.reliabilityScorePercent}% Reliable",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            PrimaryKrishiButton(
                text = "Make Offer",
                containerColor = FarmerRoleGreen,
                onClick = onMakeOfferClick
            )
        }
    }
}
