package com.stockapp.indicators;

import com.stockapp.model.OHLCV;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

@Component
public class SMA implements IndicatorCalculator {
    @Override
    public String getName() { return "sma"; }

    @Override
    public Object calculate(StockData stockData, Map<String, String> params) {
        int period = parseIntParam(params.get("period"), 14);
        List<OHLCV> ohlcv = stockData.getOhlcv();
        if (ohlcv == null || ohlcv.size() < period) return 0.0;

        int start = ohlcv.size() - period;
        OptionalDouble avg = ohlcv.subList(start, ohlcv.size())
                .stream().mapToDouble(OHLCV::getClose).average();
        return avg.orElse(0.0);
    }

    private int parseIntParam(String v, int def) {
        try { return v == null ? def : Integer.parseInt(v); } catch (Exception e) { return def; }
    }
}
