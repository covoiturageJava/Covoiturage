package com.example.carpoolingapp.microservices.Admin.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ManageRequests extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create AnchorPane as the root layout
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-image: url('img_2.png')");
        root.setPrefSize(929.0, 686.0);

        // Create the first ImageView
        ImageView imageView1 = new ImageView();
        Image image = new Image(getClass().getResourceAsStream("/protection.png"));
        imageView1.setImage(image);
        imageView1.setFitHeight(162.0);
        imageView1.setFitWidth(250.0);
        imageView1.setLayoutX(665.0);
        imageView1.setLayoutY(14.0);
        imageView1.setPickOnBounds(true);
        imageView1.setPreserveRatio(true);

        // Create the TableView
        TableView<Object> tableView = new TableView<>();
        tableView.setLayoutX(14.0);
        tableView.setLayoutY(178.0);
        tableView.setPrefSize(630.0, 408.0);

        // Create TableColumns and add to TableView
        TableColumn<Object, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setPrefWidth(130.4);

        TableColumn<Object, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setPrefWidth(134.4);

        TableColumn<Object, String> colEmail = new TableColumn<>("Email");
        colEmail.setPrefWidth(221.6);

        TableColumn<Object, String> colPhoneNumber = new TableColumn<>("Phone Number");
        colPhoneNumber.setPrefWidth(145.6);

        tableView.getColumns().addAll(colFirstName, colLastName, colEmail, colPhoneNumber);

        // Create the second ImageView
        ImageView imageView2 = new ImageView();
        imageView2.setFitHeight(150.0);
        imageView2.setFitWidth(630.0);
        imageView2.setLayoutX(14.0);
        imageView2.setLayoutY(14.0);
        imageView2.setPickOnBounds(true);
        imageView2.setPreserveRatio(true);

        // Create the Button
        Button button = new Button("See Driver To Confirm");
        button.setLayoutX(696.0);
        button.setLayoutY(343.0);
        button.setPrefSize(178.0, 47.0);
        button.setStyle("-fx-background-radius: 10;");
        button.setFont(Font.font("System Bold", 14.0));

        // Add all children to the AnchorPane
        root.getChildren().addAll(imageView1, tableView, imageView2, button);

        // Create the Scene and set it on the Stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Confirm Page");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
