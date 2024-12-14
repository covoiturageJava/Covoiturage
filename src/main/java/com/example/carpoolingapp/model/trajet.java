package com.example.carpoolingapp.model;

import java.io.Serializable;

public class trajet implements Runnable{
    private String id_user;
    private String id_driver;
    private String distance;
    private double latitudedepart;
    private double longitudedepart;
    private double latitudefin;
    private double longitudefin;
    private int rate;
    private double prix;
    private boolean trajetValid = false;
    public trajet(String id_user, double latitudedepart, double longitudedepart, double latitudefin, double longitudefin, String distance) {
        this.id_user = id_user;
        this.latitudedepart = latitudedepart;
        this.longitudedepart = longitudedepart;
        this.latitudefin = latitudefin;
        this.longitudefin = longitudefin;
        this.distance = distance;
    }

    // Getters et setters

    public boolean isTrajetValid() {
        return trajetValid;
    }

    public void setTrajetValid(boolean trajetValid) {
        this.trajetValid = trajetValid;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_driver() {
        return id_driver;
    }

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getLatitudedepart() {
        return latitudedepart;
    }

    public void setLatitudedepart(double latitudedepart) {
        this.latitudedepart = latitudedepart;
    }

    public double getLongitudedepart() {
        return longitudedepart;
    }

    public void setLongitudedepart(double longitudedepart) {
        this.longitudedepart = longitudedepart;
    }

    public double getLatitudefin() {
        return latitudefin;
    }

    public void setLatitudefin(double latitudefin) {
        this.latitudefin = latitudefin;
    }

    public double getLongitudefin() {
        return longitudefin;
    }

    public void setLongitudefin(double longitudefin) {
        this.longitudefin = longitudefin;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
    @Override
    public void run() {
        try {
            System.out.println("Thread démarré pour le trajet : " + this);
            // Simulation d'un traitement de 5 minutes (300 000 ms)
            Thread.sleep(300_000);
            // Logic after processing
            System.out.println("Trajet traité : " + this);
        } catch (InterruptedException e) {
            System.err.println("Erreur dans le traitement du trajet : " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return "Trajet{id_user='" + id_user + "', distance='" + distance + "', latitudedepart=" + latitudedepart + ", longitudedepart=" + longitudedepart +
                ", latitudefin=" + latitudefin + ", longitudefin=" + longitudefin + '}';
    }
}

