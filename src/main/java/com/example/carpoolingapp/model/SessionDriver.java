package com.example.carpoolingapp.model;

import java.io.Serializable;

public class SessionDriver implements Serializable  {
    private int driver_id;
    private double latitude;
    private double longitude;
    public SessionDriver() {}
    public SessionDriver(int driver_id, double latitude, double longitude) {
        this.driver_id = driver_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
