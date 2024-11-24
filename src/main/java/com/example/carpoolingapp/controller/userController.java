package com.example.carpoolingapp.controller;
import com.example.carpoolingapp.model.User;
import com.example.carpoolingapp.view.userregister;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import com.example.carpoolingapp.model.DatabaseInitializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class userController extends Application {
    private User userModel;
    private userregister userView;
    @Override
    public void start(Stage stage) {
        userModel = new User();
        userView = new userregister(stage, this);
    }
    public void handleRegistration() {
        try {
            String username = userView.getUsernameInput().getText();
            String email = userView.getEmailInput().getText();
            String phone = userView.getNumTelephoneInput().getText();
            String firstName = userView.getPrenomInput().getText();
            String lastName = userView.getNomInput().getText();
            String dateOfBirth = userView.getDateInput().getText();
            String password = userView.getPasswordInput().getText();
            String confirmPassword = userView.getConfirmInput().getText();

            if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || firstName.isEmpty()
                    || lastName.isEmpty() || dateOfBirth.isEmpty() || password.isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
                return;
            }
            if (!password.equals(confirmPassword)) {
                showAlert("Erreur", "Les mots de passe ne correspondent pas.", Alert.AlertType.ERROR);
                return;
            }
            userModel.setUsername(username);
            userModel.setEmail(email);
            userModel.setPhoneNumber(phone);
            userModel.setFirstName(firstName);
            userModel.setLastName(lastName);
            userModel.setBirthDate(dateOfBirth);
            userModel.setPassword(password);
            userModel.setCreationDate(java.time.LocalDate.now().toString());
            showAlert("Succès", "Inscription réussie pour: " + username, Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue s'est produite.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void saveUser(String username, String email, String phone, String firstName, String lastName, String birthDate, String password) {
        String insertQuery = "INSERT INTO Users (username, email, phoneNumber, firstName, lastName, birthDate, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection); // Select the database
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, phone);
                preparedStatement.setString(4, firstName);
                preparedStatement.setString(5, lastName);
                preparedStatement.setString(6, birthDate);
                preparedStatement.setString(7, password);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to register user.");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
