package com.example.carpoolingapp.microservices.Drivers.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class HomeSimpleDriver extends Application {

    ImageView seeMoreIcon;
    @Override
    public void start(Stage stage) {
        // Root AnchorPane
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(624, 453);

        // Sidebar
        AnchorPane sidebar = new AnchorPane();
        sidebar.setLayoutX(581);
        sidebar.setPrefSize(38, 453);
        sidebar.setStyle("-fx-background-color: #2C2F48;");

        // Sidebar Icons (Modifiez les chemins relatifs en fonction de l'emplacement réel des images)
       ImageView profileIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 42, 42, 1, 92);
        ImageView arrowIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/Arrow.png", 24, 33, -12, 37);
        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 44, 29, 0, 179);
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 44, 33, 0, 221);

        sidebar.getChildren().addAll(profileIcon, arrowIcon, homeIcon, profIcon);

        String pathImage = "file:src/main/resources/com/example/carpoolingapp/images/profile.png";
        // Offer Card 1
        AnchorPane offerCard1 = createOfferCard(48, 277, "User Name", "Distance to user",pathImage);

        // Offer Card 2
        AnchorPane offerCard2 = createOfferCard(48, 365, "User Name", "Distance to user",pathImage);


        arrowIcon.setOnMouseClicked(event -> {
            try {
                homeProfileDriver homeProfileDriver = new homeProfileDriver();
                Stage currentStage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();

                homeProfileDriver.start(currentStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Title "Offres"
        Text title = new Text("Offres");
        title.setLayoutX(22);
        title.setLayoutY(265);
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");
        title.setFont(Font.font("Aldhabi", 32));

        // WebView - Charge une page HTML (vous pouvez remplacer cela par une image si nécessaire)
        WebView webView = new WebView();
        webView.setLayoutX(25);
        webView.setLayoutY(14);
        webView.setPrefSize(537, 226);
        webView.getEngine().load("file:src/main/resources/com/example/carpoolingapp/webview-content.html");

        // Add elements to root
        root.getChildren().addAll(sidebar, offerCard1, offerCard2, title, webView);

        // Create and set scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Carpooling Application");
        stage.show();
    }

    private ImageView createImageView(String imagePath, double fitWidth, double fitHeight, double layoutX, double layoutY) {
        // Correct way to create the ImageView
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
            try{
                HomeDetailsOffresDrivers DetailsOffre = new HomeDetailsOffresDrivers();
                Stage currentStage = (Stage) ((ImageView)mouseEvent.getSource()).getScene().getWindow();
                DetailsOffre.start(currentStage);
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        // Add all elements to the card
        card.getChildren().addAll(profileIcon, userNameText, distanceText, seeMoreIcon, seeMoreText);
        return card;
    }



    public static void main(String[] args) {
        launch();
    }
}