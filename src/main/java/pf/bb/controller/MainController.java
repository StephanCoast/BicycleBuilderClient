package pf.bb.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.scene.layout.StackPane;
import pf.bb.Main;

public class MainController {

    // singleton
    private static MainController instance;

    // singleton access
    public static MainController getInstance() {
        return instance;
    }

    @FXML
    StackPane viewHolder;

    @FXML
    Label title;

    @FXML
    JFXDrawersStack drawersStack;

    @FXML
    MaterialIconView menuBtn;

    JFXDrawer menuDrawer = new JFXDrawer();

    @FXML
    public void initialize() {
        instance = this;
        title.setText(Main.APP_TITLE);

        // menu on the left side
//        menuDrawer.setSidePane(Main.loadFXML("fxml/Main.fxml"));
//        menuDrawer.setDefaultDrawerSize(250);
//        menuBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> drawersStack.toggle(menuDrawer));
    }

    public void changeView(String fxmlFilename) {
        Node view = Main.loadFXML("fxml/" + fxmlFilename + ".fxml");
        viewHolder.getChildren().setAll(view); // clears the list of child elements and adds the view as a new child element
    }
}
