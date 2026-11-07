package com.krishisetu.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.localization.LocalAppLanguage
import com.krishisetu.app.localization.LocalOnLanguageChange
import com.krishisetu.app.models.AppLanguage
import com.krishisetu.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KrishiTopBar(
    title: String,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val strings = FarmerStrings.get()
    val currentLanguage = LocalAppLanguage.current
    val onLanguageChange = LocalOnLanguageChange.current

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                KrishiSetuLogoIcon(modifier = Modifier.size(26.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = strings.appName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryGreen
                            )
                        )
                        if (title.isNotEmpty() && title != "KrishiSetu" && title != strings.appName) {
                            Text(
                                text = " • $title",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = TextPrimary,
                                maxLines = 1
                            )
                        }
                    }
                    subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                            color = TextSecondary,
                            maxLines = 1
                        )
                    }
                }
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryGreen
                    )
                }
            }
        },
        actions = {
            actions()
            if (onLanguageChange != null) {
                Row(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFE8F5E9))
                        .border(1.dp, PrimaryGreen.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 3.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val options = listOf(
                        AppLanguage.MARATHI to "मरा",
                        AppLanguage.HINDI to "हिं",
                        AppLanguage.ENGLISH to "EN"
                    )
                    options.forEach { (lang, shortLabel) ->
                        val isSelected = currentLanguage == lang
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) PrimaryGreen else Color.Transparent)
                                .clickable { onLanguageChange(lang) }
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = shortLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = if (isSelected) Color.White else PrimaryGreen
                            )
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundWarm
        )
    )
}

@Composable
fun SubmissionSuccessDialog(
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = buttonText, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎉", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = CardSurface
    )
}

@Composable
fun PrimaryKrishiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = PrimaryGreen,
    contentColor: Color = Color.White,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            icon?.let {
                Icon(imageVector = it, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        "Available", "उपलब्ध", "Accepted", "स्वीकारले", "स्वीकृत", "Verified", "सत्यापित",
        "Payment Received", "पेमेंट प्राप्त झाले", "भुगतान प्राप्त", "Resolved", "SELL NOW",
        "BEST VALUE", "सर्वोत्तम मूल्य", "सर्वोत्तम", "BEST NET REALIZATION" ->
            Pair(Color(0xFFE8F5E9), StatusSuccess)
        "Pending", "प्रलंबित", "लंबित", "Under Offer", "ऑफर सुरू", "ऑफर जारी",
        "Under Review", "CONSIDER WAITING", "Pickup Scheduled", "पिकअप निर्धारित" ->
            Pair(Color(0xFFFFF3E0), StatusWarning)
        "Rejected", "नाकारले", "अस्वीकृत", "Sold", "विकले", "बिक गया",
        "LOWEST PRICE/KG", "किमान दर", "न्यूनतम दर", "Lowest" ->
            Pair(Color(0xFFFFEBEE), StatusError)
        else ->
            Pair(Color(0xFFE3F2FD), StatusInfo)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
    }
}

@Composable
fun VerifiedBadge(
    modifier: Modifier = Modifier,
    text: String = FarmerStrings.get().verifiedBuyer
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE3F2FD))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Verified",
            tint = BuyerRoleBlue,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BuyerRoleBlue
            )
        )
    }
}

@Composable
fun QuickActionButton(
    title: String,
    subtitle: String? = null,
    iconBgColor: Color,
    icon: String, // Emoji or String Icon
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = TextPrimary
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
fun MetricStatCard(
    label: String,
    value: String,
    subtext: String? = null,
    containerColor: Color = CardSurface,
    valueColor: Color = PrimaryGreen,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                color = TextSecondary,
                minLines = 2,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = valueColor
                )
            )
            subtext?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
    }
}

