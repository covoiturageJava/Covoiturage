package com.example.carpoolingapp.microservices.auth.controller;

import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.SessionDriver;
import javafx.scene.control.Alert;
import com.example.carpoolingapp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    public boolean login(String emailOrUsername, String password, String choice) {
        try {
            Connection connection = DatabaseInitializer.getConnection();
            DatabaseInitializer.selectDatabase(connection);

            switch (choice) {
                case "User":
                    return loginUser(connection, emailOrUsername, password);
                case "Driver":
                    return loginDriver(emailOrUsername, password) != null;
                case "Admin":
                    return loginAdmin(connection, emailOrUsername, password);
                default:
                    System.out.println("Invalid choice.");
                    return false;
            }
        } catch (SQLException e) {
            System.err.println("Error during user login: " + e.getMessage());
            return false;
        }
    }

    private boolean loginUser(Connection connection, String emailOrUsername, String password) {
        String sql = "SELECT * FROM Users WHERE (email = ? OR username = ?) AND password = ?";
        return authenticate(connection, sql, emailOrUsername, password);
    }
    public SessionDriver loginDriver(String emailOrUsername, String password) throws SQLException {
        Connection connection = DatabaseInitializer.getConnection();
        DatabaseInitializer.selectDatabase(connection);
        String sql = "SELECT id, state FROM Drivers WHERE (email = ? OR username = ?) AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, emailOrUsername);
            stmt.setString(2, emailOrUsername);
            stmt.setString(3, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int state = rs.getInt("state");
                int driverId = rs.getInt("id");

                switch (state) {
                    case 1:
                        showAlert("Erreur de connexion", "Compte pas encore accepté par l'administrateur.");
                        return null;
                    case 2:
                        showAlert("Erreur de connexion", "Veuillez lancer une session depuis l'application mobile.");
                        return null;
                    case 3:
                        return checkExistingSession(connection, driverId);
                    default:
                        showAlert("Erreur inconnue", "État inconnu.");
                        return null;
                }
            } else {
                showAlert("Erreur de connexion", "Identifiants incorrects.");
                return null;
            }
        } catch (SQLException e) {
            showAlert("Erreur système", "Erreur lors de l'authentification du conducteur : " + e.getMessage());
            return null;
        }
    }

    private SessionDriver checkExistingSession(Connection connection, int driverId) {
        String sql = "SELECT session_id, latitude, longitude FROM driver_session WHERE driver_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                SessionDriver sessionDriver = new SessionDriver();
                sessionDriver.setDriver_id(driverId);
                sessionDriver.setLatitude(latitude);
                sessionDriver.setLongitude(longitude);
                return sessionDriver;
            } else {
                showAlert("Erreur de session", "Aucune session trouvée pour cet utilisateur.");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error checking existing session: " + e.getMessage());
            return null;
        }
    }

    private boolean loginAdmin(Connection connection, String emailOrUsername, String password) {
        String sql = "SELECT * FROM Admin WHERE (email = ? OR username = ?) AND password = ?";
        return authenticate(connection, sql, emailOrUsername, password);
    }

    private boolean authenticate(Connection connection, String sql, String emailOrUsername, String password) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, emailOrUsername);
            stmt.setString(2, emailOrUsername);
            stmt.setString(3, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error during authentication: " + e.getMessage());
            return false;
        }
    }
    public User getUser(String emailOrUsername) {
        String sql = "SELECT * FROM Users WHERE email = ? OR username = ?";
        try {
            Connection connection = DatabaseInitializer.getConnection();
            DatabaseInitializer.selectDatabase(connection);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, emailOrUsername);
            stmt.setString(2, emailOrUsername);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phoneNumber"),
                        rs.getString("birthDate"),
                        rs.getString("creationDate")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
