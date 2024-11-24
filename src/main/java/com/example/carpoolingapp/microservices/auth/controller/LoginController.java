package com.example.carpoolingapp.microservices.auth.controller;

import com.example.carpoolingapp.model.DatabaseInitializer;

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
                    return loginDriver(connection, emailOrUsername, password);
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

    private boolean loginDriver(Connection connection, String emailOrUsername, String password) {
        String sql = "SELECT * FROM Drivers WHERE (email = ? OR username = ?) AND password = ?";
        return authenticate(connection, sql, emailOrUsername, password);
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
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error during authentication: " + e.getMessage());
            return false;
        }
    }
}
