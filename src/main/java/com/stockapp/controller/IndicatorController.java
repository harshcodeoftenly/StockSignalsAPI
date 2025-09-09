package com.stockapp.controller;

import com.stockapp.service.IndicatorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/indicators")
public class IndicatorController {
    private final IndicatorService indicatorService;
    public IndicatorController(IndicatorService indicatorService) { this.indicatorService = indicatorService; }

    @GetMapping("/{symbol}/{indicator}")
    public Object getIndicator(@PathVariable String symbol, @PathVariable String indicator) {
        return indicatorService.getIndicator(symbol, indicator);
    }
}
