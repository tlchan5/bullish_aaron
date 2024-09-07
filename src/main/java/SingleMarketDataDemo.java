import data.MarketTick;

import java.util.concurrent.*;

public class SingleMarketDataDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        BlockingQueue<MarketTick> tickQueue = new ArrayBlockingQueue<>(100);

        // Create the engine and feed
        MarketDataEngine engine = new MarketDataEngine(tickQueue, 100, "BTC-USD", 20000d );
        MarketDataFeed feed = new MarketDataFeed(tickQueue);

        // Use an ExecutorService to manage the threads
        ExecutorService executorService = Executors.newFixedThreadPool(2);  // 2 threads: one for the engine, one for the feed

        // Submit tasks to the ExecutorService
        executorService.submit(engine).get();
        executorService.submit(feed);

        // Output the statistics for BTC-USD
        System.out.println("BTC-USD : " + feed.getMarketStatistics("BTC-USD"));

        // Shutdown the ExecutorService gracefully after all tasks are complete
        executorService.shutdown();

    }
}
