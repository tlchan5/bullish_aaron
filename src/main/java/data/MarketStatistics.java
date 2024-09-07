package data;

public class MarketStatistics {
    private double lowPrice;
    private double highPrice;
    private double currentPrice;

    // Constructor to initialize low, high, and current prices with the first tick's price
    public MarketStatistics(double initialPrice) {
        this.lowPrice = initialPrice;
        this.highPrice = initialPrice;
        this.currentPrice = initialPrice;
    }

    // Updates the current, low, and high prices with the new price
    public void updatePrice(double price) {
        currentPrice = price;
        if (price < lowPrice) {
            lowPrice = price;
        }
        if (price > highPrice) {
            highPrice = price;
        }
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    @Override
    public String toString() {
        return "MarketStatistics{" +
                "lowPrice=" + lowPrice +
                ", highPrice=" + highPrice +
                ", currentPrice=" + currentPrice +
                '}';
    }
}
