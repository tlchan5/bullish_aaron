package generator;

public class StandardPriceGenerator implements PriceGenerator {
    private static final double FIXED_INCREMENT = 10.0;

    @Override
    public double generatePrice(int tickCount) {
        // Increment the price by a fixed amount
        return FIXED_INCREMENT;
    }
}
