package com.krishisetu.app.models

import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Centralized deterministic DEMO dataset for KrishiSetu competition demonstration.
 *
 * IMPORTANT: All prices, distances, transport estimates, and 7-day trends generated here are
 * illustrative DEMO / SAMPLE DATA for prototype evaluation only and are NOT live or official
 * APMC market rates.
 *
 * Coverage:
 * - 35 agricultural commodities (15 Vegetables, 10 Fruits, 10 Grains & Pulses)
 * - 30 major Maharashtra market yards
 * - 35 x 30 = 1,050 unique, deterministic commodity-market price records
 * - Consistent unit: ₹/kg across all commodities
 */
object DemoMarketDataCatalog {

    const val DATA_SOURCE_LABEL = "DEMO / SAMPLE DATA"
    const val DEMO_DATE = "03 Nov 2026"
    const val DEMO_TIMESTAMP = "Sample Snapshot · 03:00 PM"
    const val SAMPLE_LOAD_KG = 800

    val commodities: List<CommodityInfo> = listOf(
        // Vegetables — 15
        CommodityInfo("CMD-01", "Tomato", CommodityCategory.VEGETABLES, "🍅", 24.0),
        CommodityInfo("CMD-02", "Onion", CommodityCategory.VEGETABLES, "🧅", 28.0),
        CommodityInfo("CMD-03", "Potato", CommodityCategory.VEGETABLES, "🥔", 22.0),
        CommodityInfo("CMD-04", "Green Chilli", CommodityCategory.VEGETABLES, "🌶️", 46.0),
        CommodityInfo("CMD-05", "Brinjal", CommodityCategory.VEGETABLES, "🍆", 26.0),
        CommodityInfo("CMD-06", "Cabbage", CommodityCategory.VEGETABLES, "🥬", 18.0),
        CommodityInfo("CMD-07", "Cauliflower", CommodityCategory.VEGETABLES, "🥦", 24.0),
        CommodityInfo("CMD-08", "Okra / Ladyfinger", CommodityCategory.VEGETABLES, "🌿", 34.0),
        CommodityInfo("CMD-09", "Cucumber", CommodityCategory.VEGETABLES, "🥒", 20.0),
        CommodityInfo("CMD-10", "Bitter Gourd", CommodityCategory.VEGETABLES, "🥒", 36.0),
        CommodityInfo("CMD-11", "Bottle Gourd", CommodityCategory.VEGETABLES, "🍐", 19.0),
        CommodityInfo("CMD-12", "Capsicum", CommodityCategory.VEGETABLES, "🫑", 42.0),
        CommodityInfo("CMD-13", "Carrot", CommodityCategory.VEGETABLES, "🥕", 30.0),
        CommodityInfo("CMD-14", "Green Peas", CommodityCategory.VEGETABLES, "🫛", 54.0),
        CommodityInfo("CMD-15", "Spinach", CommodityCategory.VEGETABLES, "🥬", 21.0),

        // Fruits — 10
        CommodityInfo("CMD-16", "Banana", CommodityCategory.FRUITS, "🍌", 25.0),
        CommodityInfo("CMD-17", "Mango", CommodityCategory.FRUITS, "🥭", 85.0),
        CommodityInfo("CMD-18", "Grapes", CommodityCategory.FRUITS, "🍇", 68.0),
        CommodityInfo("CMD-19", "Pomegranate", CommodityCategory.FRUITS, "🍎", 92.0),
        CommodityInfo("CMD-20", "Orange", CommodityCategory.FRUITS, "🍊", 48.0),
        CommodityInfo("CMD-21", "Papaya", CommodityCategory.FRUITS, "🍈", 24.0),
        CommodityInfo("CMD-22", "Watermelon", CommodityCategory.FRUITS, "🍉", 16.0),
        CommodityInfo("CMD-23", "Guava", CommodityCategory.FRUITS, "🍐", 38.0),
        CommodityInfo("CMD-24", "Apple", CommodityCategory.FRUITS, "🍎", 110.0),
        CommodityInfo("CMD-25", "Sweet Lime / Mosambi", CommodityCategory.FRUITS, "🍋", 42.0),

        // Grains and Pulses — 10 (all converted to consistent ₹/kg for UI)
        CommodityInfo("CMD-26", "Wheat", CommodityCategory.GRAINS_AND_PULSES, "🌾", 31.0),
        CommodityInfo("CMD-27", "Rice", CommodityCategory.GRAINS_AND_PULSES, "🍚", 42.0),
        CommodityInfo("CMD-28", "Jowar", CommodityCategory.GRAINS_AND_PULSES, "🌾", 36.0),
        CommodityInfo("CMD-29", "Bajra", CommodityCategory.GRAINS_AND_PULSES, "🌾", 29.0),
        CommodityInfo("CMD-30", "Maize", CommodityCategory.GRAINS_AND_PULSES, "🌽", 24.0),
        CommodityInfo("CMD-31", "Soybean", CommodityCategory.GRAINS_AND_PULSES, "🫘", 48.0),
        CommodityInfo("CMD-32", "Tur / Arhar", CommodityCategory.GRAINS_AND_PULSES, "🫘", 96.0),
        CommodityInfo("CMD-33", "Gram / Chana", CommodityCategory.GRAINS_AND_PULSES, "🫘", 62.0),
        CommodityInfo("CMD-34", "Moong", CommodityCategory.GRAINS_AND_PULSES, "🫘", 86.0),
        CommodityInfo("CMD-35", "Urad", CommodityCategory.GRAINS_AND_PULSES, "🫘", 84.0)
    )

