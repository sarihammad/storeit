package com.storeit;

import com.storeit.store.KeyValueStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KeyValueStoreLruTest {

    @Test
    public void testLruEviction() {
        KeyValueStore store = new KeyValueStore(3); // 3 max
        store.set("a", "1");
        store.set("b", "2");
        store.set("c", "3");
        store.get("a"); // b should be LRU here 
        store.set("d", "4"); // b should get evicted here

        assertNotNull(store.get("a")); // still here
        assertNull(store.get("b"));    // evicted
        assertNotNull(store.get("c")); // still here
        assertNotNull(store.get("d")); // just added
    }
}
