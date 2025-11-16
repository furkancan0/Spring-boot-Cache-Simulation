package com.cache.CacheUtil;

import lombok.Getter;

public class CacheEntry<V> {
    private final V value;
    private final long expiryTime;
    @Getter
    private volatile long lastAccess;

    public CacheEntry(V value, long ttlMillis) {
        this.value = value;
        this.expiryTime = System.currentTimeMillis() + ttlMillis;
        this.lastAccess = System.nanoTime();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }

    public V getValue() {
        this.lastAccess = System.nanoTime();
        return value;
    }
}
