package com.example.carpoolingapp.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class userregister {

    // Déclaration des champs de saisie pour les informations utilisateur
    private TextField usernameinput;
    private TextField emailinput;
    private TextField numtelephoneinput;
    private TextField nominput;
    private TextField prenominput;
    private DatePicker dateinput; // Utilisation d'un DatePicker pour la date de naissance
    private PasswordField passwodinput;
    private PasswordField confirminput;
    private Button submit;

    // Constructeur pour initialiser l'interface d'inscription
    public userregister(Stage stage) {
        // Création d'un conteneur GridPane avec marges et alignement
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20)); // Marges autour du conteneur
        gridPane.setVgap(15); // Espacement vertical entre les lignes
        gridPane.setHgap(10); // Espacement horizontal entre les colonnes
        gridPane.setAlignment(Pos.CENTER); // Centrer le contenu dans la fenêtre
        gridPane.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ddd; -fx-border-width: 2px;");

        // Création des champs avec des icônes et ajout au GridPane
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

        // Utilisation d'un DatePicker pour la date de naissance
        gridPane.add(createLabelWithIcon("Date de naissance:", "date.png"), 0, 5);
        dateinput = new DatePicker(); // Champ de sélection de date
        dateinput.setPromptText("JJ/MM/AAAA"); // Texte indicatif
        gridPane.add(dateinput, 1, 5);

        // Champ de saisie pour le mot de passe
        gridPane.add(createLabelWithIcon("Mot de passe:", "password.png"), 0, 6);
        passwodinput = new PasswordField();
        passwodinput.setPromptText("Entrez votre mot de passe");
        gridPane.add(passwodinput, 1, 6);

        // Champ de saisie pour confirmer le mot de passe
        gridPane.add(createLabelWithIcon("Confirmez le mot de passe:", "confirm.png"), 0, 7);
        confirminput = new PasswordField();
        confirminput.setPromptText("Confirmez votre mot de passe");
        gridPane.add(confirminput, 1, 7);

        // Création du bouton d'inscription
        submit = new Button("S'inscrire");
        submit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14;");
        submit.setOnAction(event -> handleRegistration()); // Action à exécuter lors du clic
        gridPane.add(submit, 1, 8);

        // Création et affichage de la scène
        Scene scene = new Scene(gridPane, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Formulaire d'inscription");
        stage.show();
    }

    // Méthode pour créer un label avec une icône
    private Label createLabelWithIcon(String text, String iconName) {
        Label label = new Label(text); // Création du label avec le texte
        String iconPath = "icons/" + iconName; // Chemin vers l'icône

        try {
            // Chargement de l'image depuis le chemin spécifié
            Image iconImage = new Image(getClass().getResource(iconPath).toExternalForm());
            ImageView icon = new ImageView(iconImage);
            icon.setFitWidth(20); // Largeur de l'icône
            icon.setFitHeight(20); // Hauteur de l'icône
            label.setGraphic(icon); // Ajout de l'icône au label
        } catch (NullPointerException e) {
            System.out.println("Icône non trouvée pour: " + iconPath);
        }

        // Style du label
        label.setStyle("-fx-text-fill: #333; -fx-font-weight: bold; -fx-font-size: 14;");
        return label;
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        return textField;
    }
    private void handleRegistration() {
        // Vérifie si les mots de passe correspondent
        if (!passwodinput.getText().equals(confirminput.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas");
            return;
        }
        // Affichage d'un message de succès si l'inscription est réussie
        System.out.println("Inscription réussie pour: " + usernameinput.getText());
        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Inscription réussie !");
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType); // Création d'une alerte
        alert.setTitle(title); // Titre de l'alerte
        alert.setHeaderText(null); // Pas d'en-tête
        alert.setContentText(message); // Contenu de l'alerte
        alert.showAndWait(); // Afficher l'alerte et attendre une interaction
    }

}
