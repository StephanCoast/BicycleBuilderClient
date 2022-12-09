package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pf.bb.Main;
import pf.bb.model.User;
import pf.bb.task.PostLoginTask;

import java.io.IOException;

public class LoginController {

    public static User activeUser; // user who is currently logged in

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private Label loginFailure;


    public void authenticate(ActionEvent event) throws IOException {

        PostLoginTask loginTask = new PostLoginTask(username.getText(), password.getText());
        loginTask.setOnSucceeded((WorkerStateEvent e2) -> {

            activeUser = loginTask.getValue();
            if (activeUser == null) {
                // login failed
                loginFailure.setVisible(true);
                return;
            }

            // query the bookings of the active user
            Main.queryActiveUserBookings(null);

            // navigate to catalog view
            MainController.getInstance().changeView("Dashboard");
//            MainController.getInstance().menuBtn.setVisible(true);
        });
        new Thread(loginTask).start();


//        ViewManager vm = new ViewManager();
//        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", "Dashboard.css");

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
