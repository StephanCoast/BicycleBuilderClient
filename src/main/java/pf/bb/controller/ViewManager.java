package pf.bb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pf.bb.Main;

import java.io.IOException;
import java.util.Objects;

public class ViewManager {

    public void forceView(ActionEvent e, String fileName, String title, String cssFileName) throws IOException {

        Stage stage;
        Scene scene;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pf/bb/fxml/" + fileName)));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/" + cssFileName)).toExternalForm());
        stage.setTitle(title);
        stage.setMinHeight(600.00);
        stage.setMinWidth(800.00);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    public void forceLoginView(ActionEvent e, String fileName, String title, String cssFileName) throws IOException {

        Stage stage;
        Scene scene;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pf/bb/fxml/" + fileName)));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/" + cssFileName)).toExternalForm());
        stage.setTitle(title);
        stage.setWidth(300.00);
        stage.setHeight(250.00);
        stage.setResizable(false);
        stage.setScene(scene);
        Rectangle2D sb = Screen.getPrimary().getVisualBounds();
        stage.setX((sb.getWidth() - stage.getWidth()) / 2);
        stage.setY((sb.getHeight() - stage.getHeight()) / 2);
        stage.show();
    }
}
