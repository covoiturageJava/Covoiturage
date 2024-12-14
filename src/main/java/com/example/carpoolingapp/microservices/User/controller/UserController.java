package com.example.carpoolingapp.microservices.User.controller;

import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.Driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    public Driver getDriverDetails(int driverId) {
        try {
            Connection connection = DatabaseInitializer.getConnection();
            DatabaseInitializer.selectDatabase(connection);

            String query = "SELECT * FROM drivers WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, driverId);
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
            }
        } catch (SQLException e) {
            System.err.println("Error in getDriverDetails: " + e.getMessage());
        }
        return null;
    }

    public String[] getDriverLocation(int driverId) {
        try {
            Connection connection = DatabaseInitializer.getConnection();
            DatabaseInitializer.selectDatabase(connection);

            String query = "SELECT latitude, longitude FROM sessiondriver WHERE driverId = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String latitude = String.valueOf(rs.getDouble("latitude"));
                String longitude = String.valueOf(rs.getDouble("longitude"));
                return new String[]{latitude, longitude};
            }
        } catch (SQLException e) {
            System.err.println("Error in getDriverLocation: " + e.getMessage());
        }
        return new String[]{"Location not available", "Location not available"};
    }
}
