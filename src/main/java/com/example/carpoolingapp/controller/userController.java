package com.example.carpoolingapp.controller;
import com.example.carpoolingapp.model.users;
import com.example.carpoolingapp.view.userregister;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
public class userController extends Application {
    private users userModel;
    private userregister userView;
    @Override
    public void start(Stage stage) {
        userModel = new users();
        userView = new userregister(stage, this); // Pass controller to the view
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
            userModel.setPhone(phone);
            userModel.setFirstName(firstName);
            userModel.setLastName(lastName);
            userModel.setDateOfBirth(dateOfBirth);
            userModel.setPassword(password);
            userModel.setDateOfCreation(java.time.LocalDate.now().toString());
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
    public static void main(String[] args) {
        launch(args);
    }
}
