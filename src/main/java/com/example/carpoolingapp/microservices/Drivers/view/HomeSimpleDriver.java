package com.example.carpoolingapp.microservices.Drivers.view;

import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.SessionDriver;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeSimpleDriver {

    ImageView seeMoreIcon;

    public void start(Stage stage, SessionDriver session) {
        // Récupération des coordonnées depuis la session
        int driverId = session.getDriver_id();
        double initialLatitude = session.getLatitude();
        double initialLongitude = session.getLongitude();

        System.out.println("Driver ID: " + driverId);
        System.out.println("Initial Latitude: " + initialLatitude);
        System.out.println("Initial Longitude: " + initialLongitude);

        // Root AnchorPane
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(664, 540);

        // Sidebar
        AnchorPane sidebar = new AnchorPane();
        sidebar.setLayoutX(657);
        sidebar.setPrefWidth(43);
        sidebar.setPrefSize(43, 568);
        sidebar.setStyle("-fx-background-color: #2C2F48;");

        ImageView profileIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 43, 43, 4, 92);
        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 45, 25, 0, 180);
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 45, 30, 0, 220);
        ImageView logoutImage = createImageView("file:src/main/resources/com/example/carpoolingapp/images/LoOutButton.png", 55, 299, -3, 305);
        sidebar.getChildren().addAll(profileIcon, homeIcon, profIcon, logoutImage);

        // Title
        Text title = new Text("Offres");
        title.setLayoutX(34);
        title.setLayoutY(400);
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");
        title.setFont(Font.font("Aldhabi", 32));

        // WebView
        WebView webView = new WebView();
        webView.setLayoutX(17);
        webView.setLayoutY(14);
        webView.setPrefSize(630, 366);

        // Initialisation du WebEngine
        WebEngine webEngine = webView.getEngine();
        updateMap(webEngine, initialLatitude, initialLongitude);

        // Mise à jour périodique des coordonnées
        startLocationUpdater(driverId, webEngine);

        // Vérification périodique des offres
        startOfferUpdater(root, title);

        // Ajout des éléments à la racine
        root.getChildren().addAll(sidebar, title, webView);

        // Création et affichage de la scène
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Carpooling Application");
        stage.show();
    }

    private void updateMap(WebEngine webEngine, double latitude, double longitude) {
        String mapUrl = "http://localhost:8080/map.html?lat=" + latitude + "&lng=" + longitude;
        webEngine.load(mapUrl);
    }

    private void startLocationUpdater(int driverId, WebEngine webEngine) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Variables pour suivre les coordonnées précédentes
        final double[] previousCoordinates = {Double.NaN, Double.NaN}; // Initialisées à des valeurs non valides
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Obtenez les nouvelles coordonnées
                double[] coordinates = getDriverCoordinates(driverId);
                if (coordinates != null) {
                    double newLatitude = coordinates[0];
                    double newLongitude = coordinates[1];

                    // Vérifiez si les coordonnées ont changé
                    if (newLatitude != previousCoordinates[0] || newLongitude != previousCoordinates[1]) {
                        previousCoordinates[0] = newLatitude;
                        previousCoordinates[1] = newLongitude;

                        // Mise à jour de la carte uniquement si les coordonnées ont changé
                        Platform.runLater(() -> updateMap(webEngine, newLatitude, newLongitude));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
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

    private void startOfferUpdater(AnchorPane root, Text title) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> updateOffers(root, title));
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void updateOffers(AnchorPane root, Text title) {
        try {
            ResultSet offers = fetchOffers();
            root.getChildren().removeIf(node -> node.getId() != null && node.getId().startsWith("offerCard"));

            if (offers != null) {
                double layoutY = title.getLayoutY() + 20;
                while (offers.next()) {
                    int userId = offers.getInt("id_user");
                    String distance = offers.getString("distance");
                    double price = offers.getDouble("prix");
                    String profileImagePath = "file:src/main/resources/com/example/carpoolingapp/images/prof.png";
                    AnchorPane offerCard = createOfferCard(53, layoutY, userId, distance, price, profileImagePath);
                    offerCard.setId("offerCard-" + userId);
                    root.getChildren().add(offerCard);

                    layoutY += 90;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ResultSet fetchOffers() throws SQLException {
        Connection connection = DatabaseInitializer.getConnection();
        DatabaseInitializer.selectDatabase(connection);
        String sql = "SELECT id_user, distance, prix FROM trajet WHERE etat = 1";
        PreparedStatement stmt = connection.prepareStatement(sql);
        return stmt.executeQuery();
    }

    private AnchorPane createOfferCard(double layoutX, double layoutY, int userId, String distance, double price, String profileImagePath) {
        AnchorPane card = new AnchorPane();
        card.setLayoutX(layoutX);
        card.setLayoutY(layoutY);
        card.setPrefSize(536, 73);
        card.setStyle("-fx-background-color: #2C2F48; -fx-background-radius: 20; -fx-border-color: #01B7C5; -fx-border-radius: 20;");

        ImageView profileIcon = createImageView(profileImagePath, 66, 55, 12, 8);

        Text userIdText = new Text("User ID: " + userId);
        userIdText.setLayoutX(77);
        userIdText.setLayoutY(20);
        userIdText.setFill(javafx.scene.paint.Color.WHITE);
        userIdText.setStyle("-fx-font-weight: bold;");
        userIdText.setFont(Font.font("Arial Bold", 12));

        Text distanceText = new Text("Distance: " + distance);
        distanceText.setLayoutX(77);
        distanceText.setLayoutY(40);
        distanceText.setFill(javafx.scene.paint.Color.WHITE);
        distanceText.setFont(Font.font("Arial", 13));

        Text priceText = new Text("Price: " + price + " MAD");
        priceText.setLayoutX(77);
        priceText.setLayoutY(60);
        priceText.setFill(javafx.scene.paint.Color.LIGHTGREEN);
        priceText.setFont(Font.font("Arial", 13));

        card.getChildren().addAll(profileIcon, userIdText, distanceText, priceText);
        return card;
    }

    private ImageView createImageView(String imagePath, double fitWidth, double fitHeight, double layoutX, double layoutY) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}
