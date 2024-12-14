package com.example.carpoolingapp.microservices.User.view;

import com.example.carpoolingapp.microservices.User.controller.ThreadClient;
import com.example.carpoolingapp.microservices.User.controller.ThreadClientListener;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SelectTrajet implements ThreadClientListener {
    private Stage parentStage;
    private int userId;
    private int trajetId;
    private ThreadClient client;
    private ScheduledExecutorService scheduler;
    private WebView webView;
    private double latitudepart;
    private double longitudepart;
    private User user;

    public SelectTrajet(Stage parentStage, int userId,User user) {
        this.parentStage = parentStage;
        this.userId = userId;
        this.user = user;
        try {
            this.trajetId = generateUniqueTrajetId();
        } catch (SQLException e) {
            e.printStackTrace();
            this.trajetId = -1;
        }
    }
    public void show(Stage stage) throws URISyntaxException {
        client = new ThreadClient(new URI("ws://localhost:8080"), this);
        client.connect();
        startOfferUpdater();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        String url = getClass().getResource("/map.html").toExternalForm();
        webEngine.load(url);
        webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                webEngine.executeScript("window.javaHandler = {"
                        + "getUserId: function() { return " + userId + "; },"
                        + "getTrajetId: function() { return " + trajetId + "; },"
                        + "onConfirm: function() { javafx.onConfirm(); }"
                        + "};");
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javafx", this);
            }
        });
        Button returnButton = new Button("Retour à l'accueil");
        returnButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-cursor: hand;"
        );
        returnButton.setOnAction(e -> {
            try {
                stop();
                HomePage homePage = new HomePage(user);
                homePage.show(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        BorderPane root = new BorderPane();
        root.setCenter(webView);
        HBox buttonBox = new HBox(returnButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        root.setBottom(buttonBox);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Sélection de Trajet");
        stage.show();
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
    private int generateUniqueTrajetId() throws SQLException {
        Connection connection = DatabaseInitializer.getConnection();
        DatabaseInitializer.selectDatabase(connection);
        int idTrajet;
        boolean isUnique;
        do {
            idTrajet = (int) (Math.random() * (999999 - 100000)) + 100000;
            String query = "SELECT COUNT(*) FROM trajet WHERE id_trajet = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idTrajet);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            isUnique = resultSet.getInt(1) == 0;
            resultSet.close();
            statement.close();
        } while (!isUnique);
        connection.close();
        return idTrajet;
    }

//    public void onTrajetConfirmed() {
//        Platform.runLater(() -> {
//            try {
//                Stage waitingStage = new Stage();
//                WaitingDriver waitingDriver = new WaitingDriver();
//                waitingDriver.start(waitingStage, userId, trajetId,1,1);
//                parentStage.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }

    public void start(Stage stage, int iduser, int idtrajet, User user) throws URISyntaxException {
        this.userId = iduser;
        this.trajetId = idtrajet;
        this.user=user;
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        webView = new WebView();
        webView.setVisible(false);

        root.getChildren().add(webView);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Waiting for Confirmation");
        stage.show();

        //client = new ThreadClient(new URI("ws://localhost:8080"), this);
//        client.connect();
//        startOfferUpdater();
    }

    private void startOfferUpdater() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> client.send("status")), 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void onMessageReceived(String message) {
        try {
            if (message.startsWith("Threads en attente :")) {
                String jsonPart = message.replace("Threads en attente :", "").trim();
                try {
                    JsonArray threadsArray = JsonParser.parseString(jsonPart).getAsJsonArray();
                    Platform.runLater(() -> {
                        boolean threadFound = false;
                        for (JsonElement element : threadsArray) {
                            JsonObject threadObject = element.getAsJsonObject();
                            int receivedTrajetId = threadObject.get("trajetId").getAsInt();
                            int receivedUserId = threadObject.get("userId").getAsInt();
                            String type = threadObject.get("Type").getAsString();
                            if ("Route1".equals(type) && receivedTrajetId == trajetId && receivedUserId == userId) {
                                threadFound = true;
                                double latDriver = threadObject.get("departLat").getAsDouble();
                                double lngDriver = threadObject.get("departLng").getAsDouble();
                                //loadMapView(latDriver, lngDriver);
                                break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadMapView(double latDriver, double lngDriver) {
        Platform.runLater(() -> {
            webView.setVisible(true);
            WebEngine webEngine = webView.getEngine();
            String mapUrl = "http://localhost:8081/route1.html?latdedriver=" + latDriver +
                    "&lngdriver=" + lngDriver +
                    "&latdepart=" + latitudepart +
                    "&lngdepart=" + longitudepart;
            webEngine.load(mapUrl);
        });
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
    public void onConfirm() {
        Platform.runLater(() -> {
            try {
                Stage waitingStage = new Stage();
                WaitingDriver waitingDriver = new WaitingDriver();
                waitingDriver.start(waitingStage, userId, trajetId,user);
                parentStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
