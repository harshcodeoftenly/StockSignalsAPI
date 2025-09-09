package com.stockapp.model;

public class FundamentalData {
    private double marketCap;
    private double debt;
    private double peRatio;

    public FundamentalData() {}

    public FundamentalData(double marketCap, double debt, double peRatio) {
        this.marketCap = marketCap;
        this.debt = debt;
        this.peRatio = peRatio;
    }

    public double getMarketCap() { return marketCap; }
    public void setMarketCap(double marketCap) { this.marketCap = marketCap; }
    public double getDebt() { return debt; }
    public void setDebt(double debt) { this.debt = debt; }
    public double getPeRatio() { return peRatio; }
    public void setPeRatio(double peRatio) { this.peRatio = peRatio; }
}
