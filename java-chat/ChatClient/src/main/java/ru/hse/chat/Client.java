package ru.hse.chat;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Project: ChatClient
 * Author:  Surovtsev Maxim
 * Group:   BSE151(1)
 * Date:    22.04.17
 */
public class Client extends Thread {
    public static String CLIENT_PREFIX = "#CLIENT#";
    public static String SYSTEM_PREFIX = "#SYSTEM#";
    public static String EXIT_MESSAGE = "EXIT";

    private String name;
    private String host;
    private int port;
    private Socket socket;
    private TextArea chatTextArea;
    private PrintWriter writer;
    private BufferedReader reader;

    public Client(String name, String host, int port, TextArea chatTextArea) throws IOException {
        this.name = name;
        this.host = host;
        this.port = port;
        this.chatTextArea = chatTextArea;

        socket = new Socket(this.host, this.port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        chatTextArea.appendText((String.format("Connected to server: %s:%d\n",
                socket.getInetAddress().getHostName(), socket.getPort())));
        // First sent line of text is a client name
        writer.println(this.name);
    }

    public void sendMessage(String prefix, String message) {
        if (message.isEmpty() || writer == null) {
            return;
        }
        writer.println(prefix);
        writer.println(message);
    }

    // Listens to messages
    @Override
    public void run() {
        try (Socket close = socket) {
            while (true) {
                String prefix = reader.readLine();
                String message = reader.readLine();
                if (prefix.equals(SYSTEM_PREFIX) && message.equals("EXIT")) {
                    sendMessage(prefix, message);
                    return;
                }
                if (prefix.equals(CLIENT_PREFIX)) {
                    chatTextArea.appendText(message + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            chatTextArea.appendText("Disconnected.\n");
        }
    }
}
