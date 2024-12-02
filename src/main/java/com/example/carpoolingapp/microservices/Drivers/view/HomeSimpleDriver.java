package com.example.carpoolingapp.microservices.Drivers.view;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeSimpleDriver extends Application {

    @Override
    public void start(Stage stage) {
        // Root AnchorPane
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1E2A78, #1D203E);");
        root.setPrefSize(800, 600);

        // Sidebar
        AnchorPane sidebar = new AnchorPane();
        sidebar.setLayoutX(750);
        sidebar.setPrefSize(50, 600);
        sidebar.setStyle("-fx-background-color: #2C2F48; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 0, 0);");

        // Sidebar Icons
        ImageView profileIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 42, 42, 4, 100);
        ImageView arrowIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/Arrow.png", 24, 33, 13, 50);
        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 44, 29, 4, 150);
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 44, 33, 4, 200);

        addHoverEffect(profileIcon);
        addHoverEffect(arrowIcon);
        addHoverEffect(homeIcon);
        addHoverEffect(profIcon);

        sidebar.getChildren().addAll(profileIcon, arrowIcon, homeIcon, profIcon);

        // Title "Offres"
        Text title = new Text("Offres Disponibles");
        title.setLayoutX(20);
        title.setLayoutY(50);
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Arial Black", 30));

        // WebView - Load HTML map
        WebView webView = new WebView();
        webView.setLayoutX(20);
        webView.setLayoutY(70);
        webView.setPrefSize(700, 300);

        String htmlFilePath = getClass().getResource("/map.html").toExternalForm();
        webView.getEngine().load(htmlFilePath);

        // Offer Cards
        AnchorPane offerCard1 = createOfferCard(20, 400, "John Doe", "3 km away", "file:src/main/resources/com/example/carpoolingapp/images/profile.png");
        AnchorPane offerCard2 = createOfferCard(20, 480, "Jane Smith", "5 km away", "file:src/main/resources/com/example/carpoolingapp/images/profile.png");

        // Sidebar Navigation Logic
        arrowIcon.setOnMouseClicked(event -> redirectTo("profile"));
        homeIcon.setOnMouseClicked(event -> redirectTo("home"));

        // Add elements to root
        root.getChildren().addAll(sidebar, title, webView, offerCard1, offerCard2);

        // Create and set scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Carpooling Application");
        stage.show();
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
        card.setPrefSize(700, 60);
        card.setStyle("-fx-background-color: #2C2F48; -fx-background-radius: 10; -fx-border-color: #01B7C5; -fx-border-radius: 10;");
        card.setEffect(new DropShadow(10, Color.BLACK));

        // Profile Icon
        ImageView profileIcon = createImageView(profileImagePath, 50, 50, 10, 5);

        // User Name Text
        Text userNameText = new Text(userName);
        userNameText.setLayoutX(70);
        userNameText.setLayoutY(25);
        userNameText.setFill(Color.WHITE);
        userNameText.setFont(Font.font("Arial", 16));

        // Distance Text
        Text distanceText = new Text(distance);
        distanceText.setLayoutX(70);
        distanceText.setLayoutY(45);
        distanceText.setFill(Color.LIGHTGRAY);
        distanceText.setFont(Font.font("Arial", 12));

        card.getChildren().addAll(profileIcon, userNameText, distanceText);
        return card;
    }

    private void addHoverEffect(ImageView icon) {
        icon.setOnMouseEntered(event -> icon.setEffect(new DropShadow(10, Color.CYAN)));
        icon.setOnMouseExited(event -> icon.setEffect(null));
    }

    private void redirectTo(String view) {
        System.out.println("Redirecting to " + view + " view...");
        // Implement redirection logic here
    }

    public static void main(String[] args) {
        launch();
    }
}
