package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.theme.*

@Composable
fun MapMockScreen(
    onBack: () -> Unit
) {
    val strings = FarmerStrings.get()
    var selectedPinInfo by remember { mutableStateOf("Tap any pin on the map to inspect details.") }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.mapTitle,
                subtitle = "Satara / Karad / Pune Region",
                onBackClick = onBack
            )
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Interactive Mock Canvas Map Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE8ECE9)),
                contentAlignment = Alignment.Center
            ) {
                // Map Background grid
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid lines simulating map streets/topography
                    for (i in 1..5) {
                        drawLine(Color(0xFFCFD8DC), Offset(0f, h * i / 6), Offset(w, h * i / 6), strokeWidth = 2f)
                        drawLine(Color(0xFFCFD8DC), Offset(w * i / 6, 0f), Offset(w * i / 6, h), strokeWidth = 2f)
                    }

                    // Highway road (Satara to Pune)
                    drawLine(Color(0xFF90A4AE), Offset(w * 0.2f, h * 0.8f), Offset(w * 0.8f, h * 0.2f), strokeWidth = 12f)
                }

                // Interactive Pin 1: Farmer Location
                Box(
                    modifier = Modifier
                        .offset(x = (-80).dp, y = 120.dp)
                        .clickable { selectedPinInfo = "📍 My Farm (Khed, Satara)\nHarvest Lot: 800 kg Tomato Available" }
                ) {
                    MapPinItem("🌾", "My Farm", FarmerRoleGreen)
                }

                // Interactive Pin 2: Satara Mandi
                Box(
                    modifier = Modifier
                        .offset(x = (-40).dp, y = 60.dp)
                        .clickable { selectedPinInfo = "🏛️ Satara APMC Mandi (8 km)\nTomato Modal Price: ₹22/kg" }
                ) {
                    MapPinItem("🏛️", "Satara Mandi", AccentGold)
                }

                // Interactive Pin 3: Karad APMC
                Box(
                    modifier = Modifier
                        .offset(x = (-90).dp, y = (-80).dp)
                        .clickable { selectedPinInfo = "🏭 Karad APMC (55 km)\nTomato Modal Price: ₹24/kg" }
                ) {
                    MapPinItem("🏭", "Karad APMC", AccentGold)
                }

                // Interactive Pin 4: FreshFoods Buyer (Pune)
                Box(
                    modifier = Modifier
                        .offset(x = 60.dp, y = (-110).dp)
                        .clickable { selectedPinInfo = "🏢 FreshFoods Pvt Ltd (Pune - 42 km)\nProcurement Requirement: 5,000 kg @ ₹26/kg" }
                ) {
                    MapPinItem("🏢", "FreshFoods (Buyer)", BuyerRoleBlue)
                }

                // Interactive Pin 5: Transport Hub
                Box(
                    modifier = Modifier
                        .offset(x = 30.dp, y = 40.dp)
                        .clickable { selectedPinInfo = "🚛 Satara Logistics Transport Stand\nBolero Pickup Available (₹1,700)" }
                ) {
                    MapPinItem("🚛", "Transport Stand", AdminRoleDark)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Card for Selected Pin
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Selected Map Node Info",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = selectedPinInfo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun MapPinItem(emoji: String, label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}
