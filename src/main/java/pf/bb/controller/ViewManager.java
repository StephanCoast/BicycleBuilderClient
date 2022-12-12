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

    public ViewManager() {
    }

    public static ViewManager getInstance() {
        return instance;
    }

    public void forceView(ActionEvent e, String fileName, String title) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pf/bb/fxml/" + fileName)));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle(title);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
        setFullScreenScene(stage);
    }

    public void forceLoginView(ActionEvent e, String fileNameLogin, String title) throws IOException {
        Parent rootLogin = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pf/bb/fxml/" + fileNameLogin)));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(rootLogin);
        stage.setTitle(title);
        stage.setWidth(325.00);
        stage.setHeight(300.00); /* AR: for some reason original SB size of 275 got compressed */
        stage.setResizable(false);
        stage.setScene(scene);
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

    private void setFullScreenScene(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }
}
