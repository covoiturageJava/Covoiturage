package com.example.carpoolingapp.microservices.Admin.View;

import com.example.carpoolingapp.microservices.Admin.Controller.AdminController;
import com.example.carpoolingapp.microservices.auth.controller.LoginController;
import com.example.carpoolingapp.microservices.auth.view.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainAdmin extends Application {
    @Override
    public void start(Stage primaryStage) {
        AdminController adminController = new AdminController(primaryStage);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(650.0, 404.0);
        anchorPane.setStyle("-fx-background-image: url('img.png'); -fx-background-size: cover;");

        ImageView imageView1 = new ImageView();
        Image image = new Image("protection.png");
        imageView1.setImage(image);
        imageView1.setFitHeight(150.0);
        imageView1.setFitWidth(200.0);
        imageView1.setLayoutX(14.0);
        imageView1.setLayoutY(162.0);
        imageView1.setPickOnBounds(true);
        imageView1.setPreserveRatio(true);

        Button btnManageDriversUsers = new Button("Manage Drivers and Users");
        btnManageDriversUsers.setLayoutX(394.0);
        btnManageDriversUsers.setLayoutY(169.0);
        btnManageDriversUsers.setPrefSize(182.0, 42.0);
        btnManageDriversUsers.setFont(Font.font("Bell MT Bold", 14.0));
        btnManageDriversUsers.setOnAction(event -> adminController.showDriversUsersView());

        Button ReturnToLogin = new Button("Go Back to Login Page");
        ReturnToLogin.setLayoutX(394.0);
        ReturnToLogin.setLayoutY(92.0);
        ReturnToLogin.setPrefSize(182.0, 42.0);
        ReturnToLogin.setFont(Font.font("Bell MT Bold", 14.0));
        ReturnToLogin.setOnAction(event -> {
            try {
                Stage currentStage = (Stage) ReturnToLogin.getScene().getWindow();
                currentStage.close();
                LoginController loginController = new LoginController();
                Stage loginStage = new Stage();
                Login loginDriver = new Login(loginController);
                loginDriver.show(loginStage);
                stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button btnSeeDriversRequests = new Button("Manage Drivers Requests");
        btnSeeDriversRequests.setLayoutX(394.0);
        btnSeeDriversRequests.setLayoutY(246.0);
        btnSeeDriversRequests.setPrefSize(182.0, 42.0);
        btnSeeDriversRequests.setFont(Font.font("Bell MT Bold", 14.0));
        btnSeeDriversRequests.setOnAction(event -> adminController.showManageRequestsView());

        anchorPane.getChildren().addAll(ReturnToLogin,imageView1, btnManageDriversUsers, btnSeeDriversRequests);

        Scene scene = new Scene(anchorPane);
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
