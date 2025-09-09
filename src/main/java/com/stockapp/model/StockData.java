package com.stockapp.model;

import java.util.List;
import java.util.Map;

public class StockData {
    private String symbol;
    private List<OHLCV> ohlcv;
    private Map<String, Object> meta;

    public StockData() {}

    public StockData(String symbol, List<OHLCV> ohlcv, Map<String, Object> meta) {
        this.symbol = symbol;
        this.ohlcv = ohlcv;
        this.meta = meta;
    }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public List<OHLCV> getOhlcv() { return ohlcv; }
    public void setOhlcv(List<OHLCV> ohlcv) { this.ohlcv = ohlcv; }
    public Map<String, Object> getMeta() { return meta; }
    public void setMeta(Map<String, Object> meta) { this.meta = meta; }
}
