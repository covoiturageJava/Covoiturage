package com.example.carpoolingapp.microservices.Drivers.view;

import com.example.carpoolingapp.model.SessionDriver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class homeProfileDriver {
    ImageView seeMoreIcon;

    public void start(Stage primaryStage, SessionDriver session) {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(624, 453);
        AnchorPane sidebar = new AnchorPane();
        sidebar.setLayoutX(529);
        sidebar.setPrefSize(92, 453);
        sidebar.setStyle("-fx-background-color: #2C2F48;");

        ImageView logoutImage = createImageView("file:src/main/resources/com/example/carpoolingapp/images/LoOutButton.png", 90, 73, 8, 305);
        ImageView profiledriver = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 100, 80, 13, 64);
        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 65, 55, 17, 206);
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 65, 55, 17, 251);

        // Affichage des informations utilisateur à partir de la session
        Text userName = new Text("Driver ID: " + session.getDriver_id());
        userName.setFont(Font.font("Arial Bold", 13));
        userName.setStyle("-fx-font-weight: bold;");
        userName.setFill(javafx.scene.paint.Color.WHITE);
        userName.setLayoutX(16);
        userName.setLayoutY(151);

        Text userCoordinates = new Text("Lat: " + session.getLatitude() + ", Lon: " + session.getLongitude());
        userCoordinates.setFont(Font.font("Times New Roman Italic", 10));
        userCoordinates.setFill(javafx.scene.paint.Color.WHITE);
        userCoordinates.setLayoutX(23);
        userCoordinates.setLayoutY(163);

        // Add components to sidebar
        sidebar.getChildren().addAll(profiledriver, homeIcon, profIcon, userName, logoutImage, userCoordinates);

        String pathImage = "file:src/main/resources/com/example/carpoolingapp/images/profile.png";
        // Offer Card 1
        AnchorPane offerCard1 = createOfferCard(48, 277, "User Name", "Distance to user", pathImage);

        // Offer Card 2
        AnchorPane offerCard2 = createOfferCard(48, 365, "User Name", "Distance to user", pathImage);

        // Main WebView
        WebView webView = new WebView();
        webView.setLayoutX(24);
        webView.setLayoutY(14);
        webView.setPrefSize(485, 226);

        // Header Text
        Text headerText = new Text("Offres");
        headerText.setFont(Font.font("Aldhabi", 32));
        headerText.setFill(javafx.scene.paint.Color.WHITE);
        headerText.setLayoutX(22);
        headerText.setLayoutY(265);

        // Add all components to root
        root.getChildren().addAll(sidebar, offerCard1, offerCard2, webView, headerText);

        // Scene setup
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Driver Profile - Carpooling App");
        primaryStage.show();
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

    private AnchorPane createOfferCard(double layoutX, double layoutY, String userName, String distance, String profileImagePath) {
        AnchorPane card = new AnchorPane();
        card.setLayoutX(layoutX);
        card.setLayoutY(layoutY);
        card.setPrefSize(511, 73);
        card.setStyle("-fx-background-color: #2C2F48; -fx-background-radius: 20; -fx-border-color: #01B7C5; -fx-border-radius: 20;");

        // Profile Icon
        ImageView profileIcon = createImageView(profileImagePath, 66, 55, 12, 8);

        // User Name Text
        Text userNameText = new Text(userName);
        userNameText.setLayoutX(77);
        userNameText.setLayoutY(29);
        userNameText.setFill(javafx.scene.paint.Color.WHITE);
        userNameText.setStyle("-fx-font-weight: bold;");
        userNameText.setFont(Font.font("Arial Bold", 12));

        // Distance Text
        Text distanceText = new Text(distance);
        distanceText.setLayoutX(77);
        distanceText.setLayoutY(51);
        distanceText.setFill(javafx.scene.paint.Color.WHITE);
        distanceText.setFont(Font.font("Arial", 13));

        // See More Text and Icon
        Text seeMoreText = new Text("See details");
        seeMoreText.setLayoutX(434);
        seeMoreText.setLayoutY(29);
        seeMoreText.setFill(javafx.scene.paint.Color.WHITE);
        seeMoreText.setFont(Font.font("System", 12));

        seeMoreIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/seeMor.png", 30, 24, 450, 35);
        seeMoreIcon.setOnMouseClicked(mouseEvent -> {
            try {
                HomeDetailsOffresDrivers detailsOffre = new HomeDetailsOffresDrivers();
                Stage currentStage = (Stage) ((ImageView) mouseEvent.getSource()).getScene().getWindow();
                detailsOffre.start(currentStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        card.getChildren().addAll(profileIcon, userNameText, distanceText, seeMoreIcon, seeMoreText);
        return card;
    }
//
//    public static void main(String[] args) {
//
//        SessionDriver testSession = new SessionDriver();
//        testSession.setDriver_id(12345);
//        testSession.setLatitude(37.7749);
//        testSession.setLongitude(-122.4194);
//
//        homeProfileDriver driverProfile = new homeProfileDriver();
//        Stage stage = new Stage();
//        driverProfile.start(stage, testSession);
//    }
}
