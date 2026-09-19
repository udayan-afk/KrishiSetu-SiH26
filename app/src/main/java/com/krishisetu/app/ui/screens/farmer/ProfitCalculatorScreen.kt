package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun ProfitCalculatorScreen(
    onBack: () -> Unit,
    viewModel: KrishiSetuViewModel = KrishiSetuViewModel()
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    var quantityText by remember { mutableStateOf("800") }
    var sellingPriceText by remember { mutableStateOf("26") }
    var transportCostText by remember { mutableStateOf("1800") }
    var storageCostText by remember { mutableStateOf("300") }
    var commissionText by remember { mutableStateOf("0") }
    var otherCostText by remember { mutableStateOf("200") }

    val qty = quantityText.toDoubleOrNull() ?: 0.0
    val price = sellingPriceText.toDoubleOrNull() ?: 0.0
    val transport = transportCostText.toDoubleOrNull() ?: 0.0
    val storage = storageCostText.toDoubleOrNull() ?: 0.0
    val commission = commissionText.toDoubleOrNull() ?: 0.0
    val other = otherCostText.toDoubleOrNull() ?: 0.0

    val grossRevenue = qty * price
    val totalCost = transport + storage + commission + other
    val netRealization = grossRevenue - totalCost
    val netPerKg = if (qty > 0) netRealization / qty else 0.0

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.profitCalcTitle,
                subtitle = "Real-time Net Income Math Engine",
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
            // Live Realtime Calculation Result Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmerRoleGreen),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = strings.netRealizationTitle,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "₹${netRealization.toInt()}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 36.sp
                            )
                        )
                        Text(
                            text = "Effective Rate: ₹${String.format("%.2f", netPerKg)} / kg",
                            style = MaterialTheme.typography.titleMedium.copy(color = AccentGold)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(strings.grossRevenueLabel, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                                Text("₹${grossRevenue.toInt()}", style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(strings.totalExpensesLabel, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                                Text("₹${totalCost.toInt()}", style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFFFFCDD2), fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }

            // Input Fields Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = strings.calcInputsHeader,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)

                        // 1. Quantity
                        CalcInputField(strings.quantityKgLabel, quantityText) { quantityText = it }
                        Spacer(modifier = Modifier.height(12.dp))

                        // 2. Selling Price
                        CalcInputField(strings.expectedPriceLabel, sellingPriceText) { sellingPriceText = it }
                        Spacer(modifier = Modifier.height(12.dp))

                        // 3. Transport
                        CalcInputField(strings.transportCostLabel, transportCostText) { transportCostText = it }
                        Spacer(modifier = Modifier.height(12.dp))

                        // 4. Storage
                        CalcInputField(strings.storageCostLabel, storageCostText) { storageCostText = it }
                        Spacer(modifier = Modifier.height(12.dp))

                        // 5. Commission
                        CalcInputField(strings.commissionFeeLabel, commissionText) { commissionText = it }
                        Spacer(modifier = Modifier.height(12.dp))

                        // 6. Other Cost
                        CalcInputField(strings.handlingCostLabel, otherCostText) { otherCostText = it }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalcInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(130.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = BackgroundWarm,
                unfocusedContainerColor = BackgroundWarm,
                focusedBorderColor = FarmerRoleGreen
            )
        )
    }
}
