package com.example.carpoolingapp.microservices.Admin.Controller;

import com.example.carpoolingapp.microservices.Admin.View.DriversUsersView;
import com.example.carpoolingapp.microservices.Admin.View.ManageRequests;
import javafx.stage.Stage;

public class AdminController {
    private Stage primaryStage;

    public AdminController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showDriversUsersView() {
        DriversUsersView driversUsersView = new DriversUsersView();
        try {
            driversUsersView.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showManageRequestsView() {
        ManageRequests manageRequests = new ManageRequests();
        try {
            manageRequests.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
