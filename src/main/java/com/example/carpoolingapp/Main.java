package com.example.carpoolingapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create the login screen
        VBox loginLayout = new VBox(10); // VBox with spacing between elements
        loginLayout.setStyle("-fx-padding: 20; -fx-alignment: center;"); // Add some styling

        Label titleLabel = new Label("Login");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));

        // Add all elements to the layout
        loginLayout.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton);

        // Set up the scene and stage
        Scene scene = new Scene(loginLayout, 300, 200);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Define the login handling logic
    private void handleLogin(String username, String password) {
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        // Add login validation logic here
    }

    public static void main(String[] args) {
        launch(args);
    }
}
