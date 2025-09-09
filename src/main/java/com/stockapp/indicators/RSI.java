package com.stockapp.indicators;

import com.stockapp.model.OHLCV;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RSI implements IndicatorCalculator {
    @Override
    public String getName() { return "rsi"; }

    @Override
    public Object calculate(StockData stockData, Map<String, String> params) {
        int period = parseIntParam(params.get("period"), 14);
        List<OHLCV> data = stockData.getOhlcv();
        if (data == null || data.size() <= period) return 0.0;

        double gain = 0.0;
        double loss = 0.0;

        for (int i = 1; i <= period; i++) {
            double change = data.get(i).getClose() - data.get(i - 1).getClose();
            if (change > 0) gain += change; else loss += Math.abs(change);
        }
        double avgGain = gain / period;
        double avgLoss = loss / period;

        for (int i = period + 1; i < data.size(); i++) {
            double change = data.get(i).getClose() - data.get(i - 1).getClose();
            double g = Math.max(0, change);
            double l = Math.max(0, -change);
            avgGain = ((avgGain * (period - 1)) + g) / period;
            avgLoss = ((avgLoss * (period - 1)) + l) / period;
        }

        if (avgLoss == 0) return 100.0;
        double rs = avgGain / avgLoss;
        double rsi = 100 - (100 / (1 + rs));
        return rsi;
    }

    private int parseIntParam(String v, int def) { try { return v == null ? def : Integer.parseInt(v); } catch (Exception e) { return def; } }
}
