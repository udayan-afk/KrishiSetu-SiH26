package com.krishisetu.app.ui.screens.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import com.krishisetu.app.models.AdminBuyerRecord
import com.krishisetu.app.models.AdminFarmerRecord
import com.krishisetu.app.models.MarketFeedStatusRecord
import com.krishisetu.app.ui.components.*
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    viewModel: KrishiSetuViewModel,
    onNavigateToFarmerVerification: (AdminFarmerRecord) -> Unit,
    onNavigateToBuyerVerification: (AdminBuyerRecord) -> Unit,
    onNavigateToDisputes: () -> Unit,
    onNavigateToSchemes: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Overview", "Farmers", "Buyers", "Market", "More")

    val farmers by viewModel.adminFarmers.collectAsState()
    val buyers by viewModel.adminBuyers.collectAsState()
    val marketFeeds by viewModel.marketFeeds.collectAsState()
    val disputes by viewModel.disputes.collectAsState()
    val schemes by viewModel.schemes.collectAsState()

    var showNotificationToast by remember { mutableStateOf(false) }

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
                                color = AdminRoleDark
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showNotificationToast = true }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = AdminRoleDark)
                    }
                    Spacer(modifier = Modifier.width(4.dp))

                    // Admin Avatar Circle "AD" -> opens Admin Profile
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AdminRoleDark)
                            .clickable(onClick = onNavigateToProfile),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AD",
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
                                0 -> "📊"
                                1 -> "👨‍🌾"
                                2 -> "🏢"
                                3 -> "📈"
                                4 -> "⚙️"
                                else -> "🛠️"
                            }
                            Text(emoji, fontSize = 20.sp)
                        },
                        label = {
                            Text(
                                title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AdminRoleDark else TextMuted
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
                0 -> AdminOverviewDashboardTab(
                    onJumpToVerifications = { selectedTab = 2 },
                    onJumpToDisputes = onNavigateToDisputes,
                    onJumpToMarket = { selectedTab = 3 }
                )
                1 -> AdminFarmerManagementTab(
                    farmers = farmers,
                    onSelectFarmer = onNavigateToFarmerVerification
                )
                2 -> AdminBuyerVerificationsTab(
                    viewModel = viewModel,
                    buyers = buyers,
                    onSelectBuyer = onNavigateToBuyerVerification
                )
                3 -> AdminMarketDataStatusTab(
                    viewModel = viewModel,
                    marketFeeds = marketFeeds
                )
                4 -> AdminMoreOptionsTab(
                    onNavigateToSchemes = onNavigateToSchemes,
                    onNavigateToDisputes = onNavigateToDisputes,
                    onNavigateToMarket = { selectedTab = 3 },
                    onNavigateToProfile = onNavigateToProfile
                )
            }
        }
    }

    if (showNotificationToast) {
        AlertDialog(
            onDismissRequest = { showNotificationToast = false },
            title = { Text("Admin Alerts & System Notifications", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🔔 12 pending Buyer document verifications waiting.")
                    Text("⚠️ Satara APMC price feed delayed by 4 hours.")
                    Text("⚖️ New dispute DIS-0012 raised by Vikram Sonawane.")
                }
            },
            confirmButton = {
                Button(onClick = { showNotificationToast = false }, colors = ButtonDefaults.buttonColors(containerColor = AdminRoleDark)) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }
}

// ----------------------------------------------------
// TAB 0: OVERVIEW DASHBOARD
// ----------------------------------------------------
@Composable
private fun AdminOverviewDashboardTab(
    onJumpToVerifications: () -> Unit,
    onJumpToDisputes: () -> Unit,
    onJumpToMarket: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 88.dp)
    ) {
        // Top Action Chips Row matching Figma
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AlertChip(
                    text = "12 Verifications",
                    containerColor = Color(0xFFFFF3E0),
                    textColor = StatusWarning,
                    onClick = onJumpToVerifications,
                    modifier = Modifier.weight(1f)
                )
                AlertChip(
                    text = "3 Disputes",
                    containerColor = Color(0xFFFFEBEE),
                    textColor = StatusError,
                    onClick = onJumpToDisputes,
                    modifier = Modifier.weight(1f)
                )
                AlertChip(
                    text = "2 Data Issues",
                    containerColor = Color(0xFFE3F2FD),
                    textColor = BuyerRoleBlue,
                    onClick = onJumpToMarket,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Platform Statistics Grid matching Figma
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricStatCard("Total Farmers", "2,847", valueColor = FarmerRoleGreen, modifier = Modifier.weight(1f))
                    MetricStatCard("Verified Buyers", "148", valueColor = BuyerRoleBlue, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricStatCard("Active Lots", "412", valueColor = PrimaryGreen, modifier = Modifier.weight(1f))
                    MetricStatCard("Transactions", "1,284", valueColor = AccentGold, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricStatCard("Transaction Value", "₹4.8 Cr", valueColor = AdminRoleDark, modifier = Modifier.weight(1f))
                    MetricStatCard("Disputes Open", "3", valueColor = StatusError, modifier = Modifier.weight(1f))
                }
            }
        }

        // Analytics Chart 1: Transaction Volume (7 Days Bar Chart)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Transaction Volume (7 Days)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    val values = listOf(42, 58, 51, 67, 73, 88, 95)
                    val maxVal = 100

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        values.forEachIndexed { index, valInt ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "$valInt",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AdminRoleDark,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .height(80.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.55f)
                                            .fillMaxHeight(valInt.toFloat() / maxVal)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(FarmerRoleGreen)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = days[index],
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Analytics Chart 2: Crop Demand by Volume
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Crop Demand by Volume (Tonnes)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val crops = listOf(
                        Pair("Tomato", "5k kg"),
                        Pair("Onion", "3k kg"),
                        Pair("Potato", "3k kg"),
                        Pair("Wheat", "2k kg"),
                        Pair("Maize", "1k kg"),
                        Pair("Other", "900 kg")
                    )

                    crops.forEach { (crop, vol) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(crop, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, modifier = Modifier.width(70.dp))
                            LinearProgressIndicator(
                                progress = {
                                    when (crop) {
                                        "Tomato" -> 0.9f
                                        "Onion" -> 0.6f
                                        "Potato" -> 0.6f
                                        "Wheat" -> 0.4f
                                        "Maize" -> 0.25f
                                        else -> 0.15f
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = AccentGold,
                                trackColor = BackgroundWarm
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(vol, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = TextMuted)
                        }
                    }
                }
            }
        }

        // Analytics Chart 3: Payment Completion (%)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Payment Completion (%)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val weeks = listOf("W1", "W2", "W3", "W4", "W5", "W6", "W7")
                    val percentages = listOf(88, 91, 89, 94, 93, 96, 97)
                    val maxVal = 100

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        percentages.forEachIndexed { index, pct ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "$pct%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BuyerRoleBlue,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .height(80.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.55f)
                                            .fillMaxHeight(pct.toFloat() / maxVal)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(BuyerRoleBlue)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = weeks[index],
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Analytics Chart 4: New Farmer Registrations
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "New Farmer Registrations",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val months = listOf("Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                    val regValues = listOf(89, 112, 98, 127, 134, 118, 145)
                    val maxVal = 150

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        regValues.forEachIndexed { index, regVal ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "$regVal",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7B1FA2),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .height(80.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.55f)
                                            .fillMaxHeight(regVal.toFloat() / maxVal)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(Color(0xFF7B1FA2))
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = months[index],
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertChip(
    text: String,
    containerColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold, color = textColor, fontSize = 12.sp)
            )
        }
    }
}

