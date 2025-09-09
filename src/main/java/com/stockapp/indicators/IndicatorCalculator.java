package com.stockapp.indicators;

import com.stockapp.model.StockData;

import java.util.Map;

public interface IndicatorCalculator {
    String getName();
    Object calculate(StockData stockData, Map<String, String> params);
}
