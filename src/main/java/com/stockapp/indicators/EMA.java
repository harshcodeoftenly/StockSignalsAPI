package com.stockapp.indicators;

import com.stockapp.dto.EMAResponse;
import com.stockapp.model.OHLCV;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EMA implements IndicatorCalculator {
    @Override
    public String getName() { return "ema"; }

    @Override
    public Object calculate(StockData stockData, Map<String, String> params) {
        int period = parseIntParam(params.get("period"), 14);
        List<OHLCV> data = stockData.getOhlcv();
        if (data == null || data.size() < period) {
            return new EMAResponse(stockData.getSymbol(), 0.0);
        }

        List<Double> closes = extractCloses(data);
        double ema = calculateEMA(closes, period);

        String date = data.get(data.size() - 1).getTimestamp().toString();
        return new EMAResponse(date, ema);
    }

    @Override
    public List<EMAResponse> calculateSeries(StockData stockData, Map<String, String> params) {
        int period = parseIntParam(params.get("period"), 14);
        List<OHLCV> data = stockData.getOhlcv();
        List<EMAResponse> series = new ArrayList<>();

        if (data == null || data.size() < period) return series;

        List<Double> closes = extractCloses(data);
        double k = 2.0 / (period + 1);
        double ema = closes.subList(0, period).stream().mapToDouble(d -> d).average().orElse(0.0);

        series.add(new EMAResponse(data.get(period - 1).getTimestamp().toString(), ema));

        for (int i = period; i < closes.size(); i++) {
            ema = closes.get(i) * k + ema * (1 - k);
            series.add(new EMAResponse(data.get(i).getTimestamp().toString(), ema));
        }
        return series;
    }

    private double calculateEMA(List<Double> closes, int period) {
        double k = 2.0 / (period + 1);
        double ema = closes.subList(0, period).stream().mapToDouble(d -> d).average().orElse(0.0);
        for (int i = period; i < closes.size(); i++) {
            ema = closes.get(i) * k + ema * (1 - k);
        }
        return ema;
    }

    private List<Double> extractCloses(List<OHLCV> data) {
        List<Double> closes = new ArrayList<>();
        for (OHLCV candle : data) {
            closes.add(candle.getClose());
        }
        return closes;
    }

    private int parseIntParam(String v, int def) {
        try { return v == null ? def : Integer.parseInt(v); } catch (Exception e) { return def; }
    }
}
