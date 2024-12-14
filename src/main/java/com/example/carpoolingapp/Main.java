 package com.example.carpoolingapp;

import com.example.carpoolingapp.microservices.auth.controller.LoginController;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.microservices.auth.view.Login;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            DatabaseInitializer.getConnection();
            System.out.println("Connected to the database successfully.");
            LoginController loginController = new LoginController();
            new Login(loginController).show(stage);
        } catch (Exception e) {
            System.err.println("Failed to start the application: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        launch(args);
}
}