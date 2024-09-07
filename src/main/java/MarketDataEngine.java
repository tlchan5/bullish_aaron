import data.MarketTick;
import generator.PriceGenerator;
import generator.StandardPriceGenerator;

import java.util.concurrent.BlockingQueue;

public class MarketDataEngine implements Runnable {
    private final BlockingQueue<MarketTick> tickQueue;
    private double currentPrice;
    private final PriceGenerator priceGenerator;
    private final int maxTickCount;
    private final String market;

    public MarketDataEngine(BlockingQueue<MarketTick> tickQueue, int maxTickCount, String market, double initialPrice) {
        this(tickQueue, maxTickCount, market, initialPrice, new StandardPriceGenerator());
    }

    public MarketDataEngine(BlockingQueue<MarketTick> tickQueue, int maxTickCount, String market, double initialPrice, PriceGenerator priceGenerator) {
        this.tickQueue = tickQueue;
        this.maxTickCount = maxTickCount;
        this.market = market;
        this.currentPrice = initialPrice;
        this.priceGenerator = priceGenerator;
    }

    @Override
    public void run() {
        try {
            for (int tickCount = 0; tickCount < maxTickCount; tickCount++) {
                currentPrice += priceGenerator.generatePrice(tickCount);
                MarketTick tick = new MarketTick(market, currentPrice);
                tickQueue.put(tick);  // Adds the tick to the queue
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restore interrupt status
        }
    }
}
