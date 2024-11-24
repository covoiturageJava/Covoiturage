package com.example.carpoolingapp.model;

public class drivers extends users {
    private int etat;
    private String typeVehicule;
    private String marqueVehicule;
    private String modeleVehicule;
    private String anneeVehicule;
    private String cinInfo;
    private String assuranceInfo;
    private String permitInfo;
    private String griseInfo;
    private String dateInscription;
    private String imageExterieurAvant;
    private String imageExterieurArriere;
    private String imageInterieurAvant;
    private String imageInterieurArriere;
    private String imageMatricule;
    public drivers(int etat, String typeVehicule, String marqueVehicule, String modeleVehicule,
                   String anneeVehicule, String cinInfo, String assuranceInfo, String permitInfo,
                   String griseInfo, String dateInscription, String imageExterieurAvant,
                   String imageExterieurArriere, String imageInterieurAvant,
                   String imageInterieurArriere, String imageMatricule) {
        this.etat = etat;
        this.typeVehicule = typeVehicule;
        this.marqueVehicule = marqueVehicule;
        this.modeleVehicule = modeleVehicule;
        this.anneeVehicule = anneeVehicule;
        this.cinInfo = cinInfo;
        this.assuranceInfo = assuranceInfo;
        this.permitInfo = permitInfo;
        this.griseInfo = griseInfo;
        this.dateInscription = dateInscription;
        this.imageExterieurAvant = imageExterieurAvant;
        this.imageExterieurArriere = imageExterieurArriere;
        this.imageInterieurAvant = imageInterieurAvant;
        this.imageInterieurArriere = imageInterieurArriere;
        this.imageMatricule = imageMatricule;
    }

    public drivers() {

    }
    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getTypeVehicule() {
        return typeVehicule;
    }

    public void setTypeVehicule(String typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public String getMarqueVehicule() {
        return marqueVehicule;
    }

    public void setMarqueVehicule(String marqueVehicule) {
        this.marqueVehicule = marqueVehicule;
    }

    public String getModeleVehicule() {
        return modeleVehicule;
    }

    public void setModeleVehicule(String modeleVehicule) {
        this.modeleVehicule = modeleVehicule;
    }

    public String getAnneeVehicule() {
        return anneeVehicule;
    }

    public void setAnneeVehicule(String anneeVehicule) {
        this.anneeVehicule = anneeVehicule;
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

    public String getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getImageExterieurAvant() {
        return imageExterieurAvant;
    }

    public void setImageExterieurAvant(String imageExterieurAvant) {
        this.imageExterieurAvant = imageExterieurAvant;
    }

    public String getImageExterieurArriere() {
        return imageExterieurArriere;
    }

    public void setImageExterieurArriere(String imageExterieurArriere) {
        this.imageExterieurArriere = imageExterieurArriere;
    }

    public String getImageInterieurAvant() {
        return imageInterieurAvant;
    }

    public void setImageInterieurAvant(String imageInterieurAvant) {
        this.imageInterieurAvant = imageInterieurAvant;
    }

    public String getImageInterieurArriere() {
        return imageInterieurArriere;
    }

    public void setImageInterieurArriere(String imageInterieurArriere) {
        this.imageInterieurArriere = imageInterieurArriere;
    }

    public String getImageMatricule() {
        return imageMatricule;
    }

    public void setImageMatricule(String imageMatricule) {
        this.imageMatricule = imageMatricule;
    }
}
