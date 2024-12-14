package com.example.carpoolingapp.microservices.Admin.View;
import com.example.carpoolingapp.model.DatabaseInitializer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;

public class DriversUsersView extends Application {

    private final ObservableList<User> usersList = FXCollections.observableArrayList();
    private final ObservableList<Driver> driversList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-image: url('/Admin.jpg'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: no-repeat;");

        Button btnSeeDetails = new Button("See Details");
        btnSeeDetails.setLayoutX(781.0);
        btnSeeDetails.setLayoutY(645.0);
        btnSeeDetails.setPrefSize(93.0, 38.0);
        btnSeeDetails.setStyle("-fx-background-color: #61a87a; -fx-background-radius: 10; -fx-text-fill: white;");
        btnSeeDetails.setFont(Font.font("System Bold", 14.0));

        Button btnDelete = new Button("Delete");
        btnDelete.setLayoutX(672.0);
        btnDelete.setLayoutY(645.0);
        btnDelete.setPrefSize(94.0, 38.0);
        btnDelete.setStyle("-fx-background-color: #61a87a; -fx-background-radius: 10; -fx-text-fill: white;");
        btnDelete.setFont(Font.font("System Bold", 14.0));

        TableView<Driver> driversTable = new TableView<>(driversList);
        driversTable.setLayoutX(14.0);
        driversTable.setLayoutY(173.0);
        driversTable.setPrefSize(890.0, 200.0);

        driversTable.getColumns().addAll(
                createTableColumn("First Name", "firstName", 133.6),
                createTableColumn("Last Name", "lastName", 141.6),
                createTableColumn("UserName", "username", 195.2),
                createTableColumn("Email", "email", 264.0),
                createTableColumn("Phone Number", "phoneNumber", 160.0)
        );

        TableView<User> usersTable = new TableView<>(usersList);
        usersTable.setLayoutX(14.0);
        usersTable.setLayoutY(431.0);
        usersTable.setPrefSize(890.0, 200.0);
        usersTable.getColumns().addAll(
                createTableColumn("First Name", "firstName", 133.6),
                createTableColumn("Last Name", "lastName", 141.6),
                createTableColumn("UserName", "username", 195.2),
                createTableColumn("Email", "email", 264.0),
                createTableColumn("Phone Number", "phoneNumber", 160.0)
        );

        btnDelete.setOnAction(event -> deleteSelectedItem(driversTable, usersTable));

        ImageView imageView = new ImageView();
        Image image = new Image(getClass().getResource("/protection.png").toExternalForm());
        imageView.setImage(image);
        imageView.setLayoutX(748.0);
        imageView.setLayoutY(27.0);
        imageView.setFitWidth(127.0);
        imageView.setFitHeight(108.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        Label lblWelcome = new Label("Welcome Mr.Mehdi Kadiri");
        lblWelcome.setLayoutX(335.0);
        lblWelcome.setLayoutY(42.0);
        lblWelcome.setPrefSize(247.0, 38.0);
        lblWelcome.setTextFill(javafx.scene.paint.Color.web("#0d3696"));
        lblWelcome.setFont(Font.font("System Bold Italic", 18.0));

        Separator separator = new Separator();
        separator.setLayoutX(-4.0);
        separator.setLayoutY(385.0);
        separator.setPrefWidth(926.0);

        Label lblDrivers = new Label("Drivers      :");
        lblDrivers.setLayoutX(35.0);
        lblDrivers.setLayoutY(140.0);
        lblDrivers.setPrefSize(140.0, 18.0);
        lblDrivers.setFont(Font.font("System Bold", 16.0));

        Label lblUsers = new Label("Users      :");
        lblUsers.setLayoutX(35.0);
        lblUsers.setLayoutY(400.0);
        lblUsers.setPrefSize(140.0, 18.0);
        lblUsers.setFont(Font.font("System Bold", 16.0));
        root.getChildren().addAll(
                btnSeeDetails, btnDelete, driversTable, usersTable, imageView, lblWelcome,lblDrivers,lblUsers,separator
        );

        loadData();

        Scene scene = new Scene(root, 916.0, 694.0);
        primaryStage.setTitle("Drivers and Users Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void deleteSelectedItem(TableView<Driver> driversTable, TableView<User> usersTable) {
        Driver selectedDriver = driversTable.getSelectionModel().getSelectedItem();
        if (selectedDriver != null) {
            deleteFromDatabase("drivers", selectedDriver.getUsername());
            driversList.remove(selectedDriver);
        }

        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            deleteFromDatabase("users", selectedUser.getUsername());
            usersList.remove(selectedUser);
        }
    }

    private void deleteFromDatabase(String table, String username) {
        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection);

            String query = "DELETE FROM " + table + " WHERE username = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.executeUpdate();
                System.out.println("Record deleted successfully from " + table);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting record from " + table + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private <T> TableColumn<T, String> createTableColumn(String title, String property, double width) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setPrefWidth(width);
        column.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getClass().getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1))
                                .invoke(cellData.getValue()).toString()
                );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });

        return column;
    }

    private void loadData() {
        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection);

            try (Statement stmt = connection.createStatement();
                 ResultSet rsDrivers = stmt.executeQuery("SELECT firstName, lastName, username, email, phoneNumber FROM Drivers")) {

                while (rsDrivers.next()) {
                    Driver driver = new Driver(
                            rsDrivers.getString("firstName"),
                            rsDrivers.getString("lastName"),
                            rsDrivers.getString("username"),
                            rsDrivers.getString("email"),
                            rsDrivers.getString("phoneNumber")
                    );
                    driversList.add(driver);
                }
            }

            try (Statement stmt = connection.createStatement();
                 ResultSet rsUsers = stmt.executeQuery("SELECT firstName, lastName, username, email, phoneNumber FROM Users")) {

                while (rsUsers.next()) {
                    User userObj = new User(
                            rsUsers.getString("firstName"),
                            rsUsers.getString("lastName"),
                            rsUsers.getString("username"),
                            rsUsers.getString("email"),
                            rsUsers.getString("phoneNumber")
                    );
                    usersList.add(userObj);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class Driver {
        private final String firstName;
        private final String lastName;
        private final String username;
        private final String email;
        private final String phoneNumber;

        public Driver(String firstName, String lastName, String username, String email, String phoneNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }
    }

    public static class User {
        private final String firstName;
        private final String lastName;
        private final String username;
        private final String email;
        private final String phoneNumber;

        public User(String firstName, String lastName, String username, String email, String phoneNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }
    }
}
