package com.storeit.store;

public class Entry {
    private final String value;
    private final long expiryTime; 

    public Entry(String value, long ttlSeconds) {
        this.value = value;
        this.expiryTime = (ttlSeconds > 0) ? System.currentTimeMillis() + ttlSeconds * 1000 : -1;
    }

    public String getValue() {
        return value;
    }

    public boolean isExpired() {
        return expiryTime != -1 && System.currentTimeMillis() > expiryTime;
    }
}
