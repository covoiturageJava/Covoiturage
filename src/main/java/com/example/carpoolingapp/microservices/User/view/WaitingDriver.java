package com.example.carpoolingapp.microservices.User.view;

import com.example.carpoolingapp.microservices.User.controller.ThreadClient;
import com.example.carpoolingapp.microservices.User.controller.ThreadClientListener;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.User;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WaitingDriver implements ThreadClientListener {
    private ThreadClient client;
    private ScheduledExecutorService scheduler;
    private WebView webView;
    private int idUser;
    private int idTrajet;
    private Label statusLabel;
    private int driverId;
    private double latitudeDepart;
    private double longitudeDepart;
    private double latitudedriver;
    private double longitudedriver;
    private final VBox threadList = new VBox();
    private final HashMap<Integer, HBox> threadViews = new HashMap<>();
    private JsonArray waitingThreads = new JsonArray();
    private JsonObject currentThreadObject = null;
    private Stage currentStage;
    private boolean isRatingDialogShown = false;
    private boolean isRedirecting = false;
    private User user;

    private void startStateChecker() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                int state = getTrajetState(idUser, idTrajet);
                if (state == 3 && !isRatingDialogShown) {
                    Platform.runLater(() -> {
                        isRatingDialogShown = true;
                        showRatingDialog();
                    });
                    stopScheduler();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
    private void showRatingDialog() {
        Stage ratingStage = new Stage();
        ratingStage.initModality(Modality.APPLICATION_MODAL);
        ratingStage.setTitle("Notez votre trajet");
        VBox ratingBox = new VBox(10);
        ratingBox.setPadding(new Insets(20));
        Label ratingLabel = new Label("Donnez une note à votre conducteur :");
        Slider ratingSlider = new Slider(0, 5, 3);
        ratingSlider.setShowTickLabels(true);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setBlockIncrement(1);
        ratingSlider.setSnapToTicks(true);
        Label ratingValueLabel = new Label("Note: 3");
        ratingSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                ratingValueLabel.setText(String.format("Note: %.0f", newValue.doubleValue())));
        Button submitButton = new Button("Soumettre la note");
        submitButton.setOnAction(e -> {
            int rating = (int) ratingSlider.getValue();
            saveDriverRating(rating);
            ratingStage.close();
            redirectToSelectTrajet();
        });
        ratingBox.getChildren().addAll(
                ratingLabel,
                ratingSlider,
                ratingValueLabel,
                submitButton
        );
        Scene ratingScene = new Scene(ratingBox, 300, 250);
        ratingStage.setScene(ratingScene);
        ratingStage.showAndWait();
    }
    private void saveDriverRating(int rating) {
        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection);
            // Updated SQL to include price calculation
            String sql = "UPDATE trajet SET rate = ?, prix = distance * 4, etat = 4 WHERE id_user = ? AND id_trajet = ? AND etat = 3";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, rating);
                stmt.setInt(2, idUser);
                stmt.setInt(3, idTrajet);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    if (currentThreadObject != null) {
                        JsonObject threadRemovedMessage = new JsonObject();
                        threadRemovedMessage.addProperty("action", "remove_thread");
                        threadRemovedMessage.addProperty("threadId", currentThreadObject.get("threadId").getAsString());
                        threadRemovedMessage.addProperty("userId", idUser);
                        client.send(threadRemovedMessage.toString());
                    }
                }
                else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Trajet non trouvé");
                        alert.setContentText("Impossible de trouver le trajet correspondant.");
                        alert.showAndWait();
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible d'enregistrer la note");
                alert.setContentText("Un problème est survenu lors de l'enregistrement de votre note.");
                alert.showAndWait();
            });
        }
    }

    private int getTrajetState(int userId, int trajetId) {
        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection);
            String sql = "SELECT etat FROM trajet WHERE id_user = ? AND id_trajet = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, trajetId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("etat");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private void redirectToSelectTrajet() {
        if (isRedirecting) {
            return;
        }
        isRedirecting = true;
        try {
            SelectTrajet trajet = new SelectTrajet(currentStage, idUser,user);
            trajet.show(currentStage);
            stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stopScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
    public void start(Stage stage, int idUser, int idTrajet, User user) throws URISyntaxException {
        this.currentStage = stage;
        this.user = user;
        this.idUser = idUser;
        this.idTrajet = idTrajet;
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        statusLabel = new Label("En attente de confirmation...");
        webView = new WebView();
        webView.setVisible(false);
        root.getChildren().addAll( statusLabel,webView);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("En attente d’un conducteur");
        stage.show();
        // Connexion au serveur WebSocket
        client = new ThreadClient(new URI("ws://localhost:8080"), this);
        client.connect();
        startOfferUpdater();
        startStateChecker();
    }
    private void startLocationUpdater(int driverId) {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                double[] coordinates = getDriverCoordinates(driverId);
                if (coordinates != null) {
                    Platform.runLater(() -> loadMapView());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 60, TimeUnit.SECONDS);
    }
    private double[] getDriverCoordinates(int driverId) {
        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection);
            String sql = "SELECT latitude, longitude FROM driver_session WHERE driver_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, driverId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    this.latitudedriver = rs.getDouble("latitude");
                    this.longitudedriver = rs.getDouble("longitude");
                    return new double[]{rs.getDouble("latitude"), rs.getDouble("longitude")};
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startOfferUpdater() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (client != null && client.isOpen()) {
                client.send("status");
            } else {
                System.err.println("WebSocket non connecté. Tentative d'envoi ignorée.");
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void loadMapView() {
        Platform.runLater(() -> {
            webView.setVisible(true);
            statusLabel.setText("Conducteur trouvé. attender le donne le point de depart ");
            WebEngine webEngine = webView.getEngine();
           // System.out.println(latitudedriver+","+longitudedriver+","+latitudeDepart+","+longitudeDepart);
            String mapUrl = "http://localhost:8081/route1?latDriver=" + latitudedriver +
                    "&lngDriver=" + longitudedriver +
                    "&latDepart=" + latitudeDepart +
                    "&lngDepart=" + longitudeDepart;
            webEngine.load(mapUrl);
        });
    }
    @Override
    public void onMessageReceived(String message) {
        try {
            if (message.startsWith("Threads en attente :")) {
                String jsonPart = message.replace("Threads en attente :", "").trim();
                JsonElement parsedElement = JsonParser.parseString(jsonPart);
                if (!parsedElement.isJsonArray()) {
                    throw new IllegalArgumentException("Le message n'est pas un tableau JSON valide.");
                }
                waitingThreads = parsedElement.getAsJsonArray();
                JsonArray threadsArray = parsedElement.getAsJsonArray();
                Platform.runLater(() -> {
                    threadList.getChildren().clear();
                    threadViews.clear();
                    for (JsonElement element : threadsArray) {
                        JsonObject threadObject = element.getAsJsonObject();
                        if (isValidThread(threadObject) && threadObject.get("Type").getAsString().equals("Route1") && threadObject.get("userId").getAsString().equals(String.valueOf(idUser))) {
                            currentThreadObject = threadObject;
                            HashMap<String, String> threadInfo = extractThreadInfo(threadObject);
                            this.latitudeDepart = Double.parseDouble(threadInfo.get("departLat"));
                            this.longitudeDepart = Double.parseDouble(threadInfo.get("departLng"));
                            int driverId = Integer.parseInt(threadInfo.get("userId"));
                            startLocationUpdater(driverId);
                        }
                    }
                });
            } else if (message.startsWith("Thread ID ")) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(message);
                    alert.show();
                });
            } else {
                System.err.println("Format de message non reconnu : " + message);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du message : " + e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean isValidThread(JsonObject threadObject) {
        return threadObject.has("threadId") &&
                threadObject.has("departLat") &&
                threadObject.has("departLng") &&
                threadObject.has("arriveeLat") &&
                threadObject.has("arriveeLng") &&
                threadObject.has("distance") &&
                threadObject.has("userId") &&
                threadObject.has("trajetId") &&
                threadObject.has("Type");
    }

    private HashMap<String, String> extractThreadInfo(JsonObject threadObject) {
        HashMap<String, String> threadInfo = new HashMap<>();
        threadInfo.put("departLat", threadObject.get("departLat").getAsString());
        threadInfo.put("departLng", threadObject.get("departLng").getAsString());
        threadInfo.put("arriveeLat", threadObject.get("arriveeLat").getAsString());
        threadInfo.put("arriveeLng", threadObject.get("arriveeLng").getAsString());
        threadInfo.put("distance", threadObject.get("distance").getAsString());
        threadInfo.put("userId", threadObject.get("userId").getAsString());
        threadInfo.put("trajetId", threadObject.get("trajetId").getAsString());
        return threadInfo;
    }
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
