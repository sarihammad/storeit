package com.storeit.store;

import java.util.concurrent.*;

public class KeyValueStore {
	private final Map<String, Entry> store;

    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

	private final SnapshotManager snapshotManager;

	public KeyValueStore(int maxSize, String snapshotPath) {
		store = Collections.synchronizedMap(new EvictingMap<>(maxSize));
		this.snapshotManager = new SnapshotManager(snapshotPath);

		// Restore from disk if available
		Map<String, Entry> restored = snapshotManager.load();
		if (restored != null) {
			store.putAll(restored);
			System.out.println("Restored from snapshot");
		}

		// TTL eviction
		scheduler.scheduleAtFixedRate(() -> {
			for (String key : store.keySet()) {
				Entry entry = store.get(key);
				if (entry != null && entry.isExpired()) {
					store.remove(key);
				}
			}
		}, 1, 1, TimeUnit.SECONDS);

		// Periodic snapshots
		scheduler.scheduleAtFixedRate(() -> snapshotManager.save(store), 10, 10, TimeUnit.SECONDS);

		// On shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			snapshotManager.save(store);
			scheduler.shutdown();
		}));
	}

	public KeyValueStore() {
		this(1000, "store.snapshot");
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
