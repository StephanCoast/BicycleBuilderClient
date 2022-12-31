package pf.bb.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import pf.bb.Main;
import pf.bb.model.Article;

import java.io.IOException;
import java.util.HashSet;

public class BuilderController {

    public RequiredFieldValidator validatorCustomerID, validatorCustomerFirstName, validatorCustomerLastName, validatorCustomerMail;
    public RequiredFieldValidator validatorCustomerStreet, validatorCustomerNr, validatorCustomerZipcode, validatorCustomerCity;
    public JFXTextField tfCustomerID, tfCustomerFirstName, tfCustomerLastName, tfCustomerMail, tfCustomerStreet, tfCustomerNr, tfCustomerZipcode, tfCustomerCity;
    public AnchorPane anchorContainer;
    public JFXButton btnHeaderHome, btnSaveDraft, btnAddCustomerData, btnSidebarHome;
    private Boolean catIsOpen;
    public ToggleGroup catsTogglegroup, cat1TogglegroupColor, cat1TogglegroupSize, cat2TogglegroupColor, cat3TogglegroupColor, cat3TogglegroupSize, cat4TogglegroupColor;
    public JFXComboBox<String> cat1SelectName, cat2SelectModel, cat2SelectGrip, cat3SelectModel, cat3SelectTyre, cat4SelectModel, cat5SelectModel, cat6SelectBell, cat6SelectStand, cat6SelectLight;
    public BorderPane cat1, cat2, cat3, cat4, cat5, cat6, catDefault, catFinish, bpCats, bpCustomerData;
    public JFXDrawer drawerDefault, drawerCat1, drawerCat2, drawerCat3, drawerCat4, drawerCat5, drawerCat6, drawerBottomCats, drawerBottomData, drawerFinish;
    private HashSet<JFXDrawer> drawersBuilderSide, drawersBuilderBottom;
    ViewManager vm = ViewManager.getInstance();
    ValidatorManager validatorManager = ValidatorManager.getInstance();

    public BuilderController() {
    }

