package com.storeit.store;

import java.io.Serializable;

public class Entry implements Serializable {

	private static final long serialVersionUID = 1L;
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
