package com.example.carpoolingapp.microservices.Drivers.view;

import com.example.carpoolingapp.model.DatabaseInitializer;
import com.google.gson.JsonObject;
import com.example.carpoolingapp.model.SessionDriver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PickUpDriver extends Application {

    private SessionDriver session;
    private JsonObject threadData;
    private WebView webView;
    private double latitudedriver;
    private double longitudedriver;
    private double latitudeDepart;
    private double longitudeDepart;
    private ScheduledExecutorService scheduler;
    private AnchorPane offerCard1;

    // Constructeur mis à jour pour recevoir session et thread data
    public PickUpDriver(SessionDriver session, JsonObject threadData) {
        this.session = session;
        this.threadData = threadData;
    }

    @Override
    public void start(Stage stage) {
        // Root AnchorPane
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(664, 540);

        // Extraire les données de localisation de threadData
        String userId = threadData.get("userId").getAsString();
        double distance = threadData.get("distance").getAsDouble();
        // Analyser les coordonnées de départ et d'arrivée
        latitudeDepart = Double.parseDouble(threadData.get("departLat").getAsString());
        longitudeDepart = Double.parseDouble(threadData.get("departLng").getAsString());
        String departure = latitudeDepart + ", " + longitudeDepart;
        String arrival = threadData.get("arriveeLat").getAsString() + ", " + threadData.get("arriveeLng").getAsString();
        webView = new WebView();
        webView.setLayoutX(25);
        webView.setLayoutY(14);
        webView.setPrefSize(620, 523);

        DriverDetails driverDetails = fetchDriverDetailsFromDatabase(Integer.parseInt(userId));
        offerCard1 = createOfferCard(
                driverDetails,
                distance,
                "User details: " + departure + " to " + arrival
        );
        root.getChildren().addAll( webView, offerCard1);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Carpooling Application");
        stage.show();
        // Démarrer la mise à jour de la localisation
        startLocationUpdater(session.getDriver_id());
    }

    // Méthode pour créer une ImageView
    private ImageView createImageView(String imagePath, double fitWidth, double fitHeight, double layoutX, double layoutY) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setPreserveRatio(true);
        return imageView;
    }
    private DriverDetails fetchDriverDetailsFromDatabase(int userId) {
        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection);
            String sql = "SELECT firstName, lastName, username, email, phoneNumber FROM users WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new DriverDetails(
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("phoneNumber"),
                            rs.getString("email"),
                            rs.getString("username")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Default values if no driver is found
        return new DriverDetails("Unknown", "User", "N/A", "N/A", "N/A");
    }

    private static class DriverDetails {
        private String firstName;
        private String lastName;
        private String telephone;
        private String email;
        private String username;
        private double price;

        public DriverDetails(String firstName, String lastName, String telephone, String email, String username) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.telephone = telephone;
            this.email = email;
            this.username = username;
        }

        // Getters
        public String getFullName() { return firstName + " " + lastName; }
        public String getUsername() { return username; }
        public String getTelephone() { return telephone; }
        public String getEmail() { return email; }
        public double getPrice() { return price; }
    }

    private AnchorPane createOfferCard(DriverDetails driverDetails, double distance, String date) {
        AnchorPane card = new AnchorPane();
        card.setLayoutX(360);
        card.setLayoutY(460);
        card.setPrefSize(294, 150);
        card.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2C2F48, #1D203E);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);"
        );

        // Profile Image with circular clip
        Circle clipCircle = new Circle(25, 25, 25);
        ImageView profileIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 50, 50, 12, 15);
        profileIcon.setClip(clipCircle);
        // Driver Name with emphasis
        Text userNameText = new Text(driverDetails.getFullName());
        userNameText.setFill(Color.CYAN);
        userNameText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        userNameText.setLayoutX(80);
        userNameText.setLayoutY(40);
        // Username
        Text usernameText = new Text("@" + driverDetails.getUsername());
        usernameText.setFill(Color.LIGHTGRAY);
        usernameText.setFont(Font.font("Arial", 12));
        usernameText.setLayoutX(80);
        usernameText.setLayoutY(60);
        // Contact Information
        Text phoneText = new Text("📞 +212 " + driverDetails.getTelephone());
        phoneText.setFill(Color.WHITE);
        phoneText.setFont(Font.font(12));
        phoneText.setLayoutX(80);
        phoneText.setLayoutY(80);

        Text emailText = new Text("✉️" + driverDetails.getEmail());
        emailText.setFill(Color.WHITE);
        emailText.setFont(Font.font(12));
        emailText.setLayoutX(80);
        emailText.setLayoutY(100);
        // Journey Details
        Text distanceText = new Text("🛣️" + String.format("%.2f", distance) + " km");
        distanceText.setFill(Color.WHITE);
        distanceText.setFont(Font.font(12));
        distanceText.setLayoutX(80);
        distanceText.setLayoutY(120);
        // Pickup Button
        Button pickUpButton = new Button("Pick up");
        pickUpButton.setPrefWidth(150);
        pickUpButton.setPrefHeight(30);
        pickUpButton.setLayoutX(80);
        pickUpButton.setLayoutY(140);
        pickUpButton.setStyle(
                "-fx-background-color: #01B7C5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 12px;"
        );
        pickUpButton.setOnMouseEntered(e -> pickUpButton.setStyle(
                "-fx-background-color: #02D6E4;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 12px;"
        ));
        pickUpButton.setOnMouseExited(e -> pickUpButton.setStyle(
                "-fx-background-color: #01B7C5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 12px;"
        ));
        pickUpButton.setOnAction(event -> {
            if ("Pick up".equals(pickUpButton.getText())) {
                try {
                    latitudeDepart = Double.parseDouble(threadData.get("arriveeLat").getAsString());
                    longitudeDepart = Double.parseDouble(threadData.get("arriveeLng").getAsString());
                    loadMapView();
                    pickUpButton.setText("Fin");
                    pickUpButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-weight: bold;");
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else if ("Fin".equals(pickUpButton.getText())) {
                try {
                    int driverId = session.getDriver_id();
                    int userId = threadData.get("userId").getAsInt();
                    int trajetId = threadData.get("trajetId").getAsInt();
                    updateTrajetState(driverId, userId, trajetId);
                    HomeSimpleDriver homePage = new HomeSimpleDriver();
                    Stage currentStage = (Stage) pickUpButton.getScene().getWindow();
                    homePage.start(currentStage, session);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        card.getChildren().addAll(
                profileIcon,
                userNameText,
                usernameText,
                phoneText,
                emailText,
                distanceText,
                pickUpButton
        );
        return card;
    }
    private void updateTrajetState(int driverId, int userId, int trajetId) throws SQLException {
        Connection connection = DatabaseInitializer.getConnection();
        DatabaseInitializer.selectDatabase(connection);
        String sql = "UPDATE trajet SET etat = 3 WHERE id_driver = ? AND id_user = ? AND id_trajet = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, driverId);
            stmt.setInt(2, userId);
            stmt.setInt(3, trajetId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("État mis à jour avec succès pour le trajet ID : " + trajetId);
            } else {
                System.err.println("Aucun trajet trouvé avec les IDs spécifiés.");
            }
        }
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
                    latitudedriver = rs.getDouble("latitude");
                    longitudedriver = rs.getDouble("longitude");
                    return new double[]{latitudedriver, longitudedriver};
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void loadMapView() {
        Platform.runLater(() -> {
            webView.setVisible(true);
            WebEngine webEngine = webView.getEngine();
            String mapUrl = "http://localhost:8081/route1?latDriver=" + latitudedriver +
                    "&lngDriver=" + longitudedriver +
                    "&latDepart=" + latitudeDepart +
                    "&lngDepart=" + longitudeDepart;
            webEngine.load(mapUrl);
        });
    }
    @Override
    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}