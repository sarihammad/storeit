package com.storeit.store;

import java.util.concurrent.*;

public class KeyValueStore {
	private final Map<String, Entry> store;

    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public KeyValueStore(int maxSize) {
		store = Collections.synchronizedMap(new EvictingMap<>(maxSize));
        cleaner.scheduleAtFixedRate(() -> {
            for (String key : store.keySet()) {
                Entry entry = store.get(key);
                if (entry != null && entry.isExpired()) {
                    store.remove(key);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

	public KeyValueStore() {
		this(1000);
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
