package com.example.carpoolingapp.microservices.auth.view;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.Driver;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;

public class mainregdriver extends Application {
        private Driver driver = new Driver();
        @Override
        public void start(Stage stage) {
            try {
                Connection c = DatabaseInitializer.getConnection();
                driverregister driverRegisterView = new driverregister();
                stage.setScene(driverRegisterView.getDriverRegisterScene(driver));
                stage.setTitle("Inscription Conducteur");
                stage.show();
            } catch (Exception e) {
                System.err.println("Failed to start the application: " + e.getMessage());
            }
        }
        public static void main(String[] args) {
            launch(args);
        }
    }


