package com.example.carpoolingapp.model;

public class Driver extends User {
    private String vehiculeType;
    private String cinInfo;
    private String assuranceInfo;
    private String permitInfo;
    private String griseInfo;
    private boolean confirmed;

    public String getVehiculeType() {
        return vehiculeType;
    }

    public void setVehiculeType(String vehiculeType) {
        this.vehiculeType = vehiculeType;
    }

    public String getCinInfo() {
        return cinInfo;
    }

    public void setCinInfo(String cinInfo) {
        this.cinInfo = cinInfo;
    }

    public String getAssuranceInfo() {
        return assuranceInfo;
    }

    public void setAssuranceInfo(String assuranceInfo) {
        this.assuranceInfo = assuranceInfo;
    }

    public String getPermitInfo() {
        return permitInfo;
    }

    public void setPermitInfo(String permitInfo) {
        this.permitInfo = permitInfo;
    }

    public String getGriseInfo() {
        return griseInfo;
    }

    public void setGriseInfo(String griseInfo) {
        this.griseInfo = griseInfo;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}