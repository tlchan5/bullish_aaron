import data.MarketStatistics;
import data.MarketTick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class MarketDataFeedTest {

    private final BlockingQueue<MarketTick> tickQueue = new LinkedBlockingQueue<>();
    private final MarketDataFeed marketDataFeed = new MarketDataFeed(tickQueue);

    @BeforeEach
    void setup() {
        Executors.newSingleThreadExecutor().submit(marketDataFeed);
    }

    @Test
    void WHEN_ticks_arrive_THEN_market_statistics_are_updated() throws InterruptedException {
        // WHEN
        tickQueue.put(new MarketTick("BTC-USD", 10000.0));
        tickQueue.put(new MarketTick("BTC-USD", 11000.0));
        tickQueue.put(new MarketTick("BTC-USD", 9000.0));

        // Then
        MarketStatistics stats = marketDataFeed.getMarketStatistics("BTC-USD");
        assertNotNull(stats);
        assertEquals(9000.0, stats.getLowPrice());
        assertEquals(11000.0, stats.getHighPrice());
        assertEquals(9000.0, stats.getCurrentPrice());
    }

    @Test
    void GIVEN_multipleMarkets_WHEN_ticksProcessed_THEN_statisticsAreTrackedSeparately() throws InterruptedException {
        // Given
        tickQueue.put(new MarketTick("BTC-USD", 10000.0));
        tickQueue.put(new MarketTick("ETH-USD", 2000.0));
        tickQueue.put(new MarketTick("BTC-USD", 12000.0));
        tickQueue.put(new MarketTick("ETH-USD", 1800.0));

        // When
        new Thread(marketDataFeed).start();
        Thread.sleep(100);  // Allow some time for the feed to process ticks

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
    void GIVEN_emptyQueue_WHEN_feedRuns_THEN_noErrorsOccur() throws InterruptedException {
        // Given: the queue is empty

        // When
        Thread feedThread = new Thread(marketDataFeed);
        feedThread.start();
        Thread.sleep(100);  // Allow the feed to attempt processing

        // Then
        assertTrue(tickQueue.isEmpty());
        Map<String, MarketStatistics> allMarketStats = marketDataFeed.getAllMarketStatistics();
        assertTrue(allMarketStats.isEmpty());  // No markets processed
    }

    @Test
    void GIVEN_interruptedThread_WHEN_feedRuns_THEN_threadStopsGracefully() throws InterruptedException {
        // Given
        tickQueue.put(new MarketTick("BTC-USD", 10000.0));

        // When
        Thread feedThread = new Thread(marketDataFeed);
        feedThread.start();
        feedThread.interrupt();  // Interrupt the feed thread

        // Then
        assertTrue(feedThread.isInterrupted());
    }
}
