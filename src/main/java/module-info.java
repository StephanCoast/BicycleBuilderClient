module pf.bb {
    requires javafx.controls;
    requires javafx.fxml;


    opens pf.bb to javafx.fxml;
    exports pf.bb;
    exports pf.bb.controller;
    opens pf.bb.controller to javafx.fxml;
}