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

    public void insertDriver(Driver driver) {
        String insertQuery = "INSERT INTO Drivers ("
                + "firstName, lastName, username, email, password, phoneNumber, birthDate, state, "
                + "vehiculeType, marque_vehicule, modele_vehicule, annee_vehicule, "
                + "cin_info, assurance_info, permit_info, grise_info, "
                + "image_exterieur_avant, image_exterieur_arriere, image_interieur_avant, "
                + "image_interieur_arriere, image_matricule) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            Connection connection = DatabaseInitializer.getConnection();
            DatabaseInitializer.selectDatabase(connection);
            PreparedStatement ps = connection.prepareStatement(insertQuery);
            ps.setString(1, driver.getFirstName());
            ps.setString(2, driver.getLastName());
            ps.setString(3, driver.getUsername());
            ps.setString(4, driver.getEmail());
            ps.setString(5, driver.getPassword());
            ps.setString(6, driver.getPhoneNumber());
            ps.setString(7, driver.getBirthDate());
            ps.setInt(8, driver.getEtat());
            ps.setString(9, driver.getTypeVehicule());
            ps.setString(10, driver.getMarqueVehicule());
            ps.setString(11, driver.getModeleVehicule());
            ps.setString(12, driver.getAnneeVehicule());
            ps.setString(13, driver.getCinInfo());
            ps.setString(14, driver.getAssuranceInfo());
            ps.setString(15, driver.getPermitInfo());
            ps.setString(16, driver.getGriseInfo());
            ps.setString(17, driver.getImageExterieurAvant());
            ps.setString(18, driver.getImageExterieurArriere());
            ps.setString(19, driver.getImageInterieurAvant());
            ps.setString(20, driver.getImageInterieurArriere());
            ps.setString(21, driver.getImageMatricule());
            ps.executeUpdate();
            System.out.println("Conducteur ajouté avec succès.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Violation d'une contrainte d'intégrité : " + e.getMessage());
            e.printStackTrace();
        } catch (SQLSyntaxErrorException e) {
            System.err.println("Erreur de syntaxe SQL : " + e.getMessage());
            e.printStackTrace();
        } catch (SQLDataException e) {
            System.err.println("Erreur de données SQL (problème avec un type ou une valeur) : " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur SQL générique : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }
}