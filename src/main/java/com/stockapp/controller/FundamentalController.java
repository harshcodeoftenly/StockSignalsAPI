package com.stockapp.controller;

import com.stockapp.model.Fundamental;
import com.stockapp.service.FundamentalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fundamentals")
public class FundamentalController {
    private final FundamentalService fundamentalService;

    public FundamentalController(FundamentalService fundamentalService ) {
        this.fundamentalService = fundamentalService;
    }

    @GetMapping("/{symbol}")
    public Fundamental getFundamentals(@PathVariable String symbol) {
        return fundamentalService.getFundamentalData(symbol);
    }}
