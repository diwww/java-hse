package ru.hse.chat;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Project: ChatServer
 * Author:  Surovtsev Maxim
 * Group:   BSE151(1)
 * Date:    13.04.17
 */
public class Main extends Application implements EventHandler<MouseEvent> {

    private HBox startServerContainer;
    private TextField portTextField;
    private Button startButton;
    private Button stopButton;
    private TextArea chatTextArea;
    private VBox root;
    private Scene scene;
    private Server server;


    @Override
    public void start(Stage stage) throws Exception {
        // Server params container
        startServerContainer = new HBox(10);
        startServerContainer.setAlignment(Pos.CENTER_LEFT);
        portTextField = new TextField();
        portTextField.setPromptText("Port: ");
        portTextField.setPrefWidth(100);
        startButton = new Button("Run Server");
        startButton.setOnMouseClicked(this);
        stopButton = new Button("Stop Server");
        stopButton.setOnMouseClicked(this);
        startServerContainer.getChildren().addAll(portTextField, startButton, stopButton);
        stopButton.setDisable(true);
        // Chat text field
        chatTextArea = new TextArea();
        chatTextArea.setPrefHeight(400);
        chatTextArea.setWrapText(true);
        chatTextArea.setEditable(false);
        // Root node
        root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setSpacing(10);
        root.getChildren().addAll(startServerContainer, chatTextArea);
        // Scene and stage
        scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Chat Server");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        stopServer();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private boolean runServer() {
        try {
            int port = Integer.parseInt(portTextField.getText());
            server = new Server(port, chatTextArea);
            server.setName("ChatServer");
            server.start();
            return true;
        } catch (NumberFormatException e) {
            chatTextArea.appendText("Port must be a number!\n");
            return false;
        }
    }

    private void stopServer() {
        if (server != null) {
            server.stopServer();
        }
    }

    public void handle(MouseEvent event) {
        if (event.getSource() == startButton) {
            if (runServer()) {
                startButton.setDisable(true);
                stopButton.setDisable(false);
            }
        } else if (event.getSource() == stopButton) {
            stopServer();
            startButton.setDisable(false);
            stopButton.setDisable(true);
        }
    }
}
