package com.example.carpoolingapp.view;
import com.example.carpoolingapp.model.drivers;
import javafx.application.Application;
import javafx.stage.Stage;
    public class mainregdriver extends Application {
        private drivers driver = new drivers();

        @Override
        public void start(Stage stage) {
            driverregister driverRegisterView = new driverregister();
            stage.setScene(driverRegisterView.getDriverRegisterScene(driver));
            stage.setTitle("Inscription Conducteur");
            stage.show();
        }
        public static void main(String[] args) {
            launch(args);
        }
    }


