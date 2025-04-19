package com.storeit.persistence;

import com.storeit.store.Entry;

import java.io.*;
import java.util.Map;

public class SnapshotManager {

    private final String filePath;

    public SnapshotManager(String filePath) {
        this.filePath = filePath;
    }

    public void save(Map<String, Entry> store) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(store);
            System.out.println("Snapshot saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save snapshot: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Entry> load() {
        File file = new File(filePath);
        if (!file.exists()) return null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, Entry>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load snapshot: " + e.getMessage());
            return null;
        }
    }
}
