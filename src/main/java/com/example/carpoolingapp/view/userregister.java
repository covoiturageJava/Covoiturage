package com.example.carpoolingapp.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class userregister {

    private TextField usernameinput;
    private TextField emailinput;
    private TextField numtelephoneinput;
    private TextField nominput;
    private TextField prenominput;
    private TextField dateinput;

    private PasswordField passwodinput;
    private PasswordField confirminput;
    private Button submit;

    public userregister(Stage stage) {
        // Configuration de la fenêtre et de la grille
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ddd; -fx-border-width: 2px;");

        // Création et positionnement des champs avec icônes
        gridPane.add(createLabelWithIcon("Nom d'utilisateur:", "username.png"), 0, 0);
        usernameinput = createTextField("Entrez votre nom d'utilisateur");
        gridPane.add(usernameinput, 1, 0);

        gridPane.add(createLabelWithIcon("Email:", "email.png"), 0, 1);
        emailinput = createTextField("Entrez votre email");
        gridPane.add(emailinput, 1, 1);

        gridPane.add(createLabelWithIcon("Numéro de téléphone:", "phone.png"), 0, 2);
        numtelephoneinput = createTextField("Entrez votre numéro de téléphone");
        gridPane.add(numtelephoneinput, 1, 2);

        gridPane.add(createLabelWithIcon("Nom:", "name.png"), 0, 3);
        nominput = createTextField("Entrez votre nom");
        gridPane.add(nominput, 1, 3);

        gridPane.add(createLabelWithIcon("Prénom:", "prenom.png"), 0, 4);
        prenominput = createTextField("Entrez votre prénom");
        gridPane.add(prenominput, 1, 4);

        gridPane.add(createLabelWithIcon("Date de naissance:", "date.png"), 0, 5);
        dateinput = createTextField("JJ/MM/AAAA");
        gridPane.add(dateinput, 1, 5);

        gridPane.add(createLabelWithIcon("Mot de passe:", "password.png"), 0, 6);
        passwodinput = new PasswordField();
        passwodinput.setPromptText("Entrez votre mot de passe");
        gridPane.add(passwodinput, 1, 6);

        gridPane.add(createLabelWithIcon("Confirmez le mot de passe:", "confirm.png"), 0, 7);
        confirminput = new PasswordField();
        confirminput.setPromptText("Confirmez votre mot de passe");
        gridPane.add(confirminput, 1, 7);

        // Bouton d'inscription
        submit = new Button("S'inscrire");
        submit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14;");
        submit.setOnAction(event -> handleRegistration());
        gridPane.add(submit, 1, 8);

        // Création et affichage de la scène
        Scene scene = new Scene(gridPane, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Formulaire d'inscription");
        stage.show();
    }

    // Création d'un label avec une icône
    private Label createLabelWithIcon(String text, String iconName) {
        Label label = new Label(text);
        String iconPath = "icons/" + iconName;

        try {
            Image iconImage = new Image(getClass().getResource(iconPath).toExternalForm());
            ImageView icon = new ImageView(iconImage);
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            label.setGraphic(icon);
        } catch (NullPointerException e) {
            System.out.println("Icône non trouvée pour: " + iconPath);
        }

        label.setStyle("-fx-text-fill: #333; -fx-font-weight: bold; -fx-font-size: 14;");
        return label;
    }

    // Création d'un champ de texte stylisé
    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        return textField;
    }

    // Méthode pour gérer l'inscription
    private void handleRegistration() {
        System.out.println("Inscription réussie pour: " + usernameinput.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Inscription réussie !");
        alert.showAndWait();
    }

//    public static void main(String[] args) {
//        Platform.startup(() -> {
//            Stage stage = new Stage();
//            new userregister(stage);
//        });
//    }
}
