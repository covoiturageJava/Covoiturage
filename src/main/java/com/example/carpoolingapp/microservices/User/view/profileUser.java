package com.example.carpoolingapp.microservices.User.view;

import com.example.carpoolingapp.microservices.Drivers.view.HomeSimpleDriver;
import com.example.carpoolingapp.microservices.User.controller.userprofileControlleur;
import com.example.carpoolingapp.microservices.auth.controller.LoginController;
import com.example.carpoolingapp.model.User;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class profileUser {
    private userprofileControlleur userProfileController = new userprofileControlleur();
    private User currentUser;

    public void start(Stage stage,User user ) {
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
        Text mainTitle = createText("Profile Setting", 181, 29, "System Bold", 25);
        Text profileTitle = createText("Profile Details", 22, 29, "System Bold", 17);
        Text historyTitle = createText("History of Trips", 22, 244, "System Bold", 17);
        Text contactTitle = createText("Profile Contact", 21, 168, "System Bold", 17);

        // User Info Labels and Values
        Text userNameLabel = createText("Username", 23, 57, "Arial", 14);
        Text userNameValue = createText("", 136, 57, "Arial", 13);
        Text firstNameLabel = createText("First Name", 24, 82, "Arial", 14);
        Text firstNameValue = createText("", 136, 82, "Arial", 13);
        Text lastNameLabel = createText("Last Name", 23, 107, "Arial", 14);
        Text lastNameValue = createText("", 136, 107, "Arial", 13);
        Text dobLabel = createText("Date of Birth", 22, 133, "Arial", 14);
        Text dobValue = createText("", 136, 133, "Arial", 13);
        // Contact Info Labels and Values
        Text phoneLabel = createText("Phone", 24, 193, "Arial", 14);
        Text phoneValue = createText("", 136, 193, "Arial", 13);
        Text emailLabel = createText("Email", 24, 218, "Arial", 14);
        Text emailValue = createText("", 136, 218, "Arial", 13);
        // Table for Trip History
        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #2C2F48;");
        table.setLayoutX(24);
        table.setLayoutY(270);
        table.setPrefSize(400, 150);
        // Define columns
        TableColumn<String[], String> driverCol = new TableColumn<>("Driver");
        driverCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));
        driverCol.setPrefWidth(130);

        TableColumn<String[], String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));
        dateCol.setPrefWidth(130);

        TableColumn<String[], String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));
        priceCol.setPrefWidth(120);
        // Logout Button
        Button logoutButton = new Button("Retour");
        logoutButton.setLayoutX(390);
        logoutButton.setLayoutY(396);
        logoutButton.setPrefSize(90, 30);
        logoutButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold;");
        logoutButton.setOnAction(event -> {
            try {
                HomePage testPage = new HomePage(user);
                testPage.show(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // Add columns to table
        table.getColumns().addAll(driverCol, dateCol, priceCol);

        // Populate table (controller logic to fetch history)
        userProfileController.populateTripHistoryTable(table, user.getId());

        // Fetch user details and update labels
        currentUser = userProfileController.getUserProfile(user.getId());
        if (currentUser != null) {
            userNameValue.setText(currentUser.getUsername());
            firstNameValue.setText(currentUser.getFirstName());
            lastNameValue.setText(currentUser.getLastName());
            dobValue.setText(currentUser.getBirthDate());
            phoneValue.setText(currentUser.getPhoneNumber());
            emailValue.setText(currentUser.getEmail());
        }

        // Add Components to Root
        profileDetailsPane.getChildren().addAll(profileTitle, userNameLabel, userNameValue, firstNameLabel,
                firstNameValue, lastNameLabel, lastNameValue, dobLabel, dobValue, contactTitle, phoneLabel,
                phoneValue, emailLabel, emailValue,logoutButton, historyTitle, table);
        root.getChildren().addAll(profileDetailsPane, mainTitle);

        // Scene and Stage Setup
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User Profile");
        stage.show();
    }

    private Text createText(String content, double x, double y, String fontName, double fontSize) {
        Text text = new Text(content);
        text.setLayoutX(x);
        text.setLayoutY(y);
        text.setFill(javafx.scene.paint.Color.WHITE);
        text.setFont(Font.font(fontName, fontSize));
        return text;
    }
}
