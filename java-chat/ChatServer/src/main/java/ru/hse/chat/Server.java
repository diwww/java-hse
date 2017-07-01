package ru.hse.chat;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Project: Server
 * Author:  Surovtsev Maxim
 * Group:   BSE151(1)
 * Date:    13.04.17
 */
public class Server extends Thread {
    public static String CLIENT_PREFIX = "#CLIENT#";
    public static String SYSTEM_PREFIX = "#SYSTEM#";
    public static String EXIT_MESSAGE = "EXIT";

    private final int port;
    private final List<PrintWriter> writers = Collections.synchronizedList(new ArrayList<PrintWriter>());
    private final TextArea chatTextArea;
    private boolean running;

    public Server(int port, TextArea chatTextArea) {
        this.port = port;
        this.chatTextArea = chatTextArea;
    }

    public void stopServer() {
        running = false;
        broadcastMessage(SYSTEM_PREFIX, EXIT_MESSAGE);
    }

    /**
     * Sends a message to all clients and to server
     */
    private void broadcastMessage(String prefix, String message) {
        // Send a message to every client
        for (PrintWriter w : writers) {
            w.println(prefix);
            w.println(message);
        }
        // Print a message on a server
        chatTextArea.appendText(prefix + "_" + message + "\n");
    }

    @Override
    public void run() {
        running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(1000);
            chatTextArea.appendText("[Server]: Waiting for clients...\n");
            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(socket);
                    handler.setName("ChatClientHandler");
                    handler.start();
                } catch (IOException e) {
                    System.out.println("ServerSocket waiting timeout, attempting again.");
                    System.out.println("Writers size: " + writers.size());
                }
            }
        } catch (IOException e) {
            chatTextArea.appendText("Cannot start server: port might be already in use.\n");
        } finally {
            System.out.println("ServerSocket is shut down.");
        }
    }

    /**
     * A client handler class
     */
    public class ClientHandler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void sendMessage(String prefix, String message) {
            if (message.isEmpty() || writer == null) {
                return;
            }
            writer.println(prefix);
            writer.println(message);
        }

        @Override
        public void run() {
            try (Socket close = socket) {
                reader = new BufferedReader(new InputStreamReader(close.getInputStream()));
                writer = new PrintWriter(close.getOutputStream(), true);

                // Set name
                name = reader.readLine();
                // Tell everyone, that a new user has connected
                broadcastMessage(CLIENT_PREFIX, String.format("[Server]: Client %s connected.", name));
                writers.add(writer);

                while (running) {
                    // Read a message from a client
                    String prefix = reader.readLine();
                    String message = reader.readLine();
                    if (prefix.equals(SYSTEM_PREFIX) && message.equals(EXIT_MESSAGE)) {
                        sendMessage(prefix, message);
                        return;
                    }
                    if (prefix.equals(CLIENT_PREFIX)) {
                        message = String.format("[%s]: %s", name, message);
                        broadcastMessage(prefix, message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writers.remove(writer);
                }
            }
        }
    }
}
