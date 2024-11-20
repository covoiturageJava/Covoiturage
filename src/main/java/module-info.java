module com.example.carpoolingapp {
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.controls;

    exports com.example.carpoolingapp.controller;
    exports com.example.carpoolingapp.view;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    opens com.example.carpoolingapp to javafx.fxml;
    exports com.example.carpoolingapp;
}