package com.storeit.pubsub;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PubSubService {
    private final Map<String, List<BufferedWriter>> channelSubscribers = new ConcurrentHashMap<>();

    public void subscribe(String channel, BufferedWriter out) {
        channelSubscribers
            .computeIfAbsent(channel, k -> new CopyOnWriteArrayList<>())
            .add(out);
    }

    public void publish(String channel, String message) {
        List<BufferedWriter> subs = channelSubscribers.get(channel);
        if (subs == null) return;

        for (BufferedWriter out : subs) {
            try {
                out.write("[PubSub] " + channel + ": " + message + "\n");
                out.flush();
            } catch (IOException e) {
                System.err.println("Removing dead subscriber: " + e.getMessage());
                subs.remove(out);
            }
        }
    }
}