package com.example.carpoolingapp.view;

import com.example.carpoolingapp.Main;
import com.example.carpoolingapp.controller.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login {

 private final LoginController loginController;

 public Login(LoginController loginController) {
  this.loginController = loginController;
 }

 public void show(Stage stage) {
  // Left pane
  AnchorPane leftPane = new AnchorPane();
  leftPane.setPrefSize(294, 500);
  leftPane.setStyle("-fx-background-color: #0598ff;");

  Text appTitle = new Text("Covoiturage App");
  appTitle.setFill(Color.WHITE);
  appTitle.setFont(Font.font("Chiller", 36));
  appTitle.setLayoutX(63);
  appTitle.setLayoutY(228);
  leftPane.getChildren().add(appTitle);

  // Center pane
  AnchorPane centrePane = new AnchorPane();
  centrePane.setPrefSize(415, 500);

  TextField usernameF = new TextField();
  usernameF.setPromptText("Email or Username");
  usernameF.setLayoutX(107);
  usernameF.setLayoutY(101);
  usernameF.setPrefSize(192, 27);
  usernameF.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0px 0px 2px 0px;");

  PasswordField passwordField = new PasswordField();
  passwordField.setPromptText("Password");
  passwordField.setLayoutX(108);
  passwordField.setLayoutY(161);
  passwordField.setPrefSize(193, 27);
  passwordField.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0px 0px 2px 0px;");

  // Checkboxes for user roles
  CheckBox adminCheckbox = new CheckBox("Admin");
  adminCheckbox.setLayoutX(108);
  adminCheckbox.setLayoutY(200);

  CheckBox driverCheckbox = new CheckBox("Driver");
  driverCheckbox.setLayoutX(180);
  driverCheckbox.setLayoutY(200);

  CheckBox normalUserCheckbox = new CheckBox("Normal User");
  normalUserCheckbox.setLayoutX(250);
  normalUserCheckbox.setLayoutY(200);

  // Handle mutual exclusivity manually
  adminCheckbox.setOnAction(event -> {
   if (adminCheckbox.isSelected()) {
    driverCheckbox.setSelected(false);
    normalUserCheckbox.setSelected(false);
   }
  });

  driverCheckbox.setOnAction(event -> {
   if (driverCheckbox.isSelected()) {
    adminCheckbox.setSelected(false);
    normalUserCheckbox.setSelected(false);
   }
  });

  normalUserCheckbox.setOnAction(event -> {
   if (normalUserCheckbox.isSelected()) {
    adminCheckbox.setSelected(false);
    driverCheckbox.setSelected(false);
   }
  });

  Button loginB = new Button("Login");
  loginB.setLayoutX(100);
  loginB.setLayoutY(260);
  loginB.setPrefSize(205, 29);
  loginB.setStyle("-fx-background-color: #191b9d;");
  loginB.setTextFill(Color.WHITE);
  loginB.setFont(Font.font("System", FontWeight.BOLD, 13));

  Text userLoginText = new Text("User Login");
  userLoginText.setFill(Color.web("#191b9d"));
  userLoginText.setFont(Font.font("System", FontWeight.BOLD, 20));
  userLoginText.setLayoutX(152);
  userLoginText.setLayoutY(67);

  Text usernameText = new Text("Email/Username");
  usernameText.setLayoutX(26);
  usernameText.setLayoutY(119);

  Text passwordText = new Text("Password");
  passwordText.setLayoutX(30);
  passwordText.setLayoutY(179);

  Button signUpButton = new Button("Sign Up");
  signUpButton.setLayoutX(99);
  signUpButton.setLayoutY(310);
  signUpButton.setPrefSize(205, 29);
  signUpButton.setStyle("-fx-background-color: #1ec9cf;");
  signUpButton.setTextFill(Color.WHITE);
  signUpButton.setFont(Font.font("System", FontWeight.BOLD, 13));

  centrePane.getChildren().addAll(usernameF, passwordField, loginB, usernameText, userLoginText, adminCheckbox, driverCheckbox, normalUserCheckbox, signUpButton, passwordText);

  BorderPane mainLayout = new BorderPane();
  mainLayout.setLeft(leftPane);
  mainLayout.setCenter(centrePane);

  Scene scene = new Scene(mainLayout, 700, 500);
  stage.setScene(scene);
  stage.setTitle("Login - Carpooling App");
  stage.show();

  loginB.setOnAction(event -> {
   Alert alert = new Alert(Alert.AlertType.INFORMATION);
   String identifier = usernameF.getText();
   String password = passwordField.getText();
   String userType = null;

   if (adminCheckbox.isSelected()) {
    userType = "Admin";
   } else if (driverCheckbox.isSelected()) {
    userType = "Driver";
   } else if (normalUserCheckbox.isSelected()) {
    userType = "User";
   }

   if (userType == null) {
    alert.setContentText("Please select a user type.");
    alert.show();
    return;
   }

   if (identifier.isEmpty()) {
    alert.setContentText("Please enter your email or username.");
    alert.show();
    return;
   }

   boolean loginSuccess = loginController.login(identifier, password, userType);

   if (loginSuccess) {
    alert.setContentText("Login successful.");
    alert.show();
   } else {
    alert.setContentText("Invalid credentials.");
    alert.show();
   }
  });

 }
}
