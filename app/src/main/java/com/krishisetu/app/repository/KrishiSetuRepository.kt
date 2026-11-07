package com.krishisetu.app.repository

import com.krishisetu.app.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class KrishiSetuRepository {

    // Language State
    private val _selectedLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
    }

    // Current User State
    private val _currentRole = MutableStateFlow(UserRole.NONE)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentFarmer = MutableStateFlow(
        FarmerUser(
            id = "FARM-101",
            name = "Vikram Jadhav",
            mobile = "9876543210",
            state = "Maharashtra",
            district = "Satara",
            taluka = "Satara",
            village = "Khed",
            verified = false,
            ekycStatus = EkycStatus.NOT_VERIFIED,
            aadhaarStatus = DocStatus.NOT_SUBMITTED,
            sevenTwelveStatus = DocStatus.NOT_SUBMITTED,
            gatNumberStatus = DocStatus.NOT_SUBMITTED,
            bankDetailsStatus = DocStatus.NOT_SUBMITTED
        )
    )
    val currentFarmer: StateFlow<FarmerUser> = _currentFarmer.asStateFlow()

    fun registerFarmer(
        name: String,
        mobile: String,
        state: String,
        district: String,
        taluka: String,
        village: String
    ) {
        _currentFarmer.value = _currentFarmer.value.copy(
            name = name,
            mobile = mobile,
            state = state,
            district = district,
            taluka = taluka,
            village = village
        )
    }

    fun uploadAadhaar(fileName: String = "aadhaar_card_scan.pdf") {
        _currentFarmer.value = _currentFarmer.value.copy(
            aadhaarStatus = DocStatus.SUBMITTED,
            aadhaarFileName = fileName
        )
    }

    fun uploadSevenTwelve(fileName: String = "7_12_extract_satara.pdf") {
        _currentFarmer.value = _currentFarmer.value.copy(
            sevenTwelveStatus = DocStatus.SUBMITTED,
            sevenTwelveFileName = fileName
        )
    }

    fun updateGatNumber(gatNumber: String) {
        _currentFarmer.value = _currentFarmer.value.copy(
            gatNumberStatus = DocStatus.SUBMITTED,
            gatNumberValue = gatNumber
        )
    }

    fun updateBankDetails(bankDetails: String) {
        _currentFarmer.value = _currentFarmer.value.copy(
            bankDetailsStatus = DocStatus.SUBMITTED,
            bankAccountValue = bankDetails
        )
    }

    fun uploadOtherDoc(fileName: String = "land_declaration_satara.pdf") {
        _currentFarmer.value = _currentFarmer.value.copy(
            otherDocsStatus = DocStatus.SUBMITTED,
            otherDocsFileName = fileName
        )
    }

    fun submitDocumentsForVerification() {
        val farmer = _currentFarmer.value
        val updatedAadhaar = if (farmer.aadhaarStatus != DocStatus.NOT_SUBMITTED) DocStatus.UNDER_VERIFICATION else farmer.aadhaarStatus
        val updated712 = if (farmer.sevenTwelveStatus != DocStatus.NOT_SUBMITTED) DocStatus.UNDER_VERIFICATION else farmer.sevenTwelveStatus
        val updatedGat = if (farmer.gatNumberStatus != DocStatus.NOT_SUBMITTED) DocStatus.UNDER_VERIFICATION else farmer.gatNumberStatus
        val updatedBank = if (farmer.bankDetailsStatus != DocStatus.NOT_SUBMITTED) DocStatus.UNDER_VERIFICATION else farmer.bankDetailsStatus
        val updatedOther = if (farmer.otherDocsStatus != DocStatus.NOT_SUBMITTED) DocStatus.UNDER_VERIFICATION else farmer.otherDocsStatus

        _currentFarmer.value = farmer.copy(
            ekycStatus = EkycStatus.UNDER_REVIEW,
            aadhaarStatus = updatedAadhaar,
            sevenTwelveStatus = updated712,
            gatNumberStatus = updatedGat,
            bankDetailsStatus = updatedBank,
            otherDocsStatus = updatedOther,
            submissionTimestamp = "Just now"
        )
    }

    fun setMockEkycStatus(status: EkycStatus) {
        val docStat = when (status) {
            EkycStatus.NOT_VERIFIED -> DocStatus.NOT_SUBMITTED
            EkycStatus.UNDER_REVIEW -> DocStatus.UNDER_VERIFICATION
            EkycStatus.VERIFIED -> DocStatus.VERIFIED
        }
        _currentFarmer.value = _currentFarmer.value.copy(
            ekycStatus = status,
            verified = (status == EkycStatus.VERIFIED),
            aadhaarStatus = docStat,
            sevenTwelveStatus = docStat,
            gatNumberStatus = docStat,
            bankDetailsStatus = docStat,
            otherDocsStatus = docStat
        )
    }


    // Crop Lots
    private val _lots = MutableStateFlow(
        listOf(
            CropLot("LOT-8841", "Tomato", 800.0, QualityGrade.GRADE_A, true, "09 Sep 2026", "Satara", 26.0, "Available"),
            CropLot("LOT-8835", "Onion", 1500.0, QualityGrade.GRADE_A, false, "05 Sep 2026", "Satara", 28.0, "Under Offer"),
            CropLot("LOT-8790", "Soybean", 2000.0, QualityGrade.GRADE_B, false, "01 Sep 2026", "Satara", 48.0, "Sold")
        )
    )
    val lots: StateFlow<List<CropLot>> = _lots.asStateFlow()

    // Market Prices & Shared Market Selection State (35 Commodities × 30 Maharashtra Markets = 1,050 Demo Records)
    val commodities: List<CommodityInfo> = DemoMarketDataCatalog.commodities
    val marketYards: List<MarketYardInfo> = DemoMarketDataCatalog.markets

    private val _marketPrices = MutableStateFlow(DemoMarketDataCatalog.generateAllMarketPrices())
    val marketPrices: StateFlow<List<MarketPriceData>> = _marketPrices.asStateFlow()

    private val _selectedCommodity = MutableStateFlow("Tomato")
    val selectedCommodity: StateFlow<String> = _selectedCommodity.asStateFlow()

    private val _selectedCategory = MutableStateFlow(CommodityCategory.ALL)
    val selectedCategory: StateFlow<CommodityCategory> = _selectedCategory.asStateFlow()

    private val _selectedMarketId = MutableStateFlow("MKT-01")
    val selectedMarketId: StateFlow<String> = _selectedMarketId.asStateFlow()

    private val _comparisonQuantityKg = MutableStateFlow(800.0)
    val comparisonQuantityKg: StateFlow<Double> = _comparisonQuantityKg.asStateFlow()

    fun setSelectedCommodity(cropName: String) {
        if (commodities.any { it.name.equals(cropName, ignoreCase = true) }) {
            val matched = commodities.first { it.name.equals(cropName, ignoreCase = true) }
            _selectedCommodity.value = matched.name
        }
    }

    fun setSelectedMarketId(marketId: String) {
        if (marketYards.any { it.id.equals(marketId, ignoreCase = true) }) {
            val matched = marketYards.first { it.id.equals(marketId, ignoreCase = true) }
            _selectedMarketId.value = matched.id
        }
    }

    fun setSelectedCategory(category: CommodityCategory) {
        _selectedCategory.value = category
        val currentCrop = _selectedCommodity.value
        val matchingCommodities = if (category == CommodityCategory.ALL) {
            commodities
        } else {
            commodities.filter { it.category == category }
        }
        if (matchingCommodities.none { it.name == currentCrop }) {
            matchingCommodities.firstOrNull()?.let { _selectedCommodity.value = it.name }
        }
    }

    fun setComparisonQuantityKg(quantityKg: Double) {
        if (quantityKg > 0.0 && !quantityKg.isNaN() && !quantityKg.isInfinite()) {
            _comparisonQuantityKg.value = quantityKg
        }
    }

    // Buyer State
    private val _currentBuyerProfile = MutableStateFlow(
        BuyerProfile(
            id = "BUYER-101",
            companyName = "FreshFoods Pvt Ltd",
            businessType = "Food Processor & Trader",
            contactPerson = "Rajesh Mehta",
            companyEmail = "rajesh@freshfoods.in",
            phone = "+91 98765 43210",
            state = "Maharashtra",
            district = "Pune",
            city = "Pune",
            gstNumber = "27AAACFI234M1ZX",
            pan = "AAACFI234M",
            fssaiLicense = "11512345000123",
            isVerified = true,
            gstStatus = DocStatus.VERIFIED,
            panStatus = DocStatus.VERIFIED,
            businessRegStatus = DocStatus.VERIFIED,
            fssaiStatus = DocStatus.VERIFIED,
            addressProofStatus = DocStatus.VERIFIED,
            totalSpentRs = 428000.0,
            activeRequirementsCount = 2,
            totalTransactionsCount = 47,
            pendingAmountRs = 19000.0,
            onTimeRatePercent = 96
        )
    )
    val currentBuyerProfile: StateFlow<BuyerProfile> = _currentBuyerProfile.asStateFlow()

    private val _buyerLots = MutableStateFlow(
        listOf(
            BuyerLot("L-1", "KL-TOM-58291", "Vikram Jadhav", "Tomato", 800.0, "Grade A", "Nashik · 42 km", 26.0, 98, "Available"),
            BuyerLot("L-2", "KL-TOM-58105", "Suresh Jadhav", "Tomato", 1200.0, "Grade A", "Sinnar · 58 km", 25.0, 94, "Available"),
            BuyerLot("L-3", "KL-ONI-44821", "Priya More", "Onion", 600.0, "Grade B", "Niphad · 35 km", 18.0, 82, "Negotiating"),
            BuyerLot("L-4", "KL-POT-33214", "Anil Shinde", "Potato", 2000.0, "Grade A", "Dindori · 71 km", 14.0, 89, "Available"),
            BuyerLot("L-5", "KL-TOM-57988", "Vijay Kulkarni", "Tomato", 500.0, "Grade B", "Nashik · 44 km", 24.0, 76, "Available")
        )
    )
    val buyerLots: StateFlow<List<BuyerLot>> = _buyerLots.asStateFlow()

    private val _buyerOffersList = MutableStateFlow(
        listOf(
            BuyerOfferRecord("O-1", "OF-0128", "KL-TOM-58291", "Vikram Jadhav", "Tomato", 800.0, 26.0, "Pending"),
            BuyerOfferRecord("O-2", "OF-0127", "KL-TOM-57988", "Vijay Kulkarni", "Tomato", 500.0, 25.0, "Counter Sent"),
            BuyerOfferRecord("O-3", "OF-0124", "KL-ONI-44821", "Priya More", "Onion", 600.0, 17.0, "Accepted")
        )
    )
    val buyerOffersList: StateFlow<List<BuyerOfferRecord>> = _buyerOffersList.asStateFlow()

    private val _buyerDeliveriesList = MutableStateFlow(
        listOf(
            BuyerDeliveryRecord("D-1", "DEL-0089", "Onion", 600.0, "Priya More", "Santosh Transport", "Today, 4 PM", "IN TRANSIT"),
            BuyerDeliveryRecord("D-2", "DEL-0088", "Potato", 2000.0, "Anil Shinde", "Maharashtra Cargo", "Dec 5", "DELIVERED")
        )
    )
    val buyerDeliveriesList: StateFlow<List<BuyerDeliveryRecord>> = _buyerDeliveriesList.asStateFlow()

    private val _buyerPaymentsList = MutableStateFlow(
        listOf(
            BuyerPaymentRecord("P-1", "Priya More", "Onion", "Dec 3", 10200.0, "Paid"),
            BuyerPaymentRecord("P-2", "Anil Shinde", "Potato", "Dec 2", 28000.0, "Paid"),
            BuyerPaymentRecord("P-3", "Vikram Jadhav", "Tomato", "Dec 7", 19000.0, "Pending")
        )
    )
    val buyerPaymentsList: StateFlow<List<BuyerPaymentRecord>> = _buyerPaymentsList.asStateFlow()

    // Buyers
    private val _buyers = MutableStateFlow(
        listOf(
            BuyerUser("B-201", "FreshFoods Pvt Ltd", "Processor", "Sanjay Deshmukh", "+91 98220 11223", "Pune", 42, true, 96, "96% on-time payment history"),
            BuyerUser("B-202", "Sahyadri Agro Processing", "Processor", "Prakash Kadam", "+91 94230 44556", "Karad", 50, true, 98, "98% on-time payment history"),
            BuyerUser("B-203", "Pune Food Processors", "Trader", "Amit Shah", "+91 98900 77889", "Pune", 115, true, 94, "94% on-time payment history"),
            BuyerUser("B-204", "Maharashtra Retail Chain", "Retailer", "Vikas Pawar", "+91 97660 33445", "Kolhapur", 120, false, 88, "88% on-time payment history")
        )
    )
    val buyers: StateFlow<List<BuyerUser>> = _buyers.asStateFlow()


    // Buyer Requirements
    private val _buyerRequirements = MutableStateFlow(
        listOf(
            BuyerRequirement("REQ-501", "B-201", "FreshFoods Pvt Ltd", "Tomato", 5000.0, 26.0, "Pune", 42, "In 3 Days", true),
            BuyerRequirement("REQ-502", "B-202", "Sahyadri Agro Processing", "Tomato", 3000.0, 25.5, "Karad", 50, "In 2 Days", true),
            BuyerRequirement("REQ-503", "B-203", "Pune Food Processors", "Onion", 10000.0, 29.0, "Pune", 115, "In 5 Days", true)
        )
    )
    val buyerRequirements: StateFlow<List<BuyerRequirement>> = _buyerRequirements.asStateFlow()

    // Digital Offers
    private val _offers = MutableStateFlow(
        listOf(
            DigitalOffer("OFF-101", "LOT-8841", "Tomato", 800.0, "FreshFoods Pvt Ltd", 26.0, 26.0, "Pending", "Just Now", listOf("Buyer offered ₹26/kg for 800 kg")),
            DigitalOffer("OFF-102", "LOT-8835", "Onion", 1500.0, "Pune Food Processors", 27.0, 28.0, "Countered", "1 Hour ago", listOf("Buyer offered ₹27/kg", "Farmer countered ₹28/kg"))
        )
    )
    val offers: StateFlow<List<DigitalOffer>> = _offers.asStateFlow()

    // Transport Options
    private val _transports = MutableStateFlow(
        listOf(
            TransportOption("TR-1", "Mahindra Bolero Pickup", 1.5, 1700.0, "Tomorrow Morning", "Ganesh Shinde", "+91 98231 99887", 4.9),
            TransportOption("TR-2", "Tata 407 Truck", 3.5, 1900.0, "Today Evening", "Anand Thorat", "+91 97654 11223", 4.7),
            TransportOption("TR-3", "Eicher 10.90 Truck", 6.0, 1600.0, "Day After Tomorrow", "Sunil Jadhav", "+91 94224 55667", 4.8)
        )
    )
    val transports: StateFlow<List<TransportOption>> = _transports.asStateFlow()

    // Transactions / Payments
    private val _transactions = MutableStateFlow(
        listOf(
            TransactionRecord(
                id = "TX-9001",
                lotId = "LOT-8841",
                cropName = "Tomato",
                quantityKg = 800.0,
                buyerName = "FreshFoods Pvt Ltd",
                pricePerKg = 26.0,
                grossAmount = 20800.0,
                transportCost = 1800.0,
                netRealization = 19000.0,
                status = "Deal Confirmed",
                selectedTransport = "Mahindra Bolero Pickup (₹1,700)",
                date = "10 Sep 2026"
            ),
            TransactionRecord(
                id = "TX-8995",
                lotId = "LOT-8790",
                cropName = "Soybean",
                quantityKg = 2000.0,
                buyerName = "Sahyadri Agro Processing",
                pricePerKg = 48.0,
                grossAmount = 96000.0,
                transportCost = 3500.0,
                netRealization = 92500.0,
                status = "Payment Received",
                selectedTransport = "Eicher 10.90 Truck",
                date = "04 Sep 2026"
            )
        )
    )
    val transactions: StateFlow<List<TransactionRecord>> = _transactions.asStateFlow()

    // FPO Groups
    private val _fpoGroups = MutableStateFlow(
        listOf(
            FpoGroup("FPO-01", "Satara Agro Producer Co.", "Tomato", 10000.0, 6400.0, 26.5, 12),
            FpoGroup("FPO-02", "Karad Krishi Farmers Club", "Onion", 20000.0, 14500.0, 31.0, 24)
        )
    )
    val fpoGroups: StateFlow<List<FpoGroup>> = _fpoGroups.asStateFlow()

    // Government Schemes
    private val _schemes = MutableStateFlow(
        listOf(
            GovernmentScheme(
                id = "SCH-01",
                title = "PM-KISAN Samman Nidhi",
                category = "Direct Income Support",
                benefit = "₹6,000 per year in 3 equal installments",
                eligibility = "Small and marginal farmers owning cultivable land up to 2 hectares.",
                documentsRequired = "Aadhaar Card, Land Ownership Papers (7/12 extract), Bank Passbook",
                howToApply = "Apply via PM-KISAN portal or visit nearest Common Service Center (CSC)."
            ),
            GovernmentScheme(
                id = "SCH-02",
                title = "Pradhan Mantri Fasal Bima Yojana (PMFBY)",
                category = "Crop Insurance",
                benefit = "Financial support in case of crop damage due to natural calamities.",
                eligibility = "All farmers growing notified crops in notified areas.",
                documentsRequired = "Land Sowing Certificate, Aadhaar, Bank Details",
                howToApply = "Register through bank branch or online PMFBY portal before crop cutoff date."
            ),
            GovernmentScheme(
                id = "SCH-03",
                title = "e-NAM Market Linkage Scheme",
                category = "Market Access",
                benefit = "Direct online trading platform connecting to national mandis with zero middleman fee.",
                eligibility = "Registered farmers in e-NAM mandis across Maharashtra.",
                documentsRequired = "APMC Registration, Bank Account",
                howToApply = "Register at local APMC or online eNAM portal."
            )
        )
    )
    val schemes: StateFlow<List<GovernmentScheme>> = _schemes.asStateFlow()

    // Disputes
    private val _disputes = MutableStateFlow(
        listOf(
            DisputeRecord("DSP-301", "TX-8991", "Santosh More", "Maharashtra Retail Chain", "Payment Delay", "Payment delayed beyond 48 hours after delivery confirmation.", "Under Review", "08 Sep 2026"),
            DisputeRecord("DSP-302", "TX-8988", "Ananda Bhosale", "Pune Traders", "Quality Grade Discrepancy", "Buyer claimed B-grade upon arrival for A-grade certified tomatoes.", "Resolved", "02 Sep 2026")
        )
    )
    val disputes: StateFlow<List<DisputeRecord>> = _disputes.asStateFlow()

    // Kisan AI Messages
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("MSG-1", "Kisan AI", "Namaskar Vikramji! 🙏 I am Kisan AI, your agricultural market assistant. Ask me anything in Marathi, Hindi, or English!\n\nOptions:\n• Where to sell Tomato?\n• Modal price explanation?\n• Crop insurance schemes?"),
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Dynamic Actions & State Mutators

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun addCropLot(
        cropName: String,
        quantityKg: Double,
        qualityGrade: QualityGrade,
        isOrganicCertified: Boolean,
        expectedPrice: Double
    ): CropLot {
        val newId = "LOT-${(8842..9999).random()}"
        val lot = CropLot(
            id = newId,
            cropName = cropName,
            quantityKg = quantityKg,
            qualityGrade = qualityGrade,
            isOrganicCertified = isOrganicCertified,
            harvestDate = "Today",
            location = "Satara",
            expectedPricePerKg = expectedPrice,
            status = "Available",
            farmerName = _currentFarmer.value.name
        )
        _lots.value = listOf(lot) + _lots.value
        return lot
    }

    fun addBuyerRequirement(cropName: String, quantityKg: Double, pricePerKg: Double, location: String, deadline: String) {
        val newReq = BuyerRequirement(
            id = "REQ-${(504..999).random()}",
            buyerId = "B-201",
            buyerName = "FreshFoods Pvt Ltd",
            cropName = cropName,
            requiredQuantityKg = quantityKg,
            pricePerKg = pricePerKg,
            location = location,
            distanceKm = 35,
            deadline = deadline,
            verified = true
        )
        _buyerRequirements.value = listOf(newReq) + _buyerRequirements.value
    }

    fun updateOfferStatus(offerId: String, newStatus: String, counterPrice: Double? = null) {
        _offers.value = _offers.value.map { offer ->
            if (offer.id == offerId) {
                val updatedPrice = counterPrice ?: offer.currentOfferedPricePerKg
                val newHistory = offer.history + "Status changed to: $newStatus ${counterPrice?.let { "(₹$it/kg)" } ?: ""}"
                offer.copy(
                    status = newStatus,
                    currentOfferedPricePerKg = updatedPrice,
                    history = newHistory
                )
            } else offer
        }
    }

    fun createOfferForLot(lot: CropLot, buyerName: String, pricePerKg: Double) {
        val newOffer = DigitalOffer(
            id = "OFF-${(103..999).random()}",
            lotId = lot.id,
            cropName = lot.cropName,
            quantityKg = lot.quantityKg,
            buyerName = buyerName,
            initialPricePerKg = pricePerKg,
            currentOfferedPricePerKg = pricePerKg,
            status = "Pending",
            lastUpdated = "Just Now",
            history = listOf("Offer submitted: ₹$pricePerKg/kg for ${lot.quantityKg} kg")
        )
        _offers.value = listOf(newOffer) + _offers.value
    }

    fun selectTransportForTransaction(transactionId: String, transportOption: TransportOption) {
        _transactions.value = _transactions.value.map { tx ->
            if (tx.id == transactionId) {
                tx.copy(
                    selectedTransport = "${transportOption.vehicleName} (₹${transportOption.costRs.toInt()})",
                    status = "Pickup Scheduled"
                )
            } else tx
        }
    }

    fun updateTransactionStatus(transactionId: String, newStatus: String) {
        _transactions.value = _transactions.value.map { tx ->
            if (tx.id == transactionId) {
                tx.copy(status = newStatus)
            } else tx
        }
    }

    fun toggleBuyerVerification(buyerId: String) {
        _buyers.value = _buyers.value.map { buyer ->
            if (buyer.id == buyerId) {
                buyer.copy(verified = !buyer.verified)
            } else buyer
        }
    }

    fun createDispute(
        transactionId: String,
        disputeType: String,
        description: String,
        photoAttached: Boolean,
        buyerName: String = "FreshFoods Pvt Ltd",
        cropInfo: String = "Tomato – 800 kg"
    ): DisputeRecord {
        val ticketId = "KS-DIS-${(1040..9999).random()}"
        val newDispute = DisputeRecord(
            id = ticketId,
            transactionId = transactionId,
            farmerName = _currentFarmer.value.name,
            buyerName = buyerName,
            disputeType = disputeType,
            description = description,
            status = "Open",
            date = "Today",
            ticketId = ticketId,
            cropInfo = cropInfo,
            photoAttached = photoAttached
        )
        _disputes.value = listOf(newDispute) + _disputes.value
        return newDispute
    }

    fun updateDisputeStatus(disputeId: String, newStatus: String) {
        _disputes.value = _disputes.value.map { dispute ->
            if (dispute.id == disputeId || dispute.ticketId == disputeId) {
                dispute.copy(status = newStatus)
            } else dispute
        }
    }


    fun joinFpoGroup(fpoId: String, quantityKg: Double) {
        _fpoGroups.value = _fpoGroups.value.map { fpo ->
            if (fpo.id == fpoId) {
                fpo.copy(
                    collectedQuantityKg = fpo.collectedQuantityKg + quantityKg,
                    memberCount = fpo.memberCount + 1
                )
            } else fpo
        }
    }

    fun sendChatMessage(userText: String) {
        val userMsg = ChatMessage("MSG-${System.currentTimeMillis()}", "User", userText)
        _chatMessages.value = _chatMessages.value + userMsg

        val lang = _selectedLanguage.value
        val aiReplyText = when {
            userText.contains("tomato", ignoreCase = true) || userText.contains("टोमॅटो", ignoreCase = true) || userText.contains("टमाटर", ignoreCase = true) -> when (lang) {
                AppLanguage.MARATHI ->
                    "🍅 नमुना महाराष्ट्र मंडी दरांनुसार (Sample Data):\n• पुणे गुलटेकडी: ₹23/kg (सकाळी ₹24 -> दुपारी ₹22 -> सद्य ₹23/kg)\n• मुंबई वाशी APMC: ₹27.5/kg\n• सातारा APMC: ₹22.5/kg\n\nशिफारस: अधिक निव्वळ उत्पन्नासाठी पुणे/मुंबई मंडी किंवा थेट खरेदीदार तपासा!"
                AppLanguage.HINDI ->
                    "🍅 नमूना महाराष्ट्र मंडी दरों के अनुसार (Sample Data):\n• पुणे गुलटेकडी: ₹23/kg (सुबह ₹24 -> दोपहर ₹22 -> ताज़ा ₹23/kg)\n• मुंबई वाशी APMC: ₹27.5/kg\n• सातारा APMC: ₹22.5/kg\n\nसुझाव: बेहतर शुद्ध आय के लिए पुणे/मुंबई मंडी या सीधे खरीदार से तुलना करें!"
                AppLanguage.ENGLISH ->
                    "🍅 Based on sample Maharashtra Mandi data:\n• Pune Gultekdi: ₹23/kg (08:00 AM ₹24 -> 12:00 PM ₹22 -> 03:00 PM ₹23/kg)\n• Mumbai Vashi APMC: ₹27.5/kg\n• Satara APMC: ₹22.5/kg\n\nRecommendation: Compare transport-adjusted net realization in Pune/Mumbai or sell to verified direct buyers!"
            }

            userText.contains("modal", ignoreCase = true) || userText.contains("मोडल", ignoreCase = true) || userText.contains("भाव", ignoreCase = true) -> when (lang) {
                AppLanguage.MARATHI ->
                    "📊 मोडल भाव म्हणजे बाजारात सर्वाधिक व्यवहारांचा सरासरी दर. पुणे गुलटेकडी येथे टोमॅटोचा सद्य नमुना मोडल भाव ₹23/kg आहे (किमान ₹20.5/kg, कमाल ₹25.5/kg)."
                AppLanguage.HINDI ->
                    "📊 मोडल भाव बाजार में सबसे अधिक होने वाले सौदों का औसत भाव है। पुणे गुलटेकडी में टमाटर का ताज़ा नमूना मोडल भाव ₹23/kg है (न्यूनतम ₹20.5/kg, अधिकतम ₹25.5/kg)।"
                AppLanguage.ENGLISH ->
                    "📊 Modal Price is the most frequent trading price in the market. In Pune Gultekdi (Sample Data), Tomato Modal Price is ₹23/kg with Min ₹20.5/kg and Max ₹25.5/kg."
            }

            userText.contains("lot", ignoreCase = true) || userText.contains("create", ignoreCase = true) || userText.contains("लॉट", ignoreCase = true) || userText.contains("पीक", ignoreCase = true) -> when (lang) {
                AppLanguage.MARATHI ->
                    "📦 पीक लॉट तयार करण्यासाठी:\n1. होम स्क्रीनवर '+ पीक जोडा' वर टॅप करा\n2. पीक आणि वजन (kg) निवडा\n3. दर्जा श्रेणी (Grade A/B/C) निवडा\n4. 'पीक लॉट तयार करा' दाबा!"
                AppLanguage.HINDI ->
                    "📦 फसल लॉट बनाने के लिए:\n1. होम स्क्रीन पर '+ फसल जोड़ें' दबाएं\n2. फसल और मात्रा (kg) चुनें\n3. गुणवत्ता ग्रेड (Grade A/B/C) चुनें\n4. 'फसल लॉट बनाएं' पर टैप करें!"
                AppLanguage.ENGLISH ->
                    "📦 To create a lot:\n1. Tap '+ Add Crop' on Home\n2. Select Crop & Quantity\n3. Choose Quality Grade (Single Choice)\n4. Tap 'Create Lot'\nYour lot will instantly be visible to verified buyers!"
            }

            userText.contains("insurance", ignoreCase = true) || userText.contains("scheme", ignoreCase = true) || userText.contains("योजना", ignoreCase = true) || userText.contains("विमा", ignoreCase = true) || userText.contains("बीमा", ignoreCase = true) -> when (lang) {
                AppLanguage.MARATHI ->
                    "🌾 शिफारस केलेली योजना: प्रधानमंत्री फसल विमा योजना (PMFBY). नैसर्गिक आपत्तीमुळे पिकांच्या नुकसानीस संरक्षण मिळते. शेतकऱ्यांसाठी फक्त १.५% ते २% प्रीमियम!"
                AppLanguage.HINDI ->
                    "🌾 अनुशंसित योजना: प्रधानमंत्री फसल बीमा योजना (PMFBY)। बारिश या सूखे से फसल नुकसान पर सुरक्षा। किसानों के लिए केवल 1.5% - 2% प्रीमियम!"
                AppLanguage.ENGLISH ->
                    "🌾 Recommended Scheme: PM Fasal Bima Yojana (PMFBY). Covers crop loss due to rain/drought. Premium is only 1.5% - 2% for farmers!"
            }

            userText.contains("payment", ignoreCase = true) || userText.contains("पेमेंट", ignoreCase = true) || userText.contains("भुगतान", ignoreCase = true) -> when (lang) {
                AppLanguage.MARATHI ->
                    "💳 तुमचा शेवटचा व्यवहार TX-9001 (₹19,000, ८०० किलो टोमॅटो) सध्या 'व्यवहार निश्चित' (Deal Confirmed) स्थितीत आहे."
                AppLanguage.HINDI ->
                    "💳 आपका नवीनतम लेनदेन TX-9001 (₹19,000, 800 किलो टमाटर) वर्तमान में 'सौदा पक्का' (Deal Confirmed) स्थिति में है।"
                AppLanguage.ENGLISH ->
                    "💳 Your latest transaction TX-9001 (₹19,000) for 800 kg Tomato is currently 'Deal Confirmed'. Escrow funds are secured."
            }

            else -> when (lang) {
                AppLanguage.MARATHI ->
                    "💡 कृषीसेतू टीप: 'बाजार तुलना' स्क्रीनवर ३५ पिके आणि ३० महाराष्ट्र बाजार समित्यांचे वाहतूक खर्चासह निव्वळ उत्पन्न तपासा!"
                AppLanguage.HINDI ->
                    "💡 कृषिसेतु टिप: 'बाजार तुलना' स्क्रीन पर 35 फसलों और 30 महाराष्ट्र मंडियों की परिवहन खर्च सहित शुद्ध आय की तुलना करें!"
                AppLanguage.ENGLISH ->
                    "💡 KrishiSetu Tip: Compare transport-adjusted net realization across 35 commodities and 30 Maharashtra markets on the Compare Markets screen!"
            }
        }

        val aiMsg = ChatMessage("MSG-AI-${System.currentTimeMillis()}", "Kisan AI", aiReplyText)
        _chatMessages.value = _chatMessages.value + aiMsg
    }

    // Buyer Mutators & Verification Controls
    fun registerBuyerProfile(
        companyName: String,
        businessType: String,
        contactPerson: String,
        companyEmail: String,
        phone: String,
        state: String,
        district: String,
        city: String,
        gstNumber: String,
        pan: String,
        fssaiLicense: String
    ) {
        _currentBuyerProfile.value = _currentBuyerProfile.value.copy(
            companyName = companyName,
            businessType = businessType,
            contactPerson = contactPerson,
            companyEmail = companyEmail,
            phone = phone,
            state = state,
            district = district,
            city = city,
            gstNumber = gstNumber,
            pan = pan,
            fssaiLicense = fssaiLicense,
            isVerified = false,
            gstStatus = DocStatus.SUBMITTED,
            panStatus = DocStatus.SUBMITTED,
            businessRegStatus = DocStatus.SUBMITTED,
            fssaiStatus = DocStatus.SUBMITTED,
            addressProofStatus = DocStatus.SUBMITTED
        )
    }

    fun uploadBuyerDocument(docType: String) {
        val profile = _currentBuyerProfile.value
        _currentBuyerProfile.value = when (docType) {
            "GST Certificate" -> profile.copy(gstStatus = DocStatus.SUBMITTED)
            "PAN Card" -> profile.copy(panStatus = DocStatus.SUBMITTED)
            "Business Registration" -> profile.copy(businessRegStatus = DocStatus.SUBMITTED)
            "FSSAI License" -> profile.copy(fssaiStatus = DocStatus.SUBMITTED)
            "Address Proof" -> profile.copy(addressProofStatus = DocStatus.SUBMITTED)
            else -> profile
        }
    }

    fun submitBuyerDocsForVerification() {
        val profile = _currentBuyerProfile.value
        _currentBuyerProfile.value = profile.copy(
            isVerified = true, // Simulate instant verification for prototype after doc submission
            gstStatus = DocStatus.VERIFIED,
            panStatus = DocStatus.VERIFIED,
            businessRegStatus = DocStatus.VERIFIED,
            fssaiStatus = DocStatus.VERIFIED,
            addressProofStatus = DocStatus.VERIFIED
        )
    }

    fun setBuyerVerificationStatus(isVerified: Boolean) {
        val docStat = if (isVerified) DocStatus.VERIFIED else DocStatus.NOT_SUBMITTED
        _currentBuyerProfile.value = _currentBuyerProfile.value.copy(
            isVerified = isVerified,
            gstStatus = docStat,
            panStatus = docStat,
            businessRegStatus = docStat,
            fssaiStatus = docStat,
            addressProofStatus = docStat
        )
    }

    fun makeOfferOnBuyerLot(lot: BuyerLot, pricePerKg: Double) {
        val offerCode = "OF-${(129..999).random()}"
        val newOffer = BuyerOfferRecord(
            id = "O-${(10..99).random()}",
            offerCode = offerCode,
            lotCode = lot.lotCode,
            farmerName = lot.farmerName,
            cropName = lot.cropName,
            quantityKg = lot.quantityKg,
            pricePerKg = pricePerKg,
            status = "Pending"
        )
        _buyerOffersList.value = listOf(newOffer) + _buyerOffersList.value
    }

    fun updateBuyerOfferRecordStatus(offerId: String, newStatus: String) {
        _buyerOffersList.value = _buyerOffersList.value.map { offer ->
            if (offer.id == offerId || offer.offerCode == offerId) {
                offer.copy(status = newStatus)
            } else offer
        }
    }

    fun confirmBuyerDelivery(deliveryId: String) {
        _buyerDeliveriesList.value = _buyerDeliveriesList.value.map { del ->
            if (del.id == deliveryId || del.deliveryCode == deliveryId) {
                del.copy(status = "DELIVERED")
            } else del
        }
    }

    // Admin State & Operations
    private val _adminFarmers = MutableStateFlow(
        listOf(
            AdminFarmerRecord("ADM-F-1", "Vikram Jadhav", "Nashik, MH", "Maharashtra", 2, "Pending Verification"),
            AdminFarmerRecord("ADM-F-2", "Suresh Jadhav", "Sinnar, MH", "Maharashtra", 1, "Active", "Verified", "Verified", "Verified", "Verified"),
            AdminFarmerRecord("ADM-F-3", "Priya More", "Niphad, MH", "Maharashtra", 1, "Active", "Verified", "Verified", "Verified", "Verified"),
            AdminFarmerRecord("ADM-F-4", "Santosh Shinde", "Satara, MH", "Maharashtra", 0, "Pending Verification"),
            AdminFarmerRecord("ADM-F-5", "Vijay Deshmukh", "Rajkot, GJ", "Gujarat", 3, "Active", "Verified", "Verified", "Verified", "Verified")
        )
    )
    val adminFarmers: StateFlow<List<AdminFarmerRecord>> = _adminFarmers.asStateFlow()

    private val _adminBuyers = MutableStateFlow(
        listOf(
            AdminBuyerRecord("ADM-B-1", "Agro Vision Ltd", "Food Processor", "Pune", "Applied Dec 4", "4/5", "Low Risk", "Pending"),
            AdminBuyerRecord("ADM-B-2", "Kisan Bazaar Pvt", "Trader", "Nashik", "Applied Dec 3", "3/5", "Medium Risk", "Pending"),
            AdminBuyerRecord("ADM-B-3", "Fresh Direct India", "Retailer", "Mumbai", "Applied Dec 2", "5/5", "Low Risk", "Pending"),
            AdminBuyerRecord("ADM-B-4", "FreshFoods Pvt Ltd", "Food Processor & Trader", "Pune", "Applied Dec 1", "5/5", "Low Risk", "Approved", "Verified", "Verified", "Verified", "Verified", "Verified")
        )
    )
    val adminBuyers: StateFlow<List<AdminBuyerRecord>> = _adminBuyers.asStateFlow()

    private val _marketFeeds = MutableStateFlow(
        listOf(
            MarketFeedStatusRecord("MF-1", "Pune APMC", "Live", "Last fetch: 2 min ago", 847),
            MarketFeedStatusRecord("MF-2", "Mumbai Vashi APMC", "Live", "Last fetch: 5 min ago", 1243),
            MarketFeedStatusRecord("MF-3", "Nashik APMC", "Live", "Last fetch: 1 min ago", 623),
            MarketFeedStatusRecord("MF-4", "Satara APMC", "Delayed", "Last fetch: 4 hrs ago", 234),
            MarketFeedStatusRecord("MF-5", "Kolhapur APMC", "Offline", "Last fetch: Yesterday", 189)
        )
    )
    val marketFeeds: StateFlow<List<MarketFeedStatusRecord>> = _marketFeeds.asStateFlow()

    fun approveAdminFarmer(farmerId: String) {
        _adminFarmers.value = _adminFarmers.value.map { f ->
            if (f.id == farmerId || f.name.contains("Vikram", ignoreCase = true)) {
                f.copy(
                    status = "Onboarded",
                    aadhaarStatus = "Verified",
                    sevenTwelveStatus = "Verified",
                    gatNumberStatus = "Verified",
                    bankDetailsStatus = "Verified"
                )
            } else f
        }

        // Conceptually connect to current farmer if Vikram Jadhav
        _currentFarmer.value = _currentFarmer.value.copy(
            verified = true,
            ekycStatus = EkycStatus.VERIFIED,
            aadhaarStatus = DocStatus.VERIFIED,
            sevenTwelveStatus = DocStatus.VERIFIED,
            gatNumberStatus = DocStatus.VERIFIED,
            bankDetailsStatus = DocStatus.VERIFIED
        )
    }

    fun rejectAdminFarmer(farmerId: String, reason: String) {
        _adminFarmers.value = _adminFarmers.value.map { f ->
            if (f.id == farmerId || f.name.contains("Vikram", ignoreCase = true)) {
                f.copy(status = "Rejected", rejectionReason = reason)
            } else f
        }
        _currentFarmer.value = _currentFarmer.value.copy(
            verified = false,
            ekycStatus = EkycStatus.NOT_VERIFIED
        )
    }

    fun approveAdminBuyer(buyerId: String) {
        _adminBuyers.value = _adminBuyers.value.map { b ->
            if (b.id == buyerId || b.organisationName.contains("FreshFoods", ignoreCase = true) || b.organisationName.contains("Agro Vision", ignoreCase = true)) {
                b.copy(
                    status = "Approved",
                    gstStatus = "Verified",
                    panStatus = "Verified",
                    busRegStatus = "Verified",
                    fssaiStatus = "Verified",
                    addressProofStatus = "Verified"
                )
            } else b
        }
        _currentBuyerProfile.value = _currentBuyerProfile.value.copy(
            isVerified = true,
            gstStatus = DocStatus.VERIFIED,
            panStatus = DocStatus.VERIFIED,
            businessRegStatus = DocStatus.VERIFIED,
            fssaiStatus = DocStatus.VERIFIED,
            addressProofStatus = DocStatus.VERIFIED
        )
        _buyers.value = _buyers.value.map { buyer ->
            if (buyer.companyName.contains("FreshFoods", ignoreCase = true)) {
                buyer.copy(verified = true)
            } else buyer
        }
    }

    fun rejectAdminBuyer(buyerId: String, reason: String) {
        _adminBuyers.value = _adminBuyers.value.map { b ->
            if (b.id == buyerId || b.organisationName.contains("FreshFoods", ignoreCase = true)) {
                b.copy(status = "Rejected", rejectionReason = reason)
            } else b
        }
        _currentBuyerProfile.value = _currentBuyerProfile.value.copy(isVerified = false)
        _buyers.value = _buyers.value.map { buyer ->
            if (buyer.companyName.contains("FreshFoods", ignoreCase = true)) {
                buyer.copy(verified = false)
            } else buyer
        }
    }

    fun forceRefreshMarketFeed(feedId: String) {
        _marketFeeds.value = _marketFeeds.value.map { mf ->
            if (mf.id == feedId || feedId == "ALL") {
                mf.copy(
                    status = "Live",
                    lastFetchText = "Last fetch: Just now",
                    recordsCount = mf.recordsCount + 15
                )
            } else mf
        }
    }

    fun updateGovernmentSchemeInfo(schemeId: String, benefit: String, eligibility: String) {
        _schemes.value = _schemes.value.map { s ->
            if (s.id == schemeId) {
                s.copy(benefit = benefit, eligibility = eligibility)
            } else s
        }
    }

    fun resolveDisputeWithNote(disputeId: String, note: String) {
        _disputes.value = _disputes.value.map { d ->
            if (d.id == disputeId || d.ticketId == disputeId) {
                d.copy(status = "Resolved", description = "${d.description}\n\nResolution: $note")
            } else d
        }
    }
}


