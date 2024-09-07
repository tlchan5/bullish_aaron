package generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardPriceGeneratorTest {

    @Test
    void WHEN_generate_price_THEN_returns_price_incremented_by_fixed_value() {
        // WHEN
        StandardPriceGenerator generator = new StandardPriceGenerator();
        double generatedPrice = generator.generatePrice(5);

        // Then
        assertEquals(10, generatedPrice, 0.01);
    }
}
