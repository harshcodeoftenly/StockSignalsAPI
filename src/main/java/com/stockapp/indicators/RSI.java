package com.stockapp.indicators;

import com.stockapp.dto.RSIResponse;
import com.stockapp.model.OHLCV;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RSI implements IndicatorCalculator {
    @Override
    public String getName() { return "rsi"; }

    @Override
    public Object calculate(StockData stockData, Map<String, String> params) {
        List<RSIResponse> series = calculateSeries(stockData, params);
        return series.isEmpty() ? new RSIResponse(stockData.getSymbol(), 0.0)
                : series.get(series.size() - 1);
    }

    @Override
    public List<RSIResponse> calculateSeries(StockData stockData, Map<String, String> params) {
        int period = parseIntParam(params.get("period"), 14);
        List<OHLCV> data = stockData.getOhlcv();
        List<RSIResponse> rsis = new ArrayList<>();

        if (data == null || data.size() <= period) return rsis;

        double gain = 0.0, loss = 0.0;
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

            double rsi = avgLoss == 0 ? 100.0 : 100 - (100 / (1 + (avgGain / avgLoss)));
            String date = data.get(i).getTimestamp().toString();
            rsis.add(new RSIResponse(date, rsi));
        }

        return rsis;
    }

    private int parseIntParam(String v, int def) {
        try { return v == null ? def : Integer.parseInt(v); } catch (Exception e) { return def; }
    }
}