// ----------------------------------------------------
// TAB 1: FARMER MANAGEMENT
// ----------------------------------------------------
@Composable
private fun AdminFarmerManagementTab(
    farmers: List<AdminFarmerRecord>,
    onSelectFarmer: (AdminFarmerRecord) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterChip by remember { mutableStateOf("All") }

    val filteredFarmers = farmers.filter { farmer ->
        val matchesQuery = farmer.name.contains(searchQuery, ignoreCase = true) || farmer.location.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilterChip) {
            "Maharashtra" -> farmer.state.contains("Maharashtra", ignoreCase = true) || farmer.location.contains("MH", ignoreCase = true)
            "Gujarat" -> farmer.state.contains("Gujarat", ignoreCase = true) || farmer.location.contains("GJ", ignoreCase = true)
            "Rajasthan" -> farmer.state.contains("Rajasthan", ignoreCase = true)
            else -> true
        }
        matchesQuery && matchesFilter
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 88.dp)
    ) {
        item {
            Text("Farmer Management", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search farmers...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )
        }

        // Filter Chips Row
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("All", "Maharashtra", "Gujarat", "Rajasthan").forEach { chipLabel ->
                    FilterChip(
                        selected = (selectedFilterChip == chipLabel),
                        onClick = { selectedFilterChip = chipLabel },
                        label = { Text(chipLabel, fontSize = 12.sp) }
                    )
                }
            }
        }

        // Stats Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricStatCard("Total Registered", "2,847", valueColor = FarmerRoleGreen, modifier = Modifier.weight(1f))
                MetricStatCard("Active Month", "1,203", valueColor = PrimaryGreen, modifier = Modifier.weight(1f))
            }
        }

        // Farmer List Cards
        items(filteredFarmers) { farmer ->
            Card(
                onClick = { onSelectFarmer(farmer) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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
                        Text(farmer.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("${farmer.location} · ${farmer.activeLotsCount} active lots", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                    StatusBadge(status = farmer.status)
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 2: BUYER VERIFICATIONS
// ----------------------------------------------------
@Composable
private fun AdminBuyerVerificationsTab(
    viewModel: KrishiSetuViewModel,
    buyers: List<AdminBuyerRecord>,
    onSelectBuyer: (AdminBuyerRecord) -> Unit
) {
    val pendingCount = buyers.count { it.status == "Pending" }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 88.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Buyer Verifications", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFF3E0)
                ) {
                    Text(
                        text = "$pendingCount Pending",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = StatusWarning),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        items(buyers) { buyer ->
            Card(
                onClick = { onSelectBuyer(buyer) },
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
                        Text(buyer.organisationName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold))
                        StatusBadge(status = buyer.status)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${buyer.businessType} · ${buyer.location}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Text("${buyer.appliedDate} · Docs: ${buyer.docsSubmittedFraction} · Risk: ${buyer.riskLevel}", style = MaterialTheme.typography.bodySmall.copy(color = BuyerRoleBlue, fontWeight = FontWeight.Bold))

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.rejectAdminBuyer(buyer.id, "Invalid Document") },
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusError)
                        ) {
                            Text("Reject", fontWeight = FontWeight.Bold, color = StatusError)
                        }

                        Button(
                            onClick = { viewModel.approveAdminBuyer(buyer.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen),
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Approve ✓", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// TAB 3: MARKET DATA STATUS
// ----------------------------------------------------
@Composable
private fun AdminMarketDataStatusTab(
    viewModel: KrishiSetuViewModel,
    marketFeeds: List<MarketFeedStatusRecord>
) {
    var showRefreshToast by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 88.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Market Data Status", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
                IconButton(onClick = {
                    viewModel.forceRefreshMarketFeed("ALL")
                    showRefreshToast = true
                }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh All", tint = AdminRoleDark)
                }
            }
        }

        items(marketFeeds) { feed ->
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
                        Text(feed.apmcName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold))
                        StatusBadge(status = feed.status)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${feed.lastFetchText} · ${feed.recordsCount} records", style = MaterialTheme.typography.bodySmall, color = TextMuted)

                    if (feed.status != "Live") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.forceRefreshMarketFeed(feed.id)
                                showRefreshToast = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AdminRoleDark),
                            modifier = Modifier.fillMaxWidth().height(38.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Force Refresh", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showRefreshToast) {
        AlertDialog(
            onDismissRequest = { showRefreshToast = false },
            title = { Text("Market Data Refreshed", fontWeight = FontWeight.Bold, color = FarmerRoleGreen) },
            text = { Text("Market price data feeds have been updated successfully.", color = TextSecondary) },
            confirmButton = {
                Button(onClick = { showRefreshToast = false }, colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }
}

// ----------------------------------------------------
// TAB 4: MORE OPTIONS
// ----------------------------------------------------
@Composable
private fun AdminMoreOptionsTab(
    onNavigateToSchemes: () -> Unit,
    onNavigateToDisputes: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 88.dp)
    ) {
        item {
            Text("More Admin Tools", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
        }

        item {
            AdminMoreOptionCard(
                title = "Government Schemes",
                subtitle = "18 active schemes · Information management",
                icon = Icons.Default.AccountBalance,
                onClick = onNavigateToSchemes
            )
        }

        item {
            AdminMoreOptionCard(
                title = "Disputes",
                subtitle = "3 open cases · Dispute resolution panel",
                icon = Icons.Default.Gavel,
                onClick = onNavigateToDisputes
            )
        }

        item {
            AdminMoreOptionCard(
                title = "Market Data Management",
                subtitle = "5 feeds · APMC status & synchronization",
                icon = Icons.Default.Refresh,
                onClick = onNavigateToMarket
            )
        }

        item {
            AdminMoreOptionCard(
                title = "Admin Profile & Settings",
                subtitle = "Super Admin account & platform configurations",
                icon = Icons.Default.Settings,
                onClick = onNavigateToProfile
            )
        }
    }
}

@Composable
private fun AdminMoreOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BackgroundWarm,
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = AdminRoleDark, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }
    }
}
