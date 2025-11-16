package com.cache.CacheUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TTL_LRUCache<K, V> {

    private final int maxEntries;
    private final ConcurrentHashMap<K, CacheEntry<V>> map = new ConcurrentHashMap<>();

    public TTL_LRUCache(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    public void put(K key, V value, long ttlMillis) {
        map.put(key, new CacheEntry<>(value, ttlMillis));
        evictIfNeeded();
    }

    public V get(K key) {
        CacheEntry<V> entry = map.get(key);
        if (entry == null) return null;
        if (entry.isExpired()) {
            map.remove(key);
            return null;
        }
        return entry.getValue();
    }

    private void evictIfNeeded() {
        if (map.size() <= maxEntries) return;

        List<Map.Entry<K, CacheEntry<V>>> lruSorted =
                map.entrySet().stream()
                        .sorted(Comparator.comparingLong(e -> e.getValue().getLastAccess()))
                        .toList();

        int itemsToRemove = map.size() - maxEntries;
        System.out.println("Removing " + itemsToRemove + " entries from LRU");

        for (int i = 0; i < itemsToRemove; i++) {
            map.remove(lruSorted.get(i).getKey());
        }
    }

    public void cleanUp() {
        for (K key : map.keySet()) {
            CacheEntry<V> entry = map.get(key);
            if (entry != null && entry.isExpired()) {
                map.remove(key);
            }
        }
    }
}