    val markets: List<MarketYardInfo> = listOf(
        MarketYardInfo("MKT-01", "Pune — Gultekdi Market Yard", "Pune", 115, 0.08, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-02", "Mumbai — Vashi APMC", "Navi Mumbai / Thane", 255, 0.15, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-03", "Nashik APMC", "Nashik", 320, 0.03, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-04", "Lasalgaon APMC", "Nashik", 340, -0.04, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-05", "Nagpur APMC", "Nagpur", 760, 0.09, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-06", "Chhatrapati Sambhajinagar APMC", "Chhatrapati Sambhajinagar", 315, 0.05, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-07", "Kolhapur APMC", "Kolhapur", 120, 0.01, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-08", "Sangli APMC", "Sangli", 125, -0.01, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-09", "Solapur APMC", "Solapur", 220, 0.02, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-10", "Satara APMC", "Satara", 8, -0.07, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-11", "Karad APMC", "Satara", 55, -0.02, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-12", "Baramati APMC", "Pune", 95, 0.00, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-13", "Ahmednagar / Ahilyanagar APMC", "Ahilyanagar", 210, -0.02, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-14", "Rahuri APMC", "Ahilyanagar", 245, -0.05, MarketType.REPORTED_PRICE),
        MarketYardInfo("MKT-15", "Jalgaon APMC", "Jalgaon", 480, -0.03, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-16", "Dhule APMC", "Dhule", 440, -0.04, MarketType.REPORTED_PRICE),
        MarketYardInfo("MKT-17", "Malegaon APMC", "Nashik", 385, -0.02, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-18", "Akola APMC", "Akola", 560, 0.02, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-19", "Amravati APMC", "Amravati", 640, 0.04, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-20", "Latur APMC", "Latur", 335, 0.01, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-21", "Nanded APMC", "Nanded", 465, 0.03, MarketType.REPORTED_PRICE),
        MarketYardInfo("MKT-22", "Parbhani APMC", "Parbhani", 415, 0.00, MarketType.REPORTED_PRICE),
        MarketYardInfo("MKT-23", "Jalna APMC", "Jalna", 365, 0.01, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-24", "Beed APMC", "Beed", 275, -0.03, MarketType.REPORTED_PRICE),
        MarketYardInfo("MKT-25", "Sangamner APMC", "Ahilyanagar", 250, -0.01, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-26", "Chakan APMC", "Pune", 145, 0.04, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-27", "Indapur APMC", "Pune", 140, -0.03, MarketType.AUCTION_BIDDING),
        MarketYardInfo("MKT-28", "Ratnagiri APMC", "Ratnagiri", 195, 0.11, MarketType.INTRADAY_UPDATED),
        MarketYardInfo("MKT-29", "Yavatmal APMC", "Yavatmal", 665, 0.03, MarketType.REPORTED_PRICE),
        MarketYardInfo("MKT-30", "Wardha APMC", "Wardha", 695, 0.05, MarketType.REPORTED_PRICE)
    )

    /**
     * Estimates transport cost in INR for a given load in kg based on distance in km.
     * Baseline 800 kg sample load costs:
     * Base loading/dispatch ₹280 + ₹14.5/km, scaled proportionally for other quantities.
     */
    fun estimateTransportCost(distanceKm: Int, quantityKg: Double = SAMPLE_LOAD_KG.toDouble()): Double {
        if (quantityKg <= 0.0) return 0.0
        val baseCostForSampleLoad = (280.0 + distanceKm * 14.5).roundToInt().toDouble()
        val loadFactor = (quantityKg / SAMPLE_LOAD_KG.toDouble()).coerceAtLeast(0.25)
        return (baseCostForSampleLoad * loadFactor).roundToInt().toDouble()
    }

    private fun roundToHalf(value: Double): Double {
        return (value * 2.0).roundToInt() / 2.0
    }

    private fun roundToOneDecimal(value: Double): Double {
        return (value * 10.0).roundToInt() / 10.0
    }

    private fun buildObservation(
        id: String,
        commodity: CommodityInfo,
        market: MarketYardInfo,
        sessionKey: String,
        pricePerKg: Double,
        previousSessionPrice: Double?,
        reportedTime: String,
        dataStatus: MarketDataStatus,
        freshnessLabel: String
    ): PriceObservation {
        val obsSpread = max(1.0, roundToHalf(pricePerKg * 0.08))
        val obsMin = max(3.5, roundToHalf(pricePerKg - obsSpread))
        val obsMax = roundToHalf(pricePerKg + obsSpread)
        val changeRs = previousSessionPrice?.let { roundToOneDecimal(pricePerKg - it) }
        val changePct = if (previousSessionPrice != null && previousSessionPrice > 0.0) {
            roundToOneDecimal(((pricePerKg - previousSessionPrice) / previousSessionPrice) * 100.0)
        } else {
            null
        }
        return PriceObservation(
            id = id,
            commodityId = commodity.id,
            commodityName = commodity.name,
            marketId = market.id,
            marketName = market.name,
            sessionKey = sessionKey,
            pricePerKg = pricePerKg,
            reportedDate = DEMO_DATE,
            reportedTime = reportedTime,
            sourceProvider = if (dataStatus == MarketDataStatus.REPORTED) {
                "APMC Daily Bulletin (Sample)"
            } else {
                "KrishiSetu Intraday Demo Feed"
            },
            dataStatus = dataStatus,
            freshnessLabel = freshnessLabel,
            minPricePerKg = obsMin,
            maxPricePerKg = obsMax,
            modalPricePerKg = pricePerKg,
            changeRs = changeRs,
            changePercent = changePct
        )
    }

    /**
     * Generates all 35 x 30 = 1,050 unique commodity-market price records deterministically.
     * Ensures:
     * - Separate time-stamped PriceObservation entries per market/commodity.
     * - Single-observation REPORTED_PRICE markets have 1 observation (never fabricated).
     * - INTRADAY_UPDATED & AUCTION_BIDDING markets have 3 intraday observations (08:00 AM, 12:00 PM, 03:00 PM).
     * - Pune — Gultekdi Market Yard (MKT-01) + Tomato (CMD-01) has exact 08:00 AM = ₹24/kg, 12:00 PM = ₹22/kg, 03:00 PM = ₹23/kg.
     * - currentPricePerKg always equals the latest observation's pricePerKg.
     * - AUCTION_BIDDING markets include a separate simulated AuctionSummary (never mixed with modal price).
     */
    fun generateAllMarketPrices(): List<MarketPriceData> {
        val result = ArrayList<MarketPriceData>(commodities.size * markets.size)

        commodities.forEachIndexed { cIdx, commodity ->
            markets.forEachIndexed { mIdx, market ->
                val stableId = "${commodity.id}_${market.id}"

                // Deterministic hash-like variation in [-7, +7] based on indices so every market differs
                val hashVal = ((cIdx + 1) * 37 + (mIdx + 1) * 19 + (cIdx * mIdx * 7)) % 29
                val fineOffsetPercent = (hashVal - 14) * 0.0085 // approx -11.9% to +11.9%
                val totalFactor = (1.0 + market.priceBiasFactor + fineOffsetPercent).coerceIn(0.82, 1.24)

                // Exact specification override for Pune — Gultekdi Market Yard (MKT-01) + Tomato (CMD-01):
                // 08:00 AM = ₹24/kg, 12:00 PM = ₹22/kg, 03:00 PM = ₹23/kg
                val isPuneTomato = (commodity.id == "CMD-01" && market.id == "MKT-01")

                val rawCurrent = commodity.basePricePerKg * totalFactor + ((mIdx % 5) - 2) * 0.5
                val latestPrice = if (isPuneTomato) 23.0 else max(6.0, roundToHalf(rawCurrent))

                // Build intraday price observations based on MarketType
                val observations: List<PriceObservation> = when (market.marketType) {
                    MarketType.REPORTED_PRICE -> {
                        // Single reported daily observation — do NOT fabricate morning/afternoon sessions
                        listOf(
                            buildObservation(
                                id = "OBS_${stableId}_DAILY",
                                commodity = commodity,
                                market = market,
                                sessionKey = "DAILY_REPORT",
                                pricePerKg = latestPrice,
                                previousSessionPrice = null,
                                reportedTime = "11:30 AM",
                                dataStatus = MarketDataStatus.REPORTED,
                                freshnessLabel = "Single Daily Bulletin · 11:30 AM"
                            )
                        )
                    }
                    MarketType.INTRADAY_UPDATED, MarketType.AUCTION_BIDDING -> {
                        val morningPrice: Double
                        val middayPrice: Double
                        if (isPuneTomato) {
                            morningPrice = 24.0
                            middayPrice = 22.0
                        } else {
                            val intradayStep = max(0.5, roundToHalf(commodity.basePricePerKg * 0.04))
                            val pattern = (cIdx + mIdx) % 3
                            when (pattern) {
                                0 -> {
                                    // Dip at midday, partial recovery in afternoon (like 24 -> 22 -> 23)
                                    morningPrice = max(5.0, roundToHalf(latestPrice + intradayStep))
                                    middayPrice = max(4.5, roundToHalf(latestPrice - intradayStep))
                                }
                                1 -> {
                                    // Steady climb from morning to afternoon
                                    morningPrice = max(4.5, roundToHalf(latestPrice - intradayStep * 2.0))
                                    middayPrice = max(5.0, roundToHalf(latestPrice - intradayStep))
                                }
                                else -> {
                                    // Peak at midday, slight ease in afternoon
                                    morningPrice = max(4.5, roundToHalf(latestPrice - intradayStep))
                                    middayPrice = roundToHalf(latestPrice + intradayStep)
                                }
                            }
                        }

                        val obs1 = buildObservation(
                            id = "OBS_${stableId}_MORN",
                            commodity = commodity,
                            market = market,
                            sessionKey = "MORNING",
                            pricePerKg = morningPrice,
                            previousSessionPrice = null,
                            reportedTime = "08:00 AM",
                            dataStatus = MarketDataStatus.SAMPLE_DATA,
                            freshnessLabel = "Morning Opening · 08:00 AM"
                        )
                        val obs2 = buildObservation(
                            id = "OBS_${stableId}_MID",
                            commodity = commodity,
                            market = market,
                            sessionKey = "MIDDAY",
                            pricePerKg = middayPrice,
                            previousSessionPrice = morningPrice,
                            reportedTime = "12:00 PM",
                            dataStatus = MarketDataStatus.SAMPLE_DATA,
                            freshnessLabel = "Midday Update · 12:00 PM"
                        )
                        val obs3 = buildObservation(
                            id = "OBS_${stableId}_AFT",
                            commodity = commodity,
                            market = market,
                            sessionKey = "AFTERNOON",
                            pricePerKg = latestPrice,
                            previousSessionPrice = middayPrice,
                            reportedTime = "03:00 PM",
                            dataStatus = MarketDataStatus.SAMPLE_DATA,
                            freshnessLabel = "Latest Reported · 03:00 PM"
                        )
                        listOf(obs1, obs2, obs3)
                    }
                }

                // Current price is strictly the latest observation's pricePerKg
                val currentPrice = observations.last().pricePerKg

                val spread = max(1.5, roundToHalf(currentPrice * 0.11))
                val minPrice = max(4.0, roundToHalf(currentPrice - spread))
                val maxPrice = roundToHalf(currentPrice + spread)
                val modalPrice = currentPrice

                // Deterministic 7-day trend ending at currentPrice
                val trendPattern = (cIdx * 11 + mIdx * 5) % 3 // 0 = UP, 1 = DOWN, 2 = STABLE/RECOVERY
                val stepUnit = max(0.5, roundToHalf(commodity.basePricePerKg * 0.02))

                val trend7Days: List<Double> = when (trendPattern) {
                    0 -> listOf(
                        max(3.0, roundToHalf(currentPrice - stepUnit * 3.0)),
                        max(3.5, roundToHalf(currentPrice - stepUnit * 2.5)),
                        max(4.0, roundToHalf(currentPrice - stepUnit * 2.0)),
                        max(4.5, roundToHalf(currentPrice - stepUnit * 1.5)),
                        max(5.0, roundToHalf(currentPrice - stepUnit * 1.0)),
                        max(5.5, roundToHalf(currentPrice - stepUnit * 0.5)),
                        currentPrice
                    )
                    1 -> listOf(
                        roundToHalf(currentPrice + stepUnit * 2.5),
                        roundToHalf(currentPrice + stepUnit * 2.0),
                        roundToHalf(currentPrice + stepUnit * 1.5),
                        roundToHalf(currentPrice + stepUnit * 1.0),
                        roundToHalf(currentPrice + stepUnit * 1.0),
                        roundToHalf(currentPrice + stepUnit * 0.5),
                        currentPrice
                    )
                    else -> listOf(
                        max(3.0, roundToHalf(currentPrice - stepUnit * 1.0)),
                        max(3.0, roundToHalf(currentPrice - stepUnit * 0.5)),
                        roundToHalf(currentPrice + stepUnit * 0.5),
                        max(3.0, roundToHalf(currentPrice - stepUnit * 0.5)),
                        currentPrice,
                        max(3.0, roundToHalf(currentPrice - stepUnit * 0.5)),
                        currentPrice
                    )
                }

                // Previous price for intraday markets uses the prior observation; for 1-observation markets uses yesterday's trend
                val previousPrice = if (observations.size >= 2) {
                    observations[observations.size - 2].pricePerKg
                } else {
                    trend7Days[trend7Days.size - 2]
                }

                val trendDirection = when {
                    currentPrice > previousPrice -> "UP"
                    currentPrice < previousPrice -> "DOWN"
                    else -> "STABLE"
                }

                val estTransport = estimateTransportCost(
                    distanceKm = market.distanceKmFromSatara,
                    quantityKg = SAMPLE_LOAD_KG.toDouble()
                )

                // Separate simulated AuctionSummary for AUCTION_BIDDING markets only
                val auctionSummary: AuctionSummary? = if (market.marketType == MarketType.AUCTION_BIDDING) {
                    val statusSelector = (mIdx + cIdx) % 3
                    val auctionStatus = when {
                        market.id == "MKT-03" -> AuctionStatus.OPEN
                        market.id == "MKT-04" -> AuctionStatus.CLOSED
                        market.id == "MKT-12" -> AuctionStatus.NOT_STARTED
                        statusSelector == 0 -> AuctionStatus.OPEN
                        statusSelector == 1 -> AuctionStatus.CLOSED
                        else -> AuctionStatus.NOT_STARTED
                    }
                    val bidPremium = max(1.0, roundToHalf(commodity.basePricePerKg * 0.05))
                    val highestBid = roundToHalf(currentPrice + bidPremium)
                    val winningBid = roundToHalf(currentPrice + bidPremium + 0.5)
                    val bidsCount = 12 + ((cIdx * 3 + mIdx * 5) % 19)

                    when (auctionStatus) {
                        AuctionStatus.NOT_STARTED -> AuctionSummary(
                            id = "AUC_$stableId",
                            commodityId = commodity.id,
                            commodityName = commodity.name,
                            marketId = market.id,
                            marketName = market.name,
                            status = AuctionStatus.NOT_STARTED,
                            startTime = "04:00 PM",
                            closeTime = "05:30 PM",
                            validBidsCount = 0,
                            currentHighestBidPerKg = null,
                            winningBidPerKg = null,
                            bidTimestamp = "$DEMO_DATE · Scheduled 04:00 PM",
                            unit = commodity.unit
                        )
                        AuctionStatus.OPEN -> AuctionSummary(
                            id = "AUC_$stableId",
                            commodityId = commodity.id,
                            commodityName = commodity.name,
                            marketId = market.id,
                            marketName = market.name,
                            status = AuctionStatus.OPEN,
                            startTime = "01:30 PM",
                            closeTime = "04:30 PM",
                            validBidsCount = bidsCount,
                            currentHighestBidPerKg = highestBid,
                            winningBidPerKg = null,
                            bidTimestamp = "$DEMO_DATE · 02:55 PM",
                            unit = commodity.unit
                        )
                        AuctionStatus.CLOSED -> AuctionSummary(
                            id = "AUC_$stableId",
                            commodityId = commodity.id,
                            commodityName = commodity.name,
                            marketId = market.id,
                            marketName = market.name,
                            status = AuctionStatus.CLOSED,
                            startTime = "10:00 AM",
                            closeTime = "01:00 PM",
                            validBidsCount = bidsCount + 6,
                            currentHighestBidPerKg = winningBid,
                            winningBidPerKg = winningBid,
                            bidTimestamp = "$DEMO_DATE · Closed 01:00 PM",
                            unit = commodity.unit
                        )
                    }
                } else {
                    null
                }

                val latestObs = observations.last()
                val isStaleMarket = (market.id == "MKT-24" || market.id == "MKT-29")

                result.add(
                    MarketPriceData(
                        id = stableId,
                        cropName = commodity.name,
                        marketName = market.name,
                        district = market.district,
                        currentPricePerKg = currentPrice,
                        minPricePerKg = minPrice,
                        modalPricePerKg = modalPrice,
                        maxPricePerKg = maxPrice,
                        distanceKm = market.distanceKmFromSatara,
                        transportCostEst = estTransport,
                        lastUpdated = "Sample · ${latestObs.reportedTime}",
                        priceTrend7Days = trend7Days,
                        category = commodity.category,
                        commodityId = commodity.id,
                        marketId = market.id,
                        previousPricePerKg = previousPrice,
                        trendDirection = trendDirection,
                        sampleLoadKg = SAMPLE_LOAD_KG,
                        dataSourceLabel = DATA_SOURCE_LABEL,
                        unit = commodity.unit,
                        marketType = market.marketType,
                        dataStatus = latestObs.dataStatus,
                        freshnessLabel = if (isStaleMarket) "Delayed Bulletin · 4h ago" else latestObs.freshnessLabel,
                        isStale = isStaleMarket,
                        observations = observations,
                        auctionSummary = auctionSummary
                    )
                )
            }
        }

        return result
    }
}

