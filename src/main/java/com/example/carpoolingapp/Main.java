 package com.example.carpoolingapp;

import com.example.carpoolingapp.microservices.auth.controller.LoginController;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.microservices.auth.view.Login;
import com.example.carpoolingapp.microservices.auth.serveur.SocServeur;
import com.example.carpoolingapp.microservices.Drivers.controller.MapServer;
import com.example.carpoolingapp.microservices.User.controller.ThreadCreatorServer;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

 public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            DatabaseInitializer.getConnection();
            System.out.println("Connected to the database successfully.");

            new Thread(() -> SocServeur.main(new String[]{})).start();
            new Thread(() -> {
                try {
                    MapServer.main(new String[]{});
                } catch (IOException e) {
                    System.err.println("IOException occurred in MapServer: " + e.getMessage());
                }
            }).start();
            new Thread(() -> {
                ThreadCreatorServer server = new ThreadCreatorServer(8080);
                server.start();
            }).start();

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