package com.stockapp.service;

import com.stockapp.model.StockData;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PriceService {
    private final YahooFinanceService yahooService;

    public PriceService(YahooFinanceService yahooService) { this.yahooService = yahooService; }

    public double getLivePrice(String symbol) {
        StockData sd = yahooService.fetchStockData(symbol, "1d", "1d");
        Object p = sd.getMeta().get("regularMarketPrice");
        return p instanceof Number ? ((Number)p).doubleValue() : 0.0;
    }

    public Map<String, Double> get52WeekRange(String symbol) {
        StockData sd = yahooService.fetchStockData(symbol, "1y", "1d");
        double low = sd.getOhlcv().stream().mapToDouble(o -> o.getClose()).min().orElse(0.0);
        double high = sd.getOhlcv().stream().mapToDouble(o -> o.getClose()).max().orElse(0.0);
        return Map.of("low", low, "high", high);
    }

    public Map<String, Double> getTodayRange(String symbol) {
        StockData sd = yahooService.fetchStockData(symbol, "1d", "1d");
        double low = sd.getOhlcv().stream().mapToDouble(o -> o.getLow()).min().orElse(0.0);
        double high = sd.getOhlcv().stream().mapToDouble(o -> o.getHigh()).max().orElse(0.0);
        return Map.of("low", low, "high", high);
    }

    public StockData getPriceRange(String symbol, String range) {
        return yahooService.fetchStockData(symbol, range, "1d");
    }
}
