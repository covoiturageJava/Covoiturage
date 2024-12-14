package com.example.carpoolingapp.microservices.Admin.View;

import com.example.carpoolingapp.model.DatabaseInitializer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import java.util.Base64;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.sql.*;

public class ManageRequests extends Application {
    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-image: url('img_2.png')");
        root.setPrefSize(929.0, 686.0);

        ImageView imageView1 = new ImageView();
        Image image = new Image(getClass().getResourceAsStream("/protection.png"));
        imageView1.setImage(image);
        imageView1.setFitHeight(162.0);
        imageView1.setFitWidth(250.0);
        imageView1.setLayoutX(665.0);
        imageView1.setLayoutY(14.0);
        imageView1.setPickOnBounds(true);
        imageView1.setPreserveRatio(true);

        TableView<ObservableList<String>> tableView = new TableView<>();
        tableView.setLayoutX(14.0);
        tableView.setLayoutY(178.0);
        tableView.setPrefSize(630.0, 408.0);

        TableColumn<ObservableList<String>, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        colId.setPrefWidth(80.0);
        tableView.getColumns().add(colId);

        TableColumn<ObservableList<String>, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        colFirstName.setPrefWidth(130.4);

        TableColumn<ObservableList<String>, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        colLastName.setPrefWidth(134.4);

        TableColumn<ObservableList<String>, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        colEmail.setPrefWidth(221.6);

        TableColumn<ObservableList<String>, String> colPhoneNumber = new TableColumn<>("Phone Number");
        colPhoneNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        colPhoneNumber.setPrefWidth(145.6);

        tableView.getColumns().addAll(colFirstName, colLastName, colEmail, colPhoneNumber);
        ObservableList<ObservableList<String>> driverList = getDriversFromDatabase();
        tableView.setItems(driverList);
        Button return1 = new Button("Return");
        return1.setLayoutX(696.0);
        return1.setLayoutY(410.0);
        return1.setPrefSize(178.0, 47.0);
        return1.setStyle("-fx-background-radius: 10;");
        return1.setFont(Font.font("System Bold", 14.0));
        return1.setOnAction(event -> { try {
            new MainAdmin().start(new Stage());
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        });
        Button button = new Button("See Driver To Confirm");
        button.setLayoutX(696.0);
        button.setLayoutY(343.0);
        button.setPrefSize(178.0, 47.0);
        button.setStyle("-fx-background-radius: 10;");
        button.setFont(Font.font("System Bold", 14.0));
        button.setOnAction(event -> {
            ObservableList<String> selectedDriver = tableView.getSelectionModel().getSelectedItem();
            if (selectedDriver != null) {
                int driverId = Integer.parseInt(selectedDriver.get(0));
                ObservableList<Image> images = getDriverImages(driverId);
                ConfirmRequests confirmRequests = new ConfirmRequests(images,driverId ); // Pass driverId and images
                Stage confirmStage = new Stage();
                try {
                    confirmRequests.start(confirmStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }

        });


        root.getChildren().addAll(imageView1, tableView, button,return1);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Requests");
        primaryStage.show();
    }

    private ObservableList<ObservableList<String>> getDriversFromDatabase() {
        ObservableList<ObservableList<String>> driverList = FXCollections.observableArrayList();
        try (Connection conn = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(conn);
            String query = "SELECT id, firstName, lastName, email, phoneNumber FROM Drivers WHERE state = 1";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    ObservableList<String> driverData = FXCollections.observableArrayList();
                    driverData.add(rs.getString("id"));
                    driverData.add(rs.getString("firstName"));
                    driverData.add(rs.getString("lastName"));
                    driverData.add(rs.getString("email"));
                    driverData.add(rs.getString("phoneNumber"));
                    driverList.add(driverData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching drivers from database: " + e.getMessage());
        }
        return driverList;
    }

    private ObservableList<Image> getDriverImages(int driverId) {
        ObservableList<Image> images = FXCollections.observableArrayList();
        String query = "SELECT cin_info, assurance_info, permit_info, grise_info, " +
                "image_exterieur_avant, image_exterieur_arriere, " +
                "image_interieur_avant, image_interieur_arriere, image_matricule " +
                "FROM Drivers WHERE id = ?";
        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            DatabaseInitializer.selectDatabase(conn);
            pstmt.setInt(1, driverId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    for (int i = 1; i <= 9; i++) {
                        String base64Data = rs.getString(i); // Fetch Base64 encoded data
                        if (base64Data != null && !base64Data.isEmpty()) {
                            String[] parts = base64Data.split(";"); // Split into multiple parts if separated by ";"
                            for (String part : parts) {
                                if (!part.trim().isEmpty()) {
                                    Image image = decodeBase64ToImage(part.trim());
                                    if (image != null) {
                                        images.add(image);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching driver images for ID: " + driverId);
        }
        return images;
    }
    private Image decodeBase64ToImage(String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            return new Image(new java.io.ByteArrayInputStream(imageBytes));
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors du décodage de l'image : " + e.getMessage());
            return null;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
