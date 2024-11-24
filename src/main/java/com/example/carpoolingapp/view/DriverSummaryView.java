package com.example.carpoolingapp.view;

import com.example.carpoolingapp.model.drivers;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Base64;
import java.util.Map;
public class DriverSummaryView {

    public void show(Stage stage, drivers driver) {
        VBox root = new VBox(15);
        root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f5f5f5;");

        // Ajouter le titre
        Label title = new Label("Résumé du conducteur");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        root.getChildren().add(title);

        // Images encodées (remplacez par les getters des images Base64 de votre objet `drivers`)
        Map<String, String> images = Map.of(
                "Extérieur Avant", driver.getImageExterieurAvant(),
                "Extérieur Arrière", driver.getImageExterieurArriere(),
                "Intérieur Avant", driver.getImageInterieurAvant(),
                "Intérieur Arrière", driver.getImageInterieurArriere(),
                "Matricule", driver.getImageMatricule()
        );

        // Afficher chaque image
        for (Map.Entry<String, String> entry : images.entrySet()) {
            String label = entry.getKey();
            String base64Image = entry.getValue();

            if (base64Image != null && !base64Image.isEmpty()) {
                // Décoder l'image Base64
                Image image = decodeBase64ToImage(base64Image);

                // Ajouter l'image et son label au conteneur
                Label imageLabel = new Label(label);
                imageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                imageView.setStyle("-fx-border-color: #00796b; -fx-border-radius: 10px;");

                root.getChildren().addAll(imageLabel, imageView);
            } else {
                // Si aucune image n'est présente
                Label missingLabel = new Label(label + ": Image manquante");
                missingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");
                root.getChildren().add(missingLabel);
            }
        }
        // Configurer et afficher la scène
        Scene scene = new Scene(root, 600, 800);
        stage.setTitle("Résumé du conducteur");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Décoder une chaîne Base64 en une Image JavaFX.
     *
     * @param base64Image La chaîne Base64 représentant l'image.
     * @return L'objet Image décodé.
     */
    private Image decodeBase64ToImage(String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            return new Image(new java.io.ByteArrayInputStream(imageBytes));
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors du décodage de l'image : " + e.getMessage());
            return null; // Retourne null si l'image ne peut pas être décodée
        }
    }
}
