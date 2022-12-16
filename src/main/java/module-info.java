module pf.bb {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires unirest.java;
    requires com.google.gson;
    requires json.path;
    requires com.fasterxml.jackson.annotation;

    opens pf.bb to javafx.fxml;
    exports pf.bb;
    exports pf.bb.controller;
    exports pf.bb.model;
    opens pf.bb.controller to javafx.fxml;
}