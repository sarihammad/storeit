package com.storeit.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class StoreItClient {

    private static final String HOST = "localhost";
    private static final int PORT = 6379;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(HOST, PORT);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            // Async listener thread for pub/sub messages
            Thread listener = new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException ignored) {
                }
            });
            listener.setDaemon(true);
            listener.start();

            System.out.println("Connected to StoreIt at " + HOST + ":" + PORT);
            while (true) {
                System.out.print("storeit> ");
                String input = scanner.nextLine();
                out.write(input + "\n");
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to connect: " + e.getMessage());
        }
    }
}