package com.krishisetu.app.models

enum class UserRole {
    FARMER, BUYER, ADMIN, NONE
}

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    MARATHI("mr", "Marathi", "मराठी"),
    HINDI("hi", "Hindi", "हिंदी"),
    ENGLISH("en", "English", "English")
}

enum class QualityGrade(val labelEn: String, val labelMr: String, val labelHi: String) {
    GRADE_A("Grade A (Export Quality)", "श्रेणी अ (निर्यात दर्जा)", "ग्रेड ए (निर्यात गुणवत्ता)"),
    GRADE_B("Grade B (Standard)", "श्रेणी ब (मानक दर्जा)", "ग्रेड बी (मानक गुणवत्ता)"),
    GRADE_C("Grade C (Local)", "श्रेणी क (स्थानिक दर्जा)", "ग्रेड सी (स्थानीय गुणवत्ता)"),
    NONE("", "", "")
}

enum class EkycStatus {
    NOT_VERIFIED, UNDER_REVIEW, VERIFIED
}

enum class DocStatus {
    NOT_SUBMITTED, SUBMITTED, UNDER_VERIFICATION, VERIFIED
}

data class FarmerUser(
    val id: String,
    val name: String,
    val mobile: String,
    val state: String = "Maharashtra",
    val district: String = "Satara",
    val taluka: String = "Satara",
    val village: String = "Khed",
    val verified: Boolean = false,
    val ekycStatus: EkycStatus = EkycStatus.NOT_VERIFIED,
    val aadhaarStatus: DocStatus = DocStatus.NOT_SUBMITTED,
    val aadhaarFileName: String? = null,
    val sevenTwelveStatus: DocStatus = DocStatus.NOT_SUBMITTED,
    val sevenTwelveFileName: String? = null,
    val gatNumberStatus: DocStatus = DocStatus.NOT_SUBMITTED,
    val gatNumberValue: String = "",
    val bankDetailsStatus: DocStatus = DocStatus.NOT_SUBMITTED,
    val bankAccountValue: String = "",
    val otherDocsStatus: DocStatus = DocStatus.NOT_SUBMITTED,
    val otherDocsFileName: String? = null,
    val submissionTimestamp: String? = null
)

data class BuyerUser(
    val id: String,
    val companyName: String,
    val buyerType: String, // Processor, Trader, Retailer, FPO
    val contactName: String,
    val mobile: String,
    val location: String,
    val distanceKm: Int,
    val verified: Boolean = true,
    val paymentReliability: Int = 96,
    val onTimeHistory: String = "96% on-time payment history"
)

data class BuyerProfile(
    val id: String = "BUYER-101",
    val companyName: String = "FreshFoods Pvt Ltd",
    val businessType: String = "Food Processor & Trader",
    val contactPerson: String = "Rajesh Mehta",
    val companyEmail: String = "rajesh@freshfoods.in",
    val phone: String = "+91 98765 43210",
    val state: String = "Maharashtra",
    val district: String = "Pune",
    val city: String = "Pune",
    val gstNumber: String = "27AAACFI234M1ZX",
    val pan: String = "AAACFI234M",
    val fssaiLicense: String = "11512345000123",
    val isVerified: Boolean = true,
    val gstStatus: DocStatus = DocStatus.VERIFIED,
    val panStatus: DocStatus = DocStatus.VERIFIED,
    val businessRegStatus: DocStatus = DocStatus.VERIFIED,
    val fssaiStatus: DocStatus = DocStatus.VERIFIED,
    val addressProofStatus: DocStatus = DocStatus.VERIFIED,
    val totalSpentRs: Double = 428000.0,
    val activeRequirementsCount: Int = 2,
    val totalTransactionsCount: Int = 47,
    val pendingAmountRs: Double = 19000.0,
    val onTimeRatePercent: Int = 96
)

data class BuyerLot(
    val id: String,
    val lotCode: String,
    val farmerName: String,
    val cropName: String,
    val quantityKg: Double,
    val qualityGrade: String,
    val locationDistance: String,
    val askingPricePerKg: Double,
    val reliabilityScorePercent: Int,
    val status: String = "Available"
)

