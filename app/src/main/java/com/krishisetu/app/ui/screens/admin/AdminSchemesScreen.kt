package com.krishisetu.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.app.models.GovernmentScheme
import com.krishisetu.app.ui.components.StatusBadge
import com.krishisetu.app.ui.theme.*
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSchemesScreen(
    viewModel: KrishiSetuViewModel,
    onBack: () -> Unit
) {
    val schemes by viewModel.schemes.collectAsState()
    var selectedSchemeForEdit by remember { mutableStateOf<GovernmentScheme?>(null) }
    var showUpdatedToast by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Government Schemes", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWarm)
            )
        },
        containerColor = BackgroundWarm
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 88.dp)
            ) {
                items(schemes) { scheme ->
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = FarmerRoleGreen)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = scheme.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                                    )
                                }
                                StatusBadge(status = "ACTIVE")
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Category: ${scheme.category}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                            Text(text = "Benefit: ${scheme.benefit}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = TextPrimary))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Eligibility: ${scheme.eligibility}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedButton(
                                onClick = { selectedSchemeForEdit = scheme },
                                modifier = Modifier.fillMaxWidth().height(40.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, FarmerRoleGreen)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = FarmerRoleGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Update Info", fontWeight = FontWeight.Bold, color = FarmerRoleGreen)
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Modal Dialog
    selectedSchemeForEdit?.let { scheme ->
        var editBenefit by remember { mutableStateOf(scheme.benefit) }
        var editEligibility by remember { mutableStateOf(scheme.eligibility) }

        AlertDialog(
            onDismissRequest = { selectedSchemeForEdit = null },
            title = { Text("Update Scheme: ${scheme.title}", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editBenefit,
                        onValueChange = { editBenefit = it },
                        label = { Text("Scheme Benefit Summary") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = editEligibility,
                        onValueChange = { editEligibility = it },
                        label = { Text("Eligibility Requirements") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateGovernmentSchemeInfo(scheme.id, editBenefit, editEligibility)
                        selectedSchemeForEdit = null
                        showUpdatedToast = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedSchemeForEdit = null }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }

    if (showUpdatedToast) {
        AlertDialog(
            onDismissRequest = { showUpdatedToast = false },
            title = { Text("Scheme Information Updated", fontWeight = FontWeight.Bold) },
            text = { Text("Scheme details updated successfully.", color = TextSecondary) },
            confirmButton = {
                Button(onClick = { showUpdatedToast = false }, colors = ButtonDefaults.buttonColors(containerColor = FarmerRoleGreen)) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardSurface
        )
    }
}
