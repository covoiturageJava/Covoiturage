package com.example.carpoolingapp.microservices.User.view;

import com.example.carpoolingapp.microservices.Drivers.view.ProfileDriver;
import com.example.carpoolingapp.microservices.auth.controller.LoginController;
import com.example.carpoolingapp.microservices.auth.view.Login;
import com.example.carpoolingapp.model.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URISyntaxException;

public class HomePage {
    private User user;
    public HomePage( User user) {
        this.user = user;
    }
    public void show(Stage stage) {
        Button btnHistorique = new Button("Voir Historique des Trajets");
        Button btnDemanderTrajet = new Button("Demander un Trajet");
        Button btnLogout = new Button("Logout");
        String buttonStyle = "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #3b5998; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 5px;";
        btnHistorique.setStyle(buttonStyle);
        btnHistorique.setOnAction(e -> {
            try {
                Stage currentStage = (Stage) btnDemanderTrajet.getScene().getWindow();
                currentStage.close();
                Stage profileStage = new Stage();
                profileUser profileDriver = new profileUser();
                profileDriver.start(profileStage, user);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnDemanderTrajet.setStyle(buttonStyle);
        btnLogout.setStyle(buttonStyle);
        btnDemanderTrajet.setOnAction(event -> {
            SelectTrajet trajet = new SelectTrajet(stage, user.getId(),user);
            try {
                trajet.show(stage);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
        btnLogout.setOnAction(e -> {
            try {
                Stage currentStage = (Stage) btnLogout.getScene().getWindow();
                currentStage.close();
                LoginController loginController = new LoginController();
                Stage loginStage = new Stage();
                Login loginDriver = new Login(loginController);
                loginDriver.show(loginStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        VBox root = new VBox(15);
        root.getChildren().addAll(btnHistorique, btnDemanderTrajet, btnLogout);
        root.setStyle("-fx-alignment: center; -fx-padding: 50px; -fx-background-color: #f4f4f4;");
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Page d'Accueil");
        stage.show();
    }
}

