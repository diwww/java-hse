package ru.hse.chat;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;

/**
 * Project: ChatClient
 * Author:  Surovtsev Maxim
 * Group:   BSE151(1)
 * Date:    14.04.17
 */
public class Main extends Application implements EventHandler<InputEvent> {
    // Controls
    private HBox connectContainer;
    private TextField addressTextField;
    private TextField portTextField;
    private TextField nameTextField;
    private Button connectButton;
    private TextArea chatTextArea;
    private HBox sendMessageContainer;
    private TextArea messageTextArea;
    private Button sendButton;
    private VBox root;
    private Scene scene;
    // Other fields
    private boolean connected = false;
    private Client client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        disconnectFromServer();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Server params container
        connectContainer = new HBox(10);
        connectContainer.setAlignment(Pos.CENTER_LEFT);
        addressTextField = new TextField();
        addressTextField.setPromptText("Address: ");
        addressTextField.setPrefWidth(200);
        portTextField = new TextField();
        portTextField.setPromptText("Port: ");
        portTextField.setPrefWidth(200);
        nameTextField = new TextField();
        nameTextField.setPromptText("Name");
        nameTextField.setPrefWidth(200);
        connectButton = new Button("Connect");
        connectButton.setOnMouseClicked(this);
        connectContainer.getChildren().addAll(addressTextField, portTextField, nameTextField);
        // Chat text field
        chatTextArea = new TextArea();
        chatTextArea.setPrefHeight(400);
        chatTextArea.setWrapText(true);
        chatTextArea.setEditable(false);
        // Send message container
        sendMessageContainer = new HBox(10);
        sendMessageContainer.setAlignment(Pos.CENTER_LEFT);
        messageTextArea = new TextArea();
        messageTextArea.setPrefHeight(70);
        messageTextArea.setPrefWidth(300);
        messageTextArea.setWrapText(true);
        messageTextArea.setOnKeyPressed(this);
        sendButton = new Button("Send");
        sendButton.setPrefHeight(70);
        sendButton.setPrefWidth(100);
        sendButton.setOnMouseClicked(this);
        sendMessageContainer.getChildren().addAll(messageTextArea, sendButton);
        // Root node
        root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setSpacing(10);
        root.getChildren().addAll(connectContainer, connectButton, chatTextArea, sendMessageContainer);
        // Disable some controls on start
        connectContainer.setDisable(false);
        sendMessageContainer.setDisable(true);
        // Scene and stage
        scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Chat Client");
        stage.setResizable(false);
        stage.show();
    }

    public void handle(InputEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (event.getSource() == connectButton) {
                if (!connected) {
                    connectToServer();
                } else {
                    disconnectFromServer();
                }
            } else if (event.getSource() == sendButton) {
                sendMessage();
            }
        } else if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.isShiftDown() && keyEvent.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        }
    }

    private void connectToServer() {
        try {
            String host = addressTextField.getText();
            String portString = portTextField.getText();
            if (portString.length() != 4) {
                throw new NumberFormatException();
            }
            int port = Integer.parseInt(portString);
            String name = nameTextField.getText();
            name = name.isEmpty() ? "DEFAULT_USER" : name;
            client = new Client(name, host, port, chatTextArea);
            client.setName("ChatClient");
            client.start();
            connected = true;
            connectButton.setText("Disconnect");
            connectContainer.setDisable(true);
            sendMessageContainer.setDisable(false);
        } catch (NumberFormatException e) {
            chatTextArea.appendText("Port must be a number and must contain only 4 digits!\n");
        } catch (IOException e) {
            chatTextArea.appendText("Could not connect to server. Is hostname or port correct?\n");
        }
    }

    private void disconnectFromServer() {
        if (client != null) {
            client.sendMessage(Client.SYSTEM_PREFIX, Client.EXIT_MESSAGE);
        }
        connected = false;
        connectButton.setText("Connect");
        connectContainer.setDisable(false);
        sendMessageContainer.setDisable(true);
    }

    private void sendMessage() {
        String message = messageTextArea.getText();
        if (client != null) {
            client.sendMessage(Client.CLIENT_PREFIX, message);
        }
        messageTextArea.clear();
    }
}
