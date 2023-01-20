package pf.bb.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import pf.bb.Main;
import pf.bb.model.*;
import pf.bb.task.*;
import java.io.IOException;

/**
 * Diese Klasse steuert die Login-Ansicht des BicycleBuilder und alle enthaltenen Elemente.
 * @author Alexander Rauch
 * supported by Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class LoginController {

    /**
     * Zur Kommunikation mit dem Backend.
     * Bestimmt den aktuell angemeldeten Benutzer.
     */
    public static User activeUser;

    /**
     * Variablendeklaration für die Text-Boxen Benutzername und Passworteingabe.
     */
    @FXML
    public JFXTextField username;
    @FXML
    public JFXPasswordField password;

    /**
     * Label zur Anzeige bei fehlerhaftem Login-Versuch.
     */
    @FXML
    public Label loginFailure;
    /**
     * Variablendeklaration für den Login-Button.
     */
    public JFXButton btnLogin;
    /**
     * Variablendeklaration für verschiedene Singleton-Instanzen.
     * ViewManager = steuert die verschiedenen Ansichten, stellt Methoden bereit
     * ValidatorManager = initialisiert einzelne Validator, stellt Methoden bereit
     */
    ViewManager vm = ViewManager.getInstance();
    ValidatorManager validatorManager = ValidatorManager.getInstance();

    /**
     * Variablendeklaration für die Validierung's-Objekte des Benutzer-Textfeldes und Passwort-Textfeldes.
     * Wurden in Login.fxml gesetzt.
     */
    public RequiredFieldValidator validatorName;
    public RequiredFieldValidator validatorPW;

    /**
     * Standard-Konstruktor der Klasse
     */
    public LoginController() {
    }

    /**
     * FXML Konstruktor der Klasse
     * Zusammenfassung aller Funktionen, die beim Start geladen werden sollen.
     * Regelt den Initial-Status des Logins.
     * Validatoren werden dem Benutzer- und Passwort-Textfeld zugeordnet.
     */
    @FXML
    public void initialize() {
        validatorManager.initTextValidators(username, validatorName);
        validatorManager.initPasswordValidators(password, validatorPW);
        setLoginOnEnter();

        // for developing only
        // todo: must be removed for shipping, causes label float bug
        username.setText("Consultant");
        password.setText("osmi");
    }

    /**
     * Login Button
     * @param event Click-Event des Buttons
     * supported by SK
     * Login-Daten werden überprüft und Benutzer Detail-Informationen werden zugewiesen.
     */
    public void authenticate(ActionEvent event){

        PostLoginTask loginTask = new PostLoginTask(username.getText(), password.getText());

        loginTask.setOnFailed((WorkerStateEvent loginFailed) -> {
            System.out.println("Der Server ist nicht erreichbar!");
            loginFailure.setText("Der Server ist nicht erreichbar!");
            loginFailure.setVisible(true);
        });

        loginTask.setOnSucceeded((WorkerStateEvent e2) -> {
            activeUser = loginTask.getValue();
            if (activeUser == null) {
                // login failed
                loginFailure.setVisible(true);
            } else {
                // load articles
                loadStaticData();

                GetUserDetailsTask userDetailsTask = new GetUserDetailsTask(LoginController.activeUser);
                userDetailsTask.setOnSucceeded((WorkerStateEvent e5) -> {
                    activeUser.id = userDetailsTask.getValue().id;
                    activeUser.role = userDetailsTask.getValue().role;
                    activeUser.name = userDetailsTask.getValue().name;
                    activeUser.forename = userDetailsTask.getValue().forename;
                    activeUser.lastname = userDetailsTask.getValue().lastname;
                    activeUser.email = userDetailsTask.getValue().email;

                    try {
                        // DASHBOARD VIEW LADEN NACH ERFOLGREICHER AUTHENTIFIZIERUNG
                        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                new Thread(userDetailsTask).start();
            }
        });
        new Thread(loginTask).start();
    }

    /**
     * Get Articles Task.
     * Alle vorhandenen Artikel werden der Array-Liste hinzugefügt.
     */
    private void loadStaticData() {
        // query all articles from REST API with Task Thread before Login -> faster
        GetArticlesTask articlesTask = new GetArticlesTask();
        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
        articlesTask.setOnRunning((successEvent) -> System.out.println("loading articles..."));
        articlesTask.setOnSucceeded((WorkerStateEvent e) -> {
            System.out.println("articles loaded.");
            Main.ARTICLES.addAll(articlesTask.getValue());
        });
        //Tasks in eigenem Thread ausführen
        new Thread(articlesTask).start();
    }

    /**
     * Login per Enter-Button bei vollständig ausgefüllten Textfeldern.
     * Cursor wird auf die letzte Stelle im Benutzernamen-Textfeld gesetzt.
     */
    private void setLoginOnEnter() {
        username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(()->{
                    username.deselect();
                    username.positionCaret(username.getLength());
                });
            }
        });

        username.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                btnLogin.fire();
            }
        });

        password.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                btnLogin.fire();
            }
        });
    }

    /**
     * Abbrechen Button
     * @throws IOException Fehlerbehandlung
     * Standard Frage-Dialog wird geöffnet, bei positiver Bestätigung wird die Anwendung geschlossen.
     */
    public void close() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Bicycle Builder");
        alert.setHeaderText("Möchten Sie die Anwendung schließen?");
        alert.setContentText(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        } else {
            username.resetValidation();
            password.resetValidation();
            username.requestFocus();
        }
    }
}