    @FXML
    public void initialize() throws IOException {
        catIsOpen = false;
        setupSideDrawersSet(drawerDefault, drawerFinish, drawerCat1, drawerCat2, drawerCat3, drawerCat4, drawerCat5, drawerCat6);
        setupBottomDrawersSet(drawerBottomCats, drawerBottomData);
        closeAllSideDrawers();
        closeAllBottomDrawers();
        vm.forceDrawerView(drawerDefault, catDefault);
        vm.forceDrawerView(drawerBottomCats, bpCats);
        onToggleDeselect();
        onToggleDeselectSubCat(cat1TogglegroupColor);
        onToggleDeselectSubCat(cat1TogglegroupSize);
        onToggleDeselectSubCat(cat2TogglegroupColor);
        onToggleDeselectSubCat(cat3TogglegroupColor);
        onToggleDeselectSubCat(cat3TogglegroupSize);
        onToggleDeselectSubCat(cat4TogglegroupColor);
        setupValidators();

        /* AR: set dummy data for all sidebar cat-selects */
        ObservableList<String> data = FXCollections.observableArrayList("test1", "test2", "test3");
        cat1SelectName.setItems(data);
        cat2SelectModel.setItems(data);
        cat2SelectGrip.setItems(data);
        cat3SelectModel.setItems(data);
        cat3SelectTyre.setItems(data);
        cat4SelectModel.setItems(data);
        cat5SelectModel.setItems(data);
        cat6SelectBell.setItems(data);
        cat6SelectStand.setItems(data);
        cat6SelectLight.setItems(data);
    }

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login");
    }

    public void openDashboard(ActionEvent event) throws IOException {
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
    }

    public void openCustomerDataView(ActionEvent event) throws IOException {
        vm.forceDrawerView(drawerBottomData, bpCustomerData);
        deactivateButtonsOnCustomerEdit();
    }

    public void openSidebarDefault(ActionEvent event) throws IOException {
        catIsOpen = false;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerDefault, catDefault);
        deselectAllToggles();
    }

    // AR: "Entwurf speichern"
    // todo: aktueller Entwurf/Konfiguration muss gespeichert werden -> Server? danach Weiterleitung Dashboard
    // Stephan
    public void onSaveDraft(ActionEvent event) throws IOException {
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
    }

    public void openSidebarCat1(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat1, cat1);

        /* AR: for testing only
        for (Article i : Main.ARTICLES) {
            if (i.type.equals("Rahmen")) {
                System.out.println("AR-Test: " + i.name);
            }
        }
         */
    }

    public void openSidebarCat2(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat2, cat2);
    }

    public void openSidebarCat3(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat3, cat3);
    }

    public void openSidebarCat4(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat4, cat4);
    }

    public void openSidebarCat5(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat5, cat5);
    }

    public void openSidebarCat6(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat6, cat6);
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

    // AR: hier wird "allgemein" bestimmt was nach dem Speichern der Kategorie 1-8 passieren soll
    // todo: gespeicherte Lenker, Räder etc. Einstellung muss in die Konfiguration überführt werden -> Server?
    // Stephan
    private void onCatSave(ActionEvent e) throws IOException {
        closeAllSideDrawers();
        vm.forceDrawerView(drawerDefault, catDefault);
        catIsOpen = false;
        deselectAllToggles();
    }

    public void onBottomBarClose(ActionEvent event) throws IOException {
        closeAllBottomDrawers();
        vm.forceDrawerView(drawerBottomCats, bpCats);
        setDefaultFocus();
        activateButtonsOnCustomerEdit();
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
        setDefaultFocus();
        activateButtonsOnCustomerEdit();
    }

    // AR: Auftrag Button
    // todo: PDF erstellen und anzeigen/herunterladen
    public void onFinalOrder(ActionEvent event) {

    }

    // AR: Rechnung Button
    // todo: PDF erstellen und anzeigen/herunterladen
    public void onFinalInvoice(ActionEvent event) {

    }

    private void setupSideDrawersSet(JFXDrawer drawerDefault, JFXDrawer drawerFinish, JFXDrawer drawerCat1, JFXDrawer drawerCat2, JFXDrawer drawerCat3, JFXDrawer drawerCat4, JFXDrawer drawerCat5, JFXDrawer drawerCat6) {
        drawersBuilderSide = new HashSet<JFXDrawer>();
        drawersBuilderSide.add(drawerDefault);
        drawersBuilderSide.add(drawerFinish);
        drawersBuilderSide.add(drawerCat1);
        drawersBuilderSide.add(drawerCat2);
        drawersBuilderSide.add(drawerCat3);
        drawersBuilderSide.add(drawerCat4);
        drawersBuilderSide.add(drawerCat5);
        drawersBuilderSide.add(drawerCat6);
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

    private void onToggleDeselect() {
        catsTogglegroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (catIsOpen) {
                if (newValue == null) {
                    oldValue.setSelected(true);
                }
            }
        });
    }

    private void onToggleDeselectSubCat(ToggleGroup tg) {
        tg.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            }
        });
    }

    private void deselectAllToggles() {
        if (catsTogglegroup.getSelectedToggle() != null) {
            catsTogglegroup.getSelectedToggle().setSelected(false);
        }
    }

    private void setupValidators() {
        validatorManager.initTextValidators(tfCustomerID, validatorCustomerID);
        validatorManager.initTextValidators(tfCustomerFirstName, validatorCustomerFirstName);
        validatorManager.initTextValidators(tfCustomerLastName, validatorCustomerLastName);
        validatorManager.initTextValidators(tfCustomerMail, validatorCustomerMail);
        validatorManager.initTextValidators(tfCustomerStreet, validatorCustomerStreet);
        validatorManager.initTextValidators(tfCustomerNr, validatorCustomerNr);
        validatorManager.initTextValidators(tfCustomerZipcode, validatorCustomerZipcode);
        validatorManager.initTextValidators(tfCustomerCity, validatorCustomerCity);
    }

    private void setDefaultFocus() {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                anchorContainer.requestFocus();
            }
        });
    }

    private void deactivateButtonsOnCustomerEdit() {
        btnHeaderHome.setDisable(true);
        btnAddCustomerData.setDisable(true);
        btnSaveDraft.setDisable(true);
        btnSidebarHome.setDisable(true);
    }

    private void activateButtonsOnCustomerEdit() {
        btnHeaderHome.setDisable(false);
        btnAddCustomerData.setDisable(false);
        btnSaveDraft.setDisable(false);
        btnSidebarHome.setDisable(false);
    }
}
