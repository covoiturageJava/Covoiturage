package com.example.carpoolingapp;

import com.example.carpoolingapp.model.drivers;
import java.sql.*;

public class database {
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String DATABASE_NAME = "covoituragee";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    // Méthode pour établir la connexion à la base de données
    public Connection seConnecter() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL + "/" + DATABASE_NAME, USER, PASSWORD);
            } catch (SQLException e) {
                System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
            }
        }
        return connection;
    }

    // Vérifier l'existence de la base de données et créer les tables si nécessaire
    public void checkDatabase() {
        try (Connection tempConn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = tempConn.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE '" + DATABASE_NAME + "'");
            if (!resultSet.next()) {
                statement.executeUpdate("CREATE DATABASE " + DATABASE_NAME);
                System.out.println("Base de données créée : " + DATABASE_NAME);
                creerTables();
            } else {
                seConnecter();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification ou création de la base de données : " + e.getMessage());
        }
    }

    // Créer les tables nécessaires
    private void creerTables() {
        try (Connection conn = seConnecter(); Statement statement = conn.createStatement()) {
            // Table Utilisateur
            String createUtilisateurTable = "CREATE TABLE IF NOT EXISTS Utilisateur ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "nom VARCHAR(50), "
                    + "prenom VARCHAR(50), "
                    + "username VARCHAR(50), "
                    + "email VARCHAR(100) UNIQUE, "
                    + "telephone VARCHAR(50), "
                    + "date_naissance VARCHAR(50), "
                    + "mot_de_passe VARCHAR(255), "
                    + "date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";
            statement.executeUpdate(createUtilisateurTable);
            System.out.println("Table Utilisateur créée.");
            String createConducteurTable = "CREATE TABLE IF NOT EXISTS Conducteur ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "nom VARCHAR(50), "
                    + "prenom VARCHAR(50), "
                    + "username VARCHAR(50), "
                    + "email VARCHAR(100) UNIQUE, "
                    + "telephone VARCHAR(50), "
                    + "date_naissance VARCHAR(50), "
                    + "mot_de_passe VARCHAR(255), "
                    + "etat INT, "
                    + "type_vehicule VARCHAR(255), "
                    + "marque_vehicule VARCHAR(100), "
                    + "modele_vehicule VARCHAR(100), "
                    + "annee_vehicule VARCHAR(4), "
                    + "cin_info LONGBLOB, "
                    + "assurance_info LONGBLOB, "
                    + "permit_info LONGBLOB, "
                    + "grise_info LONGBLOB, "
                    + "image_exterieur_avant LONGBLOB , "
                    + "image_exterieur_arriere LONGBLOB, "
                    + "image_interieur_avant LONGBLOB, "
                    + "image_interieur_arriere LONGBLOB, "
                    + "image_matricule LONGBLOB, "
                    + "date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";
            statement.executeUpdate(createConducteurTable);
            System.out.println("Table Conducteur créée.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création des tables : " + e.getMessage());
        }
    }
    public void insertDriver(drivers driver) {
        String insertQuery = "INSERT INTO Conducteur ("
                + "nom, prenom, username, email, telephone, date_naissance, mot_de_passe, etat, "
                + "type_vehicule, marque_vehicule, modele_vehicule, annee_vehicule, "
                + "cin_info, assurance_info, permit_info, grise_info, "
                + "image_exterieur_avant, image_exterieur_arriere, image_interieur_avant, "
                + "image_interieur_arriere, image_matricule) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = seConnecter();
             PreparedStatement ps = conn.prepareStatement(insertQuery)) {
            ps.setString(1, driver.getFirstName());
            ps.setString(2, driver.getLastName());
            ps.setString(3, driver.getUsername());
            ps.setString(4, driver.getEmail());
            ps.setString(5, driver.getPhone());
            ps.setString(6, driver.getDateOfBirth());
            ps.setString(7, driver.getPassword());
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
