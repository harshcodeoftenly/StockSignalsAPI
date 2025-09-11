package com.stockapp.controller;

import com.stockapp.service.IndicatorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/indicators")
public class IndicatorController {
    private final IndicatorService indicatorService;

    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    // Latest indicator value (DTO)
    @GetMapping("/{symbol}/{indicator}")
    public Object getIndicator(@PathVariable String symbol, @PathVariable String indicator) {
        return indicatorService.getIndicator(symbol, indicator);
    }

    // Full indicator series (List<DTO>)
    @GetMapping("/{symbol}/{indicator}/series")
    public Object getIndicatorSeries(@PathVariable String symbol, @PathVariable String indicator) {
        return indicatorService.getIndicatorSeries(symbol, indicator);
    }
}
