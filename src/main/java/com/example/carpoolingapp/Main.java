package com.example.carpoolingapp;

import com.example.carpoolingapp.controller.LoginController;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.view.Login;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            DatabaseInitializer.initializeDatabase();
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