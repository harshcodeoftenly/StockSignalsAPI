package com.stockapp.indicators;

import com.stockapp.dto.SMAResponse;
import com.stockapp.model.OHLCV;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SMA implements IndicatorCalculator {
    @Override
    public String getName() { return "sma"; }

    @Override
    public Object calculate(StockData stockData, Map<String, String> params) {
        int period = parseIntParam(params.get("period"), 14);
        List<OHLCV> data = stockData.getOhlcv();
        if (data == null || data.size() < period) {
            return new SMAResponse(stockData.getSymbol(), 0.0);
        }

        int start = data.size() - period;
        double avg = data.subList(start, data.size())
                .stream().mapToDouble(OHLCV::getClose).average().orElse(0.0);

        String date = data.get(data.size() - 1).getTimestamp().toString();
        return new SMAResponse(date, avg);
    }

    @Override
    public List<SMAResponse> calculateSeries(StockData stockData, Map<String, String> params) {
        int period = parseIntParam(params.get("period"), 14);
        List<OHLCV> data = stockData.getOhlcv();
        List<SMAResponse> series = new ArrayList<>();

        if (data == null || data.size() < period) return series;

        for (int i = 0; i <= data.size() - period; i++) {
            double avg = data.subList(i, i + period)
                    .stream().mapToDouble(OHLCV::getClose).average().orElse(0.0);

            String date = data.get(i + period - 1).getTimestamp().toString();
            series.add(new SMAResponse(date, avg));
        }
        return series;
    }

    private int parseIntParam(String v, int def) {
        try { return v == null ? def : Integer.parseInt(v); } catch (Exception e) { return def; }
    }
}
