# Market Data Engine Application

## Overview

This application simulates a simple market data engine that generates and processes market data ticks for different markets. It contains two primary components:
1. `MarketDataEngine`: Act as producer and generates market ticks
2. `MarketDataFeed`: Consumes these ticks and tracks statistical information such as the lowest, highest, and current prices for each market.

The application also supports different price generation strategies, including a standard price increment and a randomized variation based on the tick count.

## Key Features
- **Multiple Market Support**: Track statistics (low, high, and current prices) for multiple markets.
- **Custom Price Generators**: Use different strategies for price generation:
    - Standard price increments.
    - Randomized price adjustments based on tick count (odd/even).
- **Price Validation**: Ensure that generated prices are non-negative.
- **Concurrent Processing**: The engine and feed run concurrently using Java's `ExecutorService`.

## Components

### 1. `MarketDataEngine`
This component is responsible for generating ticks for a specific market. It can use different price generation strategies (e.g., fixed increments or random adjustments) and places valid ticks in a `BlockingQueue` to be consumed by the `MarketDataFeed`.

- **Key Parameters**:
    - `tickQueue`: The queue where generated ticks are placed.
    - `maxTickCount`: The number of ticks to generate.
    - `market`: The market (e.g., "BTC-USD") for which the ticks are generated.
    - `initialPrice`: The starting price for the market.
    - `priceGenerator`: A strategy for generating prices (e.g., standard or randomized).

### 2. `MarketDataFeed`
This component consumes ticks from the `BlockingQueue` and tracks statistics (low, high, and current prices) for each market. It validates each tick’s price and updates the statistics only if the price is valid.

- **Key Parameters**:
    - `tickQueue`: The queue from which ticks are consumed.
    - `marketStats`: A `ConcurrentHashMap` that stores statistics for each market.

### 3. `PriceGenerator` Interface
Defines the contract for generating prices based on the current tick count. Two implementations are provided:
- `StandardPriceGenerator`: Increments the price by a fixed amount for each tick.
- `RandomizedPriceGenerator`: Adds or subtracts a random value to the price based on whether the tick count is odd or even.

### 4. `ValidationUtil`
A utility class used for validating that market prices are non-negative.

### 5. Demo (`SingleMarketDataDemo`)
A simple demo that showcases how to run the `MarketDataEngine` and `MarketDataFeed` concurrently using an `ExecutorService`. 

## Project Structure
```
src/
│
├── data/
│   ├── MarketStatistics.java    # Stores low, high, and current prices for a market
│   ├── MarketTick.java          # Represents a market tick with price and market name
│
├── generator/
│   ├── PriceGenerator.java      # Interface for price generation strategies
│   ├── RandomizedPriceGenerator.java   # Generates random price variations
│   ├── StandardPriceGenerator.java     # Fixed price increment generator
│
├── marketdata/
│   ├── MarketDataEngine.java    # Generates market ticks
│   ├── MarketDataFeed.java      # Consumes and processes market ticks
│
├── validation/
│   └── ValidationUtil.java      # Utility for validation eg price

```

