package com.example.carpoolingapp.microservices.Drivers.controller;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService {
    public static JSONObject getRouteData() throws Exception {
        String osrmUrl = "http://localhost:5000/route/v1/driving/-8.8744460,30.4914000;-8.8808960,30.4755640?overview=full&geometries=geojson";
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
    public static double[][] getCoordinates() throws Exception {
        JSONObject routeData = getRouteData();
        return RouteParser.extractCoordinates(routeData);
    }
}
