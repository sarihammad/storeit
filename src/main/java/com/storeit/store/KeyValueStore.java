package com.storeit.store;

import java.util.concurrent.*;

public class KeyValueStore {
    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public KeyValueStore() {
        cleaner.scheduleAtFixedRate(() -> {
            for (String key : store.keySet()) {
                Entry entry = store.get(key);
                if (entry != null && entry.isExpired()) {
                    store.remove(key);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void set(String key, String value) {
        store.put(key, new Entry(value, -1));
    }

    public void set(String key, String value, long ttlSeconds) {
        store.put(key, new Entry(value, ttlSeconds));
    }

    public String get(String key) {
        Entry entry = store.get(key);
        if (entry == null || entry.isExpired()) {
            store.remove(key);
            return null;
        }
        return entry.getValue();
    }

    public void delete(String key) {
        store.remove(key);
    }
}
