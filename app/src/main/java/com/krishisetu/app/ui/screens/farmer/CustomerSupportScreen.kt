package com.krishisetu.app.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.SupportAgent
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
fun CustomerSupportScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit,
    onNavigateToRaiseDispute: () -> Unit,
    onNavigateToMyComplaints: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val supportCategories = listOf(
        SupportCategoryData(strings.supportCatPrices, "📊", Color(0xFFE3F2FD)),
        SupportCategoryData(strings.supportCatLot, "📦", Color(0xFFE8F5E9)),
        SupportCategoryData(strings.supportCatOffers, "🤝", Color(0xFFFFF3E0)),
        SupportCategoryData(strings.supportCatPayments, "💳", Color(0xFFF3E5F5)),
        SupportCategoryData(strings.supportCatDelivery, "🚚", Color(0xFFE0F2F1)),
        SupportCategoryData(strings.supportCatAccount, "📑", Color(0xFFFFF8E1))
    )

    val faqs = listOf(
        FaqItem(
            question = "How do I calculate net realization price?",
            answer = "Net realization = Gross market rate minus transport charges and mandi commission. Use our 'Best Place to Sell' or 'Profit Calculator' tool on Home."
        ),
        FaqItem(
            question = "When will I receive payment for my crop lot?",
            answer = "Payments are held safely in KrishiSetu Escrow and released directly to your bank account within 24-48 hours after buyer delivery confirmation."
        ),
        FaqItem(
            question = "What if a buyer rejects my crop grade?",
            answer = "You can immediately 'Raise a Dispute'. A KrishiSetu field officer will inspect the lot and resolve the issue within 24 hours."
        ),
        FaqItem(
            question = "How does document verification (eKYC) work?",
            answer = "Upload your Aadhaar and 7/12 extract on Profile page. Our team verifies land records and Aadhaar within 24 hours to give you a Verified Farmer badge."
        )
    )

    var expandedFaqIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            KrishiTopBar(
                title = strings.helpAndSupportTitle,
                subtitle = strings.needHelpSub,
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
            // Header Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmerRoleGreen),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = strings.helpAndSupportTitle,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = strings.needHelpSub,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 12.sp
                                )
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = "Support",
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }

            // Quick Support Action Bar (My Complaints button)
            item {
                OutlinedButton(
                    onClick = onNavigateToMyComplaints,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FarmerRoleGreen)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.myComplaintsBtn,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Categories Section Header
            item {
                Text(
                    text = "Help Categories",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            // Help Categories Grid (Custom 2-column layout)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    supportCategories.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { item ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onNavigateToRaiseDispute() },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(item.bgColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(item.icon, fontSize = 18.sp)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            ),
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // FAQs Section
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Frequently Asked Questions",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            items(faqs.size) { index ->
                val faq = faqs[index]
                val isExpanded = expandedFaqIndex == index

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedFaqIndex = if (isExpanded) null else index },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = faq.question,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Expand",
                                tint = FarmerRoleGreen
                            )
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = BorderColor)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = faq.answer,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Still Need Help Card
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = "Dispute",
                            tint = StatusError,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = strings.stillNeedHelp,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "If your transaction, quality rating, or payment issue is unresolved, raise an official dispute ticket.",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        PrimaryKrishiButton(
                            text = strings.raiseADisputeBtn,
                            containerColor = StatusError,
                            icon = Icons.Default.ReportProblem,
                            onClick = onNavigateToRaiseDispute
                        )
                    }
                }
            }
        }
    }
}

private data class SupportCategoryData(
    val title: String,
    val icon: String,
    val bgColor: Color
)

private data class FaqItem(
    val question: String,
    val answer: String
)
