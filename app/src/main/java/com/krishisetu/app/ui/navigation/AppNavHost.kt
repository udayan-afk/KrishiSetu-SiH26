package com.krishisetu.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.krishisetu.app.localization.FarmerStrings
import com.krishisetu.app.models.UserRole
import com.krishisetu.app.ui.screens.admin.*
import com.krishisetu.app.ui.screens.auth.*
import com.krishisetu.app.ui.screens.buyer.*
import com.krishisetu.app.ui.screens.farmer.*
import com.krishisetu.app.ui.theme.FarmerRoleGreen
import com.krishisetu.app.ui.theme.TextMuted
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

object ScreenRoutes {
    const val Splash = "splash"
    const val RoleSelection = "role_selection"
    const val LanguageSelection = "language_selection"
    const val Login = "login/{role}"
    const val FarmerRegister = "farmer_register"

    const val FarmerMain = "farmer_main"
    const val CreateLotWizard = "create_lot_wizard"
    const val MarketPrices = "market_prices"
    const val MarketComparison = "market_comparison"
    const val BestPlaceToSell = "best_place_to_sell"
    const val SellNowOrWait = "sell_now_or_wait"
    const val BuyerMarketplace = "buyer_marketplace"
    const val DigitalOffer = "digital_offer"
    const val FpoAggregation = "fpo_aggregation"
    const val Logistics = "logistics"
    const val ProfitCalculator = "profit_calculator"
    const val Payments = "payments"
    const val Schemes = "schemes"
    const val KisanAI = "kisan_ai"
    const val MapMock = "map_mock"
    const val FarmerProfile = "farmer_profile"
    const val CustomerSupport = "customer_support"
    const val RaiseDispute = "raise_dispute?txId={txId}&issueType={issueType}"
    const val MyComplaints = "my_complaints"

    const val BuyerLogin = "buyer_login"
    const val BuyerRegister = "buyer_register"
    const val BuyerDocVerification = "buyer_doc_verification"
    const val BuyerVerificationStatus = "buyer_verification_status"
    const val BuyerMain = "buyer_main"
    const val BuyerProfile = "buyer_profile"
    const val BuyerDeliveryTracking = "buyer_delivery_tracking"
    const val AdminLogin = "admin_login"
    const val AdminMain = "admin_main"
    const val AdminFarmerVerification = "admin_farmer_verification"
    const val AdminBuyerVerificationDetail = "admin_buyer_verification_detail"
    const val AdminDisputes = "admin_disputes"
    const val AdminSchemes = "admin_schemes"
    const val AdminProfile = "admin_profile"
}

