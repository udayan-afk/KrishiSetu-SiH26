package com.krishisetu.app.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.ui.components.KrishiSetuLogo
import com.krishisetu.app.ui.theme.PrimaryGreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToRoleSelection: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToRoleSelection()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // White version of KrishiSetu logo
            KrishiSetuLogo(
                iconSize = 96.dp,
                showSubtitle = false,
                darkTheme = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "शेतकऱ्यापासून बाजारपेठेपर्यंत",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                color = Color.White.copy(alpha = 0.9f)
            )

            Text(
                text = "From Farm to Market",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                ),
                color = Color.White.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Animated 3-Dot Loading indicator matching Figma splash screen
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "dots")
                val dot1Alpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f, targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(tween(600), repeatMode = RepeatMode.Reverse), label = "dot1"
                )
                val dot2Alpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f, targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(tween(600, delayMillis = 200), repeatMode = RepeatMode.Reverse), label = "dot2"
                )
                val dot3Alpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f, targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(tween(600, delayMillis = 400), repeatMode = RepeatMode.Reverse), label = "dot3"
                )

                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color.White.copy(alpha = dot1Alpha)))
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color.White.copy(alpha = dot2Alpha)))
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color.White.copy(alpha = dot3Alpha)))
            }
        }
    }
}
