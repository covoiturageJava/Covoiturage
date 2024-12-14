package com.example.carpoolingapp.microservices.Drivers.controller;

import com.example.carpoolingapp.model.DatabaseInitializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import com.example.carpoolingapp.model.Driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.scene.control.TableView;

public class DriveruserprofileControlleur {

    public Driver getUserProfile(int userId) {
        String sql = "SELECT id, firstName, lastName, username, email, phoneNumber, birthDate, " +
                "vehiculeType, marque_vehicule, modele_vehicule, annee_vehicule " +
                "FROM drivers WHERE id = ?";
        try {
            Connection connection = DatabaseInitializer.getConnection();
            DatabaseInitializer.selectDatabase(connection);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Driver driver = new Driver();
                driver.setId(rs.getInt("id"));
                driver.setFirstName(rs.getString("firstName"));
                driver.setLastName(rs.getString("lastName"));
                driver.setUsername(rs.getString("username"));
                driver.setEmail(rs.getString("email"));
                driver.setPhoneNumber(rs.getString("phoneNumber"));
                driver.setBirthDate(rs.getString("birthDate"));
                driver.setTypeVehicule(rs.getString("vehiculeType"));
                driver.setMarqueVehicule(rs.getString("marque_vehicule"));
                driver.setModeleVehicule(rs.getString("modele_vehicule"));
                driver.setAnneeVehicule(rs.getString("annee_vehicule"));
                return driver;
            } else {
                showAlert("Profil non trouvé", "Aucun utilisateur trouvé avec cet identifiant.");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des informations utilisateur : " + e.getMessage());
            showAlert("Erreur système", "Impossible de récupérer les informations utilisateur.");
            return null;
        }
    }
    public void populateTripHistoryTable(TableView<String[]> table, int driverId) {
        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection);
            String query = "SELECT " +
                    "u.firstName, " +
                    "u.lastName, " +
                    "t.date, " +
                    "t.prix, " +
                    "t.rate " +
                    "FROM trajet t " +
                    "JOIN drivers d ON t.id_driver = d.id " +
                    "JOIN users u ON t.id_user = u.id " +
                    "WHERE d.id = ?  AND t.etat = 4";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, driverId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    ObservableList<String[]> data = FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        String[] row = {
                                resultSet.getString("firstName") + " " + resultSet.getString("lastName"),
                                resultSet.getString("date"),
                                resultSet.getString("prix"),
                                resultSet.getString("rate")
                        };
                        data.add(row);
                    }
                    table.setItems(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double getDriverAverageRating(int driverId) {
        double averageRating = 0.0;

        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection);

            String query = "SELECT AVG(t.rate) AS avg_rating " +
                    "FROM trajet t " +
                    "WHERE t.id_driver = ? AND t.etat = 4";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, driverId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Get the average rating, default to 0 if null
                        averageRating = resultSet.getDouble("avg_rating");

                        // Round to two decimal places
                        averageRating = Math.round(averageRating * 100.0) / 100.0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return averageRating;
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

