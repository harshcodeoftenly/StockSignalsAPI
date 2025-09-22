package com.stockapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.model.OHLCV;
import com.stockapp.model.StockData;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class YahooFinanceService {
    private static final Logger log = LoggerFactory.getLogger(YahooFinanceService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final CacheConfig cacheConfig;
    private final WebClient webClient;

    private final Map<String, CachedData> cache = new ConcurrentHashMap<>();

    @Value("${stock.yahoo.base-url}")
    private String yahooBaseUrl;

    @Value("${stock.yahoo.timeout-ms:10000}")
    private int timeoutMs;

    public YahooFinanceService(CacheConfig cacheConfig,
                               WebClient.Builder webClientBuilder) {
        this.cacheConfig = cacheConfig;
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    private void init() {
        // Rebuild WebClient with the injected baseUrl
        log.info("Initializing YahooFinanceService with baseUrl={} and timeout={}ms", yahooBaseUrl, timeoutMs);
    }

    public StockData fetchStockData(String symbol, String range, String interval) {
        String key = symbol + "|" + range + "|" + interval;
        CachedData cd = cache.get(key);

        if (cd != null && (System.currentTimeMillis() - cd.timestamp) < cacheConfig.getTtlMs()) {
            return cd.stockData;
        }

        try {
            String path = "/v8/finance/chart/" + symbol +
                    "?range=" + range +
                    "&interval=" + interval +
                    "&includePrePost=false&events=div%2Csplit";

            String resp = webClient.mutate()
                    .baseUrl(yahooBaseUrl)
                    .build()
                    .get()
                    .uri(path)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            r -> r.bodyToMono(String.class)
                                    .map(msg -> new RuntimeException("Yahoo returned " + r.statusCode() + " : " + msg)))
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = mapper.readTree(resp);
            List<OHLCV> ohlcv = parseChart(root);
            Map<String, Object> meta = parseMeta(root);

            StockData stockData = new StockData(symbol, ohlcv, meta);
            cache.put(key, new CachedData(stockData));
            return stockData;
        } catch (WebClientResponseException wex) {
            log.error("Yahoo API error: {} - {}", wex.getStatusCode(), wex.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Failed to fetch/parse Yahoo data for {}: {}", symbol, e.getMessage());
        }

        // fallback: return empty StockData so services don't NPE
        return new StockData(symbol, Collections.emptyList(), Map.of("symbol", symbol));
    }

    private List<OHLCV> parseChart(JsonNode root) {
        List<OHLCV> list = new ArrayList<>();
        try {
            JsonNode chart = root.path("chart").path("result");
            if (!chart.isArray() || chart.isEmpty()) return list;

            JsonNode r = chart.get(0);
            JsonNode timestamps = r.path("timestamp");
            JsonNode indicators = r.path("indicators").path("quote");
            if (!timestamps.isArray() || !indicators.isArray() || indicators.isEmpty()) return list;

            JsonNode q = indicators.get(0);
            JsonNode opens = q.path("open");
            JsonNode highs = q.path("high");
            JsonNode lows = q.path("low");
            JsonNode closes = q.path("close");
            JsonNode volumes = q.path("volume");

            for (int i = 0; i < timestamps.size(); i++) {
                long ts = timestamps.get(i).asLong();
                OHLCV o = new OHLCV(
                        Instant.ofEpochSecond(ts),
                        safeDouble(opens, i),
                        safeDouble(highs, i),
                        safeDouble(lows, i),
                        safeDouble(closes, i),
                        safeLong(volumes, i)
                );
                list.add(o);
            }
        } catch (Exception e) {
            log.error("parseChart error: {}", e.getMessage());
        }
        return list;
    }

    private Map<String, Object> parseMeta(JsonNode root) {
        try {
            JsonNode chart = root.path("chart").path("result");
            if (!chart.isArray() || chart.isEmpty()) return Map.of();
            JsonNode meta = chart.get(0).path("meta");

            Map<String, Object> map = new HashMap<>();
            map.put("currency", text(meta, "currency"));
            map.put("symbol", text(meta, "symbol"));
            map.put("exchangeName", text(meta, "exchangeName"));
            map.put("instrumentType", text(meta, "instrumentType"));
            map.put("regularMarketPrice", meta.path("regularMarketPrice").asDouble(0.0));
            map.put("chartPreviousClose", meta.path("chartPreviousClose").asDouble(0.0));
            return map;
        } catch (Exception e) {
            log.error("parseMeta error: {}", e.getMessage());
            return Map.of();
        }
    }

    private double safeDouble(JsonNode arr, int idx) {
        try { return arr.get(idx).isNull() ? Double.NaN : arr.get(idx).asDouble(); } catch (Exception e) { return Double.NaN; }
    }

    private long safeLong(JsonNode arr, int idx) {
        try { return arr.get(idx).isNull() ? 0L : arr.get(idx).asLong(); } catch (Exception e) { return 0L; }
    }

    private String text(JsonNode node, String field) {
        return node.path(field).isMissingNode() ? null : node.path(field).asText(null);
    }

    private static class CachedData {
        long timestamp;
        StockData stockData;
        CachedData(StockData s) {
            this.timestamp = System.currentTimeMillis();
            this.stockData = s;
        }
    }
}
