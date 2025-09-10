package com.stockapp.service;

import com.stockapp.model.Fundamental;
import com.stockapp.model.FundamentalData;
import com.stockapp.model.StockData;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class FundamentalService {
    private static final String GOOGLE_SHEET_CSV_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSVt02Q-x9KWA44HtnThWtJCLW124VuW29vUMVsJAvE-UPY_WGndtKjlR3BtC_ez9fCl8jp3KP8VJJC/pub?output=csv";

    private final Map<String, Fundamental> cache = new HashMap<>();

    public Fundamental getFundamentalData(String symbol) {
        symbol = symbol.toUpperCase();

        if (cache.containsKey(symbol)) {
            return cache.get(symbol);
        }

        try {
            URL url = new URL(GOOGLE_SHEET_CSV_URL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            String[] headers = null;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);

                if (headers == null) {
                    headers = values;
                    continue;
                }

                if (values[0].equalsIgnoreCase(symbol)) {
                    Fundamental fundamental = new Fundamental();
                    fundamental.setSymbol(values[0]);
                    fundamental.setMarketCap(values[1]);
                    fundamental.setPe(values[2]);
                    fundamental.setEps(values[3]);
                    fundamental.setHigh52(values[4]);
                    fundamental.setLow52(values[5]);

                    cache.put(symbol, fundamental);
                    return fundamental;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return empty object if not found
        return new Fundamental(symbol);
    }
}
