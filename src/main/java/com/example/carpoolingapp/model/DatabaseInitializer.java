package com.example.carpoolingapp.model;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class DatabaseInitializer {
    private static final Dotenv dotenv = Dotenv.load();

    public static Connection getConnection() throws SQLException {
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");
        return DriverManager.getConnection(url, user, password);
    }

    public static void selectDatabase(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("USE " + dotenv.get("DB_NAME"));
        }
    }

    public static void initializeDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE '" + dotenv.get("DB_NAME") + "'");
            if (!resultSet.next()) {
                statement.executeUpdate("CREATE DATABASE " + dotenv.get("DB_NAME"));
                System.out.println("Database created");
            } else {
                System.out.println("Database already exists");
            }

            try (Connection dbConnection = DriverManager.getConnection(
                    dotenv.get("DB_URL") + "/" + dotenv.get("DB_NAME"), dotenv.get("DB_USER"), dotenv.get("DB_PASSWORD"))) {
                createTables(dbConnection);
            }

        } catch (SQLException e) {
            System.err.println("Error initializing the database: " + e.getMessage());
        }
    }

    private static void createTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "firstName VARCHAR(50), "
                    + "lastName VARCHAR(50), "
                    + "username VARCHAR(50) UNIQUE, "
                    + "email VARCHAR(100) UNIQUE, "
                    + "password VARCHAR(255), "
                    + "phoneNumber VARCHAR(50), "
                    + "birthDate VARCHAR(50), "
                    + "creationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";
            statement.executeUpdate(createUsersTable);
            System.out.println("Table Users created.");

            String createDriverTable = "CREATE TABLE IF NOT EXISTS Drivers ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "firstName VARCHAR(50), "
                    + "lastName VARCHAR(50), "
                    + "username VARCHAR(50) UNIQUE, "
                    + "email VARCHAR(100) UNIQUE, "
                    + "password VARCHAR(255), "
                    + "phoneNumber VARCHAR(50), "
                    + "birthDate VARCHAR(50), "
                    + "state INT(10), "
                    + "vehiculeType VARCHAR(255), "
                    + "cinInfo VARCHAR(255), "
                    + "assuranceInfo VARCHAR(255), "
                    + "permitInfo VARCHAR(255), "
                    + "griseInfo VARCHAR(255), "
                    + "creationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";
            statement.executeUpdate(createDriverTable);
            System.out.println("Table Drivers created.");

            String createAdminTable = "CREATE TABLE IF NOT EXISTS Admin ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) UNIQUE, "
                    + "email VARCHAR(100) UNIQUE, "
                    + "password VARCHAR(255)"
                    + ")";
            statement.executeUpdate(createAdminTable);
            System.out.println("Table Admin created.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }
}