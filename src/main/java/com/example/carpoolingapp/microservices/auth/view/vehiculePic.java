package com.example.carpoolingapp.microservices.auth.view;

import com.example.carpoolingapp.microservices.auth.controller.driverController;
import com.example.carpoolingapp.model.Driver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class vehiculePic {
    private final HashMap<String, String> base64Images = new HashMap<>(); // Stockage des images encodées en Base64
    private Stage currentStage;
    public void show(Stage primaryStage, Driver driver) {
        this.currentStage = primaryStage;
        primaryStage.setTitle("Images du Véhicule");
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #f0f4f8;");
        Label title = new Label("Veuillez fournir les images nécessaires");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#00796b"));
        HBox mainLayout = new HBox(30);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setSpacing(30);
        String[] documents = {"exterieur voiture", "interieur voiture", "Matricule"};
        for (String doc : documents) {
            VBox docBox = createDocumentBox(doc, primaryStage);
            mainLayout.getChildren().add(docBox);
        }

        Button submitButton = new Button("Soumettre");
        submitButton.setStyle("-fx-background-color: #00796b; -fx-text-fill: white; -fx-font-size: 16; -fx-border-radius: 10px; -fx-padding: 10px 20px;");
        submitButton.setOnAction(e -> handleSubmit(driver));
        mainContainer.getChildren().addAll(title, mainLayout, submitButton);
        Scene scene = new Scene(mainContainer, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Crée un conteneur pour un document spécifique.
     */
    private VBox createDocumentBox(String doc, Stage primaryStage) {
        VBox docBox = new VBox(15);
        docBox.setAlignment(Pos.CENTER);
        docBox.setStyle("-fx-background-color: white; -fx-border-color: #00796b; -fx-border-width: 1px; " +
                "-fx-border-radius: 10px; -fx-padding: 15px;");

        Label docLabel = new Label(doc);
        docLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        docLabel.setTextFill(Color.web("#00796b"));

        docBox.getChildren().add(docLabel);

        // Ajouter des sélections selon le type de document
        if (doc.equals("Matricule")) {
            addImageSelection(docBox, "Matricule", primaryStage);
        } else {
            addImageSelection(docBox, doc + " avant", primaryStage);
            addImageSelection(docBox, doc + " arrière", primaryStage);
        }
        return docBox;
    }

    /**
     * Gère la soumission des images.
     */
    private void handleSubmit(Driver driver) {
        List<String> errors = new ArrayList<>();
        String imageExterieurAvant = base64Images.get("exterieur voiture avant");
        String imageExterieurArriere = base64Images.get("exterieur voiture arrière");
        String imageInterieurAvant = base64Images.get("interieur voiture avant");
        String imageInterieurArriere = base64Images.get("interieur voiture arrière");
        String imageMatricule = base64Images.get("Matricule");
        if (imageExterieurAvant == null) errors.add("Image extérieure avant manquante");
        if (imageExterieurArriere == null) errors.add("Image extérieure arrière manquante");
        if (imageInterieurAvant == null) errors.add("Image intérieure avant manquante");
        if (imageInterieurArriere == null) errors.add("Image intérieure arrière manquante");
        if (imageMatricule == null) errors.add("Image de la matricule manquante");
        if (!errors.isEmpty()) {
            showAlert("Erreur de validation", String.join("\n", errors), Alert.AlertType.ERROR);
        } else {
            driverController controller = new driverController();
            driver = controller.saveStep2(driver, imageExterieurAvant, imageExterieurArriere, imageInterieurAvant, imageInterieurArriere, imageMatricule);
            ImageVerification vehiculePicView = new ImageVerification();
            Stage nextStage = new Stage();
            vehiculePicView.show(nextStage, driver);
        }
    }
    /**
     * Ajoute la possibilité de choisir une image.
     */
    private void addImageSelection(VBox docBox, String label, Stage primaryStage) {
        Label sideLabel = new Label(label);
        sideLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        sideLabel.setTextFill(Color.web("#444444"));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-border-color: #00796b; -fx-border-radius: 10px;");
        Button chooseButton = new Button("Choisir une image");
        chooseButton.setStyle("-fx-background-color: #00796b; -fx-text-fill: white; -fx-font-size: 14; -fx-border-radius: 5px;");
        chooseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers d'image", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                imageView.setImage(new Image(file.toURI().toString()));
                try {
                    String base64 = encodeImageToBase64(file);
                    base64Images.put(label, base64);
                    System.out.println(label + " image sélectionnée et encodée.");
                } catch (Exception ex) {
                    showAlert("Erreur", "Erreur lors de l'encodage de l'image : " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
        docBox.getChildren().addAll(sideLabel, imageView, chooseButton);
    }
    private String encodeImageToBase64(File file) throws Exception {
        try {
            // Lire l'image à partir du fichier
            BufferedImage originalImage = ImageIO.read(file);

            // Redimensionner l'image avant de l'encoder
            BufferedImage resizedImage = resizeImage(originalImage, 800, 800);  // Redimensionner à une taille maximale (par exemple 800x800)

            // Compresser l'image en format JPEG avec une qualité réduite (par exemple, 80%)
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", byteArrayOutputStream);

            // Convertir l'image compressée en Base64
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException ex) {
            showAlert("Erreur", "Erreur lors de l'encodage de l'image : " + ex.getMessage(), Alert.AlertType.ERROR);
            throw new Exception("Erreur lors de l'encodage de l'image", ex);
        }
    }
    private BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        double aspectRatio = (double) width / (double) height;
        if (width > height) {
            width = maxWidth;
            height = (int) (width / aspectRatio);
        } else {
            height = maxHeight;
            width = (int) (height * aspectRatio);
        }
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
