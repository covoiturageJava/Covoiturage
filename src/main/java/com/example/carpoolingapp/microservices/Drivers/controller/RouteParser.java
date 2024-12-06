package com.example.carpoolingapp.microservices.Drivers.controller;

import org.json.JSONArray;
import org.json.JSONObject;

public class RouteParser {

    public static double[][] extractCoordinates(JSONObject routeData) {
        JSONArray routesArray = routeData.getJSONArray("routes");
        if (routesArray.length() == 0) {
            throw new RuntimeException("No route data available");
        }
        JSONObject route = routesArray.getJSONObject(0);
        JSONObject geometry = route.getJSONObject("geometry");
        JSONArray coordinatesArray = geometry.getJSONArray("coordinates");

        double[][] coordinates = new double[coordinatesArray.length()][2];
        for (int i = 0; i < coordinatesArray.length(); i++) {
            JSONArray coord = coordinatesArray.getJSONArray(i);
            coordinates[i][0] = coord.getDouble(0); // longitude
            coordinates[i][1] = coord.getDouble(1); // latitude
        }
        return coordinates;
    }
}
