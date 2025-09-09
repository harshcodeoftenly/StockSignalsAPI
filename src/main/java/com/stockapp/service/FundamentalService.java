package com.stockapp.service;

import com.stockapp.model.FundamentalData;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Service;

@Service
public class FundamentalService {
    private final YahooFinanceService yahooService;

    public FundamentalService(YahooFinanceService yahooService) { this.yahooService = yahooService; }

    public FundamentalData getFundamentalData(String symbol) {
        StockData sd = yahooService.fetchStockData(symbol, "1y", "1d");
        Object mc = sd.getMeta().get("marketCap");
        double marketCap = mc instanceof Number ? ((Number) mc).doubleValue() : 0.0;
        return new FundamentalData(marketCap, 0.0, 0.0);
    }
}
