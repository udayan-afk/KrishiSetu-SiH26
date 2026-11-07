package com.krishisetu.app.viewmodel

import androidx.lifecycle.ViewModel
import com.krishisetu.app.models.*
import com.krishisetu.app.repository.KrishiSetuRepository
import kotlinx.coroutines.flow.StateFlow

class KrishiSetuViewModel(
    private val repository: KrishiSetuRepository = KrishiSetuRepository()
) : ViewModel() {

    val selectedLanguage: StateFlow<AppLanguage> = repository.selectedLanguage
    val currentRole: StateFlow<UserRole> = repository.currentRole
    val currentFarmer: StateFlow<FarmerUser> = repository.currentFarmer
    val lots: StateFlow<List<CropLot>> = repository.lots
    val commodities: List<CommodityInfo> = repository.commodities
    val marketYards: List<MarketYardInfo> = repository.marketYards
    val marketPrices: StateFlow<List<MarketPriceData>> = repository.marketPrices
    val selectedCommodity: StateFlow<String> = repository.selectedCommodity
    val selectedCategory: StateFlow<CommodityCategory> = repository.selectedCategory
    val selectedMarketId: StateFlow<String> = repository.selectedMarketId
    val comparisonQuantityKg: StateFlow<Double> = repository.comparisonQuantityKg
    val buyers: StateFlow<List<BuyerUser>> = repository.buyers
    val buyerRequirements: StateFlow<List<BuyerRequirement>> = repository.buyerRequirements
    val offers: StateFlow<List<DigitalOffer>> = repository.offers
    val transports: StateFlow<List<TransportOption>> = repository.transports
    val transactions: StateFlow<List<TransactionRecord>> = repository.transactions
    val fpoGroups: StateFlow<List<FpoGroup>> = repository.fpoGroups
    val schemes: StateFlow<List<GovernmentScheme>> = repository.schemes
    val disputes: StateFlow<List<DisputeRecord>> = repository.disputes
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages

    // Buyer Specific State Flow
    val currentBuyerProfile: StateFlow<BuyerProfile> = repository.currentBuyerProfile
    val buyerLots: StateFlow<List<BuyerLot>> = repository.buyerLots
    val buyerOffersList: StateFlow<List<BuyerOfferRecord>> = repository.buyerOffersList
    val buyerDeliveriesList: StateFlow<List<BuyerDeliveryRecord>> = repository.buyerDeliveriesList
    val buyerPaymentsList: StateFlow<List<BuyerPaymentRecord>> = repository.buyerPaymentsList

    // Admin State Flow
    val adminFarmers: StateFlow<List<AdminFarmerRecord>> = repository.adminFarmers
    val adminBuyers: StateFlow<List<AdminBuyerRecord>> = repository.adminBuyers
    val marketFeeds: StateFlow<List<MarketFeedStatusRecord>> = repository.marketFeeds

    fun approveAdminFarmer(farmerId: String) {
        repository.approveAdminFarmer(farmerId)
    }

    fun rejectAdminFarmer(farmerId: String, reason: String) {
        repository.rejectAdminFarmer(farmerId, reason)
    }

    fun approveAdminBuyer(buyerId: String) {
        repository.approveAdminBuyer(buyerId)
    }

    fun rejectAdminBuyer(buyerId: String, reason: String) {
        repository.rejectAdminBuyer(buyerId, reason)
    }

    fun forceRefreshMarketFeed(feedId: String) {
        repository.forceRefreshMarketFeed(feedId)
    }

    fun updateGovernmentSchemeInfo(schemeId: String, benefit: String, eligibility: String) {
        repository.updateGovernmentSchemeInfo(schemeId, benefit, eligibility)
    }

    fun resolveDisputeWithNote(disputeId: String, note: String) {
        repository.resolveDisputeWithNote(disputeId, note)
    }

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
        repository.registerBuyerProfile(
            companyName, businessType, contactPerson, companyEmail, phone,
            state, district, city, gstNumber, pan, fssaiLicense
        )
    }

    fun uploadBuyerDocument(docType: String) {
        repository.uploadBuyerDocument(docType)
    }

    fun submitBuyerDocsForVerification() {
        repository.submitBuyerDocsForVerification()
    }

    fun setBuyerVerificationStatus(isVerified: Boolean) {
        repository.setBuyerVerificationStatus(isVerified)
    }

    fun makeOfferOnBuyerLot(lot: BuyerLot, pricePerKg: Double) {
        repository.makeOfferOnBuyerLot(lot, pricePerKg)
    }

    fun updateBuyerOfferRecordStatus(offerId: String, newStatus: String) {
        repository.updateBuyerOfferRecordStatus(offerId, newStatus)
    }

    fun confirmBuyerDelivery(deliveryId: String) {
        repository.confirmBuyerDelivery(deliveryId)
    }

    fun setLanguage(language: AppLanguage) {

        repository.setLanguage(language)
    }

    fun selectRole(role: UserRole) {
        repository.setRole(role)
    }

    fun registerFarmer(
        name: String,
        mobile: String,
        state: String,
        district: String,
        taluka: String,
        village: String
    ) {
        repository.registerFarmer(name, mobile, state, district, taluka, village)
    }

    fun uploadAadhaar(fileName: String = "aadhaar_card_scan.pdf") {
        repository.uploadAadhaar(fileName)
    }

    fun uploadSevenTwelve(fileName: String = "7_12_extract_satara.pdf") {
        repository.uploadSevenTwelve(fileName)
    }

    fun updateGatNumber(gatNumber: String) {
        repository.updateGatNumber(gatNumber)
    }

    fun updateBankDetails(bankDetails: String) {
        repository.updateBankDetails(bankDetails)
    }

    fun uploadOtherDoc(fileName: String = "land_declaration_satara.pdf") {
        repository.uploadOtherDoc(fileName)
    }

    fun submitDocumentsForVerification() {
        repository.submitDocumentsForVerification()
    }

    fun setMockEkycStatus(status: EkycStatus) {
        repository.setMockEkycStatus(status)
    }

    fun createCropLot(
        cropName: String,
        quantityKg: Double,
        qualityGrade: QualityGrade,
        isOrganicCertified: Boolean,
        expectedPrice: Double
    ): CropLot {
        return repository.addCropLot(cropName, quantityKg, qualityGrade, isOrganicCertified, expectedPrice)
    }

    fun createBuyerRequirement(cropName: String, quantityKg: Double, pricePerKg: Double, location: String, deadline: String) {
        repository.addBuyerRequirement(cropName, quantityKg, pricePerKg, location, deadline)
    }

    fun updateOfferStatus(offerId: String, newStatus: String, counterPrice: Double? = null) {
        repository.updateOfferStatus(offerId, newStatus, counterPrice)
    }

    fun makeBuyerOffer(lot: CropLot, buyerName: String, pricePerKg: Double) {
        repository.createOfferForLot(lot, buyerName, pricePerKg)
    }

    fun selectTransport(transactionId: String, transportOption: TransportOption) {
        repository.selectTransportForTransaction(transactionId, transportOption)
    }

    fun updateTransactionStatus(transactionId: String, newStatus: String) {
        repository.updateTransactionStatus(transactionId, newStatus)
    }

    fun toggleBuyerVerification(buyerId: String) {
        repository.toggleBuyerVerification(buyerId)
    }

    fun createDispute(
        transactionId: String,
        disputeType: String,
        description: String,
        photoAttached: Boolean,
        buyerName: String = "FreshFoods Pvt Ltd",
        cropInfo: String = "Tomato – 800 kg"
    ): DisputeRecord {
        return repository.createDispute(transactionId, disputeType, description, photoAttached, buyerName, cropInfo)
    }

    fun updateDisputeStatus(disputeId: String, newStatus: String) {
        repository.updateDisputeStatus(disputeId, newStatus)
    }


    fun joinFpo(fpoId: String, quantityKg: Double) {
        repository.joinFpoGroup(fpoId, quantityKg)
    }

    fun sendChatMessage(text: String) {
        repository.sendChatMessage(text)
    }

    fun selectCommodity(cropName: String) {
        repository.setSelectedCommodity(cropName)
    }

    fun selectCategory(category: CommodityCategory) {
        repository.setSelectedCategory(category)
    }

    fun selectMarket(marketId: String) {
        repository.setSelectedMarketId(marketId)
    }

    fun updateComparisonQuantityKg(quantityKg: Double) {
        repository.setComparisonQuantityKg(quantityKg)
    }
}
