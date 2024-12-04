package com.example.carpoolingapp.microservices.Admin.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ConfirmRequests extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Root AnchorPane
        AnchorPane root = new AnchorPane();
        root.setPrefSize(931, 695);
        root.setStyle("-fx-background-image: url('img_2.png')");

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(14);
        scrollPane.setLayoutY(148);
        scrollPane.setPrefSize(902, 466);

        // VBox inside ScrollPane
        VBox vBox = new VBox();
        vBox.setPrefSize(900, 458);
        scrollPane.setContent(vBox);

        // Confirm Request Button
        Button confirmButton = new Button("Confirm Request");
        confirmButton.setLayoutX(559);
        confirmButton.setLayoutY(633);
        confirmButton.setPrefSize(150, 35);
        confirmButton.setFont(new Font(14));

        // Reject Request Button
        Button rejectButton = new Button("Reject Request");
        rejectButton.setLayoutX(737);
        rejectButton.setLayoutY(633);
        rejectButton.setPrefSize(150, 35);
        rejectButton.setFont(new Font(14));

        // ImageView
        ImageView imageView = new ImageView();
        Image image = new Image(getClass().getResourceAsStream("/protection.png"));
        imageView.setImage(image);
        imageView.setFitHeight(128);
        imageView.setFitWidth(150);
        imageView.setLayoutX(737);
        imageView.setLayoutY(14);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);

        // Label
        Label label = new Label("Request Confirmation");
        label.setLayoutX(45);
        label.setLayoutY(51);
        label.setPrefSize(537, 50);
        label.setFont(new Font(36));

        // Add all components to the root
        root.getChildren().addAll(scrollPane, confirmButton, rejectButton, imageView, label);

        // Set the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Confirm Page");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

