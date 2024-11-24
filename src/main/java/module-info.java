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
    requires java.sql;
    requires javafx.swing;
    requires java.dotenv;

    exports com.example.carpoolingapp.controller;
    exports com.example.carpoolingapp.view;

    opens com.example.carpoolingapp to javafx.fxml;
    exports com.example.carpoolingapp;
}