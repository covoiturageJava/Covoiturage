package com.example.carpoolingapp.microservices.Drivers.view;

import com.example.carpoolingapp.microservices.Drivers.controller.DriveruserprofileControlleur;
import com.example.carpoolingapp.model.Driver;
import com.example.carpoolingapp.model.SessionDriver;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProfileDriver {
    private DriveruserprofileControlleur userProfileController = new DriveruserprofileControlleur();
    private Driver currentUser;

    public void start(Stage stage, int driverId, SessionDriver session) {
        // Root Pane
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1D203E;");
        root.setPrefSize(620, 493);

        // Profile Details Pane
        AnchorPane profileDetailsPane = new AnchorPane();
        profileDetailsPane.setLayoutX(63);
        profileDetailsPane.setLayoutY(49);
        profileDetailsPane.setPrefSize(485, 430);
        profileDetailsPane.setStyle("-fx-background-color: #2C2F48; -fx-background-radius: 20; -fx-border-radius: 20;");

        // Titles and Labels
        Text mainTitle = createText("Profile Setting", 181, 29, "System Bold", 25, true);
        Text profileTitle = createText("Profile Details", 22, 29, "System Bold", 17, true);
        Text historyTitle = createText("History of Trajet", 22, 244, "System Bold", 17, true);
        Text contactTitle = createText("Profile Contact", 21, 170, "System Bold", 17, true);
        Text vehicleInfoTitle = createText("Vehicle Info", 272, 29, "System Bold", 17, true);

        // User Info Labels and Values
        Text userNameLabel = createText("Driver Name", 23, 55, "Arial", 14, false);
        Text userNameValue = createText("", 136, 55, "Arial", 13, false);
        Text firstNameLabel = createText("First Name", 24, 77, "Arial", 14, false);
        Text firstNameValue = createText("", 134, 77, "Arial", 13, false);
        Text lastNameLabel = createText("Last Name", 23, 97, "Arial", 14, false);
        Text lastNameValue = createText("", 134, 97, "Arial", 13, false);
        Text dobLabel = createText("Date of Birth", 22, 120, "Arial", 14, false);
        Text dobValue = createText("", 134, 120, "Arial", 13, false);
        Text ratingLabel = createText("Rating", 22, 143, "Arial", 14, false);
        Text ratingValue = createText("", 134, 143, "Arial", 13, false);

        // Contact Info Labels and Values
        Text phoneLabel = createText("Phone", 24, 190, "Arial", 14, false);
        Text phoneValue = createText("", 132, 190, "Arial", 13, false);
        Text emailLabel = createText("Email", 24, 210, "Arial", 14, false);
        Text emailValue = createText("", 132, 210, "Arial", 13, false);

        // Vehicle Info Labels and Values
        Text vehicleTypeLabel = createText("Vehicle Type", 272, 55, "Arial", 14, false);
        Text vehicleTypeValue = createText("", 400, 55, "Arial", 13, false);
        Text vehicleBrandLabel = createText("Brand", 272, 77, "Arial", 14, false);
        Text vehicleBrandValue = createText("", 400, 77, "Arial", 13, false);
        Text vehicleModelLabel = createText("Model", 272, 97, "Arial", 14, false);
        Text vehicleModelValue = createText("", 400, 97, "Arial", 13, false);
        Text vehicleYearLabel = createText("Year", 272, 120, "Arial", 14, false);
        Text vehicleYearValue = createText("", 400, 120, "Arial", 13, false);

        // Table for Trip History
        // Define the TableView with the correct type
        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #2C2F48;");
        table.setLayoutX(24);
        table.setLayoutY(250);
        table.setPrefSize(400, 150);

// Define columns
        TableColumn<String[], String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        userCol.setPrefWidth(130);

        TableColumn<String[], String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        dateCol.setPrefWidth(100);

        TableColumn<String[], String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        priceCol.setPrefWidth(80);
        TableColumn<String[], String> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        ratingCol.setPrefWidth(80);
        table.getColumns().addAll(userCol, dateCol, priceCol, ratingCol);
        userProfileController.populateTripHistoryTable(table, driverId);
        // Logout Button
        Button logoutButton = new Button("Retour");
        logoutButton.setLayoutX(390);
        logoutButton.setLayoutY(396);
        logoutButton.setPrefSize(90, 30);
        logoutButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold;");
        logoutButton.setOnAction(event -> {
            try {
                Stage currentStage = (Stage) logoutButton.getScene().getWindow();
                currentStage.close();
                HomeSimpleDriver driverView = new HomeSimpleDriver();
                Stage newStage = new Stage();
                driverView.start(newStage, session);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        currentUser = userProfileController.getUserProfile(driverId);
        if (currentUser != null) {
            double driverRating = userProfileController.getDriverAverageRating(driverId);
            ratingValue.setText(String.format("%.2f", driverRating));
            userNameValue.setText(currentUser.getUsername());
            firstNameValue.setText(currentUser.getFirstName());
            lastNameValue.setText(currentUser.getLastName());
            dobValue.setText(currentUser.getBirthDate());
            phoneValue.setText(currentUser.getPhoneNumber());
            emailValue.setText(currentUser.getEmail());
            vehicleTypeValue.setText(currentUser.getTypeVehicule());
            vehicleBrandValue.setText(currentUser.getMarqueVehicule());
            vehicleModelValue.setText(currentUser.getModeleVehicule());
            vehicleYearValue.setText(currentUser.getAnneeVehicule());
        }
        profileDetailsPane.getChildren().addAll(
                profileTitle, userNameLabel, userNameValue, firstNameLabel, firstNameValue, lastNameLabel,
                lastNameValue, dobLabel, dobValue, ratingLabel, ratingValue, contactTitle, phoneLabel, phoneValue,
                emailLabel, emailValue, logoutButton, vehicleInfoTitle, vehicleTypeLabel, vehicleTypeValue,
                vehicleBrandLabel, vehicleBrandValue, vehicleModelLabel, vehicleModelValue, vehicleYearLabel,
                vehicleYearValue, historyTitle, table
        );
        root.getChildren().addAll(profileDetailsPane, mainTitle);

        // Set Scene and Show Stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Profile Setting");
        stage.show();
    }

    private Text createText(String content, double x, double y, String fontName, double fontSize, boolean bold) {
        Text text = new Text(content);
        text.setLayoutX(x);
        text.setLayoutY(y);
        text.setFill(javafx.scene.paint.Color.WHITE);
        text.setFont(Font.font(fontName, bold ? FontWeight.BOLD : FontWeight.NORMAL, fontSize));
        return text;
    }

}
