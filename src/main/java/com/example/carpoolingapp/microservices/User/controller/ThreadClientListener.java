package com.example.carpoolingapp.microservices.User.controller;


import java.util.List;

public interface ThreadClientListener {
    void onMessageReceived(String message);

    void stop() throws Exception;
}

