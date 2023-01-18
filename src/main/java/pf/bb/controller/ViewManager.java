package pf.bb.controller;

import com.jfoenix.controls.JFXDrawer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Diese Klasse regelt alle Ansichten des BicycleBuilder.
 * @author Alexander Rauch
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class ViewManager {

    /**
     * Variablendeklaration für die Singleton ViewManager Instanz.
     */
    private static final ViewManager instance = new ViewManager();
    /**
     * Variablendeklaration für die JavaFX Stage und Scene.
     */
    private Stage stage;
    private Scene scene;
    /**
     * Variablendeklaration für die Default-Fenster Dimensionen.
     */
    private final double defaultWidth = 1280.00;
    private final double defaultHeight = 800.00;
    /**
     * Variablendeklaration für das Merken veralteter Dimensions-Daten der Fenster.
     */
    private double oldWidth;
    private double oldHeight;
    /**
     * Variablendeklaration für das verwendete Betriebssystem.
     */
    private static final String OS = System.getProperty("os.name");

    /**
     * Standard-Konstruktor der Klasse
     */
    public ViewManager() {
    }

    /**
     * Rückgabe-Methode der Singleton Instanz.
     * @return ViewManager Instanz
     */
    public static ViewManager getInstance() {
        return instance;
    }

    /**
     * Wird aufgerufen, um die Hauptfenster Dashboard und Konfigurator anzuzeigen.
     * @param e Click-Event des Buttons
     * @param fileName FXML Dateiname
     * @param title Titel des Fensters
     * @param comingFromLogin Boolean wenn man vom Login-Fenster kommt
     * @throws IOException Fehlerbehandlung
     */
    public void forceView(ActionEvent e, String fileName, String title, boolean comingFromLogin) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pf/bb/fxml/" + fileName)));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle(title);

        if (comingFromLogin) {
            oldWidth = defaultWidth;
            oldHeight = defaultHeight;
        } else {
            oldWidth = stage.getWidth();
            oldHeight = stage.getHeight();
        }

        stage.setMinWidth(1280.00); /* AR: must be set here to prevent resizing to zero, SC ignores */
        stage.setMinHeight(800.00);
        stage.setWidth(defaultWidth);
        stage.setHeight(defaultHeight);
        stage.setResizable(true);
        stage.setScene(scene);
        if (!OS.equals("Linux")) {stage.sizeToScene();} /* AR: fix window stage scaling anomalies with Linux/Windows */
        stage.show();
        calcWindowSize(stage, oldWidth, oldHeight);
        if (comingFromLogin) { stage.centerOnScreen(); }
    }

    /**
     * Wird aufgerufen, um das Hauptfenster des Login anzuzeigen.
     * @param e Click-Event des Buttons
     * @param fileNameLogin FXML Dateiname
     * @param title Titel des Fensters
     * @throws IOException Fehlerbehandlung
     */
    public void forceLoginView(ActionEvent e, String fileNameLogin, String title) throws IOException {
        Parent rootLogin = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pf/bb/fxml/" + fileNameLogin)));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(rootLogin);
        stage.setTitle(title);
        stage.setWidth(400.00);
        stage.setHeight(475.00);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        centerLoginScene(stage);
    }

    /**
     * Wird aufgerufen, um die verschiedenen JFX-Drawer Container anzuzeigen.
     * @param drawer JFX-Drawer Container
     * @param category Container der Kategorie
     * @throws IOException Fehlerbehandlung
     */
    public void forceDrawerView(JFXDrawer drawer, BorderPane category) throws  IOException {
        drawer.setSidePane(category);
        drawer.setVisible(true); /* AR: BugFix - JFXButton-Events not passing the JFXDrawer UI */
        drawer.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, Event::consume); /* AR: BugFix - to prevent Mouse-Dragging on JFXDrawers */
        drawer.open();
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
     * Setzt die Fenster-Dimensionen entsprechend der aktuellen Fenstergröße.
     * @param stage Stage, die verwendet werden soll
     * @param oldWidth Alte Breite
     * @param oldHeight Alte Höhe
     */
    private void calcWindowSize(Stage stage, double oldWidth, double oldHeight) {
        if (oldWidth < defaultWidth || oldWidth > defaultWidth || oldHeight < defaultHeight || oldHeight > defaultHeight) {
            stage.setWidth(oldWidth);
            stage.setHeight(oldHeight);
        }
    }

    /**
     * Standard Warn-Dialog wird geöffnet.
     * @param title Titel des Fensters
     * @param headerText Überschrift
     * @param contentText Inhalt
     */
    public static void createWarningAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Standard Info-Dialog wird geöffnet.
     * @param title Titel des Fensters
     * @param headerText Überschrift
     * @param contentText Inhalt
     */
    public static void createInfoAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
