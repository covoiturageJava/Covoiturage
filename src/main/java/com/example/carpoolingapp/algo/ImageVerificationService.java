package com.example.carpoolingapp.algo;

public class ImageVerificationService {
    public boolean verifyImageVehicule(String imagePath) {
        return imagePath != null && !imagePath.trim().isEmpty();
    }
}
