package com.cache.CacheUtil;

import com.cache.Entity.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    @Bean
    public TTL_LRUCache<String, Product> ttlCache() {
        // max entries configurable, TTL per-entry will be provided at put time
        return new TTL_LRUCache<>(8); // max 8 entries by default
    }
}