data class BuyerOfferRecord(
    val id: String,
    val offerCode: String,
    val lotCode: String,
    val farmerName: String,
    val cropName: String,
    val quantityKg: Double,
    val pricePerKg: Double,
    val status: String // Pending, Counter Sent, Accepted, Rejected
)

data class BuyerDeliveryRecord(
    val id: String,
    val deliveryCode: String,
    val cropName: String,
    val quantityKg: Double,
    val farmerName: String,
    val transporter: String,
    val eta: String,
    val status: String // IN TRANSIT, DELIVERED
)

data class BuyerPaymentRecord(
    val id: String,
    val farmerName: String,
    val cropName: String,
    val date: String,
    val amountRs: Double,
    val status: String // Paid, Pending
)


data class CropLot(
    val id: String,
    val cropName: String,
    val quantityKg: Double,
    val qualityGrade: QualityGrade,
    val isOrganicCertified: Boolean = false,
    val harvestDate: String,
    val location: String,
    val expectedPricePerKg: Double,
    val status: String = "Available",
    val farmerName: String = "Vikram Jadhav"
)

data class BuyerRequirement(
    val id: String,
    val buyerId: String,
    val buyerName: String,
    val cropName: String,
    val requiredQuantityKg: Double,
    val pricePerKg: Double,
    val location: String,
    val distanceKm: Int,
    val deadline: String,
    val verified: Boolean = true
)

data class DigitalOffer(
    val id: String,
    val lotId: String,
    val cropName: String,
    val quantityKg: Double,
    val buyerName: String,
    val initialPricePerKg: Double,
    val currentOfferedPricePerKg: Double,
    val status: String,
    val lastUpdated: String,
    val history: List<String> = emptyList()
)

enum class CommodityCategory(val displayName: String) {
    ALL("All"),
    VEGETABLES("Vegetables"),
    FRUITS("Fruits"),
    GRAINS_AND_PULSES("Grains and Pulses")
}

enum class MarketDataStatus(val labelEn: String) {
    SAMPLE_DATA("SAMPLE DATA"),
    REPORTED("REPORTED"),
    LIVE("LIVE")
}

enum class MarketType(val labelEn: String) {
    REPORTED_PRICE("Reported-Price Market"),
    INTRADAY_UPDATED("Intraday-Updated Market"),
    AUCTION_BIDDING("Auction / Bidding Market")
}

enum class AuctionStatus(val labelEn: String) {
    NOT_STARTED("Not started"),
    OPEN("Open"),
    CLOSED("Closed")
}

data class PriceObservation(
    val id: String,
    val commodityId: String,
    val commodityName: String,
    val marketId: String,
    val marketName: String,
    val sessionKey: String, // "MORNING", "MIDDAY", "AFTERNOON", "DAILY_REPORT"
    val pricePerKg: Double,
    val reportedDate: String,
    val reportedTime: String,
    val sourceProvider: String = "KrishiSetu Demo Feed (Illustrative)",
    val dataStatus: MarketDataStatus = MarketDataStatus.SAMPLE_DATA,
    val freshnessLabel: String = "Sample Intraday Snapshot",
    val minPricePerKg: Double? = null,
    val maxPricePerKg: Double? = null,
    val modalPricePerKg: Double? = null,
    val changeRs: Double? = null,
    val changePercent: Double? = null
)

data class AuctionSummary(
    val id: String,
    val commodityId: String,
    val commodityName: String,
    val marketId: String,
    val marketName: String,
    val status: AuctionStatus,
    val startTime: String,
    val closeTime: String,
    val validBidsCount: Int,
    val currentHighestBidPerKg: Double? = null,
    val winningBidPerKg: Double? = null,
    val bidTimestamp: String,
    val unit: String = "₹/kg",
    val sampleLabel: String = "SAMPLE AUCTION"
)

data class CommodityInfo(
    val id: String,
    val name: String,
    val category: CommodityCategory,
    val emoji: String,
    val basePricePerKg: Double,
    val unit: String = "₹/kg"
)

data class MarketYardInfo(
    val id: String,
    val name: String,
    val district: String,
    val distanceKmFromSatara: Int,
    val priceBiasFactor: Double,
    val marketType: MarketType = MarketType.INTRADAY_UPDATED
)

