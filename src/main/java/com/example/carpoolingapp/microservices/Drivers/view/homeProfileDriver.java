package com.example.carpoolingapp.microservices.Drivers.view;

import javafx.application.Application;
        import javafx.scene.Scene;
        import javafx.scene.control.TextField;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.text.Font;
        import javafx.scene.text.Text;
        import javafx.scene.web.WebView;
        import javafx.stage.Stage;

public class homeProfileDriver extends Application {
    ImageView seeMoreIcon;

    @Override
    public void start(Stage primaryStage) {
        // Root AnchorPane
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(624, 453);

        // Sidebar AnchorPane
        AnchorPane sidebar = new AnchorPane();
        sidebar.setLayoutX(529);
        sidebar.setPrefSize(92, 453);
        sidebar.setStyle("-fx-background-color: #2C2F48;");

        ImageView logoutImage = createImageView("file:src/main/resources/com/example/carpoolingapp/images/LoOutButton.png", 90, 73, 8, 305);
        ImageView profiledriver = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 100, 80, 13, 64);

        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 65, 55, 17, 206);
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 65, 55, 17, 251);


        Text userName = new Text("User Name");
        userName.setFont(Font.font("Arial Bold", 13));
        userName.setStyle("-fx-font-weight: bold;");
        userName.setFill(javafx.scene.paint.Color.WHITE);
        userName.setLayoutX(16);
        userName.setLayoutY(151);


        Text userNameTag = new Text("@UserName");
        userNameTag.setFont(Font.font("Times New Roman Italic", 10));
        userNameTag.setFill(javafx.scene.paint.Color.WHITE);
        userNameTag.setLayoutX(23);
        userNameTag.setLayoutY(163);


        // Add components to sidebar
        sidebar.getChildren().addAll(profiledriver,  homeIcon, profIcon, userName, logoutImage, userNameTag);



        String pathImage = "file:src/main/resources/com/example/carpoolingapp/images/profile.png";
        // Offer Card 1
        AnchorPane offerCard1 = createOfferCard(48, 277, "User Name", "Distance to user",pathImage);

        // Offer Card 2
        AnchorPane offerCard2 = createOfferCard(48, 365, "User Name", "Distance to user",pathImage);


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
        primaryStage.setTitle("JavaFX Interface");
        primaryStage.show();
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
        launch(args);
    }
}
