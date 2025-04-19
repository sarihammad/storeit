package com.storeit;

import com.storeit.store.KeyValueStore;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KeyValueStoreTest {

    @Test
    public void testSetAndGet() {
        KeyValueStore store = new KeyValueStore();
        store.set("a", "1");
        assertEquals("1", store.get("a"));
    }

    @Test
    public void testDelete() {
        KeyValueStore store = new KeyValueStore();
        store.set("b", "2");
        store.delete("b");
        assertNull(store.get("b"));
    }

    @Test
    public void testExpiration() throws InterruptedException {
        KeyValueStore store = new KeyValueStore();
        store.set("c", "3", 1);
        assertEquals("3", store.get("c"));
        Thread.sleep(1500);
        assertNull(store.get("c"));
    }

    @Test
    public void testThreadSafety() throws InterruptedException {
        KeyValueStore store = new KeyValueStore();

        Runnable writer = () -> {
            for (int i = 0; i < 1000; i++) {
                store.set("key" + i, "val" + i);
            }
        };

        Thread t1 = new Thread(writer);
        Thread t2 = new Thread(writer);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        int found = 0;
        for (int i = 0; i < 1000; i++) {
            if (store.get("key" + i) != null) found++;
        }
        assertTrue(found >= 1000); // at least one thread wrote each key
    }
}
