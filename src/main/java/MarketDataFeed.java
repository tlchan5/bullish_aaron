import data.MarketStatistics;
import data.MarketTick;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class MarketDataFeed implements Runnable {
    private final BlockingQueue<MarketTick> tickQueue;

    // Use a ConcurrentHashMap to store the market prices
    private final Map<String, MarketStatistics> marketStats = new ConcurrentHashMap<>();

    public MarketDataFeed(BlockingQueue<MarketTick> tickQueue) {
        this.tickQueue = tickQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                MarketTick tick = tickQueue.take();
                processTick(tick);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processTick(MarketTick tick) {
        String market = tick.market();
        double price = tick.price();

        // Update or create the statistics for this specific market
        marketStats.computeIfAbsent(market, key -> new MarketStatistics(price))
                .updatePrice(price);

        System.out.println("Processed Tick for Market: " + market + " Price: " + price);
    }

    // Method to get statistics for a specific market
    public MarketStatistics getMarketStatistics(String market) {
        return marketStats.getOrDefault(market, null);
    }

    public Map<String, MarketStatistics> getAllMarketStatistics() {
        return marketStats;
    }
}
