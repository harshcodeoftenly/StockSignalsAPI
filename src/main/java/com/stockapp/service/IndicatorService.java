package com.stockapp.service;

import com.stockapp.indicators.IndicatorCalculator;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndicatorService {
    private final YahooFinanceService yahooService;
    private final Map<String, IndicatorCalculator> calculators = new HashMap<>();

    public IndicatorService(YahooFinanceService yahooService, List<IndicatorCalculator> indicatorCalculators) {
        this.yahooService = yahooService;
        for (IndicatorCalculator calc : indicatorCalculators) calculators.put(calc.getName().toLowerCase(), calc);
    }

    public Object getIndicator(String symbol, String indicator) {
        String[] parts = indicator.split(":");
        String name = parts[0].toLowerCase();
        IndicatorCalculator calc = calculators.get(name);
        if (calc == null) return Map.of("error", "Indicator not implemented", "requested", indicator);

        Map<String,String> params = new HashMap<>();
        if ("sma".equals(name) || "ema".equals(name) || "rsi".equals(name)) {
            if (parts.length >= 2) params.put("period", parts[1]);
        } else if ("macd".equals(name)) {
            if (parts.length >= 2) params.put("fast", parts[1]);
            if (parts.length >= 3) params.put("slow", parts[2]);
            if (parts.length >= 4) params.put("signal", parts[3]);
        }

        StockData sd = yahooService.fetchStockData(symbol, "6mo", "1d");
        return calc.calculate(sd, params);
    }
}