@Composable
fun AppNavHost(
    viewModel: KrishiSetuViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.Splash
    ) {
        // 1. Splash Screen
        composable(ScreenRoutes.Splash) {
            SplashScreen(
                onNavigateToRoleSelection = {
                    navController.navigate(ScreenRoutes.RoleSelection) {
                        popUpTo(ScreenRoutes.Splash) { inclusive = true }
                    }
                }
            )
        }

        // 2. Role Selection
        composable(ScreenRoutes.RoleSelection) {
            RoleSelectionScreen(
                viewModel = viewModel,
                onRoleSelected = { role ->
                    viewModel.selectRole(role)
                    when (role) {
                        UserRole.FARMER -> navController.navigate(ScreenRoutes.LanguageSelection)
                        UserRole.BUYER -> navController.navigate(ScreenRoutes.BuyerLogin)
                        UserRole.ADMIN -> navController.navigate(ScreenRoutes.AdminLogin)
                        UserRole.NONE -> {}
                    }
                }
            )
        }

        // 3. Language Selection Screen (Requirement 2)
        composable(ScreenRoutes.LanguageSelection) {
            LanguageSelectionScreen(
                viewModel = viewModel,
                onLanguageConfirmed = {
                    navController.navigate("login/${UserRole.FARMER.name}")
                }
            )
        }

        // 4. Login Screens
        composable(ScreenRoutes.Login) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("role") ?: UserRole.FARMER.name
            val role = try { UserRole.valueOf(roleName) } catch (e: Exception) { UserRole.FARMER }

            RoleLoginScreen(
                role = role,
                onLoginSuccess = {
                    val dest = when (role) {
                        UserRole.FARMER -> ScreenRoutes.FarmerMain
                        UserRole.BUYER -> ScreenRoutes.BuyerMain
                        UserRole.ADMIN -> ScreenRoutes.AdminMain
                        UserRole.NONE -> ScreenRoutes.RoleSelection
                    }
                    navController.navigate(dest) {
                        popUpTo(ScreenRoutes.RoleSelection) { inclusive = false }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(ScreenRoutes.FarmerRegister)
                },
                onBackToRoleSelection = {
                    navController.popBackStack()
                }
            )
        }


        // 5. Farmer Registration
        composable(ScreenRoutes.FarmerRegister) {
            FarmerRegisterScreen(
                viewModel = viewModel,
                onRegistrationSuccess = {
                    viewModel.selectRole(UserRole.FARMER)
                    navController.navigate(ScreenRoutes.FarmerMain) {
                        popUpTo(ScreenRoutes.RoleSelection) { inclusive = false }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // 6. Farmer Experience
        composable(ScreenRoutes.FarmerMain) {
            FarmerMainContainerScreen(
                viewModel = viewModel,
                rootNavController = navController
            )
        }

        // Secondary Farmer Workflows
        composable(ScreenRoutes.CreateLotWizard) {
            CreateLotWizardScreen(
                viewModel = viewModel,
                onLotCreated = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.MarketPrices) {
            MarketPricesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToComparison = { navController.navigate(ScreenRoutes.MarketComparison) }
            )
        }

        composable(ScreenRoutes.MarketComparison) {
            MarketComparisonScreen(
                onBack = { navController.popBackStack() },
                onNavigateToBestPlaceToSell = { navController.navigate(ScreenRoutes.BestPlaceToSell) }
            )
        }

        composable(ScreenRoutes.BestPlaceToSell) {
            BestPlaceToSellScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDirectBuyer = { navController.navigate(ScreenRoutes.BuyerMarketplace) },
                onNavigateToSellOrWait = { navController.navigate(ScreenRoutes.SellNowOrWait) }
            )
        }

        composable(ScreenRoutes.SellNowOrWait) {
            SellNowOrWaitScreen(
                onBack = { navController.popBackStack() },
                onSellNowClick = { navController.navigate(ScreenRoutes.BuyerMarketplace) }
            )
        }

        composable(ScreenRoutes.BuyerMarketplace) {
            BuyerMarketplaceScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSelectBuyer = { navController.navigate(ScreenRoutes.DigitalOffer) }
            )
        }

        composable(ScreenRoutes.DigitalOffer) {
            DigitalOfferScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOfferAcceptedNavigateToLogistics = { navController.navigate(ScreenRoutes.Logistics) },
                onNavigateToRaiseDispute = { txId, issueType ->
                    navController.navigate("raise_dispute?txId=$txId&issueType=$issueType")
                }
            )
        }

        composable(ScreenRoutes.FpoAggregation) {
            FpoAggregationScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.Logistics) {
            LogisticsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onTransportSelectedNavigateToPayments = { navController.navigate(ScreenRoutes.Payments) },
                onNavigateToRaiseDispute = { txId, issueType ->
                    navController.navigate("raise_dispute?txId=$txId&issueType=$issueType")
                }
            )
        }

        composable(ScreenRoutes.ProfitCalculator) {
            ProfitCalculatorScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.Payments) {
            PaymentsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRaiseDispute = { txId, issueType ->
                    navController.navigate("raise_dispute?txId=$txId&issueType=$issueType")
                }
            )
        }

        composable(ScreenRoutes.Schemes) {
            GovernmentSchemesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.KisanAI) {
            KisanAIScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.MapMock) {
            MapMockScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.FarmerProfile) {
            FarmerProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(ScreenRoutes.RoleSelection) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToCustomerSupport = { navController.navigate(ScreenRoutes.CustomerSupport) },
                onNavigateToRaiseDispute = { navController.navigate(ScreenRoutes.RaiseDispute) },
                onNavigateToMyComplaints = { navController.navigate(ScreenRoutes.MyComplaints) }
            )
        }

        composable(ScreenRoutes.CustomerSupport) {
            CustomerSupportScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRaiseDispute = { navController.navigate(ScreenRoutes.RaiseDispute) },
                onNavigateToMyComplaints = { navController.navigate(ScreenRoutes.MyComplaints) }
            )
        }

        composable(
            route = ScreenRoutes.RaiseDispute,
            arguments = listOf(
                androidx.navigation.navArgument("txId") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                androidx.navigation.navArgument("issueType") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val txId = backStackEntry.arguments?.getString("txId")
            val issueType = backStackEntry.arguments?.getString("issueType")
            RaiseDisputeScreen(
                viewModel = viewModel,
                initialTransactionId = txId,
                initialIssueType = issueType,
                onBack = { navController.popBackStack() },
                onNavigateToMyComplaints = {
                    navController.navigate(ScreenRoutes.MyComplaints) {
                        popUpTo(ScreenRoutes.FarmerMain)
                    }
                }
            )
        }

        composable(ScreenRoutes.MyComplaints) {
            MyComplaintsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRaiseDispute = { navController.navigate(ScreenRoutes.RaiseDispute) }
            )
        }

        // 7. Buyer Experience
        composable(ScreenRoutes.BuyerLogin) {
            BuyerLoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(ScreenRoutes.BuyerMain) {
                        popUpTo(ScreenRoutes.RoleSelection) { inclusive = false }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(ScreenRoutes.BuyerRegister)
                },
                onBackToRoleSelection = {
                    navController.popBackStack()
                }
            )
        }

        composable(ScreenRoutes.BuyerRegister) {
            BuyerRegisterScreen(
                viewModel = viewModel,
                onProfileCreated = {
                    navController.navigate(ScreenRoutes.BuyerDocVerification)
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.BuyerDocVerification) {
            BuyerDocVerificationScreen(
                viewModel = viewModel,
                onVerificationCompleted = {
                    navController.navigate(ScreenRoutes.BuyerVerificationStatus) {
                        popUpTo(ScreenRoutes.BuyerMain) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.BuyerVerificationStatus) {
            BuyerVerificationStatusScreen(
                viewModel = viewModel,
                onNavigateToDashboard = {
                    navController.navigate(ScreenRoutes.BuyerMain) {
                        popUpTo(ScreenRoutes.BuyerMain) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.BuyerMain) {
            BuyerMainContainerScreen(
                viewModel = viewModel,
                onNavigateToProfile = {
                    navController.navigate(ScreenRoutes.BuyerProfile)
                },
                onNavigateToVerification = {
                    navController.navigate(ScreenRoutes.BuyerDocVerification)
                },
                onNavigateToTracking = {
                    navController.navigate(ScreenRoutes.BuyerDeliveryTracking)
                }
            )
        }

        composable(ScreenRoutes.BuyerDeliveryTracking) {
            BuyerDeliveryTrackingScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.BuyerProfile) {
            BuyerProfileScreen(
                viewModel = viewModel,
                onNavigateToVerification = {
                    navController.navigate(ScreenRoutes.BuyerVerificationStatus)
                },
                onLogout = {
                    navController.navigate(ScreenRoutes.RoleSelection) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }


        // 8. Admin Experience
        composable(ScreenRoutes.AdminLogin) {
            AdminLoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(ScreenRoutes.AdminMain) {
                        popUpTo(ScreenRoutes.RoleSelection) { inclusive = false }
                    }
                },
                onBackToRoleSelection = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.AdminMain) {
            var selectedFarmerForVerification by remember { mutableStateOf<com.krishisetu.app.models.AdminFarmerRecord?>(null) }
            var selectedBuyerForVerification by remember { mutableStateOf<com.krishisetu.app.models.AdminBuyerRecord?>(null) }

            AdminHomeScreen(
                viewModel = viewModel,
                onNavigateToFarmerVerification = { farmer ->
                    selectedFarmerForVerification = farmer
                    navController.navigate(ScreenRoutes.AdminFarmerVerification)
                },
                onNavigateToBuyerVerification = { buyer ->
                    selectedBuyerForVerification = buyer
                    navController.navigate(ScreenRoutes.AdminBuyerVerificationDetail)
                },
                onNavigateToDisputes = { navController.navigate(ScreenRoutes.AdminDisputes) },
                onNavigateToSchemes = { navController.navigate(ScreenRoutes.AdminSchemes) },
                onNavigateToProfile = { navController.navigate(ScreenRoutes.AdminProfile) },
                onLogout = {
                    navController.navigate(ScreenRoutes.RoleSelection) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )

            // Dynamic detail navigation routing holders inside scope
            selectedFarmerForVerification?.let { farmer ->
                composable(ScreenRoutes.AdminFarmerVerification) {
                    AdminFarmerVerificationScreen(
                        viewModel = viewModel,
                        farmer = farmer,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }

        composable(ScreenRoutes.AdminFarmerVerification) {
            val adminFarmers by viewModel.adminFarmers.collectAsState()
            val farmer = adminFarmers.firstOrNull() ?: com.krishisetu.app.models.AdminFarmerRecord("ADM-F-1", "Ramesh Patil", "Nashik, MH", "Maharashtra", 2, "Pending Verification")
            AdminFarmerVerificationScreen(
                viewModel = viewModel,
                farmer = farmer,
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.AdminBuyerVerificationDetail) {
            val adminBuyers by viewModel.adminBuyers.collectAsState()
            val buyer = adminBuyers.firstOrNull() ?: com.krishisetu.app.models.AdminBuyerRecord("ADM-B-1", "Agro Vision Ltd", "Food Processor", "Pune", "Applied Dec 4", "4/5", "Low Risk", "Pending")
            AdminBuyerVerificationDetailScreen(
                viewModel = viewModel,
                buyer = buyer,
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.AdminDisputes) {
            AdminDisputesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.AdminSchemes) {
            AdminSchemesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.AdminProfile) {
            AdminProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(ScreenRoutes.RoleSelection) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

// Multilingual Farmer Container Screen with Reactive Bottom Navigation
@Composable
fun FarmerMainContainerScreen(
    viewModel: KrishiSetuViewModel,
    rootNavController: NavHostController
) {
    val currentLang by viewModel.selectedLanguage.collectAsState()
    val strings = FarmerStrings.get(currentLang)

    var selectedBottomTab by remember { mutableStateOf(0) }
    val bottomNavItems = listOf(
        strings.navHome,
        strings.navMarkets,
        strings.navBuyers,
        strings.navFpo,
        strings.navKisanAI
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                bottomNavItems.forEachIndexed { index, title ->
                    val isSelected = selectedBottomTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            selectedBottomTab = index
                            when (index) {
                                1 -> rootNavController.navigate(ScreenRoutes.MarketPrices)
                                2 -> rootNavController.navigate(ScreenRoutes.BuyerMarketplace)
                                3 -> rootNavController.navigate(ScreenRoutes.FpoAggregation)
                                4 -> rootNavController.navigate(ScreenRoutes.KisanAI)
                            }
                        },
                        icon = {
                            val emoji = when (index) {
                                0 -> "🏠"
                                1 -> "📊"
                                2 -> "🏢"
                                3 -> "👥"
                                4 -> "🤖"
                                else -> "📱"
                            }
                            Text(emoji, fontSize = 20.sp)
                        },
                        label = {
                            Text(
                                title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) FarmerRoleGreen else TextMuted
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            FarmerHomeScreen(
                viewModel = viewModel,
                onNavigateToCreateLot = { rootNavController.navigate(ScreenRoutes.CreateLotWizard) },
                onNavigateToMarketPrices = { rootNavController.navigate(ScreenRoutes.MarketPrices) },
                onNavigateToMarketComparison = { rootNavController.navigate(ScreenRoutes.MarketComparison) },
                onNavigateToBestPlaceToSell = { rootNavController.navigate(ScreenRoutes.BestPlaceToSell) },
                onNavigateToBuyers = { rootNavController.navigate(ScreenRoutes.BuyerMarketplace) },
                onNavigateToPayments = { rootNavController.navigate(ScreenRoutes.Payments) },
                onNavigateToSchemes = { rootNavController.navigate(ScreenRoutes.Schemes) },
                onNavigateToMap = { rootNavController.navigate(ScreenRoutes.MapMock) },
                onNavigateToKisanAI = { rootNavController.navigate(ScreenRoutes.KisanAI) },
                onNavigateToProfitCalculator = { rootNavController.navigate(ScreenRoutes.ProfitCalculator) },
                onNavigateToProfile = { rootNavController.navigate(ScreenRoutes.FarmerProfile) }
            )
        }
    }
}

