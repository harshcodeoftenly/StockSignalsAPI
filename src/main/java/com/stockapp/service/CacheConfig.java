package com.stockapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    @Value("${stock.cache.ttl-ms:120000}")
    private long ttlMs;

    public long getTtlMs() { return ttlMs; }
}
