package com.example.carpoolingapp.microservices.User.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ThreadClientUI extends Application implements ThreadClientListener {

    private ThreadClient client;
    private final ListView<String> threadListView = new ListView<>();
    private final Map<String, JsonObject> threadData = new HashMap<>(); // Stockage des données des threads

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Threads en attente");

        // Interface principale
        VBox root = new VBox();
        threadListView.setPrefHeight(400);

        Button acceptButton = new Button("Accepter");
        acceptButton.setOnAction(e -> acceptSelectedThread());

        root.getChildren().addAll(new Label("Threads reçus :"), threadListView, acceptButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialisation du WebSocket Client
        URI serverUri = new URI("ws://localhost:8080"); // Remplacer par l'URI appropriée
        client = new ThreadClient(serverUri, this);
        client.connect();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (client != null && client.isOpen()) {
            client.close();
        }
    }

    @Override
    public void onMessageReceived(String message) {
        Platform.runLater(() -> processThreadMessage(message));
    }

    private void processThreadMessage(String message) {
        try {
            // Analyse JSON
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            String threadId = json.get("threadId").getAsString();

            if (!threadData.containsKey(threadId)) {
                threadData.put(threadId, json);
                threadListView.getItems().add("Thread ID: " + threadId + " (Distance: " + json.get("distance").getAsString() + ")");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du message : " + e.getMessage());
        }
    }

    private void acceptSelectedThread() {
        String selectedItem = threadListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Erreur", "Aucun thread sélectionné !");
            return;
        }

        String threadId = selectedItem.split(":")[1].trim().split(" ")[0];
        JsonObject threadInfo = threadData.get(threadId);

        if (threadInfo != null) {
            try {
                JsonObject response = new JsonObject();
                response.addProperty("action", "accept");
                response.add("threadInfo", threadInfo);

                client.send(response.toString());
                threadData.remove(threadId);
                threadListView.getItems().remove(selectedItem);

                showAlert("Succès", "Thread accepté avec succès !");
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de l'acceptation du thread : " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
