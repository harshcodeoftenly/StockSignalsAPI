package com.stockapp.dto;

public class MACDResponse {
    private String date;
    private Double macd;
    private Double signal;
    private Double histogram;

    public MACDResponse(String date, Double macd, Double signal, Double histogram) {
        this.date = date;
        this.macd = macd;
        this.signal = signal;
        this.histogram = histogram;
    }

    public String getDate() {
        return date;
    }

    public Double getMacd() {
        return macd;
    }

    public Double getSignal() {
        return signal;
    }

    public Double getHistogram() {
        return histogram;
    }
}
