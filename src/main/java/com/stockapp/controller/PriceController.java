package com.stockapp.controller;

import com.stockapp.model.StockData;
import com.stockapp.service.PriceService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/price")
public class PriceController {
    private final PriceService priceService;
    public PriceController(PriceService priceService) { this.priceService = priceService; }

    @GetMapping("/{symbol}/live")
    public double getLivePrice(@PathVariable String symbol) { return priceService.getLivePrice(symbol); }

    @GetMapping("/{symbol}/52week")
    public Map<String, Double> get52WeekRange(@PathVariable String symbol) { return priceService.get52WeekRange(symbol); }

    @GetMapping("/{symbol}/today")
    public Map<String, Double> getTodayRange(@PathVariable String symbol) { return priceService.getTodayRange(symbol); }

    @GetMapping("/{symbol}/range")
    public StockData getPriceRange(@PathVariable String symbol, @RequestParam String range) { return priceService.getPriceRange(symbol, range); }
}
