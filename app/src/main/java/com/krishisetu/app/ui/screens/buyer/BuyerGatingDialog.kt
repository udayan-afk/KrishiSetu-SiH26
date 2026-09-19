package com.krishisetu.app.ui.screens.buyer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.ui.components.PrimaryKrishiButton
import com.krishisetu.app.ui.theme.*

@Composable
fun BuyerGatingDialog(
    onDismiss: () -> Unit,
    onVerifyNow: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔒", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Buyer Verification Required",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = TextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Please complete organisation document verification before participating in marketplace transactions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = FarmerRoleBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Why verification? Verification protects both farmers and buyers from fraudulent transactions.",
                        style = MaterialTheme.typography.labelSmall.copy(color = FarmerRoleGreen, fontSize = 11.sp),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        },
        confirmButton = {
            PrimaryKrishiButton(
                text = "Verify Now →",
                containerColor = FarmerRoleGreen,
                onClick = {
                    onDismiss()
                    onVerifyNow()
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = CardSurface
    )
}
