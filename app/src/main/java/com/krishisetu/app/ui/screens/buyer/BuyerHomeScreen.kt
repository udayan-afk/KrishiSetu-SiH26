package com.krishisetu.app.ui.screens.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
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
import com.krishisetu.app.models.BuyerDeliveryRecord
import com.krishisetu.app.models.BuyerLot
import com.krishisetu.app.models.BuyerRequirement
import com.krishisetu.app.ui.components.*
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerMainContainerScreen(
    viewModel: KrishiSetuViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToVerification: () -> Unit,
    onNavigateToTracking: (BuyerDeliveryRecord) -> Unit = {}
) {
    val buyerProfile by viewModel.currentBuyerProfile.collectAsState()
    val buyerRequirements by viewModel.buyerRequirements.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Overview", "Lots", "Offers", "Deliveries", "Payments")

    var showGatingDialog by remember { mutableStateOf(false) }
    var showNewRequirementDialog by remember { mutableStateOf(false) }
    var showNotificationToast by remember { mutableStateOf(false) }
    var selectedLotForOfferModal by remember { mutableStateOf<BuyerLot?>(null) }
    var selectedRequirementForDetail by remember { mutableStateOf<BuyerRequirement?>(null) }

    // Gating check wrapper function
    fun executeGatedAction(action: () -> Unit) {
        if (!buyerProfile.isVerified) {
            showGatingDialog = true
        } else {
            action()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        KrishiSetuLogoIcon(modifier = Modifier.size(26.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "KrishiSetu",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryGreen
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showNotificationToast = true }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = PrimaryGreen)
                    }
                    Spacer(modifier = Modifier.width(4.dp))

                    // Buyer Avatar Circle "FF" -> opens Profile
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FarmerRoleGreen)
                            .clickable(onClick = onNavigateToProfile),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "FF",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWarm)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = CardSurface) {
                tabTitles.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = {
                            val emoji = when (index) {
                                0 -> "🏠"
                                1 -> "📦"
                                2 -> "💬"
                                3 -> "🚚"
                                4 -> "💳"
                                else -> "📱"
                            }
                            Text(emoji, fontSize = 20.sp)
                        },
                        label = {
                            Text(
                                title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) FarmerRoleGreen else TextMuted
                            )
                        }
                    )
                }
            }
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            when (selectedTab) {
                0 -> BuyerOverviewTab(
                    viewModel = viewModel,
                    onOpenNewRequirementGated = {
                        executeGatedAction { showNewRequirementDialog = true }
                    },
                    onViewRequirementDetail = { req -> selectedRequirementForDetail = req }
                )
                1 -> BuyerLotsScreen(
                    viewModel = viewModel,
                    onMakeOfferGated = { lot ->
                        executeGatedAction { selectedLotForOfferModal = lot }
                    }
                )
                2 -> BuyerOffersScreen(
                    viewModel = viewModel,
                    onActionGated = { action -> executeGatedAction(action) }
                )
                3 -> BuyerDeliveriesScreen(
                    viewModel = viewModel,
                    onTrackLive = onNavigateToTracking
                )
                4 -> BuyerPaymentsScreen(
                    viewModel = viewModel
                )
            }
        }
    }

    // Gating Access Control Dialog
    if (showGatingDialog) {
        BuyerGatingDialog(
            onDismiss = { showGatingDialog = false },
            onVerifyNow = {
                showGatingDialog = false
                onNavigateToVerification()
            }
        )
    }

    // New Requirement Form Modal
    if (showNewRequirementDialog) {
        var cropName by remember { mutableStateOf("Tomato") }
        var targetQty by remember { mutableStateOf("5000") }
        var targetPrice by remember { mutableStateOf("26") }
        var location by remember { mutableStateOf("Pune Processor Hub") }

        AlertDialog(
            onDismissRequest = { showNewRequirementDialog = false },
            title = { Text("+ New Requirement", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = cropName,
                        onValueChange = { cropName = it },
                        label = { Text("Crop Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = targetQty,
                        onValueChange = { targetQty = it },
                        label = { Text("Target Quantity (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = targetPrice,
                        onValueChange = { targetPrice = it },
                        label = { Text("Target Price (₹/kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Delivery Location") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val qty = targetQty.toDoubleOrNull() ?: 5000.0
                        val price = targetPrice.toDoubleOrNull() ?: 26.0
                        viewModel.createBuyerRequirement(cropName, qty, price, location, "By Dec 25")
                        showNewRequirementDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text("Post Requirement")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewRequirementDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }

    // Make Offer Modal Dialog
    selectedLotForOfferModal?.let { lot ->
        var offerPrice by remember { mutableStateOf("${lot.askingPricePerKg.toInt()}") }

        AlertDialog(
            onDismissRequest = { selectedLotForOfferModal = null },
            title = { Text("Make Offer to ${lot.farmerName}", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Submitting offer for ${lot.cropName} (${lot.quantityKg.toInt()} kg):", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = offerPrice,
                        onValueChange = { offerPrice = it },
                        label = { Text("Offered Price (₹/kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val price = offerPrice.toDoubleOrNull() ?: lot.askingPricePerKg
                        viewModel.makeOfferOnBuyerLot(lot, price)
                        selectedLotForOfferModal = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text("Submit Offer")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedLotForOfferModal = null }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }

    // View Requirement Detail Modal
    selectedRequirementForDetail?.let { req ->
        AlertDialog(
            onDismissRequest = { selectedRequirementForDetail = null },
            title = { Text("${req.cropName} Requirement", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Target Quantity: ${req.requiredQuantityKg.toInt()} kg", fontWeight = FontWeight.SemiBold)
                    Text("Target Rate: ₹${req.pricePerKg.toInt()}/kg", fontWeight = FontWeight.SemiBold, color = FarmerRoleGreen)
                    Text("Delivery Location: ${req.location}")
                    Text("Procurement Deadline: ${req.deadline}")
                }
            },
            confirmButton = {
                Button(onClick = { selectedRequirementForDetail = null }, colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }

    // Notification Toast Dialog
    if (showNotificationToast) {
        AlertDialog(
            onDismissRequest = { showNotificationToast = false },
            title = { Text("Notifications", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🔔 2 new farmer offers received for Tomato lot KL-TOM-58291.")
                    Text("🚚 Santosh Transport ETA updated to 4 PM for Onion shipment.")
                }
            },
            confirmButton = {
                Button(onClick = { showNotificationToast = false }, colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }
}

@Composable
private fun BuyerOverviewTab(
    viewModel: KrishiSetuViewModel,
    onOpenNewRequirementGated: () -> Unit,
    onViewRequirementDetail: (BuyerRequirement) -> Unit
) {
    val buyerProfile by viewModel.currentBuyerProfile.collectAsState()
    val buyerRequirements by viewModel.buyerRequirements.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 88.dp)
    ) {
        // 1. Company Card matching Figma
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            text = buyerProfile.companyName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (buyerProfile.isVerified) {
                                VerifiedBadge(text = "✓ Verified Buyer")
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFFFF3E0)
                                ) {
                                    Text(
                                        text = "Unverified Buyer",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = StatusWarning),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${buyerProfile.city}, ${buyerProfile.state}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        }
                    }
                }
            }
        }

        // 2. Primary CTA: "+ New Requirement" button
        item {
            Button(
                onClick = onOpenNewRequirementGated,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "+ New Requirement",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                )
            }
        }

        // 3. Statistics Grid matching Figma (2 Active Requirements, 18 Available Lots, 3 Pending Offers, 1 Deliveries Today)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricStatCard(
                        label = "Active Requirements",
                        value = "${buyerRequirements.size}",
                        valueColor = FarmerRoleGreen,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        label = "Available Lots",
                        value = "18",
                        valueColor = PrimaryGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricStatCard(
                        label = "Pending Offers",
                        value = "3",
                        valueColor = AccentGold,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        label = "Deliveries Today",
                        value = "1",
                        valueColor = StatusInfo,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. Active Requirements Section matching Figma
        item {
            Text(
                text = "Active Requirements",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = TextPrimary
            )
        }

        // Card 1: Tomato | Grade A · ₹26/kg | 2,000 / 5,000 kg | By Dec 15 | Active
        item {
            RequirementOverviewCard(
                cropName = "Tomato",
                gradePrice = "Grade A · ₹26/kg",
                fulfilledText = "2,000 / 5,000 kg",
                progress = 0.4f,
                deadline = "By Dec 15",
                status = "Active",
                onViewClick = {
                    buyerRequirements.firstOrNull { it.cropName == "Tomato" }?.let(onViewRequirementDetail)
                }
            )
        }

        // Card 2: Potato | Grade A · ₹14/kg | 2,000 / 2,000 kg | By Dec 20 | Fulfilled
        item {
            RequirementOverviewCard(
                cropName = "Potato",
                gradePrice = "Grade A · ₹14/kg",
                fulfilledText = "2,000 / 2,000 kg",
                progress = 1.0f,
                deadline = "By Dec 20",
                status = "Fulfilled",
                onViewClick = {
                    buyerRequirements.firstOrNull { it.cropName == "Potato" }?.let(onViewRequirementDetail)
                        ?: onViewRequirementDetail(
                            BuyerRequirement("REQ-MOCK", "B-201", "FreshFoods Pvt Ltd", "Potato", 2000.0, 14.0, "Pune", 42, "By Dec 20", true)
                        )
                }
            )
        }
    }
}

@Composable
private fun RequirementOverviewCard(
    cropName: String,
    gradePrice: String,
    fulfilledText: String,
    progress: Float,
    deadline: String,
    status: String,
    onViewClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = cropName, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = gradePrice, style = MaterialTheme.typography.bodyMedium.copy(color = FarmerRoleGreen, fontWeight = FontWeight.Bold))
                }
                StatusBadge(status = status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fulfillment Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Fulfilled: $fulfilledText", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontWeight = FontWeight.Medium))
                Text(text = deadline, style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = FarmerRoleGreen,
                trackColor = FarmerRoleBg
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = onViewClick,
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarmerRoleGreen)
            ) {
                Text("View Requirement", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen))
            }
        }
    }
}
