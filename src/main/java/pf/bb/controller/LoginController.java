package pf.bb.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

public class LoginController {
    public void authenticate(ActionEvent event) throws IOException {
        ViewManager vm = new ViewManager();
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
    }

    public void close() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Bicycle Builder");
        alert.setHeaderText("Möchten Sie die Anwendung schließen?");
        alert.setContentText(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }
}
