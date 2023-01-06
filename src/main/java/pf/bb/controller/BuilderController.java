package pf.bb.controller;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
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
    public JFXTextArea svgTextarea;
    public StackPane spaneSVG;
    private String svgInfoFrameModel, svgInfoFrameColor, svgInfoFrameSize;
    private String svgInfoHandlebarModel, svgInfoHandlebarColor, svgInfoHandlebarGrip;
    private String svgInfoWheelsModel, svgInfoWheelsColor, svgInfoWheelsSize, svgInfoWheelsTyre;
    private String svgInfoSaddleModel, svgInfoSaddleColor;
    private String svgInfoBrakesModel;
    private String svgInfoAttachmentsBell, svgInfoAttachmentsStand, svgInfoAttachmentsLight;
    private Boolean catIsOpen;
    public ToggleGroup catsTogglegroup, cat1TogglegroupColor, cat1TogglegroupSize, cat2TogglegroupColor, cat3TogglegroupColor, cat3TogglegroupSize, cat4TogglegroupColor;
    public JFXComboBox<String> cat1SelectName, cat2SelectModel, cat2SelectGrip, cat3SelectModel, cat3SelectTyre, cat4SelectModel, cat5SelectModel, cat6SelectBell, cat6SelectStand, cat6SelectLight;
    public BorderPane cat1, cat2, cat3, cat4, cat5, cat6, catDefault, catFinish, bpCats, bpCustomerData;
    public JFXDrawer drawerDefault, drawerCat1, drawerCat2, drawerCat3, drawerCat4, drawerCat5, drawerCat6, drawerBottomCats, drawerBottomData, drawerFinish;
    private HashSet<JFXDrawer> drawersBuilderSide, drawersBuilderBottom;
    ViewManager vm = ViewManager.getInstance();
    ValidatorManager validatorManager = ValidatorManager.getInstance();
    SVGManager svgManager = SVGManager.getInstance();

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
        initFirstSVGSet();
        renderSVGtextarea();
        initSubcatsListeners();

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

        setSubcatInitialSelectbox(cat1SelectName, 0);
        setSubcatInitialToggle(cat1TogglegroupColor,0);
        setSubcatInitialToggle(cat1TogglegroupSize,0);

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

        setSubcatInitialSelectbox(cat2SelectModel, 0);
        setSubcatInitialSelectbox(cat2SelectGrip, 0);
        setSubcatInitialToggle(cat2TogglegroupColor,0);
    }

    public void openSidebarCat3(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat3, cat3);

        setSubcatInitialSelectbox(cat3SelectModel, 0);
        setSubcatInitialSelectbox(cat3SelectTyre, 0);
        setSubcatInitialToggle(cat3TogglegroupColor,0);
        setSubcatInitialToggle(cat3TogglegroupSize,0);
    }

    public void openSidebarCat4(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat4, cat4);

        setSubcatInitialSelectbox(cat4SelectModel, 0);
        setSubcatInitialToggle(cat4TogglegroupColor,0);
    }

    public void openSidebarCat5(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat5, cat5);

        setSubcatInitialSelectbox(cat5SelectModel, 0);
    }

    public void openSidebarCat6(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat6, cat6);

        setSubcatInitialSelectbox(cat6SelectBell, 0);
        setSubcatInitialSelectbox(cat6SelectStand, 0);
        setSubcatInitialSelectbox(cat6SelectLight, 0);
    }

    private void setSubcatInitialSelectbox(JFXComboBox cb, int pos) {
        if (cb.getSelectionModel().isEmpty()) {
            cb.getSelectionModel().select(pos);
        }
    }

    private void setSubcatInitialToggle(ToggleGroup tg, int pos) {
        if (tg.getSelectedToggle() == null) {
            tg.getToggles().get(pos).setSelected(true);
        }
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

    private void initFirstSVGSet() {
        svgManager.setFrame(SVGManager.FRAME1);
        svgManager.setFrameColor(SVGManager.COLOR_BLACK);
        svgManager.setHandlebar(SVGManager.HANDLEBAR1);
        svgManager.setHandlebarColor(SVGManager.COLOR_BLACK);
        svgManager.setTire(SVGManager.TIRE1);
        svgManager.setTireColor(SVGManager.COLOR_BLACK);
        svgManager.setSeat(SVGManager.SEAT1);
        svgManager.setSeatColor(SVGManager.COLOR_BLACK);
        svgManager.setSVGSet();
        spaneSVG.getChildren().clear();
        spaneSVG.getChildren().add(SVGManager.svgGroup);

        fadeIn(SVGManager.svgGroup.getChildren().get(0), 1000);
        scaleInX(SVGManager.svgGroup.getChildren().get(1), 1000);
        scaleIn(SVGManager.svgGroup.getChildren().get(2), 1000);
        scaleInX(SVGManager.svgGroup.getChildren().get(3), 1000);
    }

    private void fadeIn(Node node, int ms) {
        FadeTransition ft = new FadeTransition(Duration.millis(ms), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private void scaleIn(Node node, int ms) {
        ScaleTransition st = new ScaleTransition(Duration.millis(ms), node);
        st.setFromX(0);
        st.setFromY(0);
        st.setFromZ(0);
        st.setByX(1);
        st.setByY(1);
        st.setByZ(1);
        st.play();
    }

    private void scaleInX(Node node, int ms) {
        ScaleTransition st = new ScaleTransition(Duration.millis(ms), node);
        st.setFromX(0);
        st.setByX(1);
        st.play();
    }

    private void renderSVGtextarea() {
        String newline = "\n";
        String sep = ", ";
        String frame = "Rahmen: ";
        String handlebar = "Lenker: ";
        String wheels = "Räder: ";
        String saddle = "Sattel: ";
        String brakes = "Bremsen: ";
        String attachments = "Zubehör: ";

        svgTextarea.clear();

        // Rahmen
        svgTextarea.appendText(frame + newline);
        svgTextarea.appendText(svgInfoFrameModel + sep + svgInfoFrameColor + sep + svgInfoFrameSize + newline + "Beschreibung" + newline + newline);
        // Lenker
        svgTextarea.appendText(handlebar + newline);
        svgTextarea.appendText( svgInfoHandlebarModel + sep + svgInfoHandlebarColor + sep + svgInfoHandlebarGrip + newline + "Beschreibung" + newline + newline);
        // Räder
        svgTextarea.appendText(wheels + newline);
        svgTextarea.appendText(svgInfoWheelsModel + sep + svgInfoWheelsColor + sep + svgInfoWheelsSize + sep + svgInfoWheelsTyre + newline + "Beschreibung" + newline + newline);
        // Sattel
        svgTextarea.appendText(saddle + newline);
        svgTextarea.appendText(svgInfoSaddleModel + sep + svgInfoSaddleColor + newline + "Beschreibung" + newline + newline);
        // Bremsen
        svgTextarea.appendText(brakes + newline);
        svgTextarea.appendText(svgInfoBrakesModel + sep + "Typ" + newline + "Beschreibung" + newline + newline);
        // Zubehör
        svgTextarea.appendText(attachments + newline);
        svgTextarea.appendText(svgInfoAttachmentsBell + sep + svgInfoAttachmentsStand + sep + svgInfoAttachmentsLight + newline);
    }

    // Rahmen
    private void initSubcatsListeners() {

        // Rahmen
        cat1SelectName.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoFrameModel = newValue;
                renderSVGtextarea();
                switch (svgInfoFrameModel) {
                    case "test1":
                        svgManager.setFrame(SVGManager.FRAME1);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(0), 500);
                        break;
                    case "test2":
                        svgManager.setFrame(SVGManager.FRAME2);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(0), 500);
                        break;
                    case "test3":
                        svgManager.setFrame(SVGManager.FRAME3);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(0), 500);
                        break;
                }
                spaneSVG.getChildren().clear();
                spaneSVG.getChildren().add(SVGManager.svgGroup);
            }
        });

        cat1TogglegroupColor.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (cat1TogglegroupColor.getSelectedToggle() != null) {
                    String fxid = ((JFXToggleNode) cat1TogglegroupColor.getSelectedToggle()).getId();
                    switch (fxid) {
                        case "cat1Color1":
                            svgInfoFrameColor = "schwarz";
                            renderSVGtextarea();
                            svgManager.setFrameColor(SVGManager.COLOR_BLACK);
                            svgManager.setSVGSet();
                            break;
                        case "cat1Color2":
                            svgInfoFrameColor = "weiß";
                            renderSVGtextarea();
                            svgManager.setFrameColor(SVGManager.COLOR_WHITE);
                            svgManager.setSVGSet();
                            break;
                        case "cat1Color3":
                            svgInfoFrameColor = "silber";
                            renderSVGtextarea();
                            svgManager.setFrameColor(SVGManager.COLOR_SILVER);
                            svgManager.setSVGSet();
                            break;
                    }
                }
            }
        });

        cat1TogglegroupSize.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (cat1TogglegroupSize.getSelectedToggle() != null) {
                    String fxid = ((JFXToggleNode) cat1TogglegroupSize.getSelectedToggle()).getId();
                    switch (fxid) {
                        case "cat1Size1":
                            svgInfoFrameSize = "Größe: S";
                            renderSVGtextarea();
                            break;
                        case "cat1Size2":
                            svgInfoFrameSize = "Größe: M";
                            renderSVGtextarea();
                            break;
                        case "cat1Size3":
                            svgInfoFrameSize = "Größe: L";
                            renderSVGtextarea();
                            break;
                    }
                }
            }
        });

        // Lenker
        cat2SelectModel.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoHandlebarModel = newValue;
                renderSVGtextarea();
                switch (svgInfoHandlebarModel) {
                    case "test1":
                        svgManager.setHandlebar(SVGManager.HANDLEBAR1);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(3), 500);
                        break;
                    case "test2":
                        svgManager.setHandlebar(SVGManager.HANDLEBAR2);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(3), 500);
                        break;
                    case "test3":
                        svgManager.setHandlebar(SVGManager.HANDLEBAR3);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(3), 500);
                        break;
                }
                spaneSVG.getChildren().clear();
                spaneSVG.getChildren().add(SVGManager.svgGroup);
            }
        });

        cat2SelectGrip.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoHandlebarGrip = newValue;
                renderSVGtextarea();
            }
        });

        cat2TogglegroupColor.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (cat2TogglegroupColor.getSelectedToggle() != null) {
                    String fxid = ((JFXToggleNode) cat2TogglegroupColor.getSelectedToggle()).getId();
                    switch (fxid) {
                        case "cat2Color1":
                            svgInfoHandlebarColor = "schwarz";
                            renderSVGtextarea();
                            svgManager.setHandlebarColor(SVGManager.COLOR_BLACK);
                            svgManager.setSVGSet();
                            break;
                        case "cat2Color2":
                            svgInfoHandlebarColor = "weiß";
                            renderSVGtextarea();
                            svgManager.setHandlebarColor(SVGManager.COLOR_WHITE);
                            svgManager.setSVGSet();
                            break;
                        case "cat2Color3":
                            svgInfoHandlebarColor = "silber";
                            renderSVGtextarea();
                            svgManager.setHandlebarColor(SVGManager.COLOR_SILVER);
                            svgManager.setSVGSet();
                            break;
                    }
                }
            }
        });

        // Räder
        cat3SelectModel.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoWheelsModel = newValue;
                renderSVGtextarea();
                switch (svgInfoWheelsModel) {
                    case "test1":
                        svgManager.setTire(SVGManager.TIRE1);
                        svgManager.setSVGSet();
                        fadeIn(SVGManager.svgGroup.getChildren().get(2), 1000);
                        break;
                    case "test2":
                        svgManager.setTire(SVGManager.TIRE2);
                        svgManager.setSVGSet();
                        fadeIn(SVGManager.svgGroup.getChildren().get(2), 1000);
                        break;
                    case "test3":
                        svgManager.setTire(SVGManager.TIRE3);
                        svgManager.setSVGSet();
                        fadeIn(SVGManager.svgGroup.getChildren().get(2), 1000);
                        break;
                }
                spaneSVG.getChildren().clear();
                spaneSVG.getChildren().add(SVGManager.svgGroup);
            }
        });

        cat3SelectTyre.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoWheelsTyre = newValue;
                renderSVGtextarea();
            }
        });

        cat3TogglegroupColor.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (cat3TogglegroupColor.getSelectedToggle() != null) {
                    String fxid = ((JFXToggleNode) cat3TogglegroupColor.getSelectedToggle()).getId();
                    switch (fxid) {
                        case "cat3Color1":
                            svgInfoWheelsColor = "schwarz";
                            renderSVGtextarea();
                            svgManager.setTireColor(SVGManager.COLOR_BLACK);
                            svgManager.setSVGSet();
                            break;
                        case "cat3Color2":
                            svgInfoWheelsColor = "weiß";
                            renderSVGtextarea();
                            svgManager.setTireColor(SVGManager.COLOR_WHITE);
                            svgManager.setSVGSet();
                            break;
                        case "cat3Color3":
                            svgInfoWheelsColor = "silber";
                            renderSVGtextarea();
                            svgManager.setTireColor(SVGManager.COLOR_SILVER);
                            svgManager.setSVGSet();
                            break;
                    }
                }
            }
        });

        cat3TogglegroupSize.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (cat3TogglegroupSize.getSelectedToggle() != null) {
                    String fxid = ((JFXToggleNode) cat3TogglegroupSize.getSelectedToggle()).getId();
                    switch (fxid) {
                        case "cat3Size1":
                            svgInfoWheelsSize = "Größe: S";
                            renderSVGtextarea();
                            break;
                        case "cat3Size2":
                            svgInfoWheelsSize = "Größe: M";
                            renderSVGtextarea();
                            break;
                        case "cat3Size3":
                            svgInfoWheelsSize = "Größe: L";
                            renderSVGtextarea();
                            break;
                    }
                }
            }
        });

        // Sattel
        cat4SelectModel.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoSaddleModel = newValue;
                renderSVGtextarea();
                switch (svgInfoSaddleModel) {
                    case "test1":
                        svgManager.setSeat(SVGManager.SEAT1);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(1), 500);
                        break;
                    case "test2":
                        svgManager.setSeat(SVGManager.SEAT2);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(1), 500);
                        break;
                    case "test3":
                        svgManager.setSeat(SVGManager.SEAT3);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(1), 500);
                        break;
                }
                spaneSVG.getChildren().clear();
                spaneSVG.getChildren().add(SVGManager.svgGroup);
            }
        });

        cat4TogglegroupColor.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (cat4TogglegroupColor.getSelectedToggle() != null) {
                    String fxid = ((JFXToggleNode) cat4TogglegroupColor.getSelectedToggle()).getId();
                    switch (fxid) {
                        case "cat4Color1":
                            svgInfoSaddleColor = "schwarz";
                            renderSVGtextarea();
                            svgManager.setSeatColor(SVGManager.COLOR_BLACK);
                            svgManager.setSVGSet();
                            break;
                        case "cat4Color2":
                            svgInfoSaddleColor = "braun";
                            renderSVGtextarea();
                            svgManager.setSeatColor(SVGManager.COLOR_BROWN);
                            svgManager.setSVGSet();
                            break;
                        case "cat4Color3":
                            svgInfoSaddleColor = "weiß";
                            renderSVGtextarea();
                            svgManager.setSeatColor(SVGManager.COLOR_WHITE);
                            svgManager.setSVGSet();
                            break;
                    }
                }
            }
        });

        // Bremsen
        cat5SelectModel.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoBrakesModel = newValue;
                renderSVGtextarea();
            }
        });

        // Zubehör
        cat6SelectBell.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoAttachmentsBell = newValue;
                renderSVGtextarea();
            }
        });

        cat6SelectStand.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoAttachmentsStand = newValue;
                renderSVGtextarea();
            }
        });

        cat6SelectLight.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoAttachmentsLight = newValue;
                renderSVGtextarea();
            }
        });

    }

}
