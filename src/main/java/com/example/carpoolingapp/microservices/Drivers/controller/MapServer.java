package com.example.carpoolingapp.microservices.Drivers.controller;

import java.io.*;
import java.net.*;

public class MapServer {
    public static void main(String[] args) throws IOException {
        int port = 8080; // Port pour le serveur HTTP

        // Création du serveur socket
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur HTTP en attente sur le port " + port);

        while (true) {
            // Accepter une connexion
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connexion reçue");

            // Lire la requête HTTP
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            StringBuilder request = new StringBuilder();
            String path = "";

            while (!(line = reader.readLine()).isEmpty()) {
                request.append(line).append("\n");
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1]; // Récupérer le chemin de la requête
                }
            }
            // Extraire les paramètres lat et lng de l'URL
            String[] queryParams = path.split("\\?");
            double lat = 51.505; // Latitude par défaut (Londres)
            double lng = -0.09;  // Longitude par défaut (Londres)
            if (queryParams.length > 1) {
                String[] params = queryParams[1].split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2) {
                        if (keyValue[0].equals("lat")) {
                            lat = Double.parseDouble(keyValue[1]);
                        } else if (keyValue[0].equals("lng")) {
                            lng = Double.parseDouble(keyValue[1]);
                        }
                    }
                }
            }
            // Générer la réponse HTML
            String htmlResponse = generateHtml(lat, lng);
            // Envoyer la réponse HTTP
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html; charset=UTF-8");
            out.println("Content-Length: " + htmlResponse.getBytes().length);
            out.println();
            out.print(htmlResponse);
            out.flush();

            // Fermer la connexion
            clientSocket.close();
        }
    }
    // Générer le contenu HTML avec les coordonnées
    private static String generateHtml(double lat, double lng) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Carte Dynamique</title>
                    <meta charset="utf-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                    <style>
                        #map {
                            width: 100%;
                            height: 100vh;
                        }
                    </style>
                </head>
                <body>
                <div id="map"></div>
                <script>
                    // Fonction pour extraire les paramètres de l'URL
                    function getQueryParams() {
                        const params = {};
                        const queryString = window.location.search;
                        const urlParams = new URLSearchParams(queryString);
                        urlParams.forEach((value, key) => {
                            params[key] = parseFloat(value); // Convertir en nombre si possible
                        });
                        return params;
                    }
                
                    // Extraire les coordonnées de l'URL
                    const params = getQueryParams();
                    const lat = params.lat || 51.505; // Latitude par défaut
                    const lng = params.lng || -0.09;  // Longitude par défaut
                
                    // Initialisation de la carte
                    var map = L.map('map').setView([lat, lng], 13);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 19,
                        attribution: '© OpenStreetMap contributors'
                    }).addTo(map);
                
                    var marker = L.marker([lat, lng]).addTo(map);
                    marker.bindPopup("Coordonnées : " + lat + ", " + lng).openPopup();
                
                    // Fonction pour mettre à jour la position
                    function updateMap(lat, lng) {
                        var newLatLng = new L.LatLng(lat, lng);
                        marker.setLatLng(newLatLng);
                        map.setView(newLatLng, 13);
                        marker.bindPopup("Coordonnées : " + lat + ", " + lng).openPopup();
                    }
                </script>
                </body>
                </html>
                
                """;
    }
}
