package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.GovernmentScheme
import com.krishisetu.app.ui.components.KrishiTopBar
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun GovernmentSchemesScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val schemes by viewModel.schemes.collectAsState()
    var selectedSchemeDialog by remember { mutableStateOf<GovernmentScheme?>(null) }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.schemesTitle,
                subtitle = strings.schemesSubtitle,
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🏛️", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = strings.schemesIntroBanner,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = FarmerRoleGreen
                        )
                    }
                }
            }

            items(schemes, key = { it.id }) { rawScheme ->
                val scheme = strings.localizeScheme(rawScheme)
                val description = strings.schemeDescription(scheme.id)
                val portalUrl = strings.schemePortalUrl(scheme.id)

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
                                text = scheme.title,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusBadge(status = scheme.category)
                        }

                        if (description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "🎁 ${strings.benefitLabel}: ${scheme.benefit}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${strings.eligibilityLabel}:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(text = scheme.eligibility, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${strings.documentsLabel}:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(text = scheme.documentsRequired, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${strings.howToApplyLabel}:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(text = scheme.howToApply, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderColor)

                        Text(
                            text = "${strings.officialSourceLabel}: ${scheme.officialSource}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Text(
                            text = "${strings.officialPortalLabel}: $portalUrl",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = FarmerRoleGreen
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { selectedSchemeDialog = rawScheme },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = strings.applyOrViewSchemeBtn,
                                fontWeight = FontWeight.Bold,
                                color = FarmerRoleGreen
                            )
                        }
                    }
                }
            }

            // Disclaimer Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("ℹ️", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.schemesDisclaimerText,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }

    selectedSchemeDialog?.let { raw ->
        val scheme = strings.localizeScheme(raw)
        val portalUrl = strings.schemePortalUrl(scheme.id)
        AlertDialog(
            onDismissRequest = { selectedSchemeDialog = null },
            title = {
                Text(scheme.title, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${strings.benefitLabel}: ${scheme.benefit}", fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                    Text("${strings.eligibilityLabel}: ${scheme.eligibility}")
                    Text("${strings.documentsLabel}: ${scheme.documentsRequired}")
                    Text("${strings.howToApplyLabel}: ${scheme.howToApply}")
                    Text("${strings.officialPortalLabel}: $portalUrl", fontWeight = FontWeight.Bold, color = BuyerRoleBlue)
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedSchemeDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text(strings.gotItBtn)
                }
            }
        )
    }
}
