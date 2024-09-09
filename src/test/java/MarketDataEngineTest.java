import data.MarketTick;
import generator.PriceGenerator;
import generator.StandardPriceGenerator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MarketDataEngineTest {
    private final BlockingQueue<MarketTick> tickQueue = new LinkedBlockingQueue<>();
    private static final String MARKET = "BTC-USD";

    @Test
    void WHEN_construct_engine_with_invalid_price_THEN_throw_exception() {
        // WHEN

        // THEN
        assertThrows(IllegalStateException.class, () -> new MarketDataEngine(tickQueue, 1, MARKET, -100));
    }

    @Test
    void GIVEN_engine_with_standard_price_generator_WHEN_engine_runs_THEN_ticks_added_to_queue() {
        // Given
        int initialPrice = 20000;
        int tickCount = 100;
        MarketDataEngine engine = new MarketDataEngine(tickQueue, tickCount, MARKET, initialPrice);

        // WHEN
        engine.run();

        // Then
        // Verify each tick in the queue is correct
        for (int i = 1; i < tickCount; i++) {
            MarketTick tick = tickQueue.poll();
            assertThat(tick, not(nullValue()));
            assertThat(tick.market(), is(MARKET));
            assertEquals(tick.price(), initialPrice + (i * 10), 0.01);
        }
    }

    @Test
    void GIVEN_engine_with_price_generator_WHEN_engine_runs_THEN_ticks_added_to_queue_with_price_from_generator() {
        // Given
        int initialPrice = 20000;
        double newPrice = 99999;
        PriceGenerator priceGenerator = mock(StandardPriceGenerator.class);

        when(priceGenerator.generatePrice(anyInt())).thenReturn(newPrice);

        MarketDataEngine engine = new MarketDataEngine(tickQueue, 1, MARKET, initialPrice, priceGenerator);

        // When
        engine.run();

        // Then
        assertThat(tickQueue.size(), is(1));
        MarketTick tick = tickQueue.poll();
        assertThat(tick, not(nullValue()));
        assertThat(tick.price(), is(initialPrice + newPrice));
        assertThat(tick.market(), is(MARKET));
    }

    @Test
    void GIVEN_price_generator_generate_a_negative_new_price_WHEN_engine_runs_THEN_no_tick_added_to_queue() {
        // Given
        int initialPrice = 1;
        double newPrice = -10;
        PriceGenerator priceGenerator = mock(StandardPriceGenerator.class);

        when(priceGenerator.generatePrice(anyInt())).thenReturn(newPrice);

        MarketDataEngine engine = new MarketDataEngine(tickQueue, 1, MARKET, initialPrice, priceGenerator);

        // When
        engine.run();

        // Then
        assertThat(tickQueue.size(), is(0));
    }

    @Test
    void GIVEN_engine_with_high_precision_price_WHEN_engine_runs_THEN_double_precision_is_limited() {
        // Given
        int maxTickCount = 1;
        double highPrecisionPrice = 0.12345678901234567;  // More than the precision of double can handle
        PriceGenerator priceGenerator = mock(PriceGenerator.class);

        when(priceGenerator.generatePrice(anyInt())).thenReturn(10d);

        MarketDataEngine engine = new MarketDataEngine(tickQueue, maxTickCount, "BTC-USD", highPrecisionPrice, priceGenerator);

        // When
        engine.run();

        // Then
        assertThat(tickQueue.size(), is(1));
        MarketTick tick = tickQueue.poll();
        assertThat(tick, not(nullValue()));
        assertThat(tick.market(), is(MARKET));
        // Assert that the price precision is limited to double's precision (~15-17 significant digits)
        // 10 + 0.12345678901234567 = 10.12345678901234567
        assertEquals(10.1234567890123456, tick.price(), 0.000000000000001);
    }
}
