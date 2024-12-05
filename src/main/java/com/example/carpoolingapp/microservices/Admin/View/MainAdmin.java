package com.example.carpoolingapp.microservices.Admin.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainAdmin extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create the AnchorPane
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(650.0, 404.0);
        anchorPane.setStyle("-fx-background-image: url('img.png'); -fx-background-size: cover;");

        // First ImageView
        ImageView imageView1 = new ImageView();
        Image image = new Image("protection.png");
        imageView1.setImage(image);
        imageView1.setFitHeight(150.0);
        imageView1.setFitWidth(200.0);
        imageView1.setLayoutX(14.0);
        imageView1.setLayoutY(162.0);
        imageView1.setPickOnBounds(true);
        imageView1.setPreserveRatio(true);

        // First Button
        Button btnManageDriversUsers = new Button("Manage Drivers and Users");
        btnManageDriversUsers.setLayoutX(394.0);
        btnManageDriversUsers.setLayoutY(169.0);
        btnManageDriversUsers.setPrefSize(182.0, 42.0);
        btnManageDriversUsers.setFont(Font.font("Bell MT Bold", 14.0));

        // Second Button
        Button btnSeeDriversRequests = new Button("Manage Drivers Requests");
        btnSeeDriversRequests.setLayoutX(394.0);
        btnSeeDriversRequests.setLayoutY(246.0);
        btnSeeDriversRequests.setPrefSize(182.0, 42.0);
        btnSeeDriversRequests.setFont(Font.font("Bell MT Bold", 14.0));


        // Add children to the AnchorPane
        anchorPane.getChildren().addAll(imageView1, btnManageDriversUsers, btnSeeDriversRequests);

        // Create the Scene and set the Stage
        Scene scene = new Scene(anchorPane);
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

