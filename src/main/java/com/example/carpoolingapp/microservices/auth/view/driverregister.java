package com.example.carpoolingapp.microservices.auth.view;

import com.example.carpoolingapp.microservices.auth.controller.driverController;
import com.example.carpoolingapp.model.Driver;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.util.List;

public class driverregister  {
    driverController controller = new driverController();
    public Scene getDriverRegisterScene(Driver driver) {
        VBox rootContainer = new VBox();
        rootContainer.setSpacing(10);
        rootContainer.setPadding(new Insets(15));
        rootContainer.setAlignment(Pos.CENTER);
        rootContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #f1f8e9, #c8e6c9);");
        Label headerLabel = new Label("Inscription Conducteur");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        GridPane formGrid = new GridPane();
        formGrid.setVgap(8);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-border-color: #a5d6a7; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 15px; -fx-background-radius: 10px;");
        formGrid.add(createLabelWithIcon("Nom:", "name.png"), 0, 0);
        TextField firstNameField = createTextField("Entrez votre prénom");
        TextField lastNameField = createTextField("Entrez votre nom");
        HBox nameFields = new HBox(8, firstNameField, lastNameField);
        formGrid.add(nameFields, 1, 0);
        formGrid.add(createLabelWithIcon("Email:", "email.png"), 0, 1);
        TextField emailField = new TextField();
        emailField.setPromptText("Entrez votre email");
        emailField.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-border-color: #a5d6a7; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        formGrid.add(emailField, 1, 1);
        formGrid.add(createLabelWithIcon("Téléphone:", "phone.png"), 0, 2);
        TextField phoneField = new TextField();
        phoneField.setPromptText("Entrez votre numéro");
        phoneField.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-border-color: #a5d6a7; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        formGrid.add(phoneField, 1, 2);
        formGrid.add(createLabelWithIcon("Date de naissance:", "date.png"), 0, 3);
        DatePicker birthDateInput = new DatePicker();
        birthDateInput.setPromptText("JJ/MM/AAAA");
        birthDateInput.setStyle("-fx-border-color: #a5d6a7; -fx-border-radius: 5px;");
        formGrid.add(birthDateInput, 1, 3);
        formGrid.add(createLabelWithIcon("Mot de passe:", "password.png"), 0, 4);
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Entrez votre mot de passe");
        formGrid.add(passwordInput, 1, 4);

        formGrid.add(createLabelWithIcon("Confirmez:", "confirm.png"), 0, 5);
        PasswordField confirmInput = new PasswordField();
        confirmInput.setPromptText("Confirmez le mot de passe");
        formGrid.add(confirmInput, 1, 5);

        formGrid.add(createLabelWithIcon("Type de véhicule:", "vehicle.png"), 0, 6);
        ComboBox<String> vehicleTypeComboBox = new ComboBox<>();
        vehicleTypeComboBox.getItems().addAll("Voiture", "Moto");
        vehicleTypeComboBox.setPromptText("Choisissez le type");
        vehicleTypeComboBox.setStyle("-fx-border-color: #a5d6a7; -fx-border-radius: 5px;");
        formGrid.add(vehicleTypeComboBox, 1, 6);
        formGrid.add(createLabelWithIcon("Marque et Modèle:", "brand.png"), 0, 7);
        TextField brandField = createTextField("Marque");
        TextField modelField = createTextField("Modèle");
        HBox brandModelFields = new HBox(8, brandField, modelField);
        formGrid.add(brandModelFields, 1, 7);
        formGrid.add(createLabelWithIcon("Année:", "year.png"), 0, 8);
        TextField yearField = new TextField();
        yearField.setPromptText("Année du véhicule");
        yearField.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-border-color: #a5d6a7; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        formGrid.add(yearField, 1, 8);
        Button registerButton = new Button("S'inscrire");
        registerButton.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-size: 14; -fx-border-radius: 8px; -fx-padding: 8px 15px;");
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-background-color: #66bb6a; -fx-text-fill: white; -fx-font-size: 14; -fx-border-radius: 8px; -fx-padding: 8px 15px;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-size: 14; -fx-border-radius: 8px; -fx-padding: 8px 15px;"));
        formGrid.add(registerButton, 1, 9);
        registerButton.setOnAction(e -> {
            Driver localDriver = driver; // Copier la variable globale
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = passwordInput.getText();
            String confirmPassword = confirmInput.getText();
            String dateOfBirth = birthDateInput.getValue() != null ? birthDateInput.getValue().toString() : null;
            String typeVehicule = vehicleTypeComboBox.getValue();
            String marque = brandField.getText();
            String modele = modelField.getText();
            String annee = yearField.getText();
            if (!password.equals(confirmPassword)) {
                showError(List.of("Les mots de passe ne correspondent pas."));
                return;
            }
            List<String> errors = controller.validateFields(firstName, lastName, firstName + "." + lastName, email, password, phone, dateOfBirth, typeVehicule, marque, modele, annee);
            if (errors.isEmpty()) {
                localDriver = controller.saveStep1(localDriver, firstName, lastName, firstName + "." + lastName, email, password, phone, dateOfBirth, typeVehicule, marque, modele, annee);
                vehiculePic vehiculePicView = new vehiculePic();
                Stage nextStage = new Stage();
                vehiculePicView.show(nextStage, localDriver);
                ((Stage) registerButton.getScene().getWindow()).close();
            } else {
                showError(errors);
            }
        });

        rootContainer.getChildren().addAll(headerLabel, formGrid);
        return new Scene(rootContainer, 500, 600);
    }
    private Label createLabelWithIcon(String text, String iconName) {
        Label label = new Label(text);
//        String iconPath = "icons/" + iconName;
//        try {
//            Image iconImage = new Image(getClass().getResource(iconPath).toExternalForm());
//            ImageView icon = new ImageView(iconImage);
//            icon.setFitWidth(16);
//            icon.setFitHeight(16);
//            label.setGraphic(icon);
//        } catch (NullPointerException e) {
//            System.out.println("Icône non trouvée pour: " + iconPath);
//        }
        label.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 12;");
        return label;
    }
    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-border-color: #a5d6a7; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        return textField;
    }
    private void showError(List<String> errors) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de validation");
        alert.setHeaderText("Veuillez corriger les erreurs suivantes :");
        String errorMessage = String.join("\n", errors);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

}
