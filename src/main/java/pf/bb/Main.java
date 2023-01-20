package pf.bb;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import pf.bb.model.Article;
import pf.bb.model.Configuration;
import pf.bb.task.PutConfigurationWriteAccessTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static pf.bb.controller.LoginController.activeUser;

/**
 * Main Hauptklasse der JavaFX-Anwendung zum Starten des Bicycle Builder Client.
 * @author Alexander Rauch, Stephan Kost
 * supported by Juliane Müller
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class Main extends Application {

    /**
     * Variablendeklaration einer Konstanten für den Pfad zum AppIcon.
     */
    public static final Image APP_ICON = new Image(Objects.requireNonNull(Main.class.getResource("/img/bbAppIcon.png")).toExternalForm(), false);
    /**
     * Variablendeklaration zur Kommunikation mit dem Server.
     * Array-Listen für Artikel und Konfigurationen
     * Konfigurationsobjekt
     * Boolean für den Schreibzugriff
     * Host-URL String
     */
    public static final ArrayList<Article> ARTICLES = new ArrayList<>();
    public static ObservableList<Configuration> CONFIGURATIONS = FXCollections.observableArrayList();
    public static Configuration currentConfig = null;
    public static boolean writeAccessGiven = false;
    public static final String API_HOST = "http://localhost:8080";

    /**
     * Standardmäßige Methode in jeder JavaFX-Anwendung.
     * @param stageMain the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException Fehlerbehandlung
     * Artikeldaten und erste Ansicht werden geladen.
     */
    @Override
    public void start(Stage stageMain) throws IOException {
        loadFirstClientView(stageMain);
    }

    /**
     * Definiert die erste Ansicht, das Login-Fenster.
     * @param stageMain the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException Fehlerbehandlung
     * FXML-Dateipfad wird bestimmt, Scene wird erstellt.
     * Titel und App-Icon werden zugeordnet.
     * Fenster wird zentriert und Fade-Animation wird geladen.
     */
    private void loadFirstClientView(Stage stageMain) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Login.fxml"));
        AnchorPane apane = fxmlLoader.load();
        Scene scene = new Scene(apane);
        stageMain.setTitle("Bicycle Builder - Login");
        stageMain.getIcons().add(APP_ICON);
        stageMain.setResizable(false);
        stageMain.setScene(scene);
        stageMain.setOnCloseRequest(windowsevent -> {
            if(Main.writeAccessGiven) {
                //return write Access
                PutConfigurationWriteAccessTask writeAccessTask1 = new PutConfigurationWriteAccessTask(activeUser, Main.currentConfig.id);
                writeAccessTask1.setOnRunning((runningEvent) -> System.out.println("trying switch writeAccess for configuration..."));
                writeAccessTask1.setOnSucceeded((WorkerStateEvent switchSuccess) -> {
                    System.out.println("OnExitBuilderController: writeAccess for configuration " + Main.currentConfig.id + " switched to: " + writeAccessTask1.getValue());
                    if (writeAccessTask1.getValue().startsWith("ACCESS RETURNED")) {
                        Main.writeAccessGiven = false; // flag zurücksetzen
                    } else {
                        System.out.println("OnExitBuilderController: writeAccess for configuration " + Main.currentConfig.id + " could not be returned on close!");
                    }
                });
                writeAccessTask1.setOnFailed((writeAccessFailed) -> System.out.println("Couldn't switch writeAccess for configuration. Server error."));
                //Tasks in eigenem Thread ausführen
                new Thread(writeAccessTask1).start();
            }
        });
        stageMain.show();
        centerLoginScene(stageMain);
        fadeLoginScene(apane);
    }

    /**
     * Zentriert das Fenster mittig auf dem vorhandenen Bildschirm.
     * @param stage Stage, die verwendet werden soll
     */
    private void centerLoginScene(Stage stage) {
        Rectangle2D sb = Screen.getPrimary().getVisualBounds();
        stage.setX((sb.getWidth() - stage.getWidth()) / 2);
        stage.setY((sb.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * Bringt das Login-Fenster mit einer Fade-Animation zur Ansicht.
     * @param pane Container, der verwendet werden soll
     * Animationsdauer: 2 Sekunden
     */
    private void fadeLoginScene(AnchorPane pane) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), pane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * Gibt das Konfigurationsobjekt anhand der gegebenen Konfiguration-ID zurück.
     * @param configId Konfiguration-ID
     * @return Konfigurationsobjekt
     */
    public static Configuration findConfigurationById(int configId) {
        Configuration localConfigObject = null;
        for (Configuration cfg : CONFIGURATIONS) {
            if (cfg.id == configId)
                localConfigObject = cfg;
        }
        return localConfigObject;
    }

    /**
     * JavaFX-Main Start Methode
     * @param args keine
     */
    public static void main(String[] args) {
        launch();
    }
}