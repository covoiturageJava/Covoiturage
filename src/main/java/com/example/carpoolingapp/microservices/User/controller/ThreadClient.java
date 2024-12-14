package com.example.carpoolingapp.microservices.User.controller;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ThreadClient extends WebSocketClient {

    private final ThreadClientListener listener;

    public ThreadClient(URI serverUri, ThreadClientListener listener) {
        super(serverUri);
        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connecté au serveur WebSocket.");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Message reçu du serveur : " + message);
        listener.onMessageReceived(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Déconnecté du serveur. Raison : " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Erreur WebSocket : " + ex.getMessage());
    }
}