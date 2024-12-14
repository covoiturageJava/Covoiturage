package com.example.carpoolingapp.microservices.Drivers.controller;

import java.io.*;
import java.net.*;

public class MapServer {
    public static void main(String[] args) throws IOException {
        int port = 8081; // Port pour le serveur HTTP
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur HTTP en attente sur le port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connexion reçue");
            // Lancer un nouveau thread pour gérer la connexion
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Lire la requête HTTP
            String line;
            StringBuilder request = new StringBuilder();
            String path = "";
            while (!(line = reader.readLine()).isEmpty()) {
                request.append(line).append("\n");
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1]; // Récupérer le chemin de la requête
                }
            }

            // Générer la réponse en fonction du chemin
            String htmlResponse = "";
            if (path.startsWith("/map")) {
                htmlResponse = handleMapRequest(path);
            } else if (path.startsWith("/route1")) {
                htmlResponse = handleRoute1Request(path);
            } else {
                htmlResponse = "404 Not Found";
            }

            // Envoyer la réponse HTTP
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html; charset=UTF-8");
            out.println("Content-Length: " + htmlResponse.getBytes().length);
            out.println();
            out.print(htmlResponse);
            out.flush();

        } catch (IOException e) {
            System.err.println("Erreur lors du traitement de la connexion client : " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la connexion client : " + e.getMessage());
            }
        }
    }

    private static String handleMapRequest(String path) {
        double lat = 51.505; // Latitude par défaut (Londres)
        double lng = -0.09; // Longitude par défaut (Londres)

        String[] queryParams = path.split("\\?");
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
        return generateMapHtml(lat, lng);
    }
    private static String handleRoute1Request(String path) {
        double latDriver = 0.0;
        double lngDriver = 0.0;
        double latDepart = 0.0;
        double lngDepart = 0.0;

        String[] queryParams = path.split("\\?");
        if (queryParams.length > 1) {
            String[] params = queryParams[1].split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    try {
                        if (keyValue[0].equals("latDriver")) {
                            latDriver = Double.parseDouble(keyValue[1]);
                        } else if (keyValue[0].equals("lngDriver")) {
                            lngDriver = Double.parseDouble(keyValue[1]);
                        } else if (keyValue[0].equals("latDepart")) {
                            latDepart = Double.parseDouble(keyValue[1]);
                        } else if (keyValue[0].equals("lngDepart")) {
                            lngDepart = Double.parseDouble(keyValue[1]);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format for parameter: " + keyValue[0]);
                    }
                }
            }
        }

        return generateMap1(latDriver, lngDriver, latDepart, lngDepart);
    }
    // Générer le contenu HTML avec les coordonnées
    private static String generateMapHtml(double lat, double lng) {
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
    private static String generateMap1(double latDriver, double lngDriver, double latDepart, double lngDepart) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Carte Dynamique - Route</title>
                <meta charset="utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>
                    body, html {
                        margin: 0;
                        padding: 0;
                        height: 100%;
                    }
                    #map {
                        width: 100%;
                        height: 100vh;
                    }
                </style>
            </head>
            <body>
            <div id="map"></div>
            <script>
                 function getQueryParams() {
                        const params = {};
                        const queryString = window.location.search;
                        const urlParams = new URLSearchParams(queryString);
                        urlParams.forEach((value, key) => {
                            params[key] = parseFloat(value); // Convertir en nombre si possible
                        });
                        return params;
                    }
                // Coordonnées du conducteur et du départ
                const params = getQueryParams();
                const driverCoords = [params.latDriver, params.lngDriver];
                const departCoords = [params.latDepart, params.lngDepart];
                // Initialisation de la carte
                let map = L.map('map').setView(driverCoords, 14);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                }).addTo(map);
                // Marqueurs pour les positions
                L.marker(driverCoords).addTo(map).bindPopup("Conducteur").openPopup();
                L.marker(departCoords).addTo(map).bindPopup("Départ").openPopup();

                // URL de l'itinéraire (OSRM API ou similaire)
                const url = `http://localhost:5000/route/v1/driving/${driverCoords[1]},${driverCoords[0]};${departCoords[1]},${departCoords[0]}?overview=full&geometries=geojson`;
                // Récupération et affichage de l'itinéraire
                fetch(url)
                    .then(response => response.json())
                    .then(data => {
                        const route = data.routes[0];
                        const coordinates = route.geometry.coordinates.map(coord => [coord[1], coord[0]]);
                        L.polyline(coordinates, { color: 'blue' }).addTo(map);
                        const distanceKm = (route.distance / 1000).toFixed(2);
                        L.popup()
                            .setLatLng(coordinates[Math.floor(coordinates.length / 2)])
                            .setContent(`Distance: ${distanceKm} km`)
                            .addTo(map);
                    })
                    .catch(error => {
                        console.error("Erreur lors de la récupération de l'itinéraire :", error);
                    });
            </script>
            </body>
            </html>
            """;
    }

}


