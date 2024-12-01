package com.example.carpoolingapp.microservices.Drivers.view;
        import javafx.application.Application;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.text.Font;
        import javafx.scene.text.Text;
        import javafx.scene.web.WebView;
        import javafx.stage.Stage;

public class HomeDetailsOffresDrivers extends Application {
    Button acceptButton;
    Button deleteButton;
    @Override
    public void start(Stage stage) {
        // Root pane
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(664, 540);

        // Sidebar
        AnchorPane sidebar = new AnchorPane();
        sidebar.setLayoutX(657);


        sidebar.setPrefSize(43, 550);
        sidebar.setStyle("-fx-background-color: #2C2F48;");

        ImageView profileIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 43, 43, 4, 92);
        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 45, 25, 0, 180);
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 45, 30, 0, 220);
        ImageView logoutImage = createImageView("file:src/main/resources/com/example/carpoolingapp/images/LoOutButton.png", 55, 299, -3, 305);
        sidebar.setMinWidth(40);
        sidebar.setPrefWidth(45);
        sidebar.setMaxWidth(45);

        sidebar.getChildren().addAll(profileIcon, homeIcon, profIcon,logoutImage);

        String pathImage = "file:src/main/resources/com/example/carpoolingapp/images/profile.png";


        // Offer Card
        AnchorPane offerCard1 = createOfferCard( "User Name", "Distance to user", pathImage);


        Text title = new Text("Offres");
        title.setLayoutX(34);
        title.setLayoutY(400);
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");
        title.setFont(Font.font("Aldhabi", 32));

        // WebView
        WebView webView = new WebView();
        webView.setLayoutX(17);
        webView.setLayoutY(14);
        webView.setPrefSize(630, 366);
        webView.getEngine().load("file:src/main/resources/webview-content.html");
        acceptButton.setOnMouseClicked(mouseEvent -> {
            try{
                PickUpDriver pick = new PickUpDriver();
                Stage currentStage = (Stage)((Button)mouseEvent.getSource()).getScene().getWindow();
                pick.start(currentStage);
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        deleteButton.setOnMouseClicked(mouseEvent -> {
            try {

                HomeSimpleDriver homeSimpleDriver = new HomeSimpleDriver();

                Stage currentStage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();

                homeSimpleDriver.start(currentStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        root.getChildren().addAll(sidebar, offerCard1, title, webView);

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

    private AnchorPane createOfferCard( String userName, String distance, String profileImagePath) {
        AnchorPane card = new AnchorPane();

        card.setLayoutX(50);
        card.setLayoutY(413);
        card.setPrefSize(526, 127);
        card.setStyle("-fx-background-color: #2C2F48; -fx-background-radius: 20; -fx-border-color: #01B7C5; -fx-border-radius: 20;");

        ImageView profileIcon = createImageView(profileImagePath, 70, 78, 7, 24);

        Text userNameText = new Text(userName);
        userNameText.setLayoutX(86);
        userNameText.setLayoutY(30);
        userNameText.setFill(javafx.scene.paint.Color.WHITE);
        userNameText.setStyle("-fx-font-weight: bold;");
        userNameText.setFont(Font.font("Arial Bold", 12));

        Text distanceText = new Text(distance);
        distanceText.setLayoutX(86);
        distanceText.setLayoutY(50);
        distanceText.setFill(javafx.scene.paint.Color.WHITE);
        distanceText.setFont(Font.font("Arial", 13));

        Text userDateText = new Text("Data of User");
        userDateText.setLayoutX(86);
        userDateText.setLayoutY(71);
        userDateText.setFill(javafx.scene.paint.Color.WHITE);
        userDateText.setFont(Font.font("Arial", 13));

        Text priceText = new Text("Prix : ");
        priceText.setLayoutX(381);
        priceText.setLayoutY(25);
        priceText.setFill(javafx.scene.paint.Color.WHITE);
        priceText.setStyle("-fx-font-weight: bold;");
        priceText.setFont(Font.font("Times New Roman Bold", 14));

        Text prixText = new Text("00.00 DH");
        prixText.setLayoutX(384);
        prixText.setLayoutY(44);
        prixText.setFill(javafx.scene.paint.Color.WHITE);
        prixText.setStyle("-fx-font-weight: bold;");
        prixText.setFont(Font.font("Times New Roman Bold", 14));

         acceptButton = new Button("Accepted");
        acceptButton.setLayoutX(384);
        acceptButton.setLayoutY(53);
        acceptButton.setPrefSize(118, 25);
        acceptButton.setStyle("-fx-background-color: #00DD37; -fx-background-radius: 8;");
        acceptButton.setTextFill(javafx.scene.paint.Color.WHITE);

        deleteButton = new Button("Delete");
        deleteButton.setLayoutX(384);
        deleteButton.setLayoutY(86);
        deleteButton.setPrefSize(118, 25);
        deleteButton.setStyle("-fx-background-color: #CC1818; -fx-background-radius: 8;");
        deleteButton.setTextFill(javafx.scene.paint.Color.WHITE);

        card.getChildren().addAll(profileIcon,userNameText, distanceText, userDateText, priceText,prixText, acceptButton, deleteButton);
        return card;
    }

    public static void main(String[] args) {
        launch();
    }
}

