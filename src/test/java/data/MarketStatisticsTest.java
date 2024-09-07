package data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarketStatisticsTest {
    private static final double INITIAL_PRICE = 100d;
    private final MarketStatistics stats = new MarketStatistics(INITIAL_PRICE);

    @Test
    void WHEN_price_is_updated_to_higher_value_THEN_high_and_current_prices_are_updated() {
        // WHEN
        double newPrice = 120d;
        stats.updatePrice(newPrice);

        // THEN
        assertEquals(100.0, stats.getLowPrice(), 0.01);
        assertEquals(newPrice, stats.getHighPrice(), 0.01);
        assertEquals(newPrice, stats.getCurrentPrice(), 0.01);
    }

    @Test
    void WHEN_price_is_updated_to_lower_value_THEN_low_and_current_prices_are_updated() {
        // WHEN
        double newPrice = 80d;
        stats.updatePrice(newPrice);

        // THEN
        assertEquals(newPrice, stats.getLowPrice(), 0.01);
        assertEquals(INITIAL_PRICE, stats.getHighPrice(), 0.01);
        assertEquals(newPrice, stats.getCurrentPrice(), 0.01);
    }

    @Test
    void WHEN_price_is_updated_multiple_times_THEN_correct_low_high_and_current_prices_are_tracked() {
        // WHEN
        stats.updatePrice(120.0);  // Higher price
        stats.updatePrice(90.0);   // Lower price
        stats.updatePrice(110.0);  // Between low and high

        // THEN
        assertEquals(90.0, stats.getLowPrice(), 0.01);
        assertEquals(120.0, stats.getHighPrice(), 0.01);
        assertEquals(110.0, stats.getCurrentPrice(), 0.01);
    }

    @Test
    void WHEN_price_is_updated_with_same_value_multiple_times_THEN_prices_remain_constant() {
        // WHEN
        stats.updatePrice(INITIAL_PRICE);
        stats.updatePrice(INITIAL_PRICE);

        // Verify that the low, high, and current prices remain the same
        assertEquals(INITIAL_PRICE, stats.getLowPrice(), 0.01);
        assertEquals(INITIAL_PRICE, stats.getHighPrice(), 0.01);
        assertEquals(INITIAL_PRICE, stats.getCurrentPrice(), 0.01);
    }
}
