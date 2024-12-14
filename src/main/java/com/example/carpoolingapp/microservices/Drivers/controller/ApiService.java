package com.example.carpoolingapp.microservices.Drivers.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService {

    public static JSONObject getRouteData(double startLng, double startLat, double endLng, double endLat) throws Exception {
        String osrmUrl = String.format(
                "http://localhost:5000/route/v1/driving/%f,%f;%f,%f?overview=full&geometries=geojson",
                startLng, startLat, endLng, endLat
        );
        URL url = new URL(osrmUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new JSONObject(response.toString());
    }

    public static double[][] getCoordinates(double startLng, double startLat, double endLng, double endLat) throws Exception {
        JSONObject routeData = getRouteData(startLng, startLat, endLng, endLat);
        return RouteParser.extractCoordinates(routeData);
    }

    /**
     * Calcule la distance totale en kilomètres pour un itinéraire donné.
     *
     * @param startCoords Coordonnées de départ (lat, lng).
     * @param endCoords   Coordonnées d'arrivée (lat, lng).
     * @return Distance totale en kilomètres.
     * @throws Exception Si une erreur survient lors de la récupération des données de l'itinéraire.
     */
    public static double calculateDistance(double[] startCoords, double[] endCoords) throws Exception {
        // Récupérer les données de l'itinéraire
        JSONObject routeData = getRouteData(startCoords[1], startCoords[0], endCoords[1], endCoords[0]);

        // Extraire la distance totale (en mètres) de la réponse JSON
        JSONArray routes = routeData.getJSONArray("routes");
        if (routes.length() > 0) {
            JSONObject route = routes.getJSONObject(0);
            double distanceMeters = route.getDouble("distance");

            // Convertir les mètres en kilomètres
            return distanceMeters / 1000.0;
        } else {
            throw new Exception("Aucun itinéraire trouvé.");
        }
    }
}
