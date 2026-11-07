package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.FpoGroup
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.components.SubmissionSuccessDialog
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
    var joinDialogGroup by remember { mutableStateOf<FpoGroup?>(null) }
    var contributionKgText by remember { mutableStateOf("800") }
    var showValidationError by remember { mutableStateOf(false) }
    var confirmedContribution by remember { mutableStateOf<Pair<FpoGroup, Int>?>(null) }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.fpoAggregationTitle,
                subtitle = strings.fpoAggregationSubtitle,
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
                                text = strings.whyJoinFpoTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            )
                            Text(
                                text = strings.whyJoinFpoSub,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            if (fpoGroups.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Text(
                            text = strings.noFpoLotsAvailable,
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            items(fpoGroups, key = { it.id }) { fpo ->
                val progress = (fpo.collectedQuantityKg / fpo.targetQuantityKg).toFloat().coerceIn(0f, 1f)
                val remainingKg = (fpo.targetQuantityKg - fpo.collectedQuantityKg).coerceAtLeast(0.0).toInt()
                val isReady = fpo.collectedQuantityKg >= fpo.targetQuantityKg
                val localizedCrop = strings.commodityName(fpo.cropTarget)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = fpo.name,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusBadge(status = if (isReady) strings.fpoStatusReady else strings.fpoStatusOpen)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = String.format(strings.fpoCropTargetLine, localizedCrop, fpo.memberCount),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = String.format(
                                        strings.fpoTotalQuantityLine,
                                        fpo.collectedQuantityKg.toInt(),
                                        fpo.targetQuantityKg.toInt(),
                                        strings.kgUnit
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = String.format(strings.fpoRemainingQtyLine, remainingKg, strings.kgUnit),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = strings.fpoOfferedPriceLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                                Text(
                                    text = "₹${fpo.offeredPricePerKg.toInt()}/${strings.kgUnit}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = FarmerRoleGreen,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = FarmerRoleGreen,
                            trackColor = BorderColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = strings.fpoAvailableOffersLabel,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = FarmerRoleGreen
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        PrimaryKrishiButton(
                            text = strings.joinFpoBtn,
                            containerColor = FarmerRoleGreen,
                            onClick = {
                                contributionKgText = "800"
                                showValidationError = false
                                joinDialogGroup = fpo
                            }
                        )
                    }
                }
            }
        }
    }

    joinDialogGroup?.let { fpo ->
        val enteredQty = contributionKgText.toDoubleOrNull() ?: 0.0
        val isValidQty = enteredQty in 50.0..10000.0
        val estPayout = (enteredQty * fpo.offeredPricePerKg).toInt()

        AlertDialog(
            onDismissRequest = { joinDialogGroup = null },
            title = {
                Text(strings.joinFpoDialogTitle, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(String.format(strings.joinFpoQtyPrompt, fpo.name))
                    OutlinedTextField(
                        value = contributionKgText,
                        onValueChange = {
                            contributionKgText = it
                            showValidationError = false
                        },
                        label = { Text(strings.quantityKgLabel) },
                        isError = showValidationError && !isValidQty,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (showValidationError && !isValidQty) {
                        Text(
                            text = strings.joinFpoValidationError,
                            style = MaterialTheme.typography.bodySmall,
                            color = StatusError
                        )
                    }
                    if (enteredQty > 0) {
                        Text(
                            text = String.format(
                                strings.joinFpoEstPayout,
                                fpo.offeredPricePerKg.toInt().toString(),
                                strings.kgUnit,
                                estPayout
                            ),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = FarmerRoleGreen
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (!isValidQty) {
                            showValidationError = true
                        } else {
                            viewModel.joinFpo(fpo.id, enteredQty)
                            joinDialogGroup = null
                            confirmedContribution = fpo to enteredQty.toInt()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text(strings.confirmJoinFpoBtn)
                }
            },
            dismissButton = {
                TextButton(onClick = { joinDialogGroup = null }) {
                    Text(strings.cancel)
                }
            }
        )
    }

    confirmedContribution?.let { (fpo, qty) ->
        SubmissionSuccessDialog(
            title = strings.joinFpoSuccessTitle,
            message = String.format(
                strings.joinFpoSuccessMsg,
                qty,
                strings.kgUnit,
                strings.commodityName(fpo.cropTarget),
                fpo.name
            ),
            buttonText = strings.gotItBtn,
            onDismiss = { confirmedContribution = null }
        )
    }
}
