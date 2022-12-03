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
import java.util.HashSet;
import java.util.Objects;

public class ViewManager {

    HashSet<JFXDrawer> drawersBuilderSide, drawersDashboard, drawersBuilderBottom;

    public ViewManager() {
    }

    public void setBuilderSideDrawers(JFXDrawer drawerDefault, JFXDrawer drawerFinish, JFXDrawer drawerCat1, JFXDrawer drawerCat2, JFXDrawer drawerCat3, JFXDrawer drawerCat4, JFXDrawer drawerCat5, JFXDrawer drawerCat6, JFXDrawer drawerCat7, JFXDrawer drawerCat8) {
        drawersBuilderSide = new HashSet<JFXDrawer>();
        drawersBuilderSide.add(drawerDefault);
        drawersBuilderSide.add(drawerFinish);
        drawersBuilderSide.add(drawerCat1);
        drawersBuilderSide.add(drawerCat2);
        drawersBuilderSide.add(drawerCat3);
        drawersBuilderSide.add(drawerCat4);
        drawersBuilderSide.add(drawerCat5);
        drawersBuilderSide.add(drawerCat6);
        drawersBuilderSide.add(drawerCat7);
        drawersBuilderSide.add(drawerCat8);
    }

    public void setBuilderBottomDrawers(JFXDrawer drawerCats, JFXDrawer drawerData) {
        drawersBuilderBottom = new HashSet<JFXDrawer>();
        drawersBuilderBottom.add(drawerCats);
        drawersBuilderBottom.add(drawerData);
    }

    public void setDashboardDrawers(JFXDrawer drawerAdmin, JFXDrawer drawerProfile) {
        drawersDashboard = new HashSet<JFXDrawer>();
        drawersDashboard.add(drawerAdmin);
        drawersDashboard.add(drawerProfile);
    }

    public void forceView(ActionEvent e, String fileName, String title, String cssFileName) throws IOException {

        Stage stage;
        Scene scene;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pf/bb/fxml/" + fileName)));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/" + cssFileName)).toExternalForm());
        stage.setTitle(title);
        stage.setMinHeight(720.00);
        stage.setMinWidth(1280.00);
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

    public void forceSidebarView(ActionEvent e, JFXDrawer drawer, BorderPane category) throws  IOException {
        for (JFXDrawer i : drawersBuilderSide) {
            i.close();
            i.setVisible(false);
        }
        drawer.setSidePane(category);
        drawer.setVisible(true); /* AR: BugFix - JFXButton-Events not passing the JFXDrawer UI */
        drawer.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, Event::consume); /* AR: BugFix - to prevent Mouse-Dragging on JFXDrawers */
        drawer.open();
    }

    public void forceSidebarInitView(JFXDrawer drawer, BorderPane category) throws  IOException {
        for (JFXDrawer i : drawersBuilderSide) {
            i.close();
            i.setVisible(false);
        }
        drawer.setSidePane(category);
        drawer.setVisible(true); /* AR: BugFix - JFXButton-Events not passing the JFXDrawer UI */
        drawer.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, Event::consume); /* AR: BugFix - to prevent Mouse-Dragging on JFXDrawers */
        drawer.open();
    }

    public void forceBottomDashboardView(ActionEvent e, JFXDrawer drawer, BorderPane category) throws  IOException {
        for (JFXDrawer i : drawersDashboard) {
            i.close();
            i.setVisible(false);
        }
        drawer.setSidePane(category);
        drawer.setVisible(true); /* AR: BugFix - JFXButton-Events not passing the JFXDrawer UI */
        drawer.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, Event::consume); /* AR: BugFix - to prevent Mouse-Dragging on JFXDrawers */
        drawer.open();
    }

    public void forceBottomBuilderView(ActionEvent e, JFXDrawer drawer, BorderPane category) throws  IOException {
        for (JFXDrawer i : drawersBuilderBottom) {
            i.close();
            i.setVisible(false);
        }
        drawer.setSidePane(category);
        drawer.setVisible(true); /* AR: BugFix - JFXButton-Events not passing the JFXDrawer UI */
        drawer.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, Event::consume); /* AR: BugFix - to prevent Mouse-Dragging on JFXDrawers */
        drawer.open();
    }
}
