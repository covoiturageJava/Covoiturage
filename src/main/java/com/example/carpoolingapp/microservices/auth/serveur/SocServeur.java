package com.example.carpoolingapp.microservices.auth.serveur;

import com.example.carpoolingapp.microservices.auth.controller.LoginController;
import com.example.carpoolingapp.model.SessionDriver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class SocServeur {
    public static void main(String[] args) {
        System.out.println("Server: Starting server...");
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server: Server is running and waiting for connections...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server: Accepted a connection from client.");
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server: IOException occurred while running the server.");
            e.printStackTrace();
        }
    }
    private static void handleClient(Socket clientSocket) {
        System.out.println("Server: Handling a new client.");
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String userType = (String) in.readObject();
            String emailOrUsername = (String) in.readObject();
            String password = (String) in.readObject();
            System.out.println("Server: Received credentials for userType: " + userType);

            if ("DRIVER".equals(userType)) {
                System.out.println("Server: Authenticating DRIVER...");
                SessionDriver session = authenticateDriver(emailOrUsername, password);
                out.writeObject(session);
                System.out.println("Server: DRIVER authentication complete.");
            } else {
                System.out.println("Server: Authenticating " + userType + "...");
                boolean isAuthenticated = authenticateUserOrAdmin(userType, emailOrUsername, password);
                out.writeObject(isAuthenticated);
                System.out.println("Server: " + userType + " authentication complete.");
            }
        } catch (Exception e) {
            System.err.println("Server: Exception occurred while handling a client.");
            e.printStackTrace();
        }
    }
    private static SessionDriver authenticateDriver(String emailOrUsername, String password) throws SQLException {
        System.out.println("Server: Calling login method for DRIVER.");
        LoginController log = new LoginController();
        SessionDriver sd = log.loginDriver(emailOrUsername, password);
        System.out.println("Server: Login method for DRIVER returned a session.");
        return sd;
    }

    private static boolean authenticateUserOrAdmin(String userType, String emailOrUsername, String password) {
        System.out.println("Server: Calling login method for " + userType + ".");
        LoginController log = new LoginController();
        boolean repons = log.login(emailOrUsername, password, userType);
        System.out.println("Server: Login method for " + userType + " returned: " + repons);
        return repons;
    }
}
