package com.example.carpoolingapp.view;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.Driver;
import javafx.application.Application;
import javafx.stage.Stage;
    public class mainregdriver extends Application {
        private Driver driver = new Driver();
        @Override
        public void start(Stage stage) {
            DatabaseInitializer.initializeDatabase();
            driverregister driverRegisterView = new driverregister();
            stage.setScene(driverRegisterView.getDriverRegisterScene(driver));
            stage.setTitle("Inscription Conducteur");
            stage.show();
        }
        public static void main(String[] args) {
            launch(args);
        }
    }


