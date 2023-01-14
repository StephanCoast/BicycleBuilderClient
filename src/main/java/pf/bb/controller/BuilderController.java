package pf.bb.controller;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pf.bb.Main;
import pf.bb.model.Article;
import pf.bb.model.Configuration;
import pf.bb.task.PostConfigurationTask;
import pf.bb.task.PutConfigurationTask;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

import static pf.bb.controller.LoginController.activeUser;

public class BuilderController {

    public RequiredFieldValidator validatorCustomerID, validatorCustomerFirstName, validatorCustomerLastName, validatorCustomerMail;
    public RequiredFieldValidator validatorCustomerStreet, validatorCustomerNr, validatorCustomerZipcode, validatorCustomerCity;
    public JFXTextField tfCustomerID, tfCustomerFirstName, tfCustomerLastName, tfCustomerMail, tfCustomerStreet, tfCustomerNr, tfCustomerZipcode, tfCustomerCity;
    public AnchorPane anchorContainer;
    public JFXButton btnHeaderHome, btnSaveDraft, btnAddCustomerData, btnSidebarHome;
    public JFXTextArea svgTextarea;
    public StackPane spaneSVG;
    private String svgInfoFrameModel, svgInfoFrameColor, svgInfoFrameSize, svgInfoFrameProducerName, svgInfoFrameDesc, svgInfoFrameColorHex;
    private String svgInfoHandlebarModel, svgInfoHandlebarColor, svgInfoHandlebarGrip, svgInfoHandlebarProducerName, svgInfoHandlebarGripProducerName, svgInfoHandlebarDesc, svgInfoHandlebarColorHex;
    private String svgInfoWheelsModel, svgInfoWheelsColor, svgInfoWheelsSize, svgInfoWheelsTyre, svgInfoWheelsProducerName, svgInfoTyresProducerName, svgInfoWheelDesc, svgInfoWheelsColorHex;
    private String svgInfoSaddleModel, svgInfoSaddleColor, svgInfoSaddleProducerName, svgInfoSaddleDesc, svgInfoSaddleColorHex;
    private String svgInfoBrakesModel, svgInfoBrakesProducerName, svgInfoBrakesTypeName, svgInfoBrakeDesc;
    private String svgInfoAttachmentsBell, svgInfoAttachmentsStand, svgInfoAttachmentsLight, svgInfoBellProducerName, svgInfoStandProducerName, svgInfoLightProducerName;
    private float cat1FramePrice, cat2HandlebarPrice, cat2GripPrice, cat3WheelPrice, cat3TyrePrice, cat4SaddlePrice, cat5BrakePrice, cat6BellPrice, cat6StandPrice, cat6LightPrice, catDefaultFinalPrice;
    public Label cat1LabelPrice, cat2LabelPrice, cat3LabelPrice, cat4LabelPrice, cat5LabelPrice, cat6LabelPrice;
    public ObservableFloatArray finalPriceArray;
    public ObservableIntegerArray finalArticleIdArray;
    public Label catLabelDefaultPrice, catLabelFinishPrice;
    private boolean catIsOpen;
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
        initSubcatsItems();
        //initSubcatsColors(); -> // TODO
        initSubcatsListeners();
        initSubcatsInitialValues();
        initFinalPriceArray();
        initFirstSVGSet();
        renderSVGtextarea();
        initTextFieldListeners();
    }

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login");
    }

    public void openDashboard(ActionEvent event) throws IOException {
        Main.currentConfig = null;
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", false);
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
    public void onSaveDraft(ActionEvent event) throws IOException {
        saveNewConfiguration(event);
    }

    private void saveNewConfiguration(ActionEvent event) throws IOException {

        finalArticleIdArray = FXCollections.observableIntegerArray();
        finalArticleIdArray.addAll(
            getArticleIDByNameSizeAndColor(cat1SelectName.getValue(), svgInfoFrameSize, svgInfoFrameColorHex), // 0=Rahmen
            getArticleIDByNameAndColor(cat2SelectModel.getValue(), svgInfoHandlebarColorHex), // 1=Lenker
            getArticleIdByName(cat2SelectGrip.getValue()), //2=Griffe
            getArticleIDByNameSizeAndColor(cat3SelectModel.getValue(), svgInfoWheelsSize, svgInfoWheelsColorHex), //3=Räder
            getArticleIdByName(cat3SelectTyre.getValue()), //4=Reifen
            getArticleIDByNameAndColor(cat4SelectModel.getValue(), svgInfoSaddleColorHex), //5=Sattel
            getArticleIdByName(cat5SelectModel.getValue()), //6=Bremsen
            getArticleIdByName(cat6SelectBell.getValue()), //7=Klingel
            getArticleIdByName(cat6SelectStand.getValue()), //8=Ständer
            getArticleIdByName(cat6SelectLight.getValue()) //9=Licht
        );

        System.out.println("BuilderController: Article-ID Collection = " + finalArticleIdArray);

        int decrement = 1;
        Configuration configNew = new Configuration(activeUser);
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(0) - decrement)); // Rahmen
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(1) - decrement)); // Lenker
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(2) - decrement)); // Griffe
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(3) - decrement)); // Räder
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(4) - decrement)); // Reifen
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(5) - decrement)); // Sattel
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(6) - decrement)); // Bremsen
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(7) - decrement)); // Klingel
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(8) - decrement)); // Ständer
        configNew.articles.add(Main.ARTICLES.get(finalArticleIdArray.get(9) - decrement)); // Licht
        System.out.println("BuilderController: Article-ID config object = " + configNew.articles);


        if (Main.currentConfig == null) {
            // NEW CONFIGURATION
            PostConfigurationTask configNewPostTask1 = new PostConfigurationTask(activeUser, configNew);
            configNewPostTask1.setOnRunning((successEvent) -> System.out.println("BuilderController: trying to save configuration..."));
            configNewPostTask1.setOnSucceeded((WorkerStateEvent e1) -> {
                System.out.println("BuilderController: configurations saved. id=" + configNewPostTask1.getValue());
                Configuration createdConfiguration = configNewPostTask1.getValue();
                System.out.println("BuilderController: Created Configuration = " + createdConfiguration.toString());
                try {
                    vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            configNewPostTask1.setOnFailed((WorkerStateEvent configCreationFailed) -> {
                System.out.println("BuilderController: saving failed" + configNewPostTask1.getException());
            });
            new Thread(configNewPostTask1).start();
        } else {
            Main.currentConfig.articles = configNew.articles;

            // Update Configuration
            PutConfigurationTask configurationPutTask1 = new PutConfigurationTask(activeUser, Main.currentConfig, Main.currentConfig.id);
            //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
            configurationPutTask1.setOnRunning((successEvent) -> System.out.println("trying to update configuration..."));
            configurationPutTask1.setOnSucceeded((WorkerStateEvent configTask4) -> {
                System.out.println("configuration updated id: " + configurationPutTask1.getValue().id);
                try {
                    vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            //Tasks in eigenem Thread ausführen
            new Thread(configurationPutTask1).start();
        }


    }

    public void openSidebarCat1(ActionEvent event) throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat1, cat1);
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
    public void onCat1Save(ActionEvent event) throws IOException {
        finalPriceArray.set(0, cat1FramePrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }
    public void onCat2Save(ActionEvent event) throws IOException {
        finalPriceArray.set(1, cat2HandlebarPrice);
        finalPriceArray.set(2, cat2GripPrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }
    public void onCat3Save(ActionEvent event) throws IOException {
        finalPriceArray.set(3, cat3WheelPrice);
        finalPriceArray.set(4, cat3TyrePrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }
    public void onCat4Save(ActionEvent event) throws IOException {
        finalPriceArray.set(5, cat4SaddlePrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }
    public void onCat5Save(ActionEvent event) throws IOException {
        finalPriceArray.set(6, cat5BrakePrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }
    public void onCat6Save(ActionEvent event) throws IOException {
        finalPriceArray.set(7, cat6BellPrice);
        finalPriceArray.set(8, cat6StandPrice);
        finalPriceArray.set(9, cat6LightPrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }

    // AR: hier wird "allgemein" bestimmt was nach dem Speichern der Kategorie 1-8 passieren soll
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

    private void initSubcatsInitialValues() {

        if (Main.currentConfig == null) {
            // SET DEFAULT CONFIG VALUES
            setSubcatSelectboxDefault(cat1SelectName, 0);
            setSubcatToggleDefault(cat1TogglegroupColor,0);
            setSubcatToggleDefault(cat1TogglegroupSize,0);

            setSubcatSelectboxDefault(cat2SelectModel, 0);
            setSubcatSelectboxDefault(cat2SelectGrip, 0);
            setSubcatToggleDefault(cat2TogglegroupColor,0);

            setSubcatSelectboxDefault(cat3SelectModel, 0);
            setSubcatSelectboxDefault(cat3SelectTyre, 0);
            setSubcatToggleDefault(cat3TogglegroupColor,0);
            setSubcatToggleDefault(cat3TogglegroupSize,0);

            setSubcatSelectboxDefault(cat4SelectModel, 0);
            setSubcatToggleDefault(cat4TogglegroupColor,0);

            setSubcatSelectboxDefault(cat5SelectModel, 0);

            setSubcatSelectboxDefault(cat6SelectBell, 0);
            setSubcatSelectboxDefault(cat6SelectStand, 0);
            setSubcatSelectboxDefault(cat6SelectLight, 0);
        } else {
            // SET CURRENT CONFIG VALUES - QUICK'n'Dirty statische Variante für 3 feste Toggles, besser Toggles dynamisch laden und Farbe aus DB zuweisen!
            // !! There must be one article of every type in the configuration! Make sure client side before save in the database!
            cat1SelectName.getSelectionModel().select(Main.currentConfig.getArticleByType("Rahmen").name);// There must be only one RAHMEN in every config!
            // FARBE
            if (Objects.equals(Main.currentConfig.getArticleByType("Rahmen").hexColor, "#000000"))
                cat1TogglegroupColor.getToggles().get(0).setSelected(true);
            else if (Objects.equals(Main.currentConfig.getArticleByType("Rahmen").hexColor, "#FFFFFF"))
                cat1TogglegroupColor.getToggles().get(1).setSelected(true);
            else
                cat1TogglegroupColor.getToggles().get(2).setSelected(true);
            // CHARACTERISTIC
            if (Objects.equals(Main.currentConfig.getArticleByType("Rahmen").characteristic, "S"))
                cat1TogglegroupSize.getToggles().get(0).setSelected(true);
            else if (Objects.equals(Main.currentConfig.getArticleByType("Rahmen").characteristic, "M"))
                cat1TogglegroupSize.getToggles().get(1).setSelected(true);
            else
                cat1TogglegroupSize.getToggles().get(2).setSelected(true);

            cat2SelectModel.getSelectionModel().select(Main.currentConfig.getArticleByType("Lenker").name);
            cat2SelectGrip.getSelectionModel().select(Main.currentConfig.getArticleByType("Griff").name);
            // COLOR
            if (Objects.equals(Main.currentConfig.getArticleByType("Lenker").hexColor, "#000000"))
                cat2TogglegroupColor.getToggles().get(0).setSelected(true);
            else if (Objects.equals(Main.currentConfig.getArticleByType("Lenker").hexColor, "#FFFFFF"))
                cat2TogglegroupColor.getToggles().get(1).setSelected(true);
            else
                cat2TogglegroupColor.getToggles().get(2).setSelected(true);

            cat3SelectModel.getSelectionModel().select(Main.currentConfig.getArticleByType("Laufrad").name);
            cat3SelectTyre.getSelectionModel().select(Main.currentConfig.getArticleByType("Reifen").name);
            // FARBE
            if (Objects.equals(Main.currentConfig.getArticleByType("Laufrad").hexColor, "#000000"))
                cat3TogglegroupColor.getToggles().get(0).setSelected(true);
            else if (Objects.equals(Main.currentConfig.getArticleByType("Laufrad").hexColor, "#FFFFFF"))
                cat3TogglegroupColor.getToggles().get(1).setSelected(true);
            else
                cat3TogglegroupColor.getToggles().get(2).setSelected(true);
            // CHARACTERISTIC
            if (Objects.equals(Main.currentConfig.getArticleByType("Laufrad").characteristic, "S"))
                cat3TogglegroupSize.getToggles().get(0).setSelected(true);
            else if (Objects.equals(Main.currentConfig.getArticleByType("Laufrad").characteristic, "M"))
                cat3TogglegroupSize.getToggles().get(1).setSelected(true);
            else
                cat3TogglegroupSize.getToggles().get(2).setSelected(true);

            cat4SelectModel.getSelectionModel().select(Main.currentConfig.getArticleByType("Sattel").name);
            // FARBE
            if (Objects.equals(Main.currentConfig.getArticleByType("Sattel").hexColor, "#000000"))
                cat4TogglegroupColor.getToggles().get(0).setSelected(true);
            else if (Objects.equals(Main.currentConfig.getArticleByType("Sattel").hexColor, "#4b2c20"))
                cat4TogglegroupColor.getToggles().get(1).setSelected(true);
            else
                cat4TogglegroupColor.getToggles().get(2).setSelected(true);

            cat5SelectModel.getSelectionModel().select(Main.currentConfig.getArticleByType("Bremse").name);

            cat6SelectBell.getSelectionModel().select(Main.currentConfig.getArticleByType("Klingel").name);
            cat6SelectStand.getSelectionModel().select(Main.currentConfig.getArticleByType("Ständer").name);
            cat6SelectLight.getSelectionModel().select(Main.currentConfig.getArticleByType("Licht").name);


        }


    }

    private void initFinalPriceArray() {
        catDefaultFinalPrice = 0.0f;
        finalPriceArray = FXCollections.observableFloatArray();
        finalPriceArray.addAll(cat1FramePrice, cat2HandlebarPrice, cat2GripPrice, cat3WheelPrice, cat3TyrePrice, cat4SaddlePrice, cat5BrakePrice, cat6BellPrice, cat6StandPrice, cat6LightPrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
    }

    private void setSubcatSelectboxDefault(JFXComboBox cb, int pos) {
        if (cb.getSelectionModel().isEmpty()) {
            cb.getSelectionModel().select(pos);
        }
    }

    private void setSubcatToggleDefault(ToggleGroup tg, int pos) {
        if (tg.getSelectedToggle() == null) {
            tg.getToggles().get(pos).setSelected(true);
        }
    }

    private String strPriceBeautify(float value) {
        return String.valueOf(String.format("%.02f", value));
    }

    private void calcFinalPriceFromArray(ObservableFloatArray fArray) {
        catDefaultFinalPrice = 0.0f;
        for (int i = 0; i < fArray.size(); i++) {
            catDefaultFinalPrice += fArray.get(i);
        }
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

        // AR: must be setText() to prevent textarea scrollToTop bug
        svgTextarea.setText(
                frame + newline
                + svgInfoFrameModel + " von " + svgInfoFrameProducerName + newline + svgInfoFrameColor + sep + "Größe: " + svgInfoFrameSize + newline + "Info: " + svgInfoFrameDesc + newline + newline
                + handlebar + newline
                + svgInfoHandlebarModel + " von " + svgInfoHandlebarProducerName + " in " + svgInfoHandlebarColor + newline + "Info: " + svgInfoHandlebarDesc + newline + "Griff: " + svgInfoHandlebarGrip + " von " + svgInfoHandlebarGripProducerName + newline + newline
                + wheels + newline
                + svgInfoWheelsModel + " von " + svgInfoWheelsProducerName + newline
                + svgInfoWheelsColor + sep + "Größe: " + svgInfoWheelsSize + newline
                + "Info: " + svgInfoWheelDesc + newline
                + "Reifen: " + svgInfoWheelsTyre + " von " + svgInfoTyresProducerName + newline + newline
                + saddle + newline
                + svgInfoSaddleModel + " von " + svgInfoSaddleProducerName + " in " + svgInfoSaddleColor + newline
                + "Info: " + svgInfoSaddleDesc + newline + newline
                + brakes + newline
                + svgInfoBrakesModel + " von " + svgInfoBrakesProducerName + sep + svgInfoBrakesTypeName + newline
                + "Info: " + svgInfoBrakeDesc + newline + newline
                + attachments + newline
                + svgInfoAttachmentsBell + " von " + svgInfoBellProducerName + newline
                + svgInfoAttachmentsStand + " von " + svgInfoStandProducerName + newline
                + "Beleuchtung: " + svgInfoAttachmentsLight + " von " + svgInfoLightProducerName + newline
        );
    }

    private void initSubcatsItems() {
        cat1SelectName.setItems(getArticleNamesByType("Rahmen"));
        cat2SelectModel.setItems(getArticleNamesByType("Lenker"));
        cat2SelectGrip.setItems(getArticleNamesByType("Griff"));
        cat3SelectModel.setItems(getArticleNamesByType("Laufrad"));
        cat3SelectTyre.setItems(getArticleNamesByType("Reifen"));
        cat4SelectModel.setItems(getArticleNamesByType("Sattel"));
        cat5SelectModel.setItems(getArticleNamesByType("Bremse"));
        cat6SelectBell.setItems(getArticleNamesByType("Klingel"));
        cat6SelectStand.setItems(getArticleNamesByType("Ständer"));
        cat6SelectLight.setItems(getArticleNamesByType("Licht"));
    }

 //   TODO remove before production
//    public void initSubcatsColor() {
//
//        getArticleColorsByName()
//        cat1TogglegroupColor.getToggles().clear();
////         for color in cat1.colors
////         toggle = new JFXToggleNode()
////         toggle.unselectedColor = Color.web("#0000FF",1.0))
////         cat1TogglegroupColor.add(toggle)
//
//
//        for (Toggle toggle : cat1TogglegroupColor.getToggles()) {
//            JFXToggleNode jfxToggleNode = (JFXToggleNode) toggle;
//            jfxToggleNode.setUnSelectedColor(Color.web("#0000FF",1.0));
//            System.out.println(jfxToggleNode.getUnSelectedColor());
//        }
//    }


    // AR: get model names with the given type, remove duplicates
    private ObservableList<String> getArticleNamesByType(String typeString) {
        ObservableList<String> data = FXCollections.observableArrayList();
        for (Article i : Main.ARTICLES) {
            if (i.type.equals(typeString)) {
                if (!data.contains(i.name)) {
                    data.add(i.name);
                }
            }
        }
        return data;
    }

    private String getProducerByArticleName(String name) {
        String pName = "";
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                pName = i.producer;
                break; //stop iterating list when found
            }
        }
        return pName;
    }

    private String getCharacteristicByArticleName(String name) {
        String tName = "";
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                tName = i.characteristic;
                break; //stop iterating list when found
            }
        }
        return tName;
    }

    private String getDescriptionByArticleName(String name) {
        String dText = "";
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                dText = i.description;
                break; //stop iterating list when found
            }
        }
        return dText;
    }

    private float getPriceByArticleName(String name) {
        float price = 0.0f;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                price = i.price;
                break; //stop iterating list when found
            }
        }
        return price;
    }

    private float getPriceByArticleNameAndSize(String name, String size) {
        float price = 0.0f;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                if (i.characteristic.equals(size)) {
                    price = i.price;
                    break; //stop iterating list when found
                }
            }
        }
        return price;
    }

    private int getArticleIdByName(String name) {
        int id = 0;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                id = i.id;
                break; //stop iterating list when found
            }
        }
        return id;
    }

    private int getArticleIDByNameAndColor(String name, String color) {
        int id = 0;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                if (i.hexColor.equals(color)) {
                    id = i.id;
                    break; //stop iterating list when found
                }
            }
        }
        return id;
    }

    private int getArticleIDByNameSizeAndColor(String name, String size, String color) {
        int id = 0;
        for (Article a : Main.ARTICLES) {
            if (a.name.equals(name)) {
                if (a.characteristic.equals(size) && a.hexColor.equals(color)) {
                    id = a.id;
                    break; //stop iterating list when found
                }
            }
        }
        return id;
    }

    // Rahmen
    private void initSubcatsListeners() {

        // Rahmen
        cat1SelectName.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoFrameModel = newValue;
                svgInfoFrameProducerName = getProducerByArticleName(cat1SelectName.getValue());
                svgInfoFrameDesc = getDescriptionByArticleName(cat1SelectName.getValue());
                renderSVGtextarea();
                switch (cat1SelectName.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        svgInfoFrameProducerName = getProducerByArticleName(cat1SelectName.getValue());
                        svgInfoFrameDesc = getDescriptionByArticleName(cat1SelectName.getValue());
                        svgManager.setFrame(SVGManager.FRAME1);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(0), 500);
                        cat1FramePrice = getPriceByArticleNameAndSize(cat1SelectName.getValue(), svgInfoFrameSize);
                        cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
                        break;
                    case 1:
                        svgInfoFrameProducerName = getProducerByArticleName(cat1SelectName.getValue());
                        svgInfoFrameDesc = getDescriptionByArticleName(cat1SelectName.getValue());
                        svgManager.setFrame(SVGManager.FRAME2);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(0), 500);
                        cat1FramePrice = getPriceByArticleNameAndSize(cat1SelectName.getValue(), svgInfoFrameSize);
                        cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
                        break;
                    case 2:
                        svgInfoFrameProducerName = getProducerByArticleName(cat1SelectName.getValue());
                        svgInfoFrameDesc = getDescriptionByArticleName(cat1SelectName.getValue());
                        svgManager.setFrame(SVGManager.FRAME3);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(0), 500);
                        cat1FramePrice = getPriceByArticleNameAndSize(cat1SelectName.getValue(), svgInfoFrameSize);
                        cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
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
                            svgInfoFrameColorHex = "#000000";
                            renderSVGtextarea();
                            svgManager.setFrameColor(SVGManager.COLOR_BLACK);
                            svgManager.setSVGSet();
                            break;
                        case "cat1Color2":
                            svgInfoFrameColor = "weiß";
                            svgInfoFrameColorHex = "#FFFFFF";
                            renderSVGtextarea();
                            svgManager.setFrameColor(SVGManager.COLOR_WHITE);
                            svgManager.setSVGSet();
                            break;
                        case "cat1Color3":
                            svgInfoFrameColor = "silber";
                            svgInfoFrameColorHex = "#808080";
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
                            svgInfoFrameSize = "S";
                            renderSVGtextarea();
                            cat1FramePrice = getPriceByArticleNameAndSize(cat1SelectName.getValue(), svgInfoFrameSize);
                            cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
                            break;
                        case "cat1Size2":
                            svgInfoFrameSize = "M";
                            renderSVGtextarea();
                            cat1FramePrice = getPriceByArticleNameAndSize(cat1SelectName.getValue(), svgInfoFrameSize);
                            cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
                            break;
                        case "cat1Size3":
                            svgInfoFrameSize = "L";
                            renderSVGtextarea();
                            cat1FramePrice = getPriceByArticleNameAndSize(cat1SelectName.getValue(), svgInfoFrameSize);
                            cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
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
                svgInfoHandlebarProducerName = getProducerByArticleName(cat2SelectModel.getValue());
                svgInfoHandlebarDesc = getDescriptionByArticleName(cat2SelectModel.getValue());
                renderSVGtextarea();
                switch (cat2SelectModel.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        svgInfoHandlebarProducerName = getProducerByArticleName(cat2SelectModel.getValue());
                        svgInfoHandlebarDesc = getDescriptionByArticleName(cat2SelectModel.getValue());
                        svgManager.setHandlebar(SVGManager.HANDLEBAR1);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(3), 500);
                        cat2HandlebarPrice = getPriceByArticleName(cat2SelectModel.getValue());
                        cat2GripPrice = getPriceByArticleName(cat2SelectGrip.getValue());
                        cat2LabelPrice.setText(strPriceBeautify(Float.sum(cat2HandlebarPrice, cat2GripPrice)));
                        break;
                    case 1:
                        svgInfoHandlebarProducerName = getProducerByArticleName(cat2SelectModel.getValue());
                        svgInfoHandlebarDesc = getDescriptionByArticleName(cat2SelectModel.getValue());
                        svgManager.setHandlebar(SVGManager.HANDLEBAR2);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(3), 500);
                        cat2HandlebarPrice = getPriceByArticleName(cat2SelectModel.getValue());
                        cat2GripPrice = getPriceByArticleName(cat2SelectGrip.getValue());
                        cat2LabelPrice.setText(strPriceBeautify(Float.sum(cat2HandlebarPrice, cat2GripPrice)));
                        break;
                    case 2:
                        svgInfoHandlebarProducerName = getProducerByArticleName(cat2SelectModel.getValue());
                        svgInfoHandlebarDesc = getDescriptionByArticleName(cat2SelectModel.getValue());
                        svgManager.setHandlebar(SVGManager.HANDLEBAR3);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(3), 500);
                        cat2HandlebarPrice = getPriceByArticleName(cat2SelectModel.getValue());
                        cat2GripPrice = getPriceByArticleName(cat2SelectGrip.getValue());
                        cat2LabelPrice.setText(strPriceBeautify(Float.sum(cat2HandlebarPrice, cat2GripPrice)));
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
                svgInfoHandlebarGripProducerName = getProducerByArticleName(cat2SelectGrip.getValue());
                renderSVGtextarea();
                cat2HandlebarPrice = getPriceByArticleName(cat2SelectModel.getValue());
                cat2GripPrice = getPriceByArticleName(cat2SelectGrip.getValue());
                cat2LabelPrice.setText(strPriceBeautify(Float.sum(cat2HandlebarPrice, cat2GripPrice)));
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
                            svgInfoHandlebarColorHex = "#000000";
                            renderSVGtextarea();
                            svgManager.setHandlebarColor(SVGManager.COLOR_BLACK);
                            svgManager.setSVGSet();
                            break;
                        case "cat2Color2":
                            svgInfoHandlebarColor = "weiß";
                            svgInfoHandlebarColorHex = "#FFFFFF";
                            renderSVGtextarea();
                            svgManager.setHandlebarColor(SVGManager.COLOR_WHITE);
                            svgManager.setSVGSet();
                            break;
                        case "cat2Color3":
                            svgInfoHandlebarColor = "silber";
                            svgInfoHandlebarColorHex = "#808080";
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
                svgInfoWheelsProducerName = getProducerByArticleName(cat3SelectModel.getValue());
                svgInfoWheelDesc = getDescriptionByArticleName(cat3SelectModel.getValue());
                renderSVGtextarea();
                switch (cat3SelectModel.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        svgInfoWheelsProducerName = getProducerByArticleName(cat3SelectModel.getValue());
                        svgInfoWheelDesc = getDescriptionByArticleName(cat3SelectModel.getValue());
                        svgManager.setTire(SVGManager.TIRE1);
                        svgManager.setSVGSet();
                        fadeIn(SVGManager.svgGroup.getChildren().get(2), 1000);
                        cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                        cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                        cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                        break;
                    case 1:
                        svgInfoWheelsProducerName = getProducerByArticleName(cat3SelectModel.getValue());
                        svgInfoWheelDesc = getDescriptionByArticleName(cat3SelectModel.getValue());
                        svgManager.setTire(SVGManager.TIRE2);
                        svgManager.setSVGSet();
                        fadeIn(SVGManager.svgGroup.getChildren().get(2), 1000);
                        cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                        cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                        cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                        break;
                    case 2:
                        svgInfoWheelsProducerName = getProducerByArticleName(cat3SelectModel.getValue());
                        svgInfoWheelDesc = getDescriptionByArticleName(cat3SelectModel.getValue());
                        svgManager.setTire(SVGManager.TIRE3);
                        svgManager.setSVGSet();
                        fadeIn(SVGManager.svgGroup.getChildren().get(2), 1000);
                        cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                        cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                        cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
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
                svgInfoTyresProducerName = getProducerByArticleName(cat3SelectTyre.getValue());
                renderSVGtextarea();
                cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
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
                            svgInfoWheelsColorHex = "#000000";
                            renderSVGtextarea();
                            svgManager.setTireColor(SVGManager.COLOR_BLACK);
                            svgManager.setSVGSet();
                            break;
                        case "cat3Color2":
                            svgInfoWheelsColor = "weiß";
                            svgInfoWheelsColorHex = "#FFFFFF";
                            renderSVGtextarea();
                            svgManager.setTireColor(SVGManager.COLOR_WHITE);
                            svgManager.setSVGSet();
                            break;
                        case "cat3Color3":
                            svgInfoWheelsColor = "silber";
                            svgInfoWheelsColorHex = "#808080";
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
                            svgInfoWheelsSize = "S";
                            renderSVGtextarea();
                            cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                            cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                            cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                            break;
                        case "cat3Size2":
                            svgInfoWheelsSize = "M";
                            renderSVGtextarea();
                            cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                            cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                            cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                            break;
                        case "cat3Size3":
                            svgInfoWheelsSize = "L";
                            renderSVGtextarea();
                            cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                            cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                            cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
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
                svgInfoSaddleProducerName = getProducerByArticleName(cat4SelectModel.getValue());
                svgInfoSaddleDesc = getDescriptionByArticleName(cat4SelectModel.getValue());
                renderSVGtextarea();
                switch (cat4SelectModel.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        svgInfoSaddleProducerName = getProducerByArticleName(cat4SelectModel.getValue());
                        svgInfoSaddleDesc = getDescriptionByArticleName(cat4SelectModel.getValue());
                        svgManager.setSeat(SVGManager.SEAT1);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(1), 500);
                        cat4SaddlePrice = getPriceByArticleName(cat4SelectModel.getValue());
                        cat4LabelPrice.setText(strPriceBeautify(cat4SaddlePrice));
                        break;
                    case 1:
                        svgInfoSaddleProducerName = getProducerByArticleName(cat4SelectModel.getValue());
                        svgInfoSaddleDesc = getDescriptionByArticleName(cat4SelectModel.getValue());
                        svgManager.setSeat(SVGManager.SEAT2);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(1), 500);
                        cat4SaddlePrice = getPriceByArticleName(cat4SelectModel.getValue());
                        cat4LabelPrice.setText(strPriceBeautify(cat4SaddlePrice));
                        break;
                    case 2:
                        svgInfoSaddleProducerName = getProducerByArticleName(cat4SelectModel.getValue());
                        svgInfoSaddleDesc = getDescriptionByArticleName(cat4SelectModel.getValue());
                        svgManager.setSeat(SVGManager.SEAT3);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(1), 500);
                        cat4SaddlePrice = getPriceByArticleName(cat4SelectModel.getValue());
                        cat4LabelPrice.setText(strPriceBeautify(cat4SaddlePrice));
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
                            svgInfoSaddleColorHex = "#000000";
                            renderSVGtextarea();
                            svgManager.setSeatColor(SVGManager.COLOR_BLACK);
                            svgManager.setSVGSet();
                            break;
                        case "cat4Color2":
                            svgInfoSaddleColor = "braun";
                            svgInfoSaddleColorHex = "#4b2c20";
                            renderSVGtextarea();
                            svgManager.setSeatColor(SVGManager.COLOR_BROWN);
                            svgManager.setSVGSet();
                            break;
                        case "cat4Color3":
                            svgInfoSaddleColor = "weiß";
                            svgInfoSaddleColorHex = "#FFFFFF";
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
                svgInfoBrakesProducerName = getProducerByArticleName(cat5SelectModel.getValue());
                svgInfoBrakesTypeName = getCharacteristicByArticleName(cat5SelectModel.getValue());
                svgInfoBrakeDesc = getDescriptionByArticleName(cat5SelectModel.getValue());
                renderSVGtextarea();
                cat5BrakePrice = getPriceByArticleName(cat5SelectModel.getValue());
                cat5LabelPrice.setText(strPriceBeautify(cat5BrakePrice));
            }
        });

        // Zubehör
        cat6SelectBell.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoAttachmentsBell = newValue;
                svgInfoBellProducerName = getProducerByArticleName(cat6SelectBell.getValue());
                renderSVGtextarea();
                cat6BellPrice = getPriceByArticleName(cat6SelectBell.getValue());
                cat6StandPrice = getPriceByArticleName(cat6SelectStand.getValue());
                cat6LightPrice = getPriceByArticleName(cat6SelectLight.getValue());
                cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
            }
        });

        cat6SelectStand.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoAttachmentsStand = newValue;
                svgInfoStandProducerName = getProducerByArticleName(cat6SelectStand.getValue());
                renderSVGtextarea();
                cat6BellPrice = getPriceByArticleName(cat6SelectBell.getValue());
                cat6StandPrice = getPriceByArticleName(cat6SelectStand.getValue());
                cat6LightPrice = getPriceByArticleName(cat6SelectLight.getValue());
                cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
            }
        });

        cat6SelectLight.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoAttachmentsLight = newValue;
                svgInfoLightProducerName = getProducerByArticleName(cat6SelectLight.getValue());
                renderSVGtextarea();
                cat6BellPrice = getPriceByArticleName(cat6SelectBell.getValue());
                cat6StandPrice = getPriceByArticleName(cat6SelectStand.getValue());
                cat6LightPrice = getPriceByArticleName(cat6SelectLight.getValue());
                cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
            }
        });

    }

    private void initTextFieldListeners() {
        setTextFieldRules(tfCustomerID, "[\\d+]");
        setTextFieldRules(tfCustomerFirstName, "[a-zA-Z-'`´]");
        setTextFieldRules(tfCustomerLastName, "[a-zA-Z-'`´]");
        setTextFieldRules(tfCustomerMail, "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        setTextFieldRules(tfCustomerStreet, "[a-zA-Z-'`´]");
        setTextFieldRules(tfCustomerNr, "[a-z0-9]");
        setTextFieldRules(tfCustomerZipcode, "[\\d{0,5}]");
        setTextFieldRules(tfCustomerCity, "[a-zA-Z-'`´]");

        int maxLengthZipCode = 5;
        int maxLengthCustomerID = 10;

        tfCustomerID.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tfCustomerID.getText().length() > maxLengthCustomerID) {
                String str = tfCustomerID.getText().substring(0, maxLengthCustomerID);
                tfCustomerID.setText(str);
            }
        });

        tfCustomerNr.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tfCustomerNr.getText().length() > maxLengthZipCode) {
                String str = tfCustomerNr.getText().substring(0, maxLengthZipCode);
                tfCustomerNr.setText(str);
            }
        });

        tfCustomerZipcode.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tfCustomerZipcode.getText().length() > maxLengthZipCode) {
                String str = tfCustomerZipcode.getText().substring(0, maxLengthZipCode);
                tfCustomerZipcode.setText(str);
            }
        });
    }

    private void setTextFieldRules(JFXTextField tf, String pattern) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(pattern)) {
                tf.setText(newValue.replaceAll("[^" + pattern + "]", ""));
            }
        });
    }

}
