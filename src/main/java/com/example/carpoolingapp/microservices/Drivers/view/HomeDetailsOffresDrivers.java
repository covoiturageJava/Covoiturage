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
        root.setPrefSize(624, 453);

        // Sidebar
        AnchorPane sidebar = new AnchorPane();
        sidebar.setLayoutX(581);
        sidebar.setPrefSize(38, 453);
        sidebar.setStyle("-fx-background-color: #2C2F48;");

        ImageView profileIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 42, 42, 1, 92);
        ImageView arrowIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/Arrow.png", 24, 33, -12, 37);
        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/home.png", 44, 29, 0, 179);
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/prof.png", 44, 33, 0, 221);

        sidebar.getChildren().addAll(profileIcon, arrowIcon, homeIcon, profIcon);

        String pathImage = "file:src/main/resources/com/example/carpoolingapp/images/profile.png";


        // Offer Card
        AnchorPane offerCard = createOfferCard(48, 277, "User Name", "Distance to user", pathImage);

        // Title
        Text title = new Text("Offres");
        title.setLayoutX(22);
        title.setLayoutY(265);
        title.setFill(javafx.scene.paint.Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");
        title.setFont(Font.font("Aldhabi", 32));

        // WebView
        WebView webView = new WebView();
        webView.setLayoutX(25);
        webView.setLayoutY(14);
        webView.setPrefSize(537, 226);
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
        root.getChildren().addAll(sidebar, offerCard, title, webView);

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
        card.setPrefSize(511, 127);
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

