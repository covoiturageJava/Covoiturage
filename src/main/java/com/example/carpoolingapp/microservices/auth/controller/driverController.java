package com.example.carpoolingapp.microservices.auth.controller;
import com.example.carpoolingapp.algo.ImageVerificationService;
import com.example.carpoolingapp.model.DatabaseInitializer;
import com.example.carpoolingapp.model.Driver;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class driverController {
    private Driver driver;
    private static ImageVerificationService imageVerifier;
    public driverController() {
        this.driver = new Driver();
        this.imageVerifier = new ImageVerificationService();
    }
    public Driver saveStep1(Driver driver,String firstName, String lastName, String username, String email, String password, String phone, String dateOfBirth, String typeVehicule,String Marque,String modele,String annee) {
        List<String> errors = validateFields(firstName, lastName, username, email, password, phone, dateOfBirth, typeVehicule,Marque,modele,annee);
        if (errors.isEmpty()) {
            driver.setFirstName(firstName);
            driver.setLastName(lastName);
            driver.setUsername(username);
            driver.setEmail(email);
            driver.setPassword(password);
            driver.setPhoneNumber(phone);
            driver.setBirthDate(dateOfBirth);
            driver.setTypeVehicule(typeVehicule);
            driver.setMarqueVehicule(Marque);
            driver.setAnneeVehicule(annee);
            driver.setModeleVehicule(modele);
            driver.setEtat(1);
        }
        return driver;
    }
    public static List<String> validateFields(String firstName, String lastName, String username, String email, String password, String phone, String dateOfBirth, String typeVehicule,String Marque,String modele,String annee) {
        List<String> errors = new ArrayList<>();
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("Le prénom ne peut pas être vide.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Le nom ne peut pas être vide.");
        }
        if (username == null || username.trim().isEmpty()) {
            errors.add("Le nom d'utilisateur ne peut pas être vide.");
        }
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.add("L'email est invalide.");
        }
        if (password == null || password.length() < 6) {
            errors.add("Le mot de passe doit contenir au moins 6 caractères.");
        }
        if (phone == null || !phone.matches("^[0-9]{10}$")) {
            errors.add("Le numéro de téléphone doit comporter exactement 10 chiffres.");
        }
        if (dateOfBirth == null || !dateOfBirth.matches("^\\d{4}-\\d{2}-\\d{2}$") || LocalDate.parse(dateOfBirth).isAfter(LocalDate.now().minusYears(19))) {
            errors.add("La date de naissance doit être au format AAAA-MM-JJ et vous devez avoir au moins 19 ans.");
        }
        if (typeVehicule == null || typeVehicule.trim().isEmpty()) {
            errors.add("Le type de véhicule ne peut pas être vide.");
        }
        if (modele == null || modele.trim().isEmpty()) {
            errors.add("Le modele ne peut pas être vide.");
        }
        if (Marque == null || Marque.trim().isEmpty()) {
            errors.add("La marque ne peut pas être vide.");
        }
        if (annee == null ) {
            errors.add("L'annee non valide.");
        }
        return errors;
    }
    public Driver saveStep2(Driver driver,String imageExterieurAvant, String imageExterieurArriere, String imageInterieurAvant, String imageInterieurArriere, String imageMatricule) {
        List<String> errors = validateImages(imageExterieurAvant, imageExterieurArriere, imageInterieurAvant, imageInterieurArriere, imageMatricule);
        if (errors.isEmpty()) {
            try {
                driver.setImageExterieurAvant(imageExterieurAvant);
                driver.setImageExterieurArriere(imageExterieurArriere);
                driver.setImageInterieurAvant(imageInterieurAvant);
                driver.setImageInterieurArriere(imageInterieurArriere);
                driver.setImageMatricule(imageMatricule);
                driver.setEtat(2);
            } catch (Exception e) {
                errors.add("Erreur lors de la sauvegarde des images : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return driver;
    }
    public static List<String> validateImages(String imageExterieurAvant, String imageExterieurArriere, String imageInterieurAvant, String imageInterieurArriere, String imageMatricule) {
        List<String> errors = new ArrayList<>();
        if (!imageVerifier.verifyImageVehicule(imageExterieurAvant)) {
            errors.add("L'image de l'extérieur avant est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(imageExterieurArriere)) {
            errors.add("L'image de l'extérieur arrière est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(imageInterieurAvant)) {
            errors.add("L'image de l'intérieur avant est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(imageInterieurArriere)) {
            errors.add("L'image de l'intérieur arrière est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(imageMatricule)) {
            errors.add("L'image de la plaque d'immatriculation est invalide.");
        }
        return errors;
    }
    public Driver saveStep3(Driver driver,String cinRecto, String cinVerso, String carteGriseRecto, String carteGriseVerso, String permisRecto, String permisVerso, String assurance) {
        List<String> errors = validateStep3(cinRecto, cinVerso, carteGriseRecto, carteGriseVerso, permisRecto, permisVerso, assurance);
        if (errors.isEmpty()) {
            try {
                driver.setCinInfo(cinRecto + ";" + cinVerso);
                driver.setGriseInfo(carteGriseRecto + ";" + carteGriseVerso);
                driver.setPermitInfo(permisRecto + ";" + permisVerso);
                driver.setAssuranceInfo(assurance);
                driver.setEtat(3);
                submitDriverToDatabase(driver);
            } catch (Exception e) {
                errors.add("Erreur lors de la sauvegarde des données : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return driver;
    }
    private List<String> validateStep3(String cinRecto, String cinVerso, String carteGriseRecto, String carteGriseVerso, String permisRecto, String permisVerso, String assurance) {
        List<String> errors = new ArrayList<>();
        if (!imageVerifier.verifyImageVehicule(cinRecto)) {
            errors.add("L'image recto de la carte d'identité est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(cinVerso)) {
            errors.add("L'image verso de la carte d'identité est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(carteGriseRecto)) {
            errors.add("L'image recto de la carte grise est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(carteGriseVerso)) {
            errors.add("L'image verso de la carte grise est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(permisRecto)) {
            errors.add("L'image recto du permis de conduire est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(permisVerso)) {
            errors.add("L'image verso du permis de conduire est invalide.");
        }
        if (!imageVerifier.verifyImageVehicule(assurance)) {
            errors.add("L'image de l'assurance est invalide.");
        }
        return errors;
    }
    public void submitDriverToDatabase(Driver driver) {
        try {
            DatabaseInitializer db = new DatabaseInitializer();
            db.insertDriver(driver);
            System.out.println("Conducteur soumis et ajouté à la base de données.");
        } catch (NullPointerException npe) {
            System.err.println("Erreur : Une variable nécessaire est null. Détails : " + npe.getMessage());
            npe.printStackTrace();
        } catch (IllegalStateException ise) {
            System.err.println("Erreur d'état : " + ise.getMessage());
            ise.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }
//    private boolean isValidBase64(String base64) {
//        try {
//            byte[] decodedBytes = Base64.getDecoder().decode(base64);
//            return decodedBytes.length > 0;
//        } catch (IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    public Object getSessionData(String key) {
//        return session.get(key);
//    }

}
