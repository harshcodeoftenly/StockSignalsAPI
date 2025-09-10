package com.stockapp.model;

public class Fundamental {
    private String symbol;
    private String marketCap;
    private String pe;
    private String eps;
    private String high52;
    private String low52;

    public Fundamental() {}
    public Fundamental(String symbol) { this.symbol = symbol; }

    // Getters and Setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getMarketCap() { return marketCap; }
    public void setMarketCap(String marketCap) { this.marketCap = marketCap; }

    public String getPe() { return pe; }
    public void setPe(String pe) { this.pe = pe; }

    public String getEps() { return eps; }
    public void setEps(String eps) { this.eps = eps; }

    public String getHigh52() { return high52; }
    public void setHigh52(String high52) { this.high52 = high52; }

    public String getLow52() { return low52; }
    public void setLow52(String low52) { this.low52 = low52; }
}
