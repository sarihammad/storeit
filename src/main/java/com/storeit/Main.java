package com.storeit;

import com.storeit.store.KeyValueStore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        KeyValueStore store = new KeyValueStore();

        System.out.println("== StoreIt: In-Memory Key-Value Store ==");

        store.set("key", "value");
        System.out.println("GET key: " + store.get("value")); 

        store.set("temp", "value", 2); 
        System.out.println("GET temp (before expiry): " + store.get("temp"));

        Thread.sleep(3000);
        System.out.println("GET temp (after expiry): " + store.get("temp")); 

        store.set("hello", "world");
        store.delete("hello");
        System.out.println("GET hello (after delete): " + store.get("hello")); 

        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                store.set("key" + i, "val" + i);
                System.out.println(Thread.currentThread().getName() + " SET key" + i);
            }
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        for (int i = 0; i < 5; i++) {
            System.out.println("GET key" + i + ": " + store.get("key" + i));
        }

        System.out.println("== Done ==");
    }
}
