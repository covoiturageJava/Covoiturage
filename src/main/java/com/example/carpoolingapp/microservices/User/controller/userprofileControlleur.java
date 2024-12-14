package com.example.carpoolingapp.microservices.User.controller;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import com.example.carpoolingapp.model.User;
import com.example.carpoolingapp.model.DatabaseInitializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userprofileControlleur {
    public User getUserProfile(int userid) {
        String sql = "SELECT id, firstName, lastName, username, email,  phoneNumber, birthDate FROM users WHERE id = ?";
        Connection connection = null;
        try {
            connection = DatabaseInitializer.getConnection();
            DatabaseInitializer.selectDatabase(connection);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setBirthDate(rs.getString("birthDate"));
                return user;
            } else {
                showAlert("Profil non trouvé", "Aucun utilisateur trouvé avec cet email ou nom d'utilisateur.");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des informations utilisateur : " + e.getMessage());
            showAlert("Erreur système", "Impossible de récupérer les informations utilisateur.");
            return null;
        }
    }
    public void populateTripHistoryTable(TableView<String[]> table, int userId) {
        try (Connection connection = DatabaseInitializer.getConnection()) {
            DatabaseInitializer.selectDatabase(connection);
            String query = "SELECT " +
                    "d.firstName, " +
                    "d.lastName, " +
                    "t.date, " +
                    "t.prix, " +
                    "t.rate " +
                    "FROM trajet t " +
                    "JOIN drivers d ON t.id_user = d.id " +
                    "JOIN users u ON t.id_user = u.id " +
                    "WHERE d.id = ? AND t.etat = 4";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
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
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
