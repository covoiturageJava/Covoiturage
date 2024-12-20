package com.example.carpoolingapp.microservices.auth.view;

import com.example.carpoolingapp.microservices.Admin.View.MainAdmin;
import com.example.carpoolingapp.microservices.Drivers.view.HomeSimpleDriver;
import com.example.carpoolingapp.microservices.Drivers.view.homeProfileDriver;
import com.example.carpoolingapp.microservices.User.view.HomePage;
import com.example.carpoolingapp.microservices.auth.controller.LoginController;
import com.example.carpoolingapp.microservices.auth.serveur.SocClient;
import com.example.carpoolingapp.microservices.auth.controller.driverController;
import com.example.carpoolingapp.microservices.auth.controller.userController;
import com.example.carpoolingapp.model.Driver;
import com.example.carpoolingapp.model.User;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.SessionDriver;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Node;
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

  signUpButton.setOnAction(event -> showRegistrationSelection(stage));

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
    Object response = null;

    try {
     // Essayer de se connecter en tant que DRIVER
     response = socClient.connectAsDriver(identifier, password);
    } catch (Exception e) {
     // Gérer l'exception et afficher une alerte à l'utilisateur
     System.err.println("UI: An error occurred while attempting to login as DRIVER.");
     alert.setContentText("An unexpected error occurred. Please try again.");
     alert.show();
     autoCloseAlert(alert, 4);
     return; // Arrêter le processus de connexion en cas d'erreur
    }

    // Vérification si la réponse est null
    if (response == null) {
     System.out.println("UI: DRIVER login failed. Response is null.");
     alert.setContentText("Driver login failed. Please check your credentials.");
     alert.show();
     autoCloseAlert(alert, 4);
     return; // Arrêter le processus car la connexion a échoué
    }

    // Vérification si la réponse est une instance de SessionDriver
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

     try {
      // Charger la vue du conducteur
      driverView.start(newStage, sessionDriver);
     } catch (Exception e) {
      // Gérer une erreur lors du chargement de la vue
      System.err.println("UI: Error occurred while opening the DRIVER view.");
      alert.setContentText("An error occurred while loading the driver dashboard.");
      alert.show();
      autoCloseAlert(alert, 4);
     }
    } else {
     // Cas inattendu : réponse non nulle mais pas une instance de SessionDriver
     System.err.println("UI: Unexpected response type for DRIVER login.");
     alert.setContentText("An unexpected error occurred. Please try again.");
     alert.show();
     autoCloseAlert(alert, 4);
    }
   }
   else {
    System.out.println("UI: Attempting to login as " + userType + ".");
    boolean isAuthenticated = socClient.connectAsUserOrAdmin(identifier, password, userType);
    if (isAuthenticated) {
     if ("User".equals(userType)) {
      User user = loginController.getUser(identifier);
      HomePage testPage = new HomePage(user);
      testPage.show(stage);
     }
     else if ("Admin".equals(userType)) {
      try {
       new MainAdmin().start(new Stage());
       ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
      } catch (Exception e) {
       e.printStackTrace();
      }
     }
    }
    else {
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

 private void showRegistrationSelection(Stage stage) {
  AnchorPane selectionPane = new AnchorPane();
  selectionPane.setPrefSize(400, 300);

  Label titleLabel = new Label("Choose Registration Type");
  titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
  titleLabel.setLayoutX(80);
  titleLabel.setLayoutY(50);

  Button driverButton = new Button("Driver Registration");
  driverButton.setLayoutX(100);
  driverButton.setLayoutY(120);
  driverButton.setPrefWidth(200);
  driverButton.setStyle("-fx-background-color: #1ec9cf;");
  driverButton.setTextFill(Color.WHITE);
  driverButton.setFont(Font.font("System", FontWeight.BOLD, 13));

  Button userButton = new Button("Normal User Registration");
  userButton.setLayoutX(100);
  userButton.setLayoutY(180);
  userButton.setPrefWidth(200);
  userButton.setStyle("-fx-background-color: #1ec9cf;");
  userButton.setTextFill(Color.WHITE);
  userButton.setFont(Font.font("System", FontWeight.BOLD, 13));

  selectionPane.getChildren().addAll(titleLabel, driverButton, userButton);

  Scene selectionScene = new Scene(selectionPane, 400, 300);
  stage.setScene(selectionScene);

  driverButton.setOnAction(event -> {
   driverController driverCtrl = new driverController();
   driverregister driverRegister = new driverregister();
   Scene driverScene = driverRegister.getDriverRegisterScene(new Driver());
   stage.setScene(driverScene);
  });

  userButton.setOnAction(event -> {
   userController userCtrl = new userController();
   new userregister(stage, userCtrl, loginController);
  });
 }


}
