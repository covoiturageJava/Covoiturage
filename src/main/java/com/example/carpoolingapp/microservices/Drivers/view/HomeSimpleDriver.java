package com.example.carpoolingapp.microservices.Drivers.view;

import com.example.carpoolingapp.microservices.User.controller.ThreadClient;
import com.example.carpoolingapp.microservices.User.controller.ThreadClientListener;
import com.example.carpoolingapp.microservices.auth.controller.LoginController;
import com.example.carpoolingapp.microservices.auth.view.Login;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.SessionDriver;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeSimpleDriver implements ThreadClientListener {

    private ThreadClient client;
    private final VBox threadList = new VBox();
    private final HashMap<Integer, HBox> threadViews = new HashMap<>();
    private ScheduledExecutorService scheduler;
    SessionDriver session = new SessionDriver();
    public void start(Stage stage, SessionDriver session) throws Exception {
        client = new ThreadClient(new URI("ws://localhost:8080"), this);
        client.connect();
        int driverId = session.getDriver_id();
        double initialLatitude = session.getLatitude();
        double initialLongitude = session.getLongitude();
        this.session=session;
        System.out.println("Driver ID: " + driverId);
        System.out.println("Initial Latitude: " + initialLatitude);
        System.out.println("Initial Longitude: " + initialLongitude);
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(664, 540);
        Text title = new Text("Offres");
        title.setLayoutX(34);
        title.setLayoutY(400);
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");
        title.setFont(Font.font("Aldhabi", 32));
        Button profilButton = createStyledButton("Profil", "#3A3E5F", "#FFFFFF");
        Button logoutButton = createStyledButton("Déconnexion", "#3A3E5F", "#FFFFFF");
        profilButton.setOnAction(e -> {
            try {
                Stage currentStage = (Stage) profilButton.getScene().getWindow();
                currentStage.close();
                Stage profileStage = new Stage();
                ProfileDriver profileDriver = new ProfileDriver();
                profileDriver.start(profileStage, session.getDriver_id(), session);
                //stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        logoutButton.setOnAction(e -> {
            try {
                Stage currentStage = (Stage) logoutButton.getScene().getWindow();
                currentStage.close();
                LoginController loginController = new LoginController();
                Stage loginStage = new Stage();
                Login loginDriver = new Login(loginController);
                loginDriver.show(loginStage);
                stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // Button Container with refined layout
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPrefWidth(630);
        buttonContainer.setLayoutX(17);
        buttonContainer.setLayoutY(540 - 50);
        buttonContainer.getChildren().addAll(profilButton, logoutButton);
        WebView webView = new WebView();
        webView.setLayoutX(17);
        webView.setLayoutY(14);
        webView.setPrefSize(630, 366);
        WebEngine webEngine = webView.getEngine();
        updateMap(webEngine, initialLatitude, initialLongitude);
        startLocationUpdater(driverId, webEngine);
        startOfferUpdater();
        threadList.setSpacing(10);
        threadList.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(threadList);
        scrollPane.setFitToWidth(true);
        scrollPane.setLayoutX(17);
        scrollPane.setLayoutY(380);
        root.getChildren().addAll(scrollPane, title, webView,buttonContainer);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Carpooling Application");
        stage.show();
    }
    private Button createStyledButton(String text, String backgroundColor, String textColor) {
        Button button = new Button(text);
        button.setPrefWidth(310);
        button.setStyle(
                "-fx-background-color: " + backgroundColor + "; " +
                        "-fx-text-fill: " + textColor + "; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px;"
        );
        return button;
    }
    private void updateMap(WebEngine webEngine, double latitude, double longitude) {
        String mapUrl = "http://localhost:8081/map.html?lat=" + latitude + "&lng=" + longitude;
        webEngine.load(mapUrl);
    }
    private void startLocationUpdater(int driverId, WebEngine webEngine) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final double[] previousCoordinates = {Double.NaN, Double.NaN};
        scheduler.scheduleAtFixedRate(() -> {
            try {
                double[] coordinates = getDriverCoordinates(driverId);
                if (coordinates != null) {
                    double newLatitude = coordinates[0];
                    double newLongitude = coordinates[1];
                    if (newLatitude != previousCoordinates[0] || newLongitude != previousCoordinates[1]) {
                        previousCoordinates[0] = newLatitude;
                        previousCoordinates[1] = newLongitude;
                        Platform.runLater(() -> updateMap(webEngine, newLatitude, newLongitude));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 60, TimeUnit.SECONDS);
    }
    private double[] getDriverCoordinates(int driverId) throws SQLException {
        Connection connection = DatabaseInitializer.getConnection();
        DatabaseInitializer.selectDatabase(connection);
        String sql = "SELECT latitude, longitude FROM driver_session WHERE driver_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new double[]{rs.getDouble("latitude"), rs.getDouble("longitude")};
            }
        }
        return null;
    }
    private void startOfferUpdater() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> client.send("status"));
        }, 0, 1, TimeUnit.SECONDS);
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
                JsonArray threadsArray = parsedElement.getAsJsonArray();
                Platform.runLater(() -> {
                    threadList.getChildren().clear();
                    threadViews.clear();
                    for (JsonElement element : threadsArray) {
                        JsonObject threadObject = element.getAsJsonObject();
                        if (threadObject.has("Type") &&
                                "Standard".equals(threadObject.get("Type").getAsString()) &&
                                validateThreadObject(threadObject)) {
                            int threadId = threadObject.get("threadId").getAsInt();
                            HashMap<String, String> threadInfo = extractThreadInfo(threadObject);
                            addThreadView(threadId, threadInfo, session.getDriver_id());
                        }
                    }
                });
            } else if (message.startsWith("Thread ID ")) {
                System.out.println(" ");
            } else {
                System.err.println("Format de message non reconnu : " + message);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du message : " + e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean validateThreadObject(JsonObject threadObject) {
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
    private void addThreadView(int threadId, HashMap<String, String> threadInfo, int driverId) {
        Label threadLabel = new Label("Thread ID : " + threadId + "\n" +
                "Départ : (" + threadInfo.get("departLat") + ", " + threadInfo.get("departLng") + ")\n" +
                "Arrivée : (" + threadInfo.get("arriveeLat") + ", " + threadInfo.get("arriveeLng") + ")\n" +
                "Distance : " + threadInfo.get("distance") + " km\n" +
                "ID du conducteur : " + driverId);
        Button acceptButton = new Button("Accepter");
        acceptButton.setOnAction(e -> {
            JsonObject threadData = new JsonObject();
            threadData.addProperty("action", "accept");
            threadData.addProperty("threadId", threadId);
            threadData.addProperty("departLat", threadInfo.get("departLat"));
            threadData.addProperty("departLng", threadInfo.get("departLng"));
            threadData.addProperty("arriveeLat", threadInfo.get("arriveeLat"));
            threadData.addProperty("arriveeLng", threadInfo.get("arriveeLng"));
            threadData.addProperty("distance", threadInfo.get("distance"));
            threadData.addProperty("driverId", driverId);
            threadData.addProperty("userId", threadInfo.get("userId"));
            threadData.addProperty("trajetId", threadInfo.get("trajetId"));
            System.out.println("Message envoyé au serveur : " + threadData.toString());
            client.send(threadData.toString());
            Stage currentStage = (Stage) acceptButton.getScene().getWindow();
            currentStage.close();
            Platform.runLater(() -> {
                try {
                    PickUpDriver pickUpDriver = new PickUpDriver(session, threadData);
                    pickUpDriver.start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            threadList.getChildren().remove(threadViews.get(threadId));
            threadViews.remove(threadId);
        });
        HBox threadView = new HBox(10, threadLabel, acceptButton);
        threadView.setPadding(new Insets(5));
        threadList.getChildren().add(threadView);
        threadViews.put(threadId, threadView);
    }
    @Override
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

}

