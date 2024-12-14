module com.example.carpoolingapp {
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.controls;

    requires org.controlsfx.controls;
   // requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires javafx.swing;
    requires java.dotenv;
    requires org.json;
    requires jdk.httpserver;

    exports com.example.carpoolingapp.microservices.auth.controller;
    exports com.example.carpoolingapp.microservices.auth.view;
    exports com.example.carpoolingapp.microservices.Drivers.view;
    exports com.example.carpoolingapp.microservices;


    opens com.example.carpoolingapp to javafx.fxml;
    exports com.example.carpoolingapp;
}