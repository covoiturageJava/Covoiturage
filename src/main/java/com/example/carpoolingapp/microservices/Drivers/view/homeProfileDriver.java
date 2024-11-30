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

        ImageView profileIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 90, 50, 1, 92);
        ImageView arrowIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/Arrow.png", 24, 33, -12, 37);
        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 65, 55, 17, 206);
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 65, 55, 17, 251);
        arrowIcon.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);

        ImageView profileIcon2 = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 42, 42, 1, 92);
        ImageView arrowIcon2 = createImageView("file:src/main/resources/com/example/carpoolingapp/images/Arrow.png", 24, 33, -12, 37);
        ImageView homeIcon2 = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 44, 29, 0, 179);
        ImageView profIcon2 = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 44, 33, 0, 221);

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
        sidebar.getChildren().addAll(profiledriver, arrowIcon, homeIcon, profIcon, userName, logoutImage, userNameTag);



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

        arrowIcon.setOnMouseClicked(event -> {
            if (sidebar.getPrefWidth() > 50) {
                // Reduce the sidebar
                root.setPrefWidth(624);
                sidebar.setPrefWidth(38);
                sidebar.setLayoutX(581);
                sidebar.getChildren().clear();
                sidebar.getChildren().addAll(arrowIcon2, profIcon2, homeIcon2, profileIcon2);

                // Resize other elements
                webView.setLayoutX(20);
                webView.setLayoutY(14);
                webView.setPrefSize(553, 226); // Adjust the size of WebView
                offerCard1.setLayoutX(48);
                offerCard2.setLayoutX(48);

                // Handle arrowIcon2 click to expand the sidebar again
                arrowIcon2.setOnMouseClicked(event2 -> {
                    // Expand the sidebar
                    sidebar.setLayoutX(541);
                    sidebar.setPrefWidth(92);
                    sidebar.getChildren().clear();
                    sidebar.getChildren().addAll(profiledriver, arrowIcon, homeIcon, profIcon, userName, logoutImage, userNameTag);
                    root.setPrefWidth(624); // Restore the full width of the root

                    // Resize other elements
                    webView.setLayoutX(20);
                    webView.setPrefSize(495, 226); // Reset size of WebView
                    offerCard1.setLayoutX(48);
                    offerCard2.setLayoutX(48);
                });
            } else {
                // Expand the sidebar
                sidebar.setLayoutX(541);
                sidebar.setPrefWidth(92);
                sidebar.getChildren().clear();
                sidebar.getChildren().addAll(profiledriver, arrowIcon, homeIcon, profIcon, userName, logoutImage, userNameTag);
                root.setPrefWidth(624);

                // Resize other elements
                webView.setLayoutX(20);
                webView.setPrefSize(455, 226); // Default size of WebView
                offerCard1.setLayoutX(48);
                offerCard2.setLayoutX(48);

                // Handle arrowIcon2 click to collapse the sidebar
                arrowIcon2.setOnMouseClicked(event2 -> {
                    // Collapse the sidebar
                    sidebar.setLayoutX(581);
                    sidebar.setPrefWidth(38);
                    sidebar.getChildren().clear();
                    sidebar.getChildren().addAll(arrowIcon2, profIcon2, homeIcon2, profileIcon2);

                    // Resize other elements
                    webView.setLayoutX(20);
                    webView.setLayoutY(14);
                    webView.setPrefSize(554, 216); // Adjust the size of WebView
                    offerCard1.setLayoutX(48);
                    offerCard2.setLayoutX(48);
                });
            }
        });

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
        card.setPrefSize(470, 73);
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
        seeMoreText.setLayoutX(404);
        seeMoreText.setLayoutY(29);
        seeMoreText.setFill(javafx.scene.paint.Color.WHITE);
        seeMoreText.setFont(Font.font("System", 12));

        ImageView seeMoreIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/seeMor.png", 30, 24, 424, 35);

        // Add all elements to the card
        card.getChildren().addAll(profileIcon, userNameText, distanceText, seeMoreIcon, seeMoreText);
        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
