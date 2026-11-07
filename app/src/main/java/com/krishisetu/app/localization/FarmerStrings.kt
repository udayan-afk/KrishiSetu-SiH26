package com.krishisetu.app.localization

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.krishisetu.app.R
import com.krishisetu.app.models.AppLanguage
import com.krishisetu.app.models.AuctionStatus
import com.krishisetu.app.models.CommodityCategory
import com.krishisetu.app.models.GovernmentScheme
import com.krishisetu.app.models.MarketDataStatus
import com.krishisetu.app.models.MarketType

class FarmerStrings(
    val language: AppLanguage = AppLanguage.ENGLISH,
    res: Resources
) {
    // App & Navigation
    val appName: String = res.getString(R.string.app_name)
    val appTagline: String = res.getString(R.string.app_tagline)
    val navHome: String = res.getString(R.string.nav_home)
    val navMarkets: String = res.getString(R.string.nav_markets)
    val navBuyers: String = res.getString(R.string.nav_buyers)
    val navMyLots: String = res.getString(R.string.nav_my_lots)
    val navKisanAI: String = res.getString(R.string.nav_kisan_ai)

    // Language Selection
    val selectLanguageTitle: String = res.getString(R.string.select_language_title)
    val selectLanguageSubtitle: String = res.getString(R.string.select_language_subtitle)
    val continueButton: String = res.getString(R.string.continue_button)

    // Role & Auth
    val whoAreYou: String = res.getString(R.string.who_are_you)
    val selectRoleSubtitle: String = res.getString(R.string.select_role_subtitle)
    val farmerRoleTitle: String = res.getString(R.string.farmer_role_title)
    val farmerRoleSubtitle: String = res.getString(R.string.farmer_role_subtitle)
    val farmerRoleDesc: String = res.getString(R.string.farmer_role_desc)
    val farmerLoginTitle: String = res.getString(R.string.farmer_login_title)
    val enterMobilePrompt: String = res.getString(R.string.enter_mobile_prompt)
    val mobileNumberLabel: String = res.getString(R.string.mobile_number_label)
    val sendOtpBtn: String = res.getString(R.string.send_otp_btn)
    val verifyLoginBtn: String = res.getString(R.string.verify_login_btn)
    val newFarmerRegisterBtn: String = res.getString(R.string.new_farmer_register_btn)
    val registerTitle: String = res.getString(R.string.register_title)
    val registerSubtitle: String = res.getString(R.string.register_subtitle)
    val fullNameLabel: String = res.getString(R.string.full_name_label)
    val villageCityLabel: String = res.getString(R.string.village_city_label)
    val createAccountBtn: String = res.getString(R.string.create_account_btn)
    val backToLogin: String = res.getString(R.string.back_to_login)

    // Home Screen
    val greetingPrefix: String = res.getString(R.string.greeting_prefix)
    val todayBestOppTitle: String = res.getString(R.string.today_best_opp_title)
    val todayBestOppSubtitle: String = res.getString(R.string.today_best_opp_subtitle)
    val quickActionsHeader: String = res.getString(R.string.quick_actions_header)
    val actionAddCrop: String = res.getString(R.string.action_add_crop)
    val actionAddCropSub: String = res.getString(R.string.action_add_crop_sub)
    val actionBestPlace: String = res.getString(R.string.action_best_place)
    val actionBestPlaceSub: String = res.getString(R.string.action_best_place_sub)
    val actionMarketPrices: String = res.getString(R.string.action_market_prices)
    val actionMarketPricesSub: String = res.getString(R.string.action_market_prices_sub)
    val actionCompareMarkets: String = res.getString(R.string.action_compare_markets)
    val actionCompareMarketsSub: String = res.getString(R.string.action_compare_markets_sub)
    val actionFindBuyers: String = res.getString(R.string.action_find_buyers)
    val actionFindBuyersSub: String = res.getString(R.string.action_find_buyers_sub)
    val actionKisanAI: String = res.getString(R.string.action_kisan_ai)
    val actionKisanAISub: String = res.getString(R.string.action_kisan_ai_sub)
    val actionProfitCalc: String = res.getString(R.string.action_profit_calc)
    val actionProfitCalcSub: String = res.getString(R.string.action_profit_calc_sub)
    val actionSchemesMap: String = res.getString(R.string.action_schemes_map)
    val actionSchemesMapSub: String = res.getString(R.string.action_schemes_map_sub)
    val myLotsHeader: String = res.getString(R.string.my_lots_header)
    val targetPriceLabel: String = res.getString(R.string.target_price_label)
    val lotIdLabel: String = res.getString(R.string.lot_id_label)
    val qualityLabel: String = res.getString(R.string.quality_label)

    // Create Lot Wizard
    val createLotWizardTitle: String = res.getString(R.string.create_lot_wizard_title)
    val stepProgress: String = res.getString(R.string.step_progress)
    val step1Title: String = res.getString(R.string.step1_title)
    val step2Title: String = res.getString(R.string.step2_title)
    val step3Title: String = res.getString(R.string.step3_title)
    val step4Title: String = res.getString(R.string.step4_title)
    val step5Title: String = res.getString(R.string.step5_title)
    val selectCropLabel: String = res.getString(R.string.select_crop_label)
    val quantityKgLabel: String = res.getString(R.string.quantity_kg_label)
    val expectedPriceLabel: String = res.getString(R.string.expected_price_label)
    val qualityGradeHeader: String = res.getString(R.string.quality_grade_header)
    val gradeALabel: String = res.getString(R.string.grade_a_label)
    val gradeBLabel: String = res.getString(R.string.grade_b_label)
    val gradeCLabel: String = res.getString(R.string.grade_c_label)
    val organicToggleLabel: String = res.getString(R.string.organic_toggle_label)
    val organicToggleSub: String = res.getString(R.string.organic_toggle_sub)
    val uploadPhotosHeader: String = res.getString(R.string.upload_photos_header)
    val photosSubtext: String = res.getString(R.string.photos_subtext)
    val reviewSummaryHeader: String = res.getString(R.string.review_summary_header)
    val createLotNowBtn: String = res.getString(R.string.create_lot_now_btn)
    val estGrossValue: String = res.getString(R.string.est_gross_value)

    // Market Prices & Comparison
    val marketPricesTitle: String = res.getString(R.string.market_prices_title)
    val marketPricesSubtitle: String = res.getString(R.string.market_prices_subtitle)
    val highestRateLabel: String = res.getString(R.string.highest_rate_label)
    val currentRateLabel: String = res.getString(R.string.current_rate_label)
    val modalPriceLabel: String = res.getString(R.string.modal_price_label)
    val maxRateLabel: String = res.getString(R.string.max_rate_label)
    val priceTrend7Days: String = res.getString(R.string.price_trend_7days)
    val compareMarketCostsBtn: String = res.getString(R.string.compare_market_costs_btn)
    val compareMarketSub: String = res.getString(R.string.compare_market_sub)
    val marketComparisonTitle: String = res.getString(R.string.market_comparison_title)
    val netRealizationFormula: String = res.getString(R.string.net_realization_formula)
    val bestValueBadge: String = res.getString(R.string.best_value_badge)
    val viewRecommendedDealBtn: String = res.getString(R.string.view_recommended_deal_btn)
    val estNetLabel: String = res.getString(R.string.est_net_label)

    // Best Place to Sell & Sell or Wait
    val bestPlaceTitle: String = res.getString(R.string.best_place_title)
    val topRecommendationBadge: String = res.getString(R.string.top_recommendation_badge)
    val grossAmountLabel: String = res.getString(R.string.gross_amount_label)
    val transportCostLabel: String = res.getString(R.string.transport_cost_label)
    val commissionFeeLabel: String = res.getString(R.string.commission_fee_label)
    val netRealizationTitle: String = res.getString(R.string.net_realization_title)
    val sellNowOrWaitTitle: String = res.getString(R.string.sell_now_or_wait_title)
    val sellNowRecommendation: String = res.getString(R.string.sell_now_recommendation)
    val marketIndicatorsTitle: String = res.getString(R.string.market_indicators_title)
    val proceedToSellBtn: String = res.getString(R.string.proceed_to_sell_btn)

    // Buyer Marketplace & Offers
    val buyerMarketplaceTitle: String = res.getString(R.string.buyer_marketplace_title)
    val searchBuyersPlaceholder: String = res.getString(R.string.search_buyers_placeholder)
    val makeOfferBtn: String = res.getString(R.string.make_offer_btn)
    val digitalOfferTitle: String = res.getString(R.string.digital_offer_title)
    val offeredPriceLabel: String = res.getString(R.string.offered_price_label)
    val negotiationHistoryHeader: String = res.getString(R.string.negotiation_history_header)
    val acceptOfferBtn: String = res.getString(R.string.accept_offer_btn)
    val counterOfferBtn: String = res.getString(R.string.counter_offer_btn)
    val rejectOfferBtn: String = res.getString(R.string.reject_offer_btn)
    val offerAcceptedSuccess: String = res.getString(R.string.offer_accepted_success)
    val selectLogisticsBtn: String = res.getString(R.string.select_logistics_btn)

    // Logistics & FPO
    val logisticsTitle: String = res.getString(R.string.logistics_title)
    val availableVehiclesHeader: String = res.getString(R.string.available_vehicles_header)
    val confirmTransportBtn: String = res.getString(R.string.confirm_transport_btn)
    val fpoAggregationTitle: String = res.getString(R.string.fpo_aggregation_title)
    val joinFpoBtn: String = res.getString(R.string.join_fpo_btn)

    // Profit Calculator
    val profitCalcTitle: String = res.getString(R.string.profit_calc_title)
    val calcInputsHeader: String = res.getString(R.string.calc_inputs_header)
    val storageCostLabel: String = res.getString(R.string.storage_cost_label)
    val handlingCostLabel: String = res.getString(R.string.handling_cost_label)
    val totalExpensesLabel: String = res.getString(R.string.total_expenses_label)
    val grossRevenueLabel: String = res.getString(R.string.gross_revenue_label)

    // Payments & Schemes
    val paymentsTitle: String = res.getString(R.string.payments_title)
    val settlementEarningsHeader: String = res.getString(R.string.settlement_earnings_header)
    val statusTimelineHeader: String = res.getString(R.string.status_timeline_header)
    val schemesTitle: String = res.getString(R.string.schemes_title)
    val benefitLabel: String = res.getString(R.string.benefit_label)
    val eligibilityLabel: String = res.getString(R.string.eligibility_label)
    val documentsLabel: String = res.getString(R.string.documents_label)

    // Status Labels
    val statusDealConfirmed: String = res.getString(R.string.status_deal_confirmed)
    val statusPickupScheduled: String = res.getString(R.string.status_pickup_scheduled)
    val statusInTransit: String = res.getString(R.string.status_in_transit)
    val statusDelivered: String = res.getString(R.string.status_delivered)
    val statusPaymentInitiated: String = res.getString(R.string.status_payment_initiated)
    val statusPaymentReceived: String = res.getString(R.string.status_payment_received)

    // Kisan AI & Map & Profile
    val kisanAITitle: String = res.getString(R.string.kisan_ai_title)
    val askAiPlaceholder: String = res.getString(R.string.ask_ai_placeholder)
    val mapTitle: String = res.getString(R.string.map_title)
    val profileTitle: String = res.getString(R.string.profile_title)
    val selectLanguageSetting: String = res.getString(R.string.select_language_setting)
    val logoutBtn: String = res.getString(R.string.logout_btn)

    // Cascading Location Dropdowns
    val stateLabel: String = res.getString(R.string.state_label)
    val districtLabel: String = res.getString(R.string.district_label)
    val talukaLabel: String = res.getString(R.string.taluka_label)
    val villageLabel: String = res.getString(R.string.village_label)
    val selectStatePrompt: String = res.getString(R.string.select_state_prompt)
    val selectDistrictPrompt: String = res.getString(R.string.select_district_prompt)
    val selectTalukaPrompt: String = res.getString(R.string.select_taluka_prompt)
    val selectVillagePrompt: String = res.getString(R.string.select_village_prompt)

    // eKYC & Document Verification
    val ekycCardTitle: String = res.getString(R.string.ekyc_card_title)
    val ekycCardSubtitle: String = res.getString(R.string.ekyc_card_subtitle)
    val verifyNowBtn: String = res.getString(R.string.verify_now_btn)
    val completeEkycBtn: String = res.getString(R.string.complete_ekyc_btn)
    val verificationInProgress: String = res.getString(R.string.verification_in_progress)
    val ekycVerified: String = res.getString(R.string.ekyc_verified)
    val docVerificationHeader: String = res.getString(R.string.doc_verification_header)
    val aadhaarCardLabel: String = res.getString(R.string.aadhaar_card_label)
    val sevenTwelveLabel: String = res.getString(R.string.seven_twelve_label)
    val gatNumberLabel: String = res.getString(R.string.gat_number_label)
    val bankDetailsLabel: String = res.getString(R.string.bank_details_label)
    val uploadBtn: String = res.getString(R.string.upload_btn)
    val submitDocsBtn: String = res.getString(R.string.submit_docs_btn)
    val notSubmittedStatus: String = res.getString(R.string.not_submitted_status)
    val submittedStatus: String = res.getString(R.string.submitted_status)
    val underVerificationStatus: String = res.getString(R.string.under_verification_status)
    val verifiedStatus: String = res.getString(R.string.verified_status)
    val docsSubmittedTitle: String = res.getString(R.string.docs_submitted_title)
    val docsSubmittedMsg: String = res.getString(R.string.docs_submitted_msg)
    val gotItBtn: String = res.getString(R.string.got_it_btn)
    val learnMoreBtn: String = res.getString(R.string.learn_more_btn)
    val govtSchemesBannerTitle: String = res.getString(R.string.govt_schemes_banner_title)

    // FPO Nav & Learn with KrishiSetu
    val navFpo: String = res.getString(R.string.nav_fpo)
    val learnWithKrishiSetuTitle: String = res.getString(R.string.learn_with_krishisetu_title)
    val learnWithKrishiSetuSub: String = res.getString(R.string.learn_with_krishisetu_sub)
    val viewAllBtn: String = res.getString(R.string.view_all_btn)
    val tutorialVideoTitle: String = res.getString(R.string.tutorial_video_title)
    val prototypeTutorialBadge: String = res.getString(R.string.prototype_tutorial_badge)
    val video1Title: String = res.getString(R.string.video_1_title)
    val video1Sub: String = res.getString(R.string.video_1_sub)
    val video1Desc: String = res.getString(R.string.video_1_desc)
    val video2Title: String = res.getString(R.string.video_2_title)
    val video2Sub: String = res.getString(R.string.video_2_sub)
    val video2Desc: String = res.getString(R.string.video_2_desc)
    val video3Title: String = res.getString(R.string.video_3_title)
    val video3Sub: String = res.getString(R.string.video_3_sub)
    val video3Desc: String = res.getString(R.string.video_3_desc)
    val videoSourceLabel: String = res.getString(R.string.video_source_label)
    val watchOnYoutubeBtn: String = res.getString(R.string.watch_on_youtube_btn)

    // Document Verification Extra
    val otherDocsLabel: String = res.getString(R.string.other_docs_label)

    // Help & Support
    val helpAndSupportTitle: String = res.getString(R.string.help_and_support_title)
    val needHelpTitle: String = res.getString(R.string.need_help_title)
    val needHelpSub: String = res.getString(R.string.need_help_sub)
    val customerSupportBtn: String = res.getString(R.string.customer_support_btn)
    val raiseADisputeBtn: String = res.getString(R.string.raise_a_dispute_btn)
    val myComplaintsBtn: String = res.getString(R.string.my_complaints_btn)
    val supportCatPrices: String = res.getString(R.string.support_cat_prices)
    val supportCatLot: String = res.getString(R.string.support_cat_lot)
    val supportCatOffers: String = res.getString(R.string.support_cat_offers)
    val supportCatPayments: String = res.getString(R.string.support_cat_payments)
    val supportCatDelivery: String = res.getString(R.string.support_cat_delivery)
    val supportCatAccount: String = res.getString(R.string.support_cat_account)
    val stillNeedHelp: String = res.getString(R.string.still_need_help)
    val reportIssueBtn: String = res.getString(R.string.report_issue_btn)

    // Raise Dispute
    val raiseDisputeTitle: String = res.getString(R.string.raise_dispute_title)
    val issueAboutPrompt: String = res.getString(R.string.issue_about_prompt)
    val issuePayment: String = res.getString(R.string.issue_payment)
    val issueQuality: String = res.getString(R.string.issue_quality)
    val issueDelivery: String = res.getString(R.string.issue_delivery)
    val issueBuyer: String = res.getString(R.string.issue_buyer)
    val issueOther: String = res.getString(R.string.issue_other)
    val selectTransactionPrompt: String = res.getString(R.string.select_transaction_prompt)
    val describeIssuePrompt: String = res.getString(R.string.describe_issue_prompt)
    val describeIssuePlaceholder: String = res.getString(R.string.describe_issue_placeholder)
    val addPhotoDoc: String = res.getString(R.string.add_photo_doc)
    val photoAttached: String = res.getString(R.string.photo_attached)
    val submitDisputeBtn: String = res.getString(R.string.submit_dispute_btn)
    val disputeSubmittedTitle: String = res.getString(R.string.dispute_submitted_title)
    val disputeSubmittedMsg: String = res.getString(R.string.dispute_submitted_msg)
    val expectedResponseTime: String = res.getString(R.string.expected_response_time)
    val trackDisputeBtn: String = res.getString(R.string.track_dispute_btn)

    // My Complaints
    val myComplaintsTitle: String = res.getString(R.string.my_complaints_title)
    val noComplaintsMsg: String = res.getString(R.string.no_complaints_msg)
    val ticketIdLabel: String = res.getString(R.string.ticket_id_label)

    // General
    val cancel: String = res.getString(R.string.cancel)
    val submit: String = res.getString(R.string.submit)

    // Market Prices, Intraday Timeline, Auction & Comparison Expansion
    val demoDataBadge: String = res.getString(R.string.demo_data_badge)
    val demoDataSubtitle: String = res.getString(R.string.demo_data_subtitle)
    val searchCommodityOrMarketHint: String = res.getString(R.string.search_commodity_or_market_hint)
    val categoryAll: String = res.getString(R.string.category_all)
    val categoryVegetables: String = res.getString(R.string.category_vegetables)
    val categoryFruits: String = res.getString(R.string.category_fruits)
    val categoryGrainsPulses: String = res.getString(R.string.category_grains_pulses)
    val lowestRateLabel: String = res.getString(R.string.lowest_rate_label)
    val avgModalLabel: String = res.getString(R.string.avg_modal_label)
    val marketsCountLabel: String = res.getString(R.string.markets_count_label)
    val intradayTimelineTitle: String = res.getString(R.string.intraday_timeline_title)
    val intradayTimelineSubtitle: String = res.getString(R.string.intraday_timeline_subtitle)
    val selectMarketTimelineLabel: String = res.getString(R.string.select_market_timeline_label)
    val sessionMorning: String = res.getString(R.string.session_morning)
    val sessionMidday: String = res.getString(R.string.session_midday)
    val sessionAfternoon: String = res.getString(R.string.session_afternoon)
    val sessionDailyReport: String = res.getString(R.string.session_daily_report)
    val singleObservationNotice: String = res.getString(R.string.single_observation_notice)
    val staleDataWarning: String = res.getString(R.string.stale_data_warning)
    val openingObservationLabel: String = res.getString(R.string.opening_observation_label)
    val changeVsPrevLabel: String = res.getString(R.string.change_vs_prev_label)
    val marketTypeReported: String = res.getString(R.string.market_type_reported)
    val marketTypeIntraday: String = res.getString(R.string.market_type_intraday)
    val marketTypeAuction: String = res.getString(R.string.market_type_auction)
    val dataStatusSample: String = res.getString(R.string.data_status_sample)
    val dataStatusReported: String = res.getString(R.string.data_status_reported)
    val dataStatusLive: String = res.getString(R.string.data_status_live)
    val auctionSummaryTitle: String = res.getString(R.string.auction_summary_title)
    val sampleAuctionBadge: String = res.getString(R.string.sample_auction_badge)
    val auctionStatusLabel: String = res.getString(R.string.auction_status_label)
    val auctionStatusNotStarted: String = res.getString(R.string.auction_status_not_started)
    val auctionStatusOpen: String = res.getString(R.string.auction_status_open)
    val auctionStatusClosed: String = res.getString(R.string.auction_status_closed)
    val auctionWindowLabel: String = res.getString(R.string.auction_window_label)
    val validBidsLabel: String = res.getString(R.string.valid_bids_label)
    val currentHighestBidLabel: String = res.getString(R.string.current_highest_bid_label)
    val winningBidLabel: String = res.getString(R.string.winning_bid_label)
    val noBidsYetLabel: String = res.getString(R.string.no_bids_yet_label)
    val auctionDisclaimerText: String = res.getString(R.string.auction_disclaimer_text)
    val commodityLoadConfigTitle: String = res.getString(R.string.commodity_load_config_title)
    val selectedCommodityLabel: String = res.getString(R.string.selected_commodity_label)
    val quickSelectCommodityLabel: String = res.getString(R.string.quick_select_commodity_label)
    val customQuantityInputLabel: String = res.getString(R.string.custom_quantity_input_label)
    val presetQuantityLabel: String = res.getString(R.string.preset_quantity_label)
    val sortMarketsByLabel: String = res.getString(R.string.sort_markets_by_label)
    val sortBestNet: String = res.getString(R.string.sort_best_net)
    val sortHighestPrice: String = res.getString(R.string.sort_highest_price)
    val sortNearestDistance: String = res.getString(R.string.sort_nearest_distance)
    val sortLowestTransport: String = res.getString(R.string.sort_lowest_transport)
    val bestMarketForLoadBadge: String = res.getString(R.string.best_market_for_load_badge)
    val grossRevenueShort: String = res.getString(R.string.gross_revenue_short)
    val transportCostShort: String = res.getString(R.string.transport_cost_short)
    val netRealizationShort: String = res.getString(R.string.net_realization_short)
    val gainVsLowestLabel: String = res.getString(R.string.gain_vs_lowest_label)
    val noMatchingMarketsMsg: String = res.getString(R.string.no_matching_markets_msg)
    val tapCardForTimelineHint: String = res.getString(R.string.tap_card_for_timeline_hint)

    // Targeted Localization Completion: Units & Common
    val verifiedFarmer: String = res.getString(R.string.verified_farmer)
    val verifiedBuyer: String = res.getString(R.string.verified_buyer)
    val kgUnit: String = res.getString(R.string.kg_unit)
    val kmUnit: String = res.getString(R.string.km_unit)
    val cropsCountLabel: String = res.getString(R.string.crops_count_label)
    val yesLabel: String = res.getString(R.string.yes_label)
    val noLabel: String = res.getString(R.string.no_label)
    val clearSearchBtn: String = res.getString(R.string.clear_search_btn)
    val nextStepBtn: String = res.getString(R.string.next_step_btn)

    // 1. Crop Lot — Section 1: Crop Selection & Wizard
    val chooseYourCropSubtitle: String = res.getString(R.string.choose_your_crop_subtitle)
    val cropCategoryLabel: String = res.getString(R.string.crop_category_label)
    val selectedCropBannerLabel: String = res.getString(R.string.selected_crop_banner_label)
    val searchCropPlaceholder: String = res.getString(R.string.search_crop_placeholder)
    val cropSelectionInstruction: String = res.getString(R.string.crop_selection_instruction)
    val noCropsFoundMsg: String = res.getString(R.string.no_crops_found_msg)
    val cropRequiredError: String = res.getString(R.string.crop_required_error)
    val quantityRequiredError: String = res.getString(R.string.quantity_required_error)
    val priceRequiredError: String = res.getString(R.string.price_required_error)
    val qualityRequiredError: String = res.getString(R.string.quality_required_error)
    val quantityPlaceholder: String = res.getString(R.string.quantity_placeholder)
    val pricePlaceholder: String = res.getString(R.string.price_placeholder)
    val photosSelectedMock: String = res.getString(R.string.photos_selected_mock)
    val tapToCapturePhotos: String = res.getString(R.string.tap_to_capture_photos)

    // 2. Best Marketplace & Sell Now or Wait
    val bestPlaceSubtitle: String = res.getString(R.string.best_place_subtitle)
    val bestNetMarketIdentifiedTitle: String = res.getString(R.string.best_net_market_identified_title)
    val bestNetMarketIdentifiedSub: String = res.getString(R.string.best_net_market_identified_sub)
    val verifiedMarketBadge: String = res.getString(R.string.verified_market_badge)
    val bestPlaceCropQtyDistrictLine: String = res.getString(R.string.best_place_crop_qty_district_line)
    val estimatedTransportCostLabel: String = res.getString(R.string.estimated_transport_cost_label)
    val dataSourceLabel: String = res.getString(R.string.data_source_label)
    val gainVsLocalMandi: String = res.getString(R.string.gain_vs_local_mandi)
    val comparisonWithLocalTitle: String = res.getString(R.string.comparison_with_local_title)
    val localMarketMetricsLine: String = res.getString(R.string.local_market_metrics_line)
    val higherNetInBestMarketMsg: String = res.getString(R.string.higher_net_in_best_market_msg)
    val localMarketIsBestMsg: String = res.getString(R.string.local_market_is_best_msg)
    val bestPlaceWarningNote: String = res.getString(R.string.best_place_warning_note)
    val noMarketsAvailableForCrop: String = res.getString(R.string.no_markets_available_for_crop)
    val sellOrWaitSubtitle: String = res.getString(R.string.sell_or_wait_subtitle)
    val sellNowHeuristicBadge: String = res.getString(R.string.sell_now_heuristic_badge)
    val waitMonitorHeuristicBadge: String = res.getString(R.string.wait_monitor_heuristic_badge)
    val trendSummaryHeading: String = res.getString(R.string.trend_summary_heading)
    val trendSummaryDescription: String = res.getString(R.string.trend_summary_description)
    val highestMarketRateRow: String = res.getString(R.string.highest_market_rate_row)
    val lowestMarketRateRow: String = res.getString(R.string.lowest_market_rate_row)
    val thirtyMarketAvgRateRow: String = res.getString(R.string.thirty_market_avg_rate_row)
    val sevenDaySampleTrendRow: String = res.getString(R.string.seven_day_sample_trend_row)
    val storagePerishabilityRow: String = res.getString(R.string.storage_perishability_row)
    val shelfLifeVegetables: String = res.getString(R.string.shelf_life_vegetables)
    val shelfLifeFruits: String = res.getString(R.string.shelf_life_fruits)
    val shelfLifeGrains: String = res.getString(R.string.shelf_life_grains)
    val trendDisclaimerNotice: String = res.getString(R.string.trend_disclaimer_notice)

    // 3. Find Buyer & Digital Offers
    val buyerMarketplaceSubtitle: String = res.getString(R.string.buyer_marketplace_subtitle)
    val filterByCropLabel: String = res.getString(R.string.filter_by_crop_label)
    val buyerRequirementLine: String = res.getString(R.string.buyer_requirement_line)
    val onTimePaymentHistoryFormat: String = res.getString(R.string.on_time_payment_history_format)
    val buyerContactLabel: String = res.getString(R.string.buyer_contact_label)
    val contactBuyerBtn: String = res.getString(R.string.contact_buyer_btn)
    val contactBuyerDialogTitle: String = res.getString(R.string.contact_buyer_dialog_title)
    val buyerCompanyLabel: String = res.getString(R.string.buyer_company_label)
    val buyerTypeLabel: String = res.getString(R.string.buyer_type_label)
    val buyerLocationLabel: String = res.getString(R.string.buyer_location_label)
    val buyerPhoneLabel: String = res.getString(R.string.buyer_phone_label)
    val noBuyersFoundTitle: String = res.getString(R.string.no_buyers_found_title)
    val noBuyersFoundSub: String = res.getString(R.string.no_buyers_found_sub)
    val offerIdLabel: String = res.getString(R.string.offer_id_label)
    val cropLotLabel: String = res.getString(R.string.crop_lot_label)
    val dealConfirmedProceedMsg: String = res.getString(R.string.deal_confirmed_proceed_msg)

    // 4. Government Schemes
    val schemesSubtitle: String = res.getString(R.string.schemes_subtitle)
    val schemesIntroBanner: String = res.getString(R.string.schemes_intro_banner)
    val howToApplyLabel: String = res.getString(R.string.how_to_apply_label)
    val officialSourceLabel: String = res.getString(R.string.official_source_label)
    val officialSourceValue: String = res.getString(R.string.official_source_value)
    val officialPortalLabel: String = res.getString(R.string.official_portal_label)
    val applyOrViewSchemeBtn: String = res.getString(R.string.apply_or_view_scheme_btn)
    val schemesDisclaimerText: String = res.getString(R.string.schemes_disclaimer_text)
    val scheme1Title: String = res.getString(R.string.scheme_1_title)
    val scheme1Category: String = res.getString(R.string.scheme_1_category)
    val scheme1Desc: String = res.getString(R.string.scheme_1_desc)
    val scheme1Benefit: String = res.getString(R.string.scheme_1_benefit)
    val scheme1Eligibility: String = res.getString(R.string.scheme_1_eligibility)
    val scheme1Docs: String = res.getString(R.string.scheme_1_docs)
    val scheme1Apply: String = res.getString(R.string.scheme_1_apply)
    val scheme2Title: String = res.getString(R.string.scheme_2_title)
    val scheme2Category: String = res.getString(R.string.scheme_2_category)
    val scheme2Desc: String = res.getString(R.string.scheme_2_desc)
    val scheme2Benefit: String = res.getString(R.string.scheme_2_benefit)
    val scheme2Eligibility: String = res.getString(R.string.scheme_2_eligibility)
    val scheme2Docs: String = res.getString(R.string.scheme_2_docs)
    val scheme2Apply: String = res.getString(R.string.scheme_2_apply)
    val scheme3Title: String = res.getString(R.string.scheme_3_title)
    val scheme3Category: String = res.getString(R.string.scheme_3_category)
    val scheme3Desc: String = res.getString(R.string.scheme_3_desc)
    val scheme3Benefit: String = res.getString(R.string.scheme_3_benefit)
    val scheme3Eligibility: String = res.getString(R.string.scheme_3_eligibility)
    val scheme3Docs: String = res.getString(R.string.scheme_3_docs)
    val scheme3Apply: String = res.getString(R.string.scheme_3_apply)

    // 5. FPO Trading
    val fpoAggregationSubtitle: String = res.getString(R.string.fpo_aggregation_subtitle)
    val whyJoinFpoTitle: String = res.getString(R.string.why_join_fpo_title)
    val whyJoinFpoSub: String = res.getString(R.string.why_join_fpo_sub)
    val fpoStatusOpen: String = res.getString(R.string.fpo_status_open)
    val fpoStatusReady: String = res.getString(R.string.fpo_status_ready)
    val fpoCropTargetLine: String = res.getString(R.string.fpo_crop_target_line)
    val fpoTotalQuantityLine: String = res.getString(R.string.fpo_total_quantity_line)
    val fpoRemainingQtyLine: String = res.getString(R.string.fpo_remaining_qty_line)
    val fpoOfferedPriceLabel: String = res.getString(R.string.fpo_offered_price_label)
    val fpoAvailableOffersLabel: String = res.getString(R.string.fpo_available_offers_label)
    val joinFpoDialogTitle: String = res.getString(R.string.join_fpo_dialog_title)
    val joinFpoQtyPrompt: String = res.getString(R.string.join_fpo_qty_prompt)
    val joinFpoValidationError: String = res.getString(R.string.join_fpo_validation_error)
    val joinFpoEstPayout: String = res.getString(R.string.join_fpo_est_payout)
    val confirmJoinFpoBtn: String = res.getString(R.string.confirm_join_fpo_btn)
    val joinFpoSuccessTitle: String = res.getString(R.string.join_fpo_success_title)
    val joinFpoSuccessMsg: String = res.getString(R.string.join_fpo_success_msg)
    val noFpoLotsAvailable: String = res.getString(R.string.no_fpo_lots_available)

    fun categoryName(category: CommodityCategory): String = when (category) {
        CommodityCategory.ALL -> categoryAll
        CommodityCategory.VEGETABLES -> categoryVegetables
        CommodityCategory.FRUITS -> categoryFruits
        CommodityCategory.GRAINS_AND_PULSES -> categoryGrainsPulses
    }

    fun marketTypeLabel(type: MarketType): String = when (type) {
        MarketType.REPORTED_PRICE -> marketTypeReported
        MarketType.INTRADAY_UPDATED -> marketTypeIntraday
        MarketType.AUCTION_BIDDING -> marketTypeAuction
    }

    fun dataStatusLabel(status: MarketDataStatus): String = when (status) {
        MarketDataStatus.SAMPLE_DATA -> dataStatusSample
        MarketDataStatus.REPORTED -> dataStatusReported
        MarketDataStatus.LIVE -> dataStatusLive
    }

    fun sessionLabel(sessionKey: String): String = when (sessionKey) {
        "MORNING" -> sessionMorning
        "MIDDAY" -> sessionMidday
        "AFTERNOON" -> sessionAfternoon
        "DAILY_REPORT" -> sessionDailyReport
        else -> sessionKey
    }

    fun auctionStatusLabel(status: AuctionStatus): String = when (status) {
        AuctionStatus.NOT_STARTED -> auctionStatusNotStarted
        AuctionStatus.OPEN -> auctionStatusOpen
        AuctionStatus.CLOSED -> auctionStatusClosed
    }

    fun commodityName(englishName: String): String {
        val cleanKey = englishName.trim()
        if (language == AppLanguage.ENGLISH) return cleanKey
        return when (cleanKey) {
            "Tomato" -> if (language == AppLanguage.MARATHI) "टोमॅटो" else "टमाटर"
            "Onion" -> if (language == AppLanguage.MARATHI) "कांदा" else "प्याज"
            "Potato" -> if (language == AppLanguage.MARATHI) "बटाटा" else "आलू"
            "Green Chilli" -> if (language == AppLanguage.MARATHI) "हिरवी मिरची" else "हरी मिर्च"
            "Brinjal" -> if (language == AppLanguage.MARATHI) "वांगी" else "बैंगन"
            "Cabbage" -> if (language == AppLanguage.MARATHI) "कोबी" else "पत्तागोभी"
            "Cauliflower" -> if (language == AppLanguage.MARATHI) "फ्लॉवर" else "फूलगोभी"
            "Okra / Ladyfinger" -> if (language == AppLanguage.MARATHI) "भेंडी" else "भिंडी"
            "Cucumber" -> if (language == AppLanguage.MARATHI) "काकडी" else "खीरा"
            "Bitter Gourd" -> if (language == AppLanguage.MARATHI) "कारले" else "करेला"
            "Bottle Gourd" -> if (language == AppLanguage.MARATHI) "दुधी भोपळा" else "लौकी"
            "Capsicum" -> if (language == AppLanguage.MARATHI) "ढोबळी मिरची" else "शिमला मिर्च"
            "Carrot" -> if (language == AppLanguage.MARATHI) "गाजर" else "गाजर"
            "Green Peas" -> if (language == AppLanguage.MARATHI) "हिरवे वाटाणे" else "हरे मटर"
            "Spinach" -> if (language == AppLanguage.MARATHI) "पालक" else "पालक"
            "Banana" -> if (language == AppLanguage.MARATHI) "केळी" else "केला"
            "Mango" -> if (language == AppLanguage.MARATHI) "आंबा" else "आम"
            "Grapes" -> if (language == AppLanguage.MARATHI) "द्राक्षे" else "अंगूर"
            "Pomegranate" -> if (language == AppLanguage.MARATHI) "डाळिंब" else "अनार"
            "Orange" -> if (language == AppLanguage.MARATHI) "संत्रा" else "संतरा"
            "Papaya" -> if (language == AppLanguage.MARATHI) "पपई" else "पपीता"
            "Watermelon" -> if (language == AppLanguage.MARATHI) "कलिंगड" else "तरबूज"
            "Guava" -> if (language == AppLanguage.MARATHI) "पेरू" else "अमरूद"
            "Apple" -> if (language == AppLanguage.MARATHI) "सफरचंद" else "सेब"
            "Sweet Lime / Mosambi" -> if (language == AppLanguage.MARATHI) "मोसंबी" else "मौसंबी"
            "Wheat" -> if (language == AppLanguage.MARATHI) "गहू" else "गेहूं"
            "Rice" -> if (language == AppLanguage.MARATHI) "तांदूळ" else "चावल"
            "Jowar" -> if (language == AppLanguage.MARATHI) "ज्वारी" else "ज्वार"
            "Bajra" -> if (language == AppLanguage.MARATHI) "बाजरी" else "बाजरा"
            "Maize" -> if (language == AppLanguage.MARATHI) "मका" else "मक्का"
            "Soybean" -> if (language == AppLanguage.MARATHI) "सोयाबीन" else "सोयाबीन"
            "Tur / Arhar" -> if (language == AppLanguage.MARATHI) "तूर" else "तूर (अरहर)"
            "Gram / Chana" -> if (language == AppLanguage.MARATHI) "हरभरा" else "चना"
            "Moong" -> if (language == AppLanguage.MARATHI) "मूग" else "मूंग"
            "Urad" -> if (language == AppLanguage.MARATHI) "उडीद" else "उड़द"
            else -> cleanKey
        }
    }

    fun marketName(englishName: String): String {
        if (language == AppLanguage.ENGLISH) return englishName
        val isMr = language == AppLanguage.MARATHI
        return when (englishName.trim()) {
            "Pune — Gultekdi Market Yard" -> if (isMr) "पुणे — गुलटेकडी मार्केट यार्ड" else "पुणे — गुलटेकड़ी मंडी"
            "Mumbai — Vashi APMC" -> if (isMr) "मुंबई — वाशी बाजार समिती" else "मुंबई — वाशी मंडी"
            "Nashik APMC" -> if (isMr) "नाशिक बाजार समिती" else "नासिक मंडी"
            "Lasalgaon APMC" -> if (isMr) "लासलगाव बाजार समिती" else "लासलगांव मंडी"
            "Nagpur APMC" -> if (isMr) "नागपूर बाजार समिती" else "नागपुर मंडी"
            "Chhatrapati Sambhajinagar APMC" -> if (isMr) "छत्रपती संभाजीनगर बाजार समिती" else "छत्रपति संभाजीनगर मंडी"
            "Kolhapur APMC" -> if (isMr) "कोल्हापूर बाजार समिती" else "कोल्हापुर मंडी"
            "Sangli APMC" -> if (isMr) "सांगली बाजार समिती" else "सांगली मंडी"
            "Solapur APMC" -> if (isMr) "सोलापूर बाजार समिती" else "सोलापुर मंडी"
            "Satara APMC" -> if (isMr) "सातारा बाजार समिती" else "सातारा मंडी"
            "Karad APMC" -> if (isMr) "कराड बाजार समिती" else "कराड मंडी"
            "Baramati APMC" -> if (isMr) "बारामती बाजार समिती" else "बारामती मंडी"
            "Ahmednagar / Ahilyanagar APMC" -> if (isMr) "अहिल्यानगर बाजार समिती" else "अहिल्यानगर मंडी"
            "Rahuri APMC" -> if (isMr) "राहुरी बाजार समिती" else "राहुरी मंडी"
            "Jalgaon APMC" -> if (isMr) "जळगाव बाजार समिती" else "जलगांव मंडी"
            "Dhule APMC" -> if (isMr) "धुळे बाजार समिती" else "धुले मंडी"
            "Malegaon APMC" -> if (isMr) "मालेगाव बाजार समिती" else "मालेगांव मंडी"
            "Akola APMC" -> if (isMr) "अकोला बाजार समिती" else "अकोला मंडी"
            "Amravati APMC" -> if (isMr) "अमरावती बाजार समिती" else "अमरावती मंडी"
            "Latur APMC" -> if (isMr) "लातूर बाजार समिती" else "लातूर मंडी"
            "Nanded APMC" -> if (isMr) "नांदेड बाजार समिती" else "नांदेड़ मंडी"
            "Parbhani APMC" -> if (isMr) "परभणी बाजार समिती" else "परभणी मंडी"
            "Jalna APMC" -> if (isMr) "जालना बाजार समिती" else "जालना मंडी"
            "Beed APMC" -> if (isMr) "बीड बाजार समिती" else "बीड मंडी"
            "Sangamner APMC" -> if (isMr) "संगमनेर बाजार समिती" else "संगमनेर मंडी"
            "Chakan APMC" -> if (isMr) "चाकण बाजार समिती" else "चाकण मंडी"
            "Indapur APMC" -> if (isMr) "इंदापूर बाजार समिती" else "इंदापुर मंडी"
            "Ratnagiri APMC" -> if (isMr) "रत्नागिरी बाजार समिती" else "रत्नागिरी मंडी"
            "Yavatmal APMC" -> if (isMr) "यवतमाळ बाजार समिती" else "यवतमाल मंडी"
            "Wardha APMC" -> if (isMr) "वर्धा बाजार समिती" else "वर्धा मंडी"
            else -> englishName
        }
    }

    fun districtName(englishDistrict: String): String {
        if (language == AppLanguage.ENGLISH) return englishDistrict
        val isMr = language == AppLanguage.MARATHI
        return when (englishDistrict.trim()) {
            "Pune" -> "पुणे"
            "Navi Mumbai / Thane" -> if (isMr) "नवी मुंबई / ठाणे" else "नवी मुंबई / ठाणे"
            "Nashik" -> if (isMr) "नाशिक" else "नासिक"
            "Nagpur" -> if (isMr) "नागपूर" else "नागपुर"
            "Chhatrapati Sambhajinagar" -> if (isMr) "छत्रपती संभाजीनगर" else "छत्रपति संभाजीनगर"
            "Kolhapur" -> if (isMr) "कोल्हापूर" else "कोल्हापुर"
            "Sangli" -> "सांगली"
            "Solapur" -> if (isMr) "सोलापूर" else "सोलापुर"
            "Satara" -> "सातारा"
            "Karad" -> "कराड"
            "Ahilyanagar" -> "अहिल्यानगर"
            "Jalgaon" -> if (isMr) "जळगाव" else "जलगांव"
            "Dhule" -> if (isMr) "धुळे" else "धुले"
            "Akola" -> "अकोला"
            "Amravati" -> "अमरावती"
            "Latur" -> "लातूर"
            "Nanded" -> if (isMr) "नांदेड" else "नांदेड़"
            "Parbhani" -> "परभणी"
            "Jalna" -> "जालना"
            "Beed" -> "बीड"
            "Ratnagiri" -> "रत्नागिरी"
            "Yavatmal" -> if (isMr) "यवतमाळ" else "यवतमाल"
            "Wardha" -> "वर्धा"
            else -> englishDistrict
        }
    }

    fun translateBuyerType(buyerType: String): String {
        if (language == AppLanguage.ENGLISH) return buyerType
        val isMr = language == AppLanguage.MARATHI
        return when (buyerType.trim()) {
            "Food Processor" -> if (isMr) "अन्न प्रक्रिया उद्योग" else "फूड प्रोसेसर"
            "APMC Trader" -> if (isMr) "बाजार समिती व्यापारी" else "मंडी व्यापारी"
            "Retail Chain" -> if (isMr) "किरकोळ विक्री साखळी" else "रिटेल चेन"
            "FPO Aggregator" -> if (isMr) "शेतकरी उत्पादक संस्था (FPO)" else "किसान उत्पादक संगठन (FPO)"
            else -> buyerType
        }
    }

    fun translateDeadline(deadline: String): String {
        if (language == AppLanguage.ENGLISH) return deadline
        val isMr = language == AppLanguage.MARATHI
        return when (deadline.trim()) {
            "Open All Year" -> if (isMr) "वर्षभर सुरू" else "साल भर खुला"
            "31st July (Kharif)" -> if (isMr) "31 जुलै (खरीप)" else "31 जुलाई (खरीफ)"
            else -> deadline
        }
    }

    fun translateQualityGrade(gradeKey: String): String = when (gradeKey.trim()) {
        "Grade A" -> gradeALabel
        "Grade B" -> gradeBLabel
        "Grade C" -> gradeCLabel
        else -> gradeKey
    }

    fun localizeScheme(scheme: GovernmentScheme): GovernmentScheme = when (scheme.id) {
        "SCH-01" -> scheme.copy(
            title = scheme1Title,
            category = scheme1Category,
            benefit = scheme1Benefit,
            eligibility = scheme1Eligibility,
            documentsRequired = scheme1Docs,
            howToApply = scheme1Apply,
            officialSource = officialSourceValue
        )
        "SCH-02" -> scheme.copy(
            title = scheme2Title,
            category = scheme2Category,
            benefit = scheme2Benefit,
            eligibility = scheme2Eligibility,
            documentsRequired = scheme2Docs,
            howToApply = scheme2Apply,
            officialSource = officialSourceValue
        )
        "SCH-03" -> scheme.copy(
            title = scheme3Title,
            category = scheme3Category,
            benefit = scheme3Benefit,
            eligibility = scheme3Eligibility,
            documentsRequired = scheme3Docs,
            howToApply = scheme3Apply,
            officialSource = officialSourceValue
        )
        else -> scheme
    }

    fun schemeDescription(schemeId: String): String = when (schemeId) {
        "SCH-01" -> scheme1Desc
        "SCH-02" -> scheme2Desc
        "SCH-03" -> scheme3Desc
        else -> ""
    }

    fun schemePortalUrl(schemeId: String): String = when (schemeId) {
        "SCH-01" -> "https://pmkisan.gov.in"
        "SCH-02" -> "https://pmfby.gov.in"
        "SCH-03" -> "https://enam.gov.in"
        else -> "https://agricoop.nic.in"
    }

    fun translateStatus(statusKey: String): String = when (statusKey) {
        "Deal Confirmed" -> statusDealConfirmed
        "Pickup Scheduled" -> statusPickupScheduled
        "In Transit", "IN TRANSIT" -> statusInTransit
        "Delivered", "DELIVERED" -> statusDelivered
        "Payment Initiated" -> statusPaymentInitiated
        "Payment Received" -> statusPaymentReceived
        "Available" -> when (language) {
            AppLanguage.MARATHI -> "उपलब्ध"
            AppLanguage.HINDI -> "उपलब्ध"
            AppLanguage.ENGLISH -> "Available"
        }
        "Under Offer", "Negotiating" -> when (language) {
            AppLanguage.MARATHI -> "ऑफर सुरू"
            AppLanguage.HINDI -> "ऑफर जारी"
            AppLanguage.ENGLISH -> statusKey
        }
        "Sold" -> when (language) {
            AppLanguage.MARATHI -> "विकले"
            AppLanguage.HINDI -> "बिक गया"
            AppLanguage.ENGLISH -> "Sold"
        }
        "Pending" -> when (language) {
            AppLanguage.MARATHI -> "प्रलंबित"
            AppLanguage.HINDI -> "लंबित"
            AppLanguage.ENGLISH -> "Pending"
        }
        "Countered", "Counter Sent" -> when (language) {
            AppLanguage.MARATHI -> "काउंटर ऑफर"
            AppLanguage.HINDI -> "काउंटर ऑफर"
            AppLanguage.ENGLISH -> statusKey
        }
        "Accepted" -> when (language) {
            AppLanguage.MARATHI -> "स्वीकारले"
            AppLanguage.HINDI -> "स्वीकृत"
            AppLanguage.ENGLISH -> "Accepted"
        }
        "Rejected" -> when (language) {
            AppLanguage.MARATHI -> "नाकारले"
            AppLanguage.HINDI -> "अस्वीकृत"
            AppLanguage.ENGLISH -> "Rejected"
        }
        else -> statusKey
    }

    companion object {
        @Composable
        fun get(language: AppLanguage = LocalAppLanguage.current): FarmerStrings {
            val context = LocalContext.current
            return remember(language, context) {
                val localizedContext = LocaleHelper.updateLocale(context, language)
                FarmerStrings(
                    language = language,
                    res = localizedContext.resources
                )
            }
        }
    }
}
