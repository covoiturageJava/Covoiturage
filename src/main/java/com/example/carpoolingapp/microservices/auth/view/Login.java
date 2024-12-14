package com.example.carpoolingapp.microservices.auth.view;

import com.example.carpoolingapp.microservices.Drivers.view.HomeSimpleDriver;
import com.example.carpoolingapp.microservices.Drivers.view.homeProfileDriver;
import com.example.carpoolingapp.microservices.auth.controller.LoginController;
import com.example.carpoolingapp.microservices.auth.serveur.SocClient;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.SessionDriver;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

import java.sql.SQLException;

public class Login {

 private final LoginController loginController;
 SocClient socClient = new SocClient();

 public Login(LoginController loginController, SocClient socClient) {
  this.loginController = loginController;
  this.socClient = socClient;
 }
 public Login(LoginController loginController) {
  this.loginController = loginController;
 }
 public void show(Stage stage) {
  AnchorPane leftPane = new AnchorPane();
  leftPane.setPrefSize(294, 500);
  leftPane.setStyle("-fx-background-color: #0598ff;");

  Text appTitle = new Text("Covoiturage App");
  appTitle.setFill(Color.WHITE);
  appTitle.setFont(Font.font("Chiller", 36));
  appTitle.setLayoutX(63);
  appTitle.setLayoutY(228);
  leftPane.getChildren().add(appTitle);

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

  CheckBox adminCheckbox = new CheckBox("Admin");
  adminCheckbox.setLayoutX(108);
  adminCheckbox.setLayoutY(200);

  CheckBox driverCheckbox = new CheckBox("Driver");
  driverCheckbox.setLayoutX(180);
  driverCheckbox.setLayoutY(200);

  CheckBox normalUserCheckbox = new CheckBox("Normal User");
  normalUserCheckbox.setLayoutX(250);
  normalUserCheckbox.setLayoutY(200);

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
  mainLayout.setMinSize(624, 453);

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
    autoCloseAlert(alert, 3);
    return;
   }

   if (identifier.isEmpty()) {
    alert.setContentText("Please enter your email or username.");
    alert.show();
    autoCloseAlert(alert, 3);
    return;
   }

   if (password.isEmpty()) {
    alert.setContentText("Please enter your password.");
    alert.show();
    autoCloseAlert(alert, 3);
    return;
   }

   if ("Driver".equals(userType)) {
    System.out.println("UI: Attempting to login as DRIVER.");
    Object response = socClient.connectAsDriver(identifier, password);
    if (response instanceof SessionDriver) {
     System.out.println("UI: DRIVER login successful.");
     alert.setContentText("Driver login successful!");
     alert.show();
     autoCloseAlert(alert, 4);
     SessionDriver sessionDriver = (SessionDriver) response;
     Stage currentStage = (Stage) loginB.getScene().getWindow();
     currentStage.close();
     HomeSimpleDriver driverView = new HomeSimpleDriver();
     Stage newStage = new Stage();
     driverView.start(newStage, sessionDriver);
    } else {
     System.out.println("UI: DRIVER login failed.");
     alert.setContentText("Login failed for Driver. Please check your credentials.");
     alert.show();
     autoCloseAlert(alert, 3);
    }
   } else {
    System.out.println("UI: Attempting to login as " + userType + ".");
    boolean isAuthenticated = socClient.connectAsUserOrAdmin(identifier, password, userType);
    if (isAuthenticated) {
     System.out.println("UI: " + userType + " login successful.");
     alert.setContentText(userType + " login successful!");
     alert.show();
     autoCloseAlert(alert, 3);
     Stage currentStage = (Stage) loginB.getScene().getWindow();
     currentStage.close();
    } else {
     System.out.println("UI: " + userType + " login failed.");
     alert.setContentText("Login failed for " + userType + ". Please check your credentials.");
     alert.show();
     autoCloseAlert(alert, 3); 
    }
   }
  });

 }
 private void autoCloseAlert(Alert alert, int seconds) {
  PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
  delay.setOnFinished(e -> alert.close());
  delay.play();
 }
}