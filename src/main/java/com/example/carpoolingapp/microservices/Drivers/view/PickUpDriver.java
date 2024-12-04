package com.example.carpoolingapp.microservices.Drivers.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PickUpDriver extends Application {

    @Override
    public void start(Stage stage) {
        // Root AnchorPane
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(664, 540);

        // Sidebar
        AnchorPane sidebar = new AnchorPane();
        sidebar.setLayoutX(657);


        sidebar.setPrefSize(43, 590);
        sidebar.setStyle("-fx-background-color: #2C2F48;");

        ImageView profileIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 43, 43, 4, 92);
        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 45, 25, 0, 180);
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 45, 30, 0, 220);
        ImageView logoutImage = createImageView("file:src/main/resources/com/example/carpoolingapp/images/LoOutButton.png", 55, 299, -3, 305);
        sidebar.setMinWidth(40);
        sidebar.setPrefWidth(45);
        sidebar.setMaxWidth(45);

        sidebar.getChildren().addAll(profileIcon,  homeIcon, profIcon,logoutImage);

        // Offer Card 1
        AnchorPane offerCard1 = createOfferCard("User Name", 15.5, 20.0, " data of user\n2024-11-27", "file:src/main/resources/com/example/carpoolingapp/images/profile.png");




        WebView webView = new WebView();
        webView.setLayoutX(25);
        webView.setLayoutY(14);
        webView.setPrefSize(620, 523);
        webView.getEngine().load("file:src/main/resources/com/example/carpoolingapp/webview-content.html");


        // Add elements to root
        root.getChildren().addAll(sidebar , webView, offerCard1);

        // Create and set scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Carpooling Application");
        stage.show();
    }

    // Helper method to create ImageView
    private ImageView createImageView(String imagePath, double fitWidth, double fitHeight, double layoutX, double layoutY) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    // Helper method to create an Offer Card
    private AnchorPane createOfferCard(String userName, double distance, double price, String date, String profileImagePath) {
        AnchorPane card = new AnchorPane();
        card.setLayoutX(360);
        card.setLayoutY(460);
        card.setPrefSize(294, 115); // Increase height for new elements
        card.setStyle("-fx-background-color: #2C2F48; -fx-background-radius: 20; -fx-border-color: #01B7C5; -fx-border-radius: 20;");

        // Profile Icon
        ImageView profileIcon = createImageView(profileImagePath, 66, 55, 12, 25);

        // User Name Text
        Text userNameText = new Text(userName);
        userNameText.setLayoutX(77);
        userNameText.setLayoutY(29);
        userNameText.setFill(Color.WHITE);
        userNameText.setStyle("-fx-font-weight: bold;");
        userNameText.setFont(Font.font("Arial", 13));


        Text distanceText = new Text("Distance : " + distance + "Km");
        distanceText.setLayoutX(77);
        distanceText.setLayoutY(49);
        distanceText.setFill(Color.WHITE);
        distanceText.setFont(Font.font("Arial", 13));

        Text dateText = new Text("Data : " + date);
        dateText.setLayoutX(77);
        dateText.setLayoutY(69);
        dateText.setFill(Color.WHITE);
        dateText.setFont(Font.font("Arial", 13));


        // Price Text
        Text priceText = new Text("Price: " + price+ " DH");
        priceText.setLayoutX(77);
        priceText.setLayoutY(99);
        priceText.setFill(Color.WHITE);
        priceText.setFont(Font.font("Arial", 13));

        // Pick Up Button
        Button pickUpButton = new Button("Pick up");
        pickUpButton.prefWidth(159);
        pickUpButton.prefHeight(25);
        pickUpButton.setLayoutX(224);
        pickUpButton.setLayoutY(75);
        pickUpButton.setStyle("-fx-background-color: #01B7C5; -fx-text-fill: white; -fx-font-weight: bold;");

        // Add event handler to the button
        pickUpButton.setOnAction(event -> {
            if ("Pick up".equals(pickUpButton.getText())) {
                // Change color and text of the button
                pickUpButton.setText("Fin");
                pickUpButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-weight: bold;");
            } else if ("Fin".equals(pickUpButton.getText())) {
                // Redirect to HomeSimpleDriver page
                try {
                    HomeSimpleDriver homePage = new HomeSimpleDriver();
                    Stage currentStage = (Stage) pickUpButton.getScene().getWindow();
//                    homePage.start(currentStage,sessionDriver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // Add elements to the card
        card.getChildren().addAll(profileIcon, userNameText, distanceText,dateText, priceText, pickUpButton);

        return card;
    }

    public static void main(String[] args) {
        launch();
    }
}
