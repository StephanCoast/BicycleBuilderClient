package pf.bb.controller;

import com.jfoenix.controls.JFXDrawer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashSet;

public class BuilderController {

    public ToggleGroup catsTogglegroup;
    public BorderPane cat1, cat2, cat3, cat4, cat5, cat6, cat7, cat8, catDefault, catFinish, bpCats, bpCustomerData;
    public JFXDrawer drawerDefault, drawerCat1, drawerCat2, drawerCat3, drawerCat4, drawerCat5, drawerCat6, drawerCat7, drawerCat8, drawerBottomCats, drawerBottomData, drawerFinish;
    private HashSet<JFXDrawer> drawersBuilderSide, drawersBuilderBottom;
    ViewManager vm = ViewManager.getInstance();

    public BuilderController() {
    }

    @FXML
    public void initialize() throws IOException {
        setupSideDrawersSet(drawerDefault, drawerFinish, drawerCat1, drawerCat2, drawerCat3, drawerCat4, drawerCat5, drawerCat6, drawerCat7, drawerCat8);
        setupBottomDrawersSet(drawerBottomCats, drawerBottomData);
        closeAllSideDrawers();
        closeAllBottomDrawers();
        vm.forceDrawerView(drawerDefault, catDefault);
        vm.forceDrawerView(drawerBottomCats, bpCats);
        deactivateToggleDeselect();
    }

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login");
    }

    public void openDashboard(ActionEvent event) throws IOException {
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
    }

    public void openCustomerDataView(ActionEvent event) throws IOException {
        vm.forceDrawerView(drawerBottomData, bpCustomerData);
    }

    public void openSidebarDefault(ActionEvent event) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerDefault, catDefault);
        if (catsTogglegroup.getSelectedToggle() != null) {
            catsTogglegroup.getSelectedToggle().setSelected(false);
        }
    }

    // AR: "Entwurf speichern"
    // todo: aktueller Entwurf/Konfiguration muss gespeichert werden -> Server?
    // Stephan
    public void onSaveDraft(ActionEvent event) {

    }

    private void deactivateToggleDeselect() {
        catsTogglegroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == null) { oldValue.setSelected(true); }
            }
        });
    }

    public void openSidebarCat1(ActionEvent event) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat1, cat1);
    }

    public void openSidebarCat2(ActionEvent event) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat2, cat2);
    }

    public void openSidebarCat3(ActionEvent event) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat3, cat3);
    }

    public void openSidebarCat4(ActionEvent event) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat4, cat4);
    }

    public void openSidebarCat5(ActionEvent event) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat5, cat5);
    }

    public void openSidebarCat6(ActionEvent event) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat6, cat6);
    }

    public void openSidebarCat7(ActionEvent event) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat7, cat7);
    }

    public void openSidebarCat8(ActionEvent event) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat8, cat8);
    }

    // AR: hier wird "einzeln" bestimmt was nach dem Speichern der Kategorie 1-8 passieren soll
    // todo: gespeicherte Lenker, Räder etc. Einstellung muss in die Konfiguration überführt werden -> Server?
    // Stephan
    public void onCat1Save(ActionEvent event) throws IOException {onCatSave(event);}
    public void onCat2Save(ActionEvent event) throws IOException {onCatSave(event);}
    public void onCat3Save(ActionEvent event) throws IOException {onCatSave(event);}
    public void onCat4Save(ActionEvent event) throws IOException {onCatSave(event);}
    public void onCat5Save(ActionEvent event) throws IOException {onCatSave(event);}
    public void onCat6Save(ActionEvent event) throws IOException {onCatSave(event);}
    public void onCat7Save(ActionEvent event) throws IOException {onCatSave(event);}
    public void onCat8Save(ActionEvent event) throws IOException {onCatSave(event);}

    // AR: hier wird "allgemein" bestimmt was nach dem Speichern der Katergorie 1-8 passieren soll
    // todo: gespeicherte Lenker, Räder etc. Einstellung muss in die Konfiguration überführt werden -> Server?
    // Stephan
    private void onCatSave(ActionEvent e) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerDefault, catDefault);
        if (catsTogglegroup.getSelectedToggle() != null) {
            catsTogglegroup.getSelectedToggle().setSelected(false);
        }
    }

    public void onBottomBarClose(ActionEvent event) throws IOException {
        closeAllBottomDrawers();
        vm.forceDrawerView(drawerBottomCats, bpCats);
    }

    // AR: "Abschliessen" - hier wird bestimmt was nach Eingabe der Kundendaten passiert
    // todo: Kundendaten speichern bzw weiterverarbeiten -> Server
    // Stephan
    public void onBottomBarFinish(ActionEvent event) throws IOException {
        closeAllBottomDrawers();
        vm.forceDrawerView(drawerBottomCats, bpCats);
        vm.forceDrawerView(drawerFinish, catFinish);
        catsTogglegroup.getToggles().forEach(toggle -> {
            Node node = (Node) toggle ;
            node.setDisable(true);
        });
    }

    private void setupSideDrawersSet(JFXDrawer drawerDefault, JFXDrawer drawerFinish, JFXDrawer drawerCat1, JFXDrawer drawerCat2, JFXDrawer drawerCat3, JFXDrawer drawerCat4, JFXDrawer drawerCat5, JFXDrawer drawerCat6, JFXDrawer drawerCat7, JFXDrawer drawerCat8) {
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

    private void setupBottomDrawersSet(JFXDrawer drawerCats, JFXDrawer drawerData) {
        drawersBuilderBottom = new HashSet<JFXDrawer>();
        drawersBuilderBottom.add(drawerCats);
        drawersBuilderBottom.add(drawerData);
    }

    private void closeAllBottomDrawers() {
        for (JFXDrawer i : drawersBuilderBottom) {
            i.close();
            i.setVisible(false);
        }
    }

    private void closeAllSideDrawers() {
        for (JFXDrawer i : drawersBuilderSide) {
            i.close();
            i.setVisible(false);
        }
    }
}
