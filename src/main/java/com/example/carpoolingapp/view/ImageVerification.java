package com.example.carpoolingapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ImageVerification {

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Verification");

        // Main layout container
        HBox mainLayout = new HBox(30);  // Spacing between each document group
        mainLayout.setPadding(new Insets(20, 20, 40, 20));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // Title label
        Label title = new Label("Please provide us with the necessary images");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.DARKBLUE);

        // Add title at the top of the main layout
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.getChildren().add(title);

        // Document types and their layout
        String[] documents = {"ID Card", "Carte grise", "Driving licence", "Assurance"};

        for (String doc : documents) {
            VBox docBox = new VBox(15);  // Each document box has spacing between items
            docBox.setAlignment(Pos.CENTER);

            Label docLabel = new Label(doc);
            docLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
            docLabel.setTextFill(Color.BLACK);
            docBox.getChildren().add(docLabel);

            // For Assurance, only add one image selection
            if (doc.equals("Assurance")) {
                addImageSelection(docBox, "Assurance", primaryStage);
            } else {
                // Add both Recto and Verso for the other documents
                addImageSelection(docBox, doc + " Recto", primaryStage);
                addImageSelection(docBox, doc + " Verso", primaryStage);
            }

            // Add each document's VBox to the main HBox layout
            mainLayout.getChildren().add(docBox);
        }

        mainContainer.getChildren().add(mainLayout);

        // Set up the scene
        Scene scene = new Scene(mainContainer, 1000, 600);  // Adjusted width for better layout
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to add an image selection option
    private void addImageSelection(VBox docBox, String label, Stage primaryStage) {
        Label sideLabel = new Label(label);
        sideLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        sideLabel.setTextFill(Color.DARKGRAY);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        Button chooseButton = new Button("Choose " + label);
        chooseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                System.out.println(label + " selected: " + file.getName());
            }
        });

        docBox.getChildren().addAll(sideLabel, imageView, chooseButton);
    }

}
