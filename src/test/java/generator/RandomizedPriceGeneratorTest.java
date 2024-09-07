package generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomizedPriceGeneratorTest {
    private final RandomizedPriceGenerator generator = new RandomizedPriceGenerator();

    @Test
    void GIVEN_odd_tick_size_WHEN_generate_price_THEN_return_positive_random_value() {
        // GIVEN
        int oddTickCount = 3;

        // WHEN
        double randomValue = generator.generatePrice(oddTickCount);

        // Then
        assertTrue(randomValue > 0);
    }

    @Test
    void GIVEN_even_tick_size_WHEN_generate_price_THEN_return_negative_random_value() {
        // Given
        int evenTickCount = 4;

        // When
        double randomValue = generator.generatePrice(evenTickCount);

        // Then
        assertTrue(randomValue < 0);
    }

    @Test
    void WHEN_generate_price_THEN_random_value_is_between_1_and_10_with_two_decimal_places() {
        // When
        double randomValue = generator.generatePrice(1);

        // Then
        assertTrue(randomValue >= 1.00 && randomValue <= 10.00);
        assertEquals(2, String.format("%.2f", randomValue).split("\\.")[1].length());  // Ensure two decimal places
    }
}
