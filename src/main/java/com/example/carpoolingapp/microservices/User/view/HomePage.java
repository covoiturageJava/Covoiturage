package com.example.carpoolingapp.microservices.User.view;

import com.example.carpoolingapp.microservices.User.controller.UserController;
import com.example.carpoolingapp.model.Driver;
import com.example.carpoolingapp.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Map<String, String[]> placeCoordinates = new HashMap<>();
    private UserController userController = new UserController();
    private User user;
    private String fromLatitude;
    private String fromLongitude;
    private String toLatitude;
    private String toLongitude;


    public HomePage(User usr){
        this.user = usr;
    }

    public void show(Stage primaryStage) {
        // Root layout
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(900, 500);

        // Sidebar
        AnchorPane sidebar = createSidebar();

        // Search Fields
        TextField fromField = createSearchField("Localisation de départ", 20);
        TextField toField = createSearchField("Localisation d'arrivée", 20);
        toField.setLayoutX(400);

        // Suggestions Lists
        ListView<String> fromSuggestionsList = createSuggestionsList(60);
        ListView<String> toSuggestionsList = createSuggestionsList(60);
        toSuggestionsList.setLayoutX(400);

        // Listeners for user input
        fromField.textProperty().addListener((observable, oldValue, newValue) -> updateSuggestions(newValue, fromSuggestionsList, fromField, toField));
        toField.textProperty().addListener((observable, oldValue, newValue) -> updateSuggestions(newValue, toSuggestionsList, fromField, toField));


        // Handle clicks on suggestions
        fromSuggestionsList.setOnMouseClicked(event -> handleSuggestionClick(fromSuggestionsList));
        toSuggestionsList.setOnMouseClicked(event -> handleSuggestionClick(toSuggestionsList));

        // WebView
        WebView webView = new WebView();
        webView.setLayoutX(24);
        webView.setLayoutY(80);
        webView.setPrefSize(750, 250);

        // Header Text
        Text headerText = createText("Drivers Offres", "Aldhabi", 32, "#FFFFFF", 24, 380);

        // Offer Cards
        AnchorPane offerCard1 = createOfferCard(24, 400, "User Name", "Distance to user", "file:src/main/resources/com/example/carpoolingapp/images/profile.png", this.user.getId(), 1);
        AnchorPane offerCard2 = createOfferCard(24, 500, "User Name", "Distance to user", "file:src/main/resources/com/example/carpoolingapp/images/profile.png", this.user.getId(), 1);

        // Add components to root
        root.getChildren().addAll(sidebar, webView, headerText, offerCard1, offerCard2, fromField, toField, fromSuggestionsList, toSuggestionsList);

        // Ensure suggestions overlay the WebView
        fromSuggestionsList.toFront();
        toSuggestionsList.toFront();

        // Scene setup
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Carpooling Application");
        primaryStage.show();
    }

    private AnchorPane createSidebar() {
        AnchorPane sidebar = new AnchorPane();
        sidebar.setLayoutX(800);
        sidebar.setPrefSize(100, 600);
        sidebar.setStyle("-fx-background-color: #2C2F48;");

        // Arrow Icon
        ImageView arrowIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/Arrow.png", 24, 33, -12, 37);
        arrowIcon.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);

        // Profile Driver Image
        ImageView profiledriver = createImageView("file:src/main/resources/com/example/carpoolingapp/images/profile.png", 100, 80, 13, 64);
        Text userName = new Text(this.user.getFirstName() + " " + this.user.getLastName());
        userName.setFont(Font.font("Arial Bold", 13));
        userName.setStyle("-fx-font-weight: bold;");
        userName.setFill(javafx.scene.paint.Color.WHITE);
        userName.setLayoutX(16);
        userName.setLayoutY(151);

        Text userNameTag = new Text("@" + this.user.getUsername());
        userNameTag.setFont(Font.font("Times New Roman Italic", 10));
        userNameTag.setFill(javafx.scene.paint.Color.WHITE);
        userNameTag.setLayoutX(23);
        userNameTag.setLayoutY(163);

        // Home Icon
        ImageView homeIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/HomeProfile.png", 100, 50, 0, 206);

        // Profile Icon
        ImageView profIcon = createImageView("file:src/main/resources/com/example/carpoolingapp/images/PofileDriver.png", 100, 50, 0, 251);

        // Add all components to sidebar
        sidebar.getChildren().addAll(arrowIcon, profiledriver, userName, userNameTag, homeIcon, profIcon);

        // Create a boolean flag to track the sidebar state
        final boolean[] isMinimized = {false};

        // Handle Arrow Icon Click
        arrowIcon.setOnMouseClicked(event -> {
            if (isMinimized[0]) {
                // Expand Sidebar
                sidebar.setPrefWidth(100);
                sidebar.setLayoutX(800);
                homeIcon.setImage(new Image("file:src/main/resources/com/example/carpoolingapp/images/HomeProfile.png"));
                profIcon.setImage(new Image("file:src/main/resources/com/example/carpoolingapp/images/PofileDriver.png"));
                homeIcon.setFitWidth(100);
                profIcon.setFitWidth(100);
                userNameTag.setVisible(true);
                userName.setVisible(true);

                profiledriver.setFitWidth(100);
                profiledriver.setFitHeight(80);

                // Reset Arrow Orientation
                arrowIcon.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            } else {
                // Minimize Sidebar
                sidebar.setPrefWidth(50);
                sidebar.setLayoutX(850);
                homeIcon.setImage(new Image("file:src/main/resources/com/example/carpoolingapp/images/home.png"));
                homeIcon.setFitWidth(50);
                profIcon.setImage(new Image("file:src/main/resources/com/example/carpoolingapp/images/prof.png"));
                profIcon.setFitWidth(50);
                userNameTag.setVisible(false);
                userName.setVisible(false);

                profiledriver.setFitWidth(40);
                profiledriver.setFitHeight(40);
                profiledriver.setLayoutX(5);

                // Reset Arrow Orientation
                arrowIcon.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
            }
            // Toggle the state
            isMinimized[0] = !isMinimized[0];
        });

        return sidebar;
    }

    private TextField createSearchField(String prompt, double layoutY) {
        TextField searchField = new TextField();
        searchField.setPromptText(prompt);
        searchField.setLayoutX(24);
        searchField.setLayoutY(layoutY);
        searchField.setPrefWidth(350);
        return searchField;
    }

    private ListView<String> createSuggestionsList(double layoutY) {
        ListView<String> suggestionsList = new ListView<>();
        suggestionsList.setLayoutX(24);
        suggestionsList.setLayoutY(layoutY);
        suggestionsList.setPrefSize(350, 100);
        suggestionsList.setVisible(false);
        return suggestionsList;
    }

    private AnchorPane createOfferCard(double layoutX, double layoutY, String userName, String distance, String profileImagePath, int userId, int driverId) {
        AnchorPane card = new AnchorPane();
        card.setLayoutX(layoutX);
        card.setLayoutY(layoutY);
        card.setPrefSize(750, 80);
        card.setStyle("-fx-background-color: #2C2F48; -fx-background-radius: 20;");

        ImageView profileIcon = createImageView(profileImagePath, 66, 55, 12, 8);
        Text userNameText = createText(userName, "Arial Bold", 12, "#FFFFFF", 90, 29);
        Text distanceText = createText(distance, "Arial", 13, "#FFFFFF", 90, 51);

        // Create the "Choose" button
        Button chooseButton = new Button("Choose");
        chooseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");
        chooseButton.setPrefSize(100, 30);

        chooseButton.setOnAction(event -> {
            try {
                Driver driver = userController.getDriverDetails(driverId);
                if (driver == null) {
                    System.out.println("Driver not found.");
                    return;
                }

                String[] driverLocation = userController.getDriverLocation(driverId);
                if (driverLocation == null) {
                    System.out.println("Driver location not found.");
                    return;
                }

                // Use the instance variables for fromLatitude and fromLongitude
                if (fromLatitude != null && fromLongitude != null) {
                    System.out.println("Start Location: Lat: " + fromLatitude + ", Long: " + fromLongitude);
                } else {
                    System.out.println("Start location coordinates are not available.");
                }

                Stage popupStage = new Stage();
                AnchorPane popupRoot = new AnchorPane();
                popupRoot.setPrefSize(400, 300);
                popupRoot.setStyle("-fx-background-color: #1D203E; -fx-padding: 20px;");

                Text driverInfo = new Text("Driver: " + driver.getFirstName() + " " + driver.getLastName() +
                        "\nVehicle: " + driver.getMarqueVehicule() + " " + driver.getModeleVehicule() +
                        "\nYear: " + driver.getAnneeVehicule());
                driverInfo.setFill(javafx.scene.paint.Color.WHITE);
                driverInfo.setLayoutY(20);

                Text driverLocationText = new Text("Driver Location: Lat: " + driverLocation[0] + ", Long: " + driverLocation[1]);
                driverLocationText.setFill(javafx.scene.paint.Color.WHITE);
                driverLocationText.setLayoutY(80);

                Text fromLocationText = new Text("Start Location: Lat: " + fromLatitude + ", Long: " + fromLongitude);
                fromLocationText.setFill(javafx.scene.paint.Color.WHITE);
                fromLocationText.setLayoutY(120);

                Text priceText = new Text("Price:");
                priceText.setFill(javafx.scene.paint.Color.WHITE);
                priceText.setLayoutY(160);
                TextField priceField = new TextField("40dh");
                priceField.setLayoutY(180);

                Button confirmButton = new Button("Confirm");
                confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                confirmButton.setLayoutY(220);
                confirmButton.setOnAction(confirmEvent -> {
                    try {
                        String newPrice = priceField.getText();
                        System.out.println("Driver confirmed with price: " + newPrice);

                        // Close the current popup
                        popupStage.close();

                        // Open a new popup with WebView
                        Stage webViewPopup = new Stage();
                        AnchorPane webViewRoot = new AnchorPane();
                        webViewRoot.setPrefSize(400, 300);
                        webViewRoot.setStyle("-fx-background-color: #1D203E; -fx-padding: 20px;");

                        // Create WebView
                        WebView webView = new WebView();
                        webView.setPrefSize(360, 200);
                        webView.setLayoutX(20);
                        webView.setLayoutY(60);

                        // Create Text
                        Text waitText = new Text("Wait here: Latitude " + fromLatitude + ", Longitude " + fromLongitude);
                        waitText.setFill(javafx.scene.paint.Color.WHITE);
                        waitText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                        waitText.setLayoutX(20);
                        waitText.setLayoutY(40);

                        // Add components to the root
                        webViewRoot.getChildren().addAll(waitText, webView);

                        // Set the scene and show the new popup
                        webViewPopup.setScene(new Scene(webViewRoot));
                        webViewPopup.setTitle("Driver Location");
                        webViewPopup.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Failed to open WebView popup.");
                    }
                });

                popupRoot.getChildren().addAll(driverInfo, driverLocationText, fromLocationText, priceText, priceField, confirmButton);

                popupStage.setScene(new Scene(popupRoot));
                popupStage.setTitle("Driver Details");
                popupStage.show();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to fetch driver details or create popup.");
            }
        });


        // Position the button at the right center
        AnchorPane.setTopAnchor(chooseButton, 25.0);  // 25 pixels from the top of the card
        AnchorPane.setRightAnchor(chooseButton, 20.0); // 20 pixels from the right of the card

        // Add components to the card
        card.getChildren().addAll(profileIcon, userNameText, distanceText, chooseButton);

        return card;
    }

    private ImageView createImageView(String path, double width, double height, double x, double y) {
        ImageView imageView = new ImageView(path);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        return imageView;
    }

    private Text createText(String text, String fontFamily, int fontSize, String color, double x, double y) {
        Text textElement = new Text(text);
        textElement.setFont(Font.font(fontFamily, fontSize));
        textElement.setFill(javafx.scene.paint.Color.web(color));
        textElement.setLayoutX(x);
        textElement.setLayoutY(y);
        return textElement;
    }

    private void updateSuggestions(String query, ListView<String> suggestionsList, TextField fromField, TextField toField) {
        if (query.isEmpty()) {
            suggestionsList.setVisible(false);
            return;
        }

        // Send a request to OpenStreetMap Nominatim API for location suggestions
        String apiUrl = String.format("https://nominatim.openstreetmap.org/search?format=json&q=%s&addressdetails=1&limit=10", query);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
            String responseBody = response.body();

            // Parse the response using Gson
            JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
            List<String> suggestions = new ArrayList<>();
            placeCoordinates.clear(); // Clear previous coordinates

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String displayName = jsonObject.get("display_name").getAsString();
                String latitude = jsonObject.get("lat").getAsString();
                String longitude = jsonObject.get("lon").getAsString();

                // Store coordinates
                placeCoordinates.put(displayName, new String[]{latitude, longitude});

                // Store the latitude and longitude in instance variables
                if (query.equals(fromField.getText())) {
                    fromLatitude = latitude;
                    fromLongitude = longitude;
                } else if (query.equals(toField.getText())) {
                    toLatitude = latitude;
                    toLongitude = longitude;
                }

                suggestions.add(displayName);
            }

            // Update the suggestions list on the UI thread
            javafx.application.Platform.runLater(() -> {
                ObservableList<String> observableSuggestions = FXCollections.observableArrayList(suggestions);
                suggestionsList.setItems(observableSuggestions);
                suggestionsList.setVisible(true);
            });
        });
    }

    private void handleSuggestionClick(ListView<String> suggestionsList) {
        String selectedSuggestion = suggestionsList.getSelectionModel().getSelectedItem();
        if (selectedSuggestion != null) {
            String[] coordinates = placeCoordinates.get(selectedSuggestion);
            if (coordinates != null) {
                String latitude = coordinates[0];
                String longitude = coordinates[1];
                System.out.println("Location: " + selectedSuggestion + " | Latitude: " + latitude + " | Longitude: " + longitude);
            }
        }
    }
}
