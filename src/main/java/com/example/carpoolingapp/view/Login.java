package com.example.carpoolingapp.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login extends Application {

 @Override
 public void start(Stage primaryStage) {
  //left
  AnchorPane leftPane = new AnchorPane();
  leftPane.setPrefSize(294, 500);
  leftPane.setStyle("-fx-background-color: #0598ff;");

  Text appTitle = new Text("Covoiturage app");
  appTitle.setFill(Color.WHITE);
  appTitle.setFont(Font.font("chiller",36));
  appTitle.setLayoutX(63);
  appTitle.setLayoutY(228);
  leftPane.getChildren().add(appTitle);

  //centre
  AnchorPane centrePane = new AnchorPane();
  centrePane.setPrefSize(415,500);
  TextField usernameF = new TextField();
  usernameF.setPromptText("User name");
  usernameF.setLayoutX(107);
  usernameF.setLayoutY(101);
  usernameF.setPrefSize(192,27);
  usernameF.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0px 0px 2px 0px;");

  PasswordField passwordField = new PasswordField();
  passwordField.setPromptText("Password");
  passwordField.setLayoutX(108);
  passwordField.setLayoutY(161);
  passwordField.setPrefSize(193,27);
  passwordField.setStyle("-fx-background-color: transparent; -fx-border-color: #0598ff; -fx-border-width: 0px 0px 2px 0px;");

  Button loginB = new Button("Login");
  loginB.setLayoutX(100);
  loginB.setLayoutY(235);
  loginB.setPrefSize(205,29);
  loginB.setStyle("-fx-background-color: #191b9d;");
  loginB.setTextFill(Color.WHITE);
  loginB.setFont(Font.font("System", FontWeight.BOLD, 13));

  Text userLoginText = new Text("User Login");
  userLoginText.setFill(Color.web("#191b9d"));
  userLoginText.setFont(Font.font("System", FontWeight.BOLD, 20));
  userLoginText.setLayoutX(152);
  userLoginText.setLayoutY(67);

  Text usernameText = new Text("User Name");
  usernameText.setLayoutX(26);
  usernameText.setLayoutY(119);

  Text passwordText = new Text("Password");
  passwordText.setLayoutX(30);
  passwordText.setLayoutY(179);

  Button singUpButton = new Button("Sign Up ");
  singUpButton.setLayoutX(99);
  singUpButton.setLayoutY(287);
  singUpButton.setPrefSize(205,29);
  singUpButton.setStyle("-fx-background-color: #1ec9cf;");
  singUpButton.setTextFill(Color.WHITE);
  singUpButton.setFont(Font.font("System", FontWeight.BOLD, 13));

  centrePane.getChildren().addAll(usernameF,passwordField,loginB,usernameText,userLoginText,singUpButton,passwordText);

  BorderPane mainLayout = new BorderPane();
  mainLayout.setLeft(leftPane);
  mainLayout.setCenter(centrePane);

  Scene scene = new Scene(mainLayout,700,500);
  primaryStage.setScene(scene);
  primaryStage.setTitle("Login - Carpooling app");
  primaryStage.show();





 }

}
