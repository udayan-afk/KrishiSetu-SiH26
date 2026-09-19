package com.krishisetu.app.ui.screens.farmer

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.AppLanguage
import com.krishisetu.app.models.EkycStatus
import com.krishisetu.app.ui.components.KrishiSetuLogoIcon
import com.krishisetu.app.ui.components.QuickActionButton
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel
import kotlinx.coroutines.delay

private data class TutorialVideoData(
    val title: String,
    val subtitle: String,
    val description: String,
    val source: String,
    val youtubeUrl: String,
    val icon: String,
    val bgColor: Color
)

private data class SchemeSlideData(
    val tag: String,
    val title: String,
    val description: String,
    val icon: String,
    val bgColor: Color
)

private data class Tuple6<A, B, C, D, E, F>(
    val a: A, val b: B, val c: C, val d: D, val e: E, val f: F
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FarmerHomeScreen(
    viewModel: KrishiSetuViewModel,
    onNavigateToCreateLot: () -> Unit,
    onNavigateToMarketPrices: () -> Unit,
    onNavigateToMarketComparison: () -> Unit,
    onNavigateToBestPlaceToSell: () -> Unit,
    onNavigateToBuyers: () -> Unit,
    onNavigateToPayments: () -> Unit,
    onNavigateToSchemes: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToKisanAI: () -> Unit,
    onNavigateToProfitCalculator: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    val farmer by viewModel.currentFarmer.collectAsState()
    val lots by viewModel.lots.collectAsState()

    val context = LocalContext.current
    var selectedTutorialVideo by remember { mutableStateOf<TutorialVideoData?>(null) }

    // Tutorial Video Fullscreen/Dialog Overlay
    selectedTutorialVideo?.let { video ->
        val playVideoAction = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.youtubeUrl))
            context.startActivity(intent)
        }

        AlertDialog(
            onDismissRequest = { selectedTutorialVideo = null },
            confirmButton = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { playVideoAction() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(strings.watchOnYoutubeBtn, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { selectedTutorialVideo = null },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(strings.cancel, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                    }
                }
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.tutorialVideoTitle,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    StatusBadge(status = strings.prototypeTutorialBadge)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                            .clickable { playVideoAction() },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = video.bgColor)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(video.icon, fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = FarmerRoleGreen,
                                    shadowElevation = 4.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("▶", color = Color.White, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Play Video ▶",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = video.subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = FarmerRoleGreen)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = video.source,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextMuted)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = video.description,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = TextSecondary
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = CardSurface
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // 1. Farmer Top Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FarmerRoleGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(52.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                KrishiSetuLogoIcon(modifier = Modifier.size(34.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "KrishiSetu",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = AccentGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "•  शेतकऱ्यापासून बाजारपेठेपर्यंत",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 10.sp
                                    )
                                )
                            }
                            Text(
                                text = "Namaskar, ${farmer.name} 🙏",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = AccentGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${farmer.village}, ${farmer.district} (${farmer.state.take(2).uppercase()})",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onNavigateToProfile,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // 2. KrishiSetu eKYC / Document Verification Card
        item {
            val (cardBg, iconBg, badgeText, btnText, titleText, descText) = when (farmer.ekycStatus) {
                EkycStatus.NOT_VERIFIED -> Tuple6(
                    Color(0xFFFFF3E0),
                    Color(0xFFFFE0B2),
                    "ACTION REQUIRED",
                    strings.verifyNowBtn,
                    strings.ekycCardTitle,
                    strings.ekycCardSubtitle
                )
                EkycStatus.UNDER_REVIEW -> Tuple6(
                    Color(0xFFE3F2FD),
                    Color(0xFFBBDEFB),
                    "UNDER REVIEW",
                    "View Status",
                    "KrishiSetu Verification Under Review",
                    strings.verificationInProgress
                )
                EkycStatus.VERIFIED -> Tuple6(
                    Color(0xFFE8F5E9),
                    Color(0xC8C8E6C9),
                    "VERIFIED ✓",
                    "View Profile",
                    "KrishiSetu eKYC Verified ✓",
                    strings.ekycVerified
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToProfile),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(iconBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (farmer.ekycStatus == EkycStatus.VERIFIED) Icons.Default.CheckCircle else Icons.Default.VerifiedUser,
                                    contentDescription = "KYC",
                                    tint = FarmerRoleGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "KrishiSetu eKYC",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = FarmerRoleGreen
                                )
                            )
                        }
                        StatusBadge(status = badgeText)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = descText,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onNavigateToProfile,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = btnText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. Quick Actions Header
        item {
            Text(
                text = strings.quickActionsHeader,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        // 3. Quick Actions Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = strings.actionAddCrop,
                        subtitle = strings.actionAddCropSub,
                        iconBgColor = Color(0xFFE8F5E9),
                        icon = "➕",
                        onClick = onNavigateToCreateLot,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = strings.actionBestPlace,
                        subtitle = strings.actionBestPlaceSub,
                        iconBgColor = Color(0xFFFFF3E0),
                        icon = "🎯",
                        onClick = onNavigateToBestPlaceToSell,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = strings.actionMarketPrices,
                        subtitle = strings.actionMarketPricesSub,
                        iconBgColor = Color(0xFFE3F2FD),
                        icon = "📊",
                        onClick = onNavigateToMarketPrices,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = strings.actionCompareMarkets,
                        subtitle = strings.actionCompareMarketsSub,
                        iconBgColor = Color(0xFFF3E5F5),
                        icon = "⚖️",
                        onClick = onNavigateToMarketComparison,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = strings.actionFindBuyers,
                        subtitle = strings.actionFindBuyersSub,
                        iconBgColor = Color(0xFFE0F2F1),
                        icon = "🏢",
                        onClick = onNavigateToBuyers,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = strings.actionKisanAI,
                        subtitle = strings.actionKisanAISub,
                        iconBgColor = Color(0xFFFFF8E1),
                        icon = "🤖",
                        onClick = onNavigateToKisanAI,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        title = strings.actionProfitCalc,
                        subtitle = strings.actionProfitCalcSub,
                        iconBgColor = Color(0xFFE8EAF6),
                        icon = "🧮",
                        onClick = onNavigateToProfitCalculator,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = strings.actionSchemesMap,
                        subtitle = strings.actionSchemesMapSub,
                        iconBgColor = Color(0xFFFBE9E7),
                        icon = "🏛️",
                        onClick = onNavigateToSchemes,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3.5. Learn with KrishiSetu Section (Tutorial Area)
        item {
            val videoList = listOf(
                TutorialVideoData(
                    title = strings.video1Title,
                    subtitle = strings.video1Sub,
                    description = strings.video1Desc,
                    source = strings.videoSourceLabel,
                    youtubeUrl = "https://www.youtube.com/watch?v=TrUBa9nT1q4",
                    icon = "🏛️",
                    bgColor = Color(0xFFE8F5E9)
                ),
                TutorialVideoData(
                    title = strings.video2Title,
                    subtitle = strings.video2Sub,
                    description = strings.video2Desc,
                    source = strings.videoSourceLabel,
                    youtubeUrl = "https://www.youtube.com/watch?v=TrUBa9nT1q4",
                    icon = "📲",
                    bgColor = Color(0xFFFFF3E0)
                ),
                TutorialVideoData(
                    title = strings.video3Title,
                    subtitle = strings.video3Sub,
                    description = strings.video3Desc,
                    source = strings.videoSourceLabel,
                    youtubeUrl = "https://www.youtube.com/watch?v=TrUBa9nT1q4",
                    icon = "🤖",
                    bgColor = Color(0xFFE3F2FD)
                )
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = strings.learnWithKrishiSetuTitle,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = strings.learnWithKrishiSetuSub,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = TextSecondary
                        )
                    }
                    TextButton(onClick = { selectedTutorialVideo = videoList[0] }) {
                        Text(strings.viewAllBtn, color = FarmerRoleGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    videoList.forEach { video ->
                        Card(
                            modifier = Modifier
                                .width(220.dp)
                                .clickable { selectedTutorialVideo = video },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(95.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(video.bgColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(video.icon, fontSize = 28.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(FarmerRoleGreen)
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text("▶ PLAY", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = video.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = video.subtitle,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                                    color = TextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. My Active Lots Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${strings.myLotsHeader} (${lots.size})",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                TextButton(onClick = onNavigateToCreateLot) {
                    Text("+ ${strings.actionAddCrop}", color = FarmerRoleGreen, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 4. My Active Lots Items
        items(lots) { lot ->
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
                        Text(
                            text = "${lot.cropName} (${lot.quantityKg.toInt()} kg)",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        StatusBadge(status = lot.status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Lot ID: ${lot.id}", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                        val gradeText = when (currentLang) {
                            AppLanguage.MARATHI -> lot.qualityGrade.labelMr
                            AppLanguage.HINDI -> lot.qualityGrade.labelHi
                            AppLanguage.ENGLISH -> lot.qualityGrade.labelEn
                        }
                        Text(text = "Grade: $gradeText", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${strings.targetPriceLabel}: ₹${lot.expectedPricePerKg.toInt()}/kg",
                            style = MaterialTheme.typography.titleMedium.copy(color = FarmerRoleGreen, fontWeight = FontWeight.Bold)
                        )
                        Button(
                            onClick = onNavigateToBuyers,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                        ) {
                            Text(strings.actionFindBuyers, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 5. Government Schemes Moving Auto-Scroll Banner
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.govtSchemesBannerTitle,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    TextButton(onClick = onNavigateToSchemes) {
                        Text(strings.learnMoreBtn, color = FarmerRoleGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val schemeSlides = listOf(
                    SchemeSlideData(
                        tag = "PM-KISAN",
                        title = "Income Support Scheme",
                        description = "₹6,000 annual direct income support in 3 installments for eligible farmers.",
                        icon = "🏛️",
                        bgColor = Color(0xFFE8F5E9)
                    ),
                    SchemeSlideData(
                        tag = "PMFBY",
                        title = "Crop Insurance Scheme",
                        description = "Financial assistance against crop loss due to natural calamities & rainfall failure.",
                        icon = "🌾",
                        bgColor = Color(0xFFFFF3E0)
                    ),
                    SchemeSlideData(
                        tag = "e-NAM",
                        title = "Digital Agricultural Market",
                        description = "Direct online transparent trading platform connecting to 1,000+ national mandis.",
                        icon = "📲",
                        bgColor = Color(0xFFE3F2FD)
                    )
                )

                val pagerState = rememberPagerState(pageCount = { schemeSlides.size })

                // Auto Scroll Timer
                LaunchedEffect(pagerState) {
                    while (true) {
                        delay(3500)
                        val nextPage = (pagerState.currentPage + 1) % schemeSlides.size
                        pagerState.animateScrollToPage(nextPage, animationSpec = tween(durationMillis = 600))
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val slide = schemeSlides[page]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigateToSchemes),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = slide.bgColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                StatusBadge(status = slide.tag)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = slide.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = slide.description,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                    color = TextSecondary,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "${strings.learnMoreBtn} →",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = FarmerRoleGreen
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = slide.icon, fontSize = 42.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Page Indicator Dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(schemeSlides.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (isSelected) 10.dp else 6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) FarmerRoleGreen else BorderColor)
                        )
                    }
                }
            }
        }
    }
}
