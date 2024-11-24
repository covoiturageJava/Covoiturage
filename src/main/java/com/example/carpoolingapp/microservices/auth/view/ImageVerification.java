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

public class ImageVerification {

    private final HashMap<String, String> base64Images = new HashMap<>();

    public void show(Stage stage, Driver driver) {
        stage.setTitle("Vérification des Images");

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
        Map<String, Boolean> documents = Map.of(
                "Carte d'identité", true,
                "Carte Grise", true,
                "Permis de conduire", true,
                "Assurance", false
        );

        for (Map.Entry<String, Boolean> entry : documents.entrySet()) {
            VBox docBox = createDocumentBox(entry.getKey(), entry.getValue(), stage);
            mainLayout.getChildren().add(docBox);
        }

        Button submitButton = new Button("Soumettre");
        submitButton.setStyle("-fx-background-color: #00796b; -fx-text-fill: white; -fx-font-size: 16; -fx-border-radius: 10px; -fx-padding: 10px 20px;");
        submitButton.setOnAction(e -> handleSubmit(driver));

        mainContainer.getChildren().addAll(title, mainLayout, submitButton);

        Scene scene = new Scene(mainContainer, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createDocumentBox(String doc, boolean isDualImage, Stage stage) {
        VBox docBox = new VBox(15);
        docBox.setAlignment(Pos.CENTER);
        docBox.setStyle("-fx-background-color: white; -fx-border-color: #00796b; -fx-border-width: 1px; -fx-border-radius: 10px; -fx-padding: 15px;");

        Label docLabel = new Label(doc);
        docLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        docLabel.setTextFill(Color.web("#00796b"));

        docBox.getChildren().add(docLabel);

        if (isDualImage) {
            addImageSelection(docBox, doc + " Recto", stage);
            addImageSelection(docBox, doc + " Verso", stage);
        } else {
            addImageSelection(docBox, doc, stage);
        }

        return docBox;
    }
    private void handleSubmit(Driver driver) {
        List<String> errors = new ArrayList<>();
        String cinFront = base64Images.get("Carte d'identité Recto");
        String cinBack = base64Images.get("Carte d'identité Verso");
        String griseFront = base64Images.get("Carte Grise Recto");
        String griseBack = base64Images.get("Carte Grise Verso");
        String permitFront = base64Images.get("Permis de conduire Recto");
        String permitBack = base64Images.get("Permis de conduire Verso");
        String assurance = base64Images.get("Assurance");
        if (cinFront == null || cinBack == null) errors.add("Images de la Carte d'identité manquantes");
        if (griseFront == null || griseBack == null) errors.add("Images de la Carte Grise manquantes");
        if (permitFront == null || permitBack == null) errors.add("Images du Permis de conduire manquantes");
        if (assurance == null) errors.add("Image de l'Assurance manquante");
        if (!errors.isEmpty()) {
            showAlert("Erreur de validation", String.join("\n", errors), Alert.AlertType.ERROR);
        } else {
            driver.setCinInfo(cinFront + ";" + cinBack);
            driver.setGriseInfo(griseFront + ";" + griseBack);
            driver.setPermitInfo(permitFront + ";" + permitBack);
            driver.setAssuranceInfo(assurance);
            driverController controller = new driverController();
            controller.saveStep3(driver,cinFront,cinBack,griseFront,griseBack,permitFront,permitBack,assurance);
            System.out.println("Images enregistrées avec succès !");
        }
    }

    private void addImageSelection(VBox docBox, String label, Stage stage) {
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
            File file = fileChooser.showOpenDialog(stage);
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

    private String encodeImageToBase64(File file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file);
        BufferedImage resizedImage = resizeImage(originalImage, 800, 800);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
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
