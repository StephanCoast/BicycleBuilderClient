package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

public class LoginController {

    ViewManager vm = ViewManager.getInstance();
    ValidatorManager validatorManager = ValidatorManager.getInstance();
    public JFXTextField username;
    public JFXPasswordField password;
    public RequiredFieldValidator validatorName, validatorPW;

    @FXML
    public void initialize() {
        validatorManager.initTextValidators(username, validatorName);
        validatorManager.initPasswordValidators(password, validatorPW);
    }

    public void authenticate(ActionEvent event) throws IOException {
        // todo: nach stephanLogin() verschieben beim erfolgreichen Login
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
        // Stephan loginTask - zur Trennung
        stephanLogin();
    }

    //
    private void stephanLogin() {
        // alles was du in Login machen möchtest bitte hier rein
        // benenne die methode wie du willst
    }

    // todo: eventuell für fehlerhaften Login ebenfalls eine Alert-Box
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
