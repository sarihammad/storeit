package com.storeit;

import com.storeit.store.KeyValueStore;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class KeyValueStorePersistenceTest {

    private static final String SNAPSHOT_PATH = "test-store.snapshot";

    @Test
    public void testSaveAndLoadSnapshot() {
        // Delete old snapshot if exists
        File file = new File(SNAPSHOT_PATH);
        if (file.exists()) file.delete();

        // Create and save data
        KeyValueStore store1 = new KeyValueStore(10, SNAPSHOT_PATH);
        store1.set("test1", "123");
        store1.set("test2", "456");
        store1.set("test3", "789");
        store1.shutdown(); // force save via hook manually

        // New store should load from snapshot
        KeyValueStore store2 = new KeyValueStore(10, SNAPSHOT_PATH);
        assertEquals("123", store2.get("test1"));
        assertEquals("456", store2.get("test2"));
        assertEquals("789", store2.get("test3"));

        // Clean up
        store2.shutdown();
        file.delete();
    }
}
