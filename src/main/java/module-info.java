module pf.bb {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;

    opens pf.bb to javafx.fxml;
    exports pf.bb;
    exports pf.bb.controller;
    exports pf.bb.model;
    opens pf.bb.controller to javafx.fxml;
}