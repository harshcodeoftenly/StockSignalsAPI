package com.stockapp.indicators;

import com.stockapp.dto.MACDResponse;
import com.stockapp.model.OHLCV;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MACD implements IndicatorCalculator {
    @Override
    public String getName() { return "macd"; }

    @Override
    public Object calculate(StockData stockData, Map<String, String> params) {
        List<MACDResponse> series = calculateSeries(stockData, params);
        return series.isEmpty() ? new MACDResponse(stockData.getSymbol(), 0.0, 0.0, 0.0)
                : series.get(series.size() - 1);
    }

    @Override
    public List<MACDResponse> calculateSeries(StockData stockData, Map<String, String> params) {
        int fast = parseIntParam(params.get("fast"), 12);
        int slow = parseIntParam(params.get("slow"), 26);
        int signal = parseIntParam(params.get("signal"), 9);

        List<OHLCV> data = stockData.getOhlcv();
        if (data == null) return Collections.emptyList();

        List<Double> closes = data.stream().map(OHLCV::getClose).collect(Collectors.toList());
        List<Double> emaFast = computeEMAList(closes, fast);
        List<Double> emaSlow = computeEMAList(closes, slow);

        List<Double> macdLine = new ArrayList<>(Collections.nCopies(closes.size(), null));
        for (int i = 0; i < closes.size(); i++) {
            Double ef = emaFast.get(i);
            Double es = emaSlow.get(i);
            if (ef != null && es != null) {
                macdLine.set(i, ef - es);
            }
        }

        List<Double> macdNonNull = macdLine.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (macdNonNull.size() < signal) return Collections.emptyList();

        List<Double> signalEmaOnMacd = computeEMAList(macdNonNull, signal);

        List<MACDResponse> responses = new ArrayList<>();
        for (int i = slow; i < data.size(); i++) {
            if (macdLine.get(i) != null) {
                int macdIndex = macdNonNull.indexOf(macdLine.get(i));
                if (macdIndex >= signal - 1 && macdIndex < signalEmaOnMacd.size()) {
                    double macdVal = macdLine.get(i);
                    double signalVal = signalEmaOnMacd.get(macdIndex);
                    double histogram = macdVal - signalVal;

                    String date = data.get(i).getTimestamp().toString();
                    responses.add(new MACDResponse(date, macdVal, signalVal, histogram));
                }
            }
        }
        return responses;
    }

    private List<Double> computeEMAList(List<Double> input, int period) {
        int n = input.size();
        List<Double> emas = new ArrayList<>(Collections.nCopies(n, null));
        if (n < period) return emas;

        double seed = input.subList(0, period).stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double multiplier = 2.0 / (period + 1.0);
        double ema = seed;
        emas.set(period - 1, ema);

        for (int i = period; i < n; i++) {
            double price = input.get(i);
            ema = ((price - ema) * multiplier) + ema;
            emas.set(i, ema);
        }
        return emas;
    }

    private int parseIntParam(String v, int def) {
        try { return v == null ? def : Integer.parseInt(v); } catch (Exception e) { return def; }
    }
}