data class MarketPriceData(
    val id: String,
    val cropName: String,
    val marketName: String,
    val district: String,
    val currentPricePerKg: Double,
    val minPricePerKg: Double,
    val modalPricePerKg: Double,
    val maxPricePerKg: Double,
    val distanceKm: Int,
    val transportCostEst: Double,
    val lastUpdated: String = "Demo Snapshot · 03:00 PM",
    val priceTrend7Days: List<Double> = listOf(21.0, 21.5, 22.0, 23.5, 24.0, 25.0, 26.0),
    val category: CommodityCategory = CommodityCategory.VEGETABLES,
    val commodityId: String = "CMD-01",
    val marketId: String = "MKT-01",
    val previousPricePerKg: Double = 21.0,
    val trendDirection: String = "UP",
    val sampleLoadKg: Int = 800,
    val dataSourceLabel: String = "DEMO / SAMPLE DATA",
    val unit: String = "₹/kg",
    val marketType: MarketType = MarketType.INTRADAY_UPDATED,
    val dataStatus: MarketDataStatus = MarketDataStatus.SAMPLE_DATA,
    val freshnessLabel: String = "Sample Intraday Snapshot",
    val isStale: Boolean = false,
    val observations: List<PriceObservation> = emptyList(),
    val auctionSummary: AuctionSummary? = null
)

data class TransportOption(
    val id: String,
    val vehicleName: String,
    val capacityTonnes: Double,
    val costRs: Double,
    val availability: String,
    val driverName: String,
    val driverPhone: String,
    val rating: Double = 4.8
)

data class TransactionRecord(
    val id: String,
    val lotId: String,
    val cropName: String,
    val quantityKg: Double,
    val buyerName: String,
    val pricePerKg: Double,
    val grossAmount: Double,
    val transportCost: Double,
    val netRealization: Double,
    val status: String,
    val selectedTransport: String? = null,
    val date: String = "10 Sep 2026"
)

data class GovernmentScheme(
    val id: String,
    val title: String,
    val category: String,
    val benefit: String,
    val eligibility: String,
    val documentsRequired: String,
    val howToApply: String,
    val officialSource: String = "Ministry of Agriculture & Farmers Welfare (MOCK DATA)"
)

data class DisputeRecord(
    val id: String,
    val transactionId: String,
    val farmerName: String,
    val buyerName: String,
    val disputeType: String,
    val description: String,
    val status: String,
    val date: String,
    val ticketId: String = id,
    val cropInfo: String = "Tomato – 800 kg",
    val photoAttached: Boolean = false
)


data class FpoGroup(
    val id: String,
    val name: String,
    val cropTarget: String,
    val targetQuantityKg: Double,
    val collectedQuantityKg: Double,
    val offeredPricePerKg: Double,
    val memberCount: Int
)

data class ChatMessage(
    val id: String,
    val sender: String,
    val text: String,
    val timestamp: String = "10:30 AM"
)

data class AdminFarmerRecord(
    val id: String,
    val name: String,
    val location: String,
    val state: String,
    val activeLotsCount: Int,
    val status: String, // Active, Pending Verification, Onboarded, Rejected
    val aadhaarStatus: String = "Submitted",
    val sevenTwelveStatus: String = "Submitted",
    val gatNumberStatus: String = "Submitted",
    val bankDetailsStatus: String = "Submitted",
    val rejectionReason: String? = null
)

data class AdminBuyerRecord(
    val id: String,
    val organisationName: String,
    val businessType: String,
    val location: String,
    val appliedDate: String,
    val docsSubmittedFraction: String,
    val riskLevel: String, // Low Risk, Medium Risk, High Risk
    val status: String, // Pending, Approved, Rejected
    val gstStatus: String = "Submitted",
    val panStatus: String = "Submitted",
    val busRegStatus: String = "Submitted",
    val fssaiStatus: String = "Submitted",
    val addressProofStatus: String = "Submitted",
    val rejectionReason: String? = null
)

data class MarketFeedStatusRecord(
    val id: String,
    val apmcName: String,
    val status: String, // Live, Delayed, Offline
    val lastFetchText: String,
    val recordsCount: Int
)

