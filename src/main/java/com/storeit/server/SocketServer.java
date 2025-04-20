package com.storeit.server;

import com.storeit.pubsub.PubSubService;
import com.storeit.store.KeyValueStore;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketServer {

    private static final int PORT = 6379;
    private final KeyValueStore store = new KeyValueStore(); 
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final PubSubService pubsub = new PubSubService();

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("StoreIt Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.submit(() -> handleClient(clientSocket));
            }
        }
    }

    private void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            out.write("Welcome to StoreIt. Use SET, GET, DELETE commands.\n");
            out.flush();

            String line;
            while ((line = in.readLine()) != null) {
                String response = processCommand(line.trim(), out);
                if (response != null) {
                    out.write(response + "\n");
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    private String processCommand(String input, BufferedWriter out) {
        String[] parts = input.split(" ", 3);
        if (parts.length == 0) return "Invalid command";
    
        switch (parts[0].toUpperCase()) {
            case "SET":
                if (parts.length < 3) return "Usage: SET key value";
                store.set(parts[1], parts[2]);
                return "OK";
            case "GET":
                if (parts.length < 2) return "Usage: GET key";
                String val = store.get(parts[1]);
                return (val != null) ? val : "(nil)";
            case "DELETE":
                if (parts.length < 2) return "Usage: DELETE key";
                store.delete(parts[1]);
                return "Deleted";
            case "SUBSCRIBE":
                if (parts.length < 2) return "Usage: SUBSCRIBE channel";
                pubsub.subscribe(parts[1], out);
                return "Subscribed to " + parts[1];
            case "PUBLISH":
                if (parts.length < 3) return "Usage: PUBLISH channel message";
                pubsub.publish(parts[1], parts[2]);
                return "Published to " + parts[1];
            default:
                return "Unknown command";
        }
    }

    public static void main(String[] args) throws IOException {
        new SocketServer().start();
    }
}
