package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.AppLanguage
import com.krishisetu.app.models.QualityGrade
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun CreateLotWizardScreen(
    viewModel: KrishiSetuViewModel,
    onLotCreated: () -> Unit,
    onBack: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    var step by remember { mutableStateOf(1) }

    var selectedCrop by remember { mutableStateOf("Tomato") }
    var quantityText by remember { mutableStateOf("800") }
    var expectedPriceText by remember { mutableStateOf("26") }

    // Mutually exclusive single-choice quality grade state
    var selectedQualityGrade by remember { mutableStateOf<QualityGrade>(QualityGrade.GRADE_A) }
    // Separate organic certification boolean
    var isOrganicCertified by remember { mutableStateOf(false) }

    val crops = listOf("Tomato 🍅", "Onion 🧅", "Potato 🥔", "Wheat 🌾", "Soybean 🫘")

    val qualityGradesList = listOf(
        QualityGrade.GRADE_A,
        QualityGrade.GRADE_B,
        QualityGrade.GRADE_C
    )

    // Validation: Step 3 requires valid selectedQualityGrade != NONE
    val canContinue = when (step) {
        1 -> selectedCrop.isNotBlank()
        2 -> (quantityText.toDoubleOrNull() ?: 0.0) > 0 && (expectedPriceText.toDoubleOrNull() ?: 0.0) > 0
        3 -> selectedQualityGrade != QualityGrade.NONE
        4 -> true
        5 -> true
        else -> true
    }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.createLotWizardTitle,
                subtitle = String.format(strings.stepProgress, step),
                onBackClick = {
                    if (step > 1) step-- else onBack()
                }
            )
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Step Progress Bar
                LinearProgressIndicator(
                    progress = { step / 5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = FarmerRoleGreen,
                    trackColor = BorderColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                when (step) {
                    1 -> {
                        Text(
                            text = strings.step1Title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        crops.forEach { crop ->
                            val rawCropName = crop.split(" ")[0]
                            val isSelected = selectedCrop == rawCropName
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { selectedCrop = rawCropName },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) FarmerRoleBg else CardSurface
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, FarmerRoleGreen) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = crop,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = FarmerRoleGreen
                                        )
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        Text(
                            text = strings.step2Title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = strings.quantityKgLabel,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = { quantityText = it },
                            placeholder = { Text("e.g. 800") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FarmerRoleGreen)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = strings.expectedPriceLabel,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = expectedPriceText,
                            onValueChange = { expectedPriceText = it },
                            placeholder = { Text("e.g. 26") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FarmerRoleGreen)
                        )
                    }

                    // STEP 3: MUTUALLY EXCLUSIVE QUALITY SELECTION (FIXED)
                    3 -> {
                        Text(
                            text = strings.step3Title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = strings.qualityGradeHeader,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Single Choice Mutually Exclusive Cards
                        qualityGradesList.forEach { grade ->
                            val isSelected = selectedQualityGrade == grade
                            val gradeLabel = when (currentLang) {
                                AppLanguage.MARATHI -> grade.labelMr
                                AppLanguage.HINDI -> grade.labelHi
                                AppLanguage.ENGLISH -> grade.labelEn
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { selectedQualityGrade = grade },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) FarmerRoleBg else CardSurface
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, FarmerRoleGreen) else androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { selectedQualityGrade = grade },
                                            colors = RadioButtonDefaults.colors(selectedColor = FarmerRoleGreen)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = gradeLabel,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = FarmerRoleGreen)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Separate Organic Certification Toggle
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isOrganicCertified = !isOrganicCertified },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isOrganicCertified) Color(0xFFFFF8E1) else CardSurface
                            ),
                            border = if (isOrganicCertified) androidx.compose.foundation.BorderStroke(2.dp, AccentGold) else androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🌿 ${strings.organicToggleLabel}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = strings.organicToggleSub,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                        color = TextSecondary
                                    )
                                }
                                Switch(
                                    checked = isOrganicCertified,
                                    onCheckedChange = { isOrganicCertified = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = Color(0xFFFFE082))
                                )
                            }
                        }
                    }

                    4 -> {
                        Text(
                            text = strings.step4Title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = strings.photosSubtext,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .border(2.dp, BorderColor, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "📸", fontSize = 40.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "3 Photos Selected (Mock)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                                )
                                Text(
                                    text = "Tap to capture or change photos",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    5 -> {
                        Text(
                            text = strings.step5Title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = strings.reviewSummaryHeader,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(strings.selectCropLabel, color = TextSecondary)
                                    Text(selectedCrop, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(strings.quantityKgLabel, color = TextSecondary)
                                    Text("$quantityText kg", fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(strings.qualityGradeHeader, color = TextSecondary)
                                    val gradeLabel = when (currentLang) {
                                        AppLanguage.MARATHI -> selectedQualityGrade.labelMr
                                        AppLanguage.HINDI -> selectedQualityGrade.labelHi
                                        AppLanguage.ENGLISH -> selectedQualityGrade.labelEn
                                    }
                                    Text(gradeLabel, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(strings.organicToggleLabel, color = TextSecondary)
                                    Text(if (isOrganicCertified) "✓ Yes" else "No", fontWeight = FontWeight.Bold, color = if (isOrganicCertified) AccentGold else TextPrimary)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(strings.expectedPriceLabel, color = TextSecondary)
                                    Text("₹$expectedPriceText / kg", fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(strings.grossAmountLabel, color = TextSecondary)
                                    val total = (quantityText.toDoubleOrNull() ?: 0.0) * (expectedPriceText.toDoubleOrNull() ?: 0.0)
                                    Text("₹${total.toInt()}", fontWeight = FontWeight.ExtraBold, color = FarmerRoleGreen)
                                }
                            }
                        }
                    }
                }
            }

            PrimaryKrishiButton(
                text = if (step < 5) "${strings.continueButton} →" else strings.createLotNowBtn,
                enabled = canContinue,
                containerColor = FarmerRoleGreen,
                onClick = {
                    if (step < 5) {
                        step++
                    } else {
                        val qty = quantityText.toDoubleOrNull() ?: 800.0
                        val price = expectedPriceText.toDoubleOrNull() ?: 26.0
                        viewModel.createCropLot(selectedCrop, qty, selectedQualityGrade, isOrganicCertified, price)
                        onLotCreated()
                    }
                }
            )
        }
    }
}
