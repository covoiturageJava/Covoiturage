package com.example.carpoolingapp.microservices.auth.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.ByteArrayInputStream;

public class DriverImagesApp extends Application {

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://carpooling-db.mysql.database.azure.com:3306/carpooling";
    private static final String DB_USER = "adminuser";
    private static final String DB_PASSWORD = "azerty123$$";

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(15);
        root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f5f5f5;");

        // Title
        root.getChildren().add(new javafx.scene.control.Label("Driver Permit Information"));

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT permit_info FROM Drivers WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 1); // Driver ID = 1

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Fetch the permit info image data
                byte[] permitInfo = resultSet.getBytes("permit_info");

                if (permitInfo != null && permitInfo.length > 0) {
                    // Decode the permit info image
                    Image permitImage = new Image(new ByteArrayInputStream(permitInfo));
                    ImageView imageView = new ImageView(permitImage);
                    imageView.setFitWidth(300);
                    imageView.setFitHeight(300);
                    imageView.setPreserveRatio(true);

                    root.getChildren().add(imageView);
                } else {
                    root.getChildren().add(new javafx.scene.control.Label("Permit information not available"));
                }
            } else {
                root.getChildren().add(new javafx.scene.control.Label("No data found for driver ID 1."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            root.getChildren().add(new javafx.scene.control.Label("Error fetching data: " + e.getMessage()));
        }

        // Set up the scene and stage
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Driver Permit Information");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
