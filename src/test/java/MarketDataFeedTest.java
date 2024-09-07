import data.MarketStatistics;
import data.MarketTick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class MarketDataFeedTest {

    private final BlockingQueue<MarketTick> tickQueue = new LinkedBlockingQueue<>();
    private final MarketDataFeed marketDataFeed = new MarketDataFeed(tickQueue);

    @BeforeEach
    void startDataFeed() {
        new Thread(marketDataFeed).start();
    }

    @Test
    void WHEN_new_ticks_arrive_THEN_market_statistics_are_updated() throws InterruptedException {
        // WHEN
        tickQueue.put(new MarketTick("BTC-USD", 10000.0));
        tickQueue.put(new MarketTick("BTC-USD", 11000.0));
        tickQueue.put(new MarketTick("BTC-USD", 9000.0));

        sleep(10);

        // Then
        MarketStatistics stats = marketDataFeed.getMarketStatistics("BTC-USD");
        assertNotNull(stats);
        assertEquals(9000.0, stats.getLowPrice());
        assertEquals(11000.0, stats.getHighPrice());
        assertEquals(9000.0, stats.getCurrentPrice());
    }

    @Test
    void WHEN_multiple_markets_ticks_arrive_THEN_statistics_are_tracked_separately() throws InterruptedException {
        // When
        tickQueue.put(new MarketTick("BTC-USD", 10000.0));
        tickQueue.put(new MarketTick("ETH-USD", 2000.0));
        tickQueue.put(new MarketTick("BTC-USD", 12000.0));
        tickQueue.put(new MarketTick("ETH-USD", 1800.0));

        sleep(10);

        // Then
        MarketStatistics btcStats = marketDataFeed.getMarketStatistics("BTC-USD");
        MarketStatistics ethStats = marketDataFeed.getMarketStatistics("ETH-USD");

        assertNotNull(btcStats);
        assertNotNull(ethStats);

        // Verify BTC-USD statistics
        assertEquals(10000.0, btcStats.getLowPrice());
        assertEquals(12000.0, btcStats.getHighPrice());
        assertEquals(12000.0, btcStats.getCurrentPrice());

        // Verify ETH-USD statistics
        assertEquals(1800.0, ethStats.getLowPrice());
        assertEquals(2000.0, ethStats.getHighPrice());
        assertEquals(1800.0, ethStats.getCurrentPrice());
    }

    @Test
    void WHEN_negative_price_tick_arrives_THEN_no_market_statistic() throws InterruptedException {
        // When
        tickQueue.put(new MarketTick("ETH-USD", -2000.0));
        sleep(10);

        // Then
        Map<String, MarketStatistics> allMarketStats = marketDataFeed.getAllMarketStatistics();
        assertTrue(allMarketStats.isEmpty());
    }
}
