//package com.stockapp.indicators;
//
//import com.stockapp.model.OHLCV;
//import com.stockapp.model.StockData;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//public class MACD implements IndicatorCalculator {
//    @Override
//    public String getName() { return "macd"; }
//
//    @Override
//    public Object calculate(StockData stockData, Map<String, String> params) {
//        int fast = parseIntParam(params.get("fast"), 12);
//        int slow = parseIntParam(params.get("slow"), 26);
//        int signal = parseIntParam(params.get("signal"), 9);
//
//        List<OHLCV> data = stockData.getOhlcv();
//        if (data == null || data.size() < slow + signal) {
//            return Map.of("macd", 0.0, "signal", 0.0, "histogram", 0.0);
//        }
//
//        List<Double> closes = data.stream().map(OHLCV::getClose).collect(Collectors.toList());
//        List<Double> emaFast = computeEMAList(closes, fast);
//        List<Double> emaSlow = computeEMAList(closes, slow);
//
//        int offset = emaSlow.size() - emaFast.size();
//        List<Double> macdLine = new ArrayList<>();
//        for (int i = 0; i < emaFast.size(); i++) {
//            macdLine.add(emaFast.get(i) - emaSlow.get(i + offset));
//        }
//
//        List<Double> signalLine = computeEMAList(macdLine, signal);
//        double macd = macdLine.get(macdLine.size() - 1);
//        double sig = signalLine.get(signalLine.size() - 1);
//        double hist = macd - sig;
//
//        return Map.of("macd", macd, "signal", sig, "histogram", hist);
//    }
//
//    private int parseIntParam(String v, int def) { try { return v == null ? def : Integer.parseInt(v); } catch (Exception e) { return def; } }
//
//    private List<Double> computeEMAList(List<Double> closes, int period) {
//        if (closes.size() < period) return Collections.emptyList();
//        double seed = closes.subList(0, period).stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
//        double multiplier = 2.0 / (period + 1.0);
//        List<Double> emas = new ArrayList<>();
//        double ema = seed;
//        emas.add(ema);
//        for (int i = period; i < closes.size(); i++) {
//            double price = closes.get(i);
//            ema = ((price - ema) * multiplier) + ema;
//            emas.add(ema);
//        }
//        return emas;
//    }
//}

package com.stockapp.indicators;

import com.stockapp.model.OHLCV;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MACD implements IndicatorCalculator {
    @Override
    public String getName() {
        return "macd";
    }

    @Override
    public Object calculate(StockData stockData, Map<String, String> params) {
        int fast = parseIntParam(params.get("fast"), 12);
        int slow = parseIntParam(params.get("slow"), 26);
        int signal = parseIntParam(params.get("signal"), 9);

        List<OHLCV> data = stockData.getOhlcv();
        if (data == null || data.size() < Math.max(slow, fast) + signal) {
            return Map.of("error", "Not enough data for MACD calculation");
        }

        List<Double> closes = data.stream()
                .map(OHLCV::getClose)
                .collect(Collectors.toList());

        // Compute aligned EMA lists
        List<Double> emaFast = computeEMAList(closes, fast);
        List<Double> emaSlow = computeEMAList(closes, slow);

        // MACD line = fastEMA - slowEMA
        List<Double> macdLine = new ArrayList<>();
        for (int i = 0; i < closes.size(); i++) {
            if (emaFast.get(i) != null && emaSlow.get(i) != null) {
                macdLine.add(emaFast.get(i) - emaSlow.get(i));
            }
        }

        // Signal line = EMA of MACD line
        List<Double> signalLine = computeEMAList(macdLine, signal);

        // Latest values
        double macd = macdLine.get(macdLine.size() - 1);
        double sig = signalLine.get(signalLine.size() - 1);
        double hist = macd - sig;

        return Map.of("macd", macd, "signal", sig, "histogram", hist);
    }

    private int parseIntParam(String v, int def) {
        try {
            return v == null ? def : Integer.parseInt(v);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Compute EMA list aligned with closes length.
     * First (period-1) values are null until enough data is available.
     */
    private List<Double> computeEMAList(List<Double> closes, int period) {
        List<Double> emas = new ArrayList<>(Collections.nCopies(closes.size(), null));

        if (closes.size() < period) return emas; // Not enough data

        // Initial seed SMA
        double seed = closes.subList(0, period).stream()
                .mapToDouble(Double::doubleValue)
                .average().orElse(0.0);

        double multiplier = 2.0 / (period + 1.0);
        double ema = seed;
        emas.set(period - 1, ema);

        // Continue with EMA from period onward
        for (int i = period; i < closes.size(); i++) {
            double price = closes.get(i);
            ema = ((price - ema) * multiplier) + ema;
            emas.set(i, ema);
        }

        return emas;
    }
}
