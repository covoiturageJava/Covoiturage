package com.example.carpoolingapp.microservices.Admin.View;

import com.example.carpoolingapp.microservices.Admin.Controller.AdminController;
import com.example.carpoolingapp.microservices.Admin.Controller.EmailSender;
import com.example.carpoolingapp.model.DatabaseInitializer;
import jakarta.mail.MessagingException;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfirmRequests extends Application {

    private ObservableList<Image> images;
    private int driverId;

    public ConfirmRequests(ObservableList<Image> images, int driverId) {
        this.images = images;
        this.driverId = driverId;
    }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(931, 695);
        root.setStyle("-fx-background-image: url('img_2.png')");

        VBox vBox = new VBox(10);
        vBox.setLayoutX(14);
        vBox.setLayoutY(148);
        vBox.setPrefSize(902, 466);

        if (images != null && !images.isEmpty()) {
            for (Image image : images) {
                if (image != null) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(400);
                    imageView.setFitHeight(300);
                    imageView.setPreserveRatio(true);
                    vBox.getChildren().add(imageView);
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setLayoutX(14);
        scrollPane.setLayoutY(148);
        scrollPane.setPrefSize(902, 466);

        Button confirmButton = new Button("Confirm Request");
        confirmButton.setLayoutX(559);
        confirmButton.setLayoutY(633);
        confirmButton.setPrefSize(150, 35);
        confirmButton.setFont(new Font(14));
        confirmButton.setOnAction(event ->{confirmRequest();
            AdminController adminController = new AdminController(primaryStage);
            adminController.showManageRequestsView();
            Node source = (Node) event.getSource();
            if (source != null) {
                Scene scene = source.getScene();
                if (scene != null) {
                    Window window = scene.getWindow();
                    if (window != null && window instanceof Stage) {
                        ((Stage) window).close();
                    }
                }
            }
        });
        Button return1 = new Button("Return");
        return1.setLayoutX(381);
        return1.setLayoutY(633);
        return1.setPrefSize(150, 35);
        return1.setFont(new Font(14));
        return1.setOnAction(event -> {
            AdminController adminController = new AdminController(primaryStage);
            adminController.showManageRequestsView();
            Node source = (Node) event.getSource();
            if (source != null) {
                Scene scene = source.getScene();
                if (scene != null) {
                    Window window = scene.getWindow();
                    if (window != null && window instanceof Stage) {
                        ((Stage) window).close();
                    }
                }
            }
        });

        Button rejectButton = new Button("Reject Request");
        rejectButton.setLayoutX(737);
        rejectButton.setLayoutY(633);
        rejectButton.setPrefSize(150, 35);
        rejectButton.setFont(new Font(14));
        rejectButton.setOnAction(event ->{rejectRequest();
                    AdminController adminController = new AdminController(primaryStage);
                    adminController.showManageRequestsView();
                    Node source = (Node) event.getSource();
                    if (source != null) {
                        Scene scene = source.getScene();
                        if (scene != null) {
                            Window window = scene.getWindow();
                            if (window != null && window instanceof Stage) {
                                ((Stage) window).close();
                            }
                        }
                    }
                }
            );
        ImageView protectionImageView = new ImageView();
        Image protectionImage = new Image(getClass().getResourceAsStream("/protection.png"));
        protectionImageView.setImage(protectionImage);
        protectionImageView.setFitHeight(128);
        protectionImageView.setFitWidth(150);
        protectionImageView.setLayoutX(737);
        protectionImageView.setLayoutY(14);
        protectionImageView.setPreserveRatio(true);
        protectionImageView.setPickOnBounds(true);

        Label label = new Label("Request Confirmation");
        label.setLayoutX(45);
        label.setLayoutY(51);
        label.setPrefSize(537, 50);
        label.setFont(new Font(36));

        root.getChildren().addAll(scrollPane,return1, confirmButton, rejectButton, protectionImageView, label);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Confirm Page");
        primaryStage.show();
    }

    private void confirmRequest() {
        String query = "UPDATE Drivers SET state = 2 WHERE id = ?";
        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            DatabaseInitializer.selectDatabase(conn);
            pstmt.setInt(1, driverId);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                String emailQuery = "SELECT email FROM Drivers WHERE id = ?";
                try (PreparedStatement emailStmt = conn.prepareStatement(emailQuery)) {
                    emailStmt.setInt(1, driverId);
                    try (ResultSet rs = emailStmt.executeQuery()) {
                        if (rs.next()) {
                            String email = rs.getString("email");
                            String subject = "Request Approved";
                            String body = "Congratulations, your driver account has just been confirmed.\n" +
                                    "\n" +
                                    "Before starting, ensure you have the following installed: Docker and nodejs (On your desktop), ExpoGo (On your mobile device)\n" +
                                    "\n" +
                                    "1- Run the following commands to pull the app to your desktop:\n" +
                                    "\n" +
                                    "\tgit clone https://github.com/covoiturageJava/carpoolingMobile.git\n" +
                                    "\n" +
                                    "- Or download it via the link : https://github.com/covoiturageJava/carpoolingMobile/archive/refs/heads/main.zip\n" +
                                    "\n" +
                                    "2- Navigate to the directories /carpoolingMobile/backend and run : npm i\n" +
                                    "\n" +
                                    "3- Navigate to the directories /carpoolingMobile/carpoolingMobile and run : npm i\n" +
                                    "\n" +
                                    "4- Navigate to /carpoolingMobile/carpoolingMobile/App.js and edit the \"serverIpAddress\" with your ip address\n" +
                                    "\n" +
                                    "5- Go back to the root of the app /carpoolingMobile, where the docker-compose.yml is located\n" +
                                    "\n" +
                                    "6- Edit the \"REACT_NATIVE_PACKAGER_HOSTNAME\" (line 29) environment to hold your ip address\n" +
                                    "\n" +
                                    "7- run: docker-compose up --build (this may take some time)\n" +
                                    "\n" +
                                    "8- Access the Application: Visit ExpoGo app on your mobile, and scan the QR code.";
                            EmailSender.sendEmail(email, subject, body);
                            showAlert("Success", "Driver state updated to 'Confirmed' and email sent.", Alert.AlertType.INFORMATION);
                        }
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                showAlert("Error", "Driver not found or unable to update state.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the driver's state.", Alert.AlertType.ERROR);
        }
    }


    private void rejectRequest() {
        String query = "DELETE FROM Drivers WHERE id = ?";
        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            DatabaseInitializer.selectDatabase(conn);
            pstmt.setInt(1, driverId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                showAlert("Success", "Driver has been rejected and deleted from the database.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Driver not found or unable to delete.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting the driver.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
