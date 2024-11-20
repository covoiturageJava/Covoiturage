package com.example.carpoolingapp.view;
import com.example.carpoolingapp.controller.userController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
    private userController controller;
    public userregister(Stage stage, userController controller) {
        this.controller = controller;
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ddd; -fx-border-width: 2px;");

        gridPane.add(createLabel("Nom d'utilisateur:"), 0, 0);
        usernameinput = createTextField("Entrez votre nom d'utilisateur");
        gridPane.add(usernameinput, 1, 0);

        gridPane.add(createLabel("Email:"), 0, 1);
        emailinput = createTextField("Entrez votre email");
        gridPane.add(emailinput, 1, 1);

        gridPane.add(createLabel("Numéro de téléphone:"), 0, 2);
        numtelephoneinput = createTextField("Entrez votre numéro de téléphone");
        gridPane.add(numtelephoneinput, 1, 2);

        gridPane.add(createLabel("Nom:"), 0, 3);
        nominput = createTextField("Entrez votre nom");
        gridPane.add(nominput, 1, 3);

        gridPane.add(createLabel("Prénom:"), 0, 4);
        prenominput = createTextField("Entrez votre prénom");
        gridPane.add(prenominput, 1, 4);

        gridPane.add(createLabel("Date de naissance:"), 0, 5);
        dateinput = createTextField("JJ/MM/AAAA");
        gridPane.add(dateinput, 1, 5);

        gridPane.add(createLabel("Mot de passe:"), 0, 6);
        passwodinput = new PasswordField();
        passwodinput.setPromptText("Entrez votre mot de passe");
        gridPane.add(passwodinput, 1, 6);

        gridPane.add(createLabel("Confirmez le mot de passe:"), 0, 7);
        confirminput = new PasswordField();
        confirminput.setPromptText("Confirmez votre mot de passe");
        gridPane.add(confirminput, 1, 7);

        submit = new Button("S'inscrire");
        submit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14;");
        submit.setOnAction(event -> controller.handleRegistration());
        gridPane.add(submit, 1, 8);

        Scene scene = new Scene(gridPane, 700, 600);
        stage.setScene(scene);
        stage.setTitle("Formulaire d'inscription");
        stage.show();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #333; -fx-font-weight: bold; -fx-font-size: 14;");
        return label;
    }
    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setPrefWidth(300);
        textField.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        return textField;
    }
    public TextField getUsernameInput() {
        return usernameinput;
    }
    public TextField getEmailInput() {
        return emailinput;
    }
    public TextField getNumTelephoneInput() {
        return numtelephoneinput;
    }

    public TextField getNomInput() {
        return nominput;
    }

    public TextField getPrenomInput() {
        return prenominput;
    }

    public TextField getDateInput() {
        return dateinput;
    }

    public PasswordField getPasswordInput() {
        return passwodinput;
    }

    public PasswordField getConfirmInput() {
        return confirminput;
    }
}