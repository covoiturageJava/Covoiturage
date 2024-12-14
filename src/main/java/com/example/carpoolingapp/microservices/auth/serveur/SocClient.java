package com.example.carpoolingapp.microservices.auth.serveur;


import com.example.carpoolingapp.model.SessionDriver;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocClient {
    private String host = "127.0.0.1";
    private int port = 12345;

    public Object connectAsDriver(String emailOrUsername, String password) {
        System.out.println("Client: Attempting to connect as DRIVER...");
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Client: Connected to server.");

            // Envoyer les informations d'identification
            out.writeObject("DRIVER");
            out.writeObject(emailOrUsername);
            out.writeObject(password);
            System.out.println("Client: Sent DRIVER credentials to server.");

            try {
                // Lire la réponse du serveur
                Object response = null;
                try {
                    response = in.readObject();
                } catch (Exception e) {
                    // Traitement silencieux des exceptions pendant la lecture
                    System.err.println("Client: Failed to read response from server. Defaulting to null.");
                    response = null;
                }

                // Vérifier si la réponse est null
                if (response == null) {
                    System.out.println("Client: Received null response from server.");
                    return null;
                }

                System.out.println("Client: Received response from server.");
                if (response instanceof SessionDriver) {
                    System.out.println("Client: Login successful for DRIVER.");
                    return response;
                } else {
                    System.out.println("Client: Login failed for DRIVER.");
                    System.out.println(response); // Affiche la réponse reçue si elle n'est pas valide
                    return response;
                }
            } catch (Exception e) {
                // Gestion des exceptions inattendues pendant le traitement
                System.err.println("Client: An unexpected error occurred while processing the server response.");
                return null;
            }
        } catch (Exception e) {
            // Gestion des exceptions pendant la connexion au serveur
            System.err.println("Client: Exception occurred while connecting as DRIVER. Suppressing stack trace.");
            return null;
        }
    }




    public boolean connectAsUserOrAdmin(String emailOrUsername, String password, String userType) {
        System.out.println("Client: Attempting to connect as " + userType + "...");
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            System.out.println("Client: Connected to server.");
            out.writeObject(userType);
            out.writeObject(emailOrUsername);
            out.writeObject(password);
            System.out.println("Client: Sent " + userType + " credentials to server.");
            boolean response = (boolean) in.readObject();
            System.out.println("Client: Received authentication result: " + response);
            return response;
        } catch (Exception e) {
            System.err.println("Client: Exception occurred while connecting as " + userType + ".");
            e.printStackTrace();
            return false;
        }
    }

}
