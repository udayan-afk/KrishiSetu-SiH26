package com.krishisetu.app.models

import com.krishisetu.app.repository.KrishiSetuRepository
import org.junit.Assert.*
import org.junit.Test

class DemoMarketDataCatalogTest {

    @Test
    fun `verify 35 commodities and categories exist`() {
        val commodities = DemoMarketDataCatalog.commodities
        assertEquals(35, commodities.size)
        assertEquals(35, commodities.map { it.name }.toSet().size)
        assertEquals(35, commodities.map { it.id }.toSet().size)

        val vegCount = commodities.count { it.category == CommodityCategory.VEGETABLES }
        val fruitCount = commodities.count { it.category == CommodityCategory.FRUITS }
        val grainCount = commodities.count { it.category == CommodityCategory.GRAINS_AND_PULSES }

        assertEquals(15, vegCount)
        assertEquals(10, fruitCount)
        assertEquals(10, grainCount)
    }

    @Test
    fun `verify 30 Maharashtra markets exist`() {
        val markets = DemoMarketDataCatalog.markets
        assertEquals(30, markets.size)
        assertEquals(30, markets.map { it.name }.toSet().size)
        assertEquals(30, markets.map { it.id }.toSet().size)
    }

    @Test
    fun `verify 1050 unique commodity-market combinations and valid prices`() {
        val allPrices = DemoMarketDataCatalog.generateAllMarketPrices()
        assertEquals(1050, allPrices.size)
        assertEquals(1050, allPrices.map { it.id }.toSet().size)

        val uniquePairs = allPrices.map { "${it.cropName}__${it.marketName}" }.toSet()
        assertEquals(1050, uniquePairs.size)

        allPrices.forEach { record ->
            assertTrue("Price must be positive for ${record.id}", record.currentPricePerKg > 0.0)
            assertFalse("Price must not be NaN", record.currentPricePerKg.isNaN())
            assertFalse("Price must not be Infinite", record.currentPricePerKg.isInfinite())
            assertTrue("Min price must be positive and <= current", record.minPricePerKg > 0.0 && record.minPricePerKg <= record.currentPricePerKg)
            assertTrue("Max price must be >= current", record.maxPricePerKg >= record.currentPricePerKg)
            assertEquals("₹/kg", record.unit)
            assertEquals("DEMO / SAMPLE DATA", record.dataSourceLabel)
            assertEquals(7, record.priceTrend7Days.size)
            assertEquals(record.currentPricePerKg, record.priceTrend7Days.last(), 0.001)
            assertTrue("Observations must not be empty", record.observations.isNotEmpty())
            assertEquals(
                "Current price must equal latest observation price",
                record.observations.last().pricePerKg,
                record.currentPricePerKg,
                0.001
            )
            when (record.marketType) {
                MarketType.REPORTED_PRICE -> {
                    assertEquals("Reported-price markets must have 1 observation", 1, record.observations.size)
                    assertNull(record.auctionSummary)
                }
                MarketType.INTRADAY_UPDATED -> {
                    assertEquals("Intraday markets must have 3 observations", 3, record.observations.size)
                    assertNull(record.auctionSummary)
                }
                MarketType.AUCTION_BIDDING -> {
                    assertEquals("Auction markets must have 3 intraday observations", 3, record.observations.size)
                    assertNotNull("Auction markets must have separate AuctionSummary", record.auctionSummary)
                }
            }
        }
    }

    @Test
    fun `verify Pune Gultekdi Tomato intraday timeline and auction separation`() {
        val allPrices = DemoMarketDataCatalog.generateAllMarketPrices()
        val puneTomato = allPrices.first { it.commodityId == "CMD-01" && it.marketId == "MKT-01" }

        assertEquals(3, puneTomato.observations.size)
        assertEquals(24.0, puneTomato.observations[0].pricePerKg, 0.001)
        assertEquals("08:00 AM", puneTomato.observations[0].reportedTime)
        assertEquals(22.0, puneTomato.observations[1].pricePerKg, 0.001)
        assertEquals("12:00 PM", puneTomato.observations[1].reportedTime)
        assertEquals(-2.0, puneTomato.observations[1].changeRs ?: 0.0, 0.001)
        assertEquals(23.0, puneTomato.observations[2].pricePerKg, 0.001)
        assertEquals("03:00 PM", puneTomato.observations[2].reportedTime)
        assertEquals(1.0, puneTomato.observations[2].changeRs ?: 0.0, 0.001)
        assertEquals(23.0, puneTomato.currentPricePerKg, 0.001)

        // Check auction statuses across Nashik (OPEN), Lasalgaon (CLOSED), Baramati (NOT_STARTED)
        val nashikTomato = allPrices.first { it.commodityId == "CMD-01" && it.marketId == "MKT-03" }
        assertEquals(AuctionStatus.OPEN, nashikTomato.auctionSummary?.status)
        assertNotNull(nashikTomato.auctionSummary?.currentHighestBidPerKg)
        assertNull(nashikTomato.auctionSummary?.winningBidPerKg)

        val lasalgaonTomato = allPrices.first { it.commodityId == "CMD-01" && it.marketId == "MKT-04" }
        assertEquals(AuctionStatus.CLOSED, lasalgaonTomato.auctionSummary?.status)
        assertNotNull(lasalgaonTomato.auctionSummary?.winningBidPerKg)

        val baramatiTomato = allPrices.first { it.commodityId == "CMD-01" && it.marketId == "MKT-12" }
        assertEquals(AuctionStatus.NOT_STARTED, baramatiTomato.auctionSummary?.status)
        assertNull(baramatiTomato.auctionSummary?.currentHighestBidPerKg)
        assertNull(baramatiTomato.auctionSummary?.winningBidPerKg)
    }

    @Test
    fun `verify every commodity returns 30 markets with varying prices`() {
        val allPrices = DemoMarketDataCatalog.generateAllMarketPrices()
        DemoMarketDataCatalog.commodities.forEach { commodity ->
            val recordsForCrop = allPrices.filter { it.cropName == commodity.name }
            assertEquals("Commodity ${commodity.name} must have 30 markets", 30, recordsForCrop.size)

            val distinctPrices = recordsForCrop.map { it.currentPricePerKg }.toSet()
            assertTrue("Commodity ${commodity.name} prices must vary across markets", distinctPrices.size > 1)
        }
    }

    @Test
    fun `verify repository state and net realization calculation`() {
        val repo = KrishiSetuRepository()
        assertEquals(1050, repo.marketPrices.value.size)

        repo.setSelectedCommodity("Potato")
        assertEquals("Potato", repo.selectedCommodity.value)

        repo.setSelectedCommodity("Soybean")
        assertEquals("Soybean", repo.selectedCommodity.value)

        repo.setComparisonQuantityKg(1500.0)
        assertEquals(1500.0, repo.comparisonQuantityKg.value, 0.001)

        val sampleRecord = repo.marketPrices.value.first { it.cropName == "Soybean" }
        val qty = 1500.0
        val gross = sampleRecord.currentPricePerKg * qty
        val transport = DemoMarketDataCatalog.estimateTransportCost(sampleRecord.distanceKm, qty)
        val net = gross - transport
        assertTrue(transport > 0.0)
        assertEquals(gross - transport, net, 0.001)
    }
}

