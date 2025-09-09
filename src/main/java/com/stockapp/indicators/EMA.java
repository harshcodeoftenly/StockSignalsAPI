package com.stockapp.indicators;

import com.stockapp.model.OHLCV;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EMA implements IndicatorCalculator {
    @Override
    public String getName() { return "ema"; }

    @Override
    public Object calculate(StockData stockData, Map<String, String> params) {
        int period = parseIntParam(params.get("period"), 14);
        List<OHLCV> data = stockData.getOhlcv();
        if (data == null || data.size() < period) return 0.0;

        int seedStart = data.size() - period;
        double seed = data.subList(seedStart, seedStart + period)
                .stream().mapToDouble(OHLCV::getClose).average().orElse(0.0);

        double multiplier = 2.0 / (period + 1.0);
        double ema = seed;

        for (int i = seedStart + period; i < data.size(); i++) {
            double close = data.get(i).getClose();
            ema = ((close - ema) * multiplier) + ema;
        }
        return ema;
    }

    private int parseIntParam(String v, int def) { try { return v == null ? def : Integer.parseInt(v); } catch (Exception e) { return def; } }
}
