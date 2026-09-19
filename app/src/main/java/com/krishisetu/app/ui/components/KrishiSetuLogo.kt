package com.krishisetu.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.ui.theme.AccentGold
import com.krishisetu.app.ui.theme.PrimaryGreen
import com.krishisetu.app.ui.theme.TextMuted
import com.krishisetu.app.ui.theme.TextPrimary

@Composable
fun KrishiSetuLogoIcon(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false
) {
    val logoGreen = if (darkTheme) Color.White else PrimaryGreen
    val logoGold = AccentGold

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Draw Green Arch Bridge
        val path = Path().apply {
            moveTo(w * 0.15f, h * 0.65f)
            cubicTo(
                w * 0.35f, h * 0.25f,
                w * 0.65f, h * 0.25f,
                w * 0.85f, h * 0.65f
            )
            lineTo(w * 0.90f, h * 0.65f)
            cubicTo(
                w * 0.65f, h * 0.15f,
                w * 0.35f, h * 0.15f,
                w * 0.10f, h * 0.65f
            )
            close()
        }
        drawPath(path, color = logoGreen)

        // Bridge Base Beam
        drawRect(
            color = logoGreen,
            topLeft = Offset(w * 0.10f, h * 0.65f),
            size = Size(w * 0.80f, h * 0.08f)
        )

        // Inner Bridge Arch Line
        val innerArch = Path().apply {
            moveTo(w * 0.25f, h * 0.65f)
            cubicTo(
                w * 0.40f, h * 0.38f,
                w * 0.60f, h * 0.38f,
                w * 0.75f, h * 0.65f
            )
        }
        drawPath(innerArch, color = logoGreen, style = Stroke(width = w * 0.04f))

        // 2. Golden Sprout 1 (Left)
        val leftSprout = Path().apply {
            moveTo(w * 0.15f, h * 0.55f)
            cubicTo(w * 0.05f, h * 0.35f, w * 0.10f, h * 0.20f, w * 0.15f, h * 0.15f)
            cubicTo(w * 0.22f, h * 0.25f, w * 0.20f, h * 0.40f, w * 0.15f, h * 0.55f)
        }
        drawPath(leftSprout, color = logoGold)

        // 3. Golden Sprout 2 (Right)
        val rightSprout = Path().apply {
            moveTo(w * 0.85f, h * 0.55f)
            cubicTo(w * 0.95f, h * 0.35f, w * 0.90f, h * 0.20f, w * 0.85f, h * 0.15f)
            cubicTo(w * 0.78f, h * 0.25f, w * 0.80f, h * 0.40f, w * 0.85f, h * 0.55f)
        }
        drawPath(rightSprout, color = logoGold)
    }
}

@Composable
fun KrishiSetuLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp,
    showSubtitle: Boolean = true,
    darkTheme: Boolean = false
) {
    val textColor = if (darkTheme) Color.White else TextPrimary
    val subTextColor = if (darkTheme) Color(0xFFC8E6C9) else TextMuted

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KrishiSetuLogoIcon(
            modifier = Modifier.size(iconSize),
            darkTheme = darkTheme
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Brand Title
        Text(
            text = "KrishiSetu",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (iconSize > 60.dp) 26.sp else 20.sp
            ),
            color = textColor
        )

        if (showSubtitle) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "शेतकऱ्यापासून बाजारपेठेपर्यंत",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = subTextColor
            )
        }
    }
}
