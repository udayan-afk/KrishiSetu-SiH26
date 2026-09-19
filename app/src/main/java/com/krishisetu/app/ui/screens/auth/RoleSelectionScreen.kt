package com.krishisetu.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.collectAsState
import com.krishisetu.app.R
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.UserRole
import com.krishisetu.app.ui.components.KrishiSetuLogo
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@Composable
fun RoleSelectionScreen(
    viewModel: KrishiSetuViewModel? = null,
    onRoleSelected: (UserRole) -> Unit
) {
    val currentLang = viewModel?.selectedLanguage?.collectAsState()?.value ?: com.krishisetu.app.models.AppLanguage.ENGLISH
    val strings = FarmerStrings.get(currentLang)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Brand Header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                KrishiSetuLogo(iconSize = 56.dp)

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = strings.whoAreYou,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp
                    ),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = strings.selectRoleSubtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                    color = TextSecondary
                )
            }

            // Role Options List matching Figma design
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Farmer Card
                RoleSelectionCard(
                    title = strings.farmerRoleTitle,
                    subtitleMarathi = strings.farmerRoleSubtitle,
                    description = strings.farmerRoleDesc,
                    iconEmoji = "👨‍🌾",
                    iconBgColor = FarmerRoleGreen,
                    borderColor = FarmerRoleGreen.copy(alpha = 0.3f),
                    onClick = { onRoleSelected(UserRole.FARMER) }
                )

                // 2. Buyer / Organisation Card
                RoleSelectionCard(
                    title = stringResource(R.string.buyer_role_title),
                    subtitleMarathi = "Processor · Trader · Retailer · FPO",
                    description = "Source directly from verified farmers, manage procurement",
                    iconEmoji = "🏢",
                    iconBgColor = BuyerRoleBlue,
                    borderColor = BuyerRoleBlue.copy(alpha = 0.3f),
                    onClick = { onRoleSelected(UserRole.BUYER) }
                )

                // 3. Admin Card
                RoleSelectionCard(
                    title = stringResource(R.string.admin_role_title),
                    subtitleMarathi = "Platform Management",
                    description = "Manage farmers, verify buyers, monitor market data",
                    iconEmoji = "🛠️",
                    iconBgColor = AdminRoleDark,
                    borderColor = AdminRoleDark.copy(alpha = 0.3f),
                    onClick = { onRoleSelected(UserRole.ADMIN) }
                )
            }

            // Footer
            Text(
                text = "KrishiSetu · Secure · Verified · Trusted",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                color = TextMuted,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun RoleSelectionCard(
    title: String,
    subtitleMarathi: String,
    description: String,
    iconEmoji: String,
    iconBgColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconEmoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextPrimary
                )
                Text(
                    text = subtitleMarathi,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = iconBgColor
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Select",
                tint = TextMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
