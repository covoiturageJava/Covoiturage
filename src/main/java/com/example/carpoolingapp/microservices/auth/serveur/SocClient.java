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
            out.writeObject("DRIVER");
            out.writeObject(emailOrUsername);
            out.writeObject(password);
            System.out.println("Client: Sent DRIVER credentials to server.");

            Object response = in.readObject();
            System.out.println("Client: Received response from server.");
            if (response instanceof SessionDriver) {
                System.out.println("Client: Login successful for DRIVER.");
                return response;
            } else {
                System.out.println("Client: Login failed for DRIVER.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Client: Exception occurred while connecting as DRIVER.");
            e.printStackTrace();
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
