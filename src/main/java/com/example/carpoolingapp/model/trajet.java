package com.example.carpoolingapp.model;

public class trajet {
    private String id_user;
    private String id_driver;
    private String distance;
    private double latitudedepart;
    private double longitudedepart;
    private double latitudefin;
    private double longitudefin;
    private int rate;
    private double prix;

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
}

