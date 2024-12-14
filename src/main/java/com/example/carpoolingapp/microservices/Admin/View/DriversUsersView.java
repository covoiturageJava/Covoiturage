package com.example.carpoolingapp.microservices.Admin.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DriversUsersView  extends Application {

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-image: url('/Admin.jpg'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: no-repeat;");
        // "See Details" Button
        Button btnSeeDetails = new Button("See Details");
        btnSeeDetails.setLayoutX(781.0);
        btnSeeDetails.setLayoutY(645.0);
        btnSeeDetails.setPrefSize(93.0, 38.0);
        btnSeeDetails.setStyle("-fx-background-color: #61a87a; -fx-background-radius: 10; -fx-text-fill: white;");
        btnSeeDetails.setFont(Font.font("System Bold", 14.0));

        // "Delete" Button
        Button btnDelete = new Button("Delete");
        btnDelete.setLayoutX(672.0);
        btnDelete.setLayoutY(645.0);
        btnDelete.setPrefSize(94.0, 38.0);
        btnDelete.setStyle("-fx-background-color: #61a87a; -fx-background-radius: 10; -fx-text-fill: white;");
        btnDelete.setFont(Font.font("System Bold", 14.0));

        // TableView for Drivers
        TableView<String> driversTable = new TableView<>();
        driversTable.setLayoutX(14.0);
        driversTable.setLayoutY(173.0);
        driversTable.setPrefSize(890.0, 200.0);

        // Columns for Drivers Table
        TableColumn<String, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setPrefWidth(133.6);
        TableColumn<String, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setPrefWidth(141.6);
        TableColumn<String, String> colUsername = new TableColumn<>("UserName");
        colUsername.setPrefWidth(195.2);
        TableColumn<String, String> colEmail = new TableColumn<>("Email");
        colEmail.setPrefWidth(264.0);
        TableColumn<String, String> colPhoneNumber = new TableColumn<>("Phone Number");
        colPhoneNumber.setPrefWidth(160.0);

        driversTable.getColumns().addAll(colFirstName, colLastName, colUsername, colEmail, colPhoneNumber);

        // Separator
        Separator separator = new Separator();
        separator.setLayoutX(-4.0);
        separator.setLayoutY(385.0);
        separator.setPrefWidth(926.0);

        // Drivers Label
        Label lblDrivers = new Label("Drivers      :");
        lblDrivers.setLayoutX(35.0);
        lblDrivers.setLayoutY(140.0);
        lblDrivers.setPrefSize(140.0, 18.0);
        lblDrivers.setFont(Font.font("System Bold", 16.0));

        // Users Label
        Label lblUsers = new Label("Users      :");
        lblUsers.setLayoutX(35.0);
        lblUsers.setLayoutY(400.0);
        lblUsers.setPrefSize(140.0, 18.0);
        lblUsers.setFont(Font.font("System Bold", 16.0));

        // TableView for Users
        TableView<String> usersTable = new TableView<>();
        usersTable.setLayoutX(14.0);
        usersTable.setLayoutY(431.0);
        usersTable.setPrefSize(890.0, 200.0);

        // Columns for Users Table
        TableColumn<String, String> userColFirstName = new TableColumn<>("First Name");
        userColFirstName.setPrefWidth(133.6);
        TableColumn<String, String> userColLastName = new TableColumn<>("Last Name");
        userColLastName.setPrefWidth(141.6);
        TableColumn<String, String> userColUsername = new TableColumn<>("UserName");
        userColUsername.setPrefWidth(195.2);
        TableColumn<String, String> userColEmail = new TableColumn<>("Email");
        userColEmail.setPrefWidth(264.0);
        TableColumn<String, String> userColPhoneNumber = new TableColumn<>("Phone Number");
        userColPhoneNumber.setPrefWidth(160.0);

        usersTable.getColumns().addAll(userColFirstName, userColLastName, userColUsername, userColEmail, userColPhoneNumber);

        // ImageView
        ImageView imageView = new ImageView();
        Image image = new Image(getClass().getResource("/protection.png").toExternalForm());
        imageView.setImage(image);
        imageView.setLayoutX(748.0);
        imageView.setLayoutY(27.0);
        imageView.setFitWidth(127.0);
        imageView.setFitHeight(108.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        // Welcome Label
        Label lblWelcome = new Label("Welcome Mr.Mehdi Kadiri");
        lblWelcome.setLayoutX(335.0);
        lblWelcome.setLayoutY(42.0);
        lblWelcome.setPrefSize(247.0, 38.0);
        lblWelcome.setTextFill(javafx.scene.paint.Color.web("#0d3696"));
        lblWelcome.setFont(Font.font("System Bold Italic", 18.0));

        // Adding all nodes to the root
        root.getChildren().addAll(
                btnSeeDetails, btnDelete, driversTable, separator, lblDrivers,
                lblUsers, usersTable, imageView, lblWelcome
        );

        // Setting the Scene
        Scene scene = new Scene(root, 916.0, 694.0);

        // Stage Setup
        primaryStage.setTitle("Drivers and Users Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
