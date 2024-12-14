package com.example.carpoolingapp.microservices.Admin.View;

import com.example.carpoolingapp.microservices.Admin.Controller.EmailSender;
import com.example.carpoolingapp.model.DatabaseInitializer;
import jakarta.mail.MessagingException;
import javafx.application.Application;
import javafx.collections.ObservableList;
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
        confirmButton.setOnAction(event ->confirmRequest());

        Button rejectButton = new Button("Reject Request");
        rejectButton.setLayoutX(737);
        rejectButton.setLayoutY(633);
        rejectButton.setPrefSize(150, 35);
        rejectButton.setFont(new Font(14));
        rejectButton.setOnAction(event ->
            rejectRequest());

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

        root.getChildren().addAll(scrollPane, confirmButton, rejectButton, protectionImageView, label);

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
                                    "Before starting, ensure you have the following installed: Docker (On your desktop), ExpoGo (On your mobile device)\n" +
                                    "\n" +
                                    "1- Run the following commands to pull the required Docker images from Docker Hub:\n" +
                                    "\n" +
                                    "docker pull mehkad/frontend:latest\n" +
                                    "docker pull mehkad/backend:latest\n" +
                                    "docker pull mehkad/database:latest\n" +
                                    "\n" +
                                    "2- Create a docker-compose.yml file in your project directory and paste the following configuration:\n" +
                                    "\n" +
                                    "version: '3.8'\n" +
                                    "\n" +
                                    "services:\n" +
                                    "  backend:\n" +
                                    "    image: mehkad/backend:latest\n" +
                                    "    ports:\n" +
                                    "      - \"6666:6666\"\n" +
                                    "    environment:\n" +
                                    "      - DB_HOST=carpooling-db\n" +
                                    "      - DB_PORT=3306\n" +
                                    "      - DB_USER=adminuser\n" +
                                    "      - DB_PASSWORD=azerty123$$\n" +
                                    "      - DB_NAME=carpooling\n" +
                                    "    depends_on:\n" +
                                    "      - database\n" +
                                    "\n" +
                                    "  frontend:\n" +
                                    "    image: mehkad/frontend:latest\n" +
                                    "    ports:\n" +
                                    "      - \"8081:8081\"\n" +
                                    "      - \"19000:19000\"\n" +
                                    "      - \"19001:19001\"\n" +
                                    "      - \"19002:19002\"\n" +
                                    "      - \"19006:19006\"\n" +
                                    "    environment:\n" +
                                    "      - CHOKIDAR_USEPOLLING=true\n" +
                                    "      - REACT_NATIVE_PACKAGER_HOSTNAME=${SERVER_IP_ADDRESS}\n" +
                                    "    depends_on:\n" +
                                    "      - backend\n" +
                                    "\n" +
                                    "  database:\n" +
                                    "    image: mehkad/database:latest\n" +
                                    "    container_name: carpooling-db\n" +
                                    "    ports:\n" +
                                    "      - \"3306:3306\"\n" +
                                    "    environment:\n" +
                                    "      MYSQL_ROOT_PASSWORD: azerty123$$\n" +
                                    "      MYSQL_DATABASE=carpooling\n" +
                                    "      MYSQL_USER=adminuser\n" +
                                    "      MYSQL_PASSWORD=azerty123$$\n" +
                                    "\n" +
                                    "3- Before running the application, set the SERVER_IP_ADDRESS environment variable to your local machine's IP address. Use the following command based on your operating system:\n" +
                                    "\n" +
                                    "- For Windows (PowerShell): $env:SERVER_IP_ADDRESS=\"<Your-IP-Address>\"\n" +
                                    "- For Windows (CMD): env: SERVER_IP_ADDRESS=\"<Your-IP-Address>\"\n" +
                                    "- For Linux/Mac: export SERVER_IP_ADDRESS=\"<Your-IP-Address>\"\n" +
                                    "\n" +
                                    "Replace <Your-IP-Address> with your actual IP address.\n" +
                                    "\n" +
                                    "4- To start all the services, navigate to the directory containing the docker-compose.yml file and run: docker-compose up\n" +
                                    "\n" +
                                    "5- Access the Application: Visit ExpoGo app on your mobile, and scan the QR code.";
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
