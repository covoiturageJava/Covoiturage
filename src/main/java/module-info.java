module com.example.carpoolingapp {
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.controls;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.swing;
    requires java.dotenv;
    requires com.google.gson;
    requires jdk.httpserver;
    requires java.net.http;
    requires org.json;
    requires org.java_websocket;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires jdk.jsobject;
    requires eu.hansolo.toolbox;

    exports com.example.carpoolingapp.microservices.auth.controller;
    exports com.example.carpoolingapp.microservices.auth.view;
    exports com.example.carpoolingapp.microservices.Drivers.view;
    exports com.example.carpoolingapp.microservices.Admin.View;
    exports com.example.carpoolingapp.microservices.User.controller;

    opens com.example.carpoolingapp to javafx.fxml;
    exports com.example.carpoolingapp;
    exports com.example.carpoolingapp.microservices.User.view;
    opens com.example.carpoolingapp.microservices.User.view to javafx.fxml;
}