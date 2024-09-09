import data.MarketStatistics;
import data.MarketTick;
import generator.RandomizedPriceGenerator;

import java.util.Map;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class MarketDataEngineDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        BlockingQueue<MarketTick> tickQueue = new ArrayBlockingQueue<>(100);

        // Create the engine and feed
        MarketDataEngine btcMarketDataEngine = new MarketDataEngine(tickQueue, 100, "BTC-USD", 20000d);
        MarketDataEngine ethMarketDataEngine = new MarketDataEngine(tickQueue, 100, "ETH-USD", 1000, new RandomizedPriceGenerator());
        MarketDataFeed feed = new MarketDataFeed(tickQueue);

        // Use an ExecutorService to manage the threads
        ExecutorService producerExecutor = Executors.newFixedThreadPool(2);
        ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();

        // Submit tasks to the ExecutorService
        Future<?> btcPublishingFuture = producerExecutor.submit(btcMarketDataEngine);
        Future<?> ethPublishingFuture = producerExecutor.submit(ethMarketDataEngine);
        consumerExecutor.submit(feed);

        btcPublishingFuture.get();
        ethPublishingFuture.get();

        producerExecutor.shutdown();

        sleep(10);

        // Output the statistics for all markets
        Map<String, MarketStatistics> stats = feed.getAllMarketStatistics();
        for (Map.Entry<String, MarketStatistics> entry : stats.entrySet()) {
            System.out.println("Market Statistics:" + entry.getKey() + "-" + entry.getValue());
        }

        consumerExecutor.shutdownNow();
    }
}
