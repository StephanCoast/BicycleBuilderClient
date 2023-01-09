package pf.bb.controller;

import com.jfoenix.controls.JFXDrawer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewManager {

    private static final ViewManager instance = new ViewManager();
    private Stage stage;
    private Scene scene;
    private final Double defaultWidth = 1280.00;
    private final Double defaultHeight = 800.00;
    private Double oldWidth;
    private Double oldHeight;
    private static final String OS = System.getProperty("os.name");

    public ViewManager() {
    }

    public static ViewManager getInstance() {
        return instance;
    }

    public void forceView(ActionEvent e, String fileName, String title, Boolean comingFromLogin) throws IOException {
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

    public void forceDrawerView(JFXDrawer drawer, BorderPane category) throws  IOException {
        drawer.setSidePane(category);
        drawer.setVisible(true); /* AR: BugFix - JFXButton-Events not passing the JFXDrawer UI */
        drawer.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, Event::consume); /* AR: BugFix - to prevent Mouse-Dragging on JFXDrawers */
        drawer.open();
    }

    private void centerLoginScene(Stage stage) {
        Rectangle2D sb = Screen.getPrimary().getVisualBounds();
        stage.setX((sb.getWidth() - stage.getWidth()) / 2);
        stage.setY((sb.getHeight() - stage.getHeight()) / 2);
    }

    private void calcWindowSize(Stage stage, Double oldWidth, Double oldHeight) {
        if (oldWidth < defaultWidth || oldWidth > defaultWidth || oldHeight < defaultHeight || oldHeight > defaultHeight) {
            stage.setWidth(oldWidth);
            stage.setHeight(oldHeight);
        }
    }
}
