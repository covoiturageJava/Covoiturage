package com.example.carpoolingapp.model;

public class drivers extends users{

    private String idCardInfo;
    private String assuranceInfo;
    private String permitInfo;
    private String cartGriseInfo;
    private boolean confirmed;
    private String typeOfVehicule;

    public drivers() {
    }

    public drivers(int id, String firstName, String lastName, String username, String email, String password, String phone, String dateOfCreation, String dateOfBirth, String idCardInfo, String assuranceInfo, String permitInfo, String cartGriseInfo, boolean confirmed, String typeOfVehicule) {
        super(id, firstName, lastName, username, email, password, phone, dateOfCreation, dateOfBirth);
        this.idCardInfo = idCardInfo;
        this.assuranceInfo = assuranceInfo;
        this.permitInfo = permitInfo;
        this.cartGriseInfo = cartGriseInfo;
        this.confirmed = confirmed;
        this.typeOfVehicule = typeOfVehicule;
    }

    public String getIdCardInfo() {
        return idCardInfo;
    }

    public void setIdCardInfo(String idCardInfo) {
        this.idCardInfo = idCardInfo;
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

    public String getCartGriseInfo() {
        return cartGriseInfo;
    }

    public void setCartGriseInfo(String cartGriseInfo) {
        this.cartGriseInfo = cartGriseInfo;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getTypeOfVehicule() {
        return typeOfVehicule;
    }

    public void setTypeOfVehicule(String typeOfVehicule) {
        this.typeOfVehicule = typeOfVehicule;
    }
}
