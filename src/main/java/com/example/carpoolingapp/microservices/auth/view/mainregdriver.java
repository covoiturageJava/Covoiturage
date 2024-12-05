package com.example.carpoolingapp.microservices.auth.view;

import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.Driver;
import javafx.application.Application;
import javafx.stage.Stage;

public class mainregdriver extends Application {
    private Driver driver = new Driver();

    @Override
    public void start(Stage stage) {
        try {
            DatabaseInitializer.getConnection();
            System.out.println("Connected to the database successfully.");

            driverregister driverRegisterView = new driverregister();
            stage.setScene(driverRegisterView.getDriverRegisterScene(driver));
            stage.setTitle("Inscription Conducteur");
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to start the driver registration process: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
