package data;

public record MarketTick(String market, double price) {
    @Override
    public String market() {
        return market;
    }

    @Override
    public double price() {
        return price;
    }
}
