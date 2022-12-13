package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import pf.bb.model.User;
import pf.bb.task.PostLoginTask;

import java.io.IOException;

public class LoginController {

    ViewManager vm = ViewManager.getInstance();


    public static User activeUser; // user who is currently logged in
    @FXML
    public JFXTextField username;
    @FXML
    public JFXPasswordField password;
    @FXML
    public Label loginFailure;

    public void authenticate(ActionEvent event) throws IOException {
        // todo: nach stephanLogin() verschieben beim erfolgreichen Login
        //vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
        // Stephan loginTask - zur Trennung
        stephanLogin(event);
    }

    //
    private void stephanLogin(ActionEvent event) {
        // alles was du in Login machen möchtest bitte hier rein
        // benenne die methode wie du willst

        PostLoginTask loginTask = new PostLoginTask(username.getText(), password.getText());
        loginTask.setOnSucceeded((WorkerStateEvent e2) -> {

            activeUser = loginTask.getValue();
            if (activeUser == null) {
                // login failed
                loginFailure.setVisible(true);
            } else {
                try {
                    vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        new Thread(loginTask).start();
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
