package generator;

import java.util.Random;

public class RandomizedPriceGenerator implements PriceGenerator {
    private final Random random = new Random();

    @Override
    public double generatePrice(int tickCount) {
        // Generate a random value between 1.00 and 10.00, inclusive of two decimal places
        double randomValue = 1.00 + (random.nextDouble() * 9.00);
        randomValue = Math.round(randomValue * 100.0) / 100.0;  // Round to two decimal places


        // If tickCount is odd, add the random value; if even, subtract it
        if (tickCount % 2 == 0) {
            return -randomValue;  // Even tick count: subtract
        } else {
            return randomValue;  // Odd tick count: add
        }
    }
}
