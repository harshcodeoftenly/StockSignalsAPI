package com.stockapp.dto;

public class EMAResponse {
    private String date;
    private Double value;

    public EMAResponse(String date, Double value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public Double getValue() {
        return value;
    }
}
