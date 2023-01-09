package pf.bb.controller;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
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

import java.io.IOException;
import java.util.HashSet;

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
        initSubcatsListeners();
        initSubcatsInitialValues();
        initFinalPriceArray();
        initFirstSVGSet();
        renderSVGtextarea();
    }

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login");
    }

    public void openDashboard(ActionEvent event) throws IOException {
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
                getArticleIDBySizeAndColorFromModel(cat1SelectName.getValue(), svgInfoFrameSize, svgInfoFrameColorHex),
                getArticleIDByColorFromModel(cat2SelectModel.getValue(), svgInfoHandlebarColorHex),
                getArticleIDFromModel(cat2SelectGrip.getValue()),
                getArticleIDBySizeAndColorFromModel(cat3SelectModel.getValue(), svgInfoWheelsSize, svgInfoWheelsColorHex),
                getArticleIDFromModel(cat3SelectTyre.getValue()),
                getArticleIDByColorFromModel(cat4SelectModel.getValue(), svgInfoSaddleColorHex),
                getArticleIDFromModel(cat5SelectModel.getValue()),
                getArticleIDFromModel(cat6SelectBell.getValue()),
                getArticleIDFromModel(cat6SelectStand.getValue()),
                getArticleIDFromModel(cat6SelectLight.getValue())
        );

        System.out.println("BuilderController: Article-ID Collection = " + finalArticleIdArray);

        int decrement = 1;
        Configuration configNew = new Configuration(activeUser);
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(0)) - decrement)); // Rahmen
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(1)) - decrement)); // Lenker
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(2)) - decrement)); // Griffe
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(3)) - decrement)); // Räder
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(4)) - decrement)); // Reifen
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(5)) - decrement)); // Sattel
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(6)) - decrement)); // Bremsen
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(7)) - decrement)); // Klingel
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(8)) - decrement)); // Ständer
        configNew.articles.add(Main.ARTICLES.get((finalArticleIdArray.get(9)) - decrement)); // Licht

        System.out.println("BuilderController: Article-ID config object = " + configNew.articles);

        PostConfigurationTask configNewPostTask1 = new PostConfigurationTask(activeUser, configNew);
        configNewPostTask1.setOnRunning((successEvent) -> System.out.println("BuilderController: trying to save configuration..."));
        configNewPostTask1.setOnSucceeded((WorkerStateEvent e1) -> {
            System.out.println("BuilderController: configurations saved. id=" + configNewPostTask1.getValue());
            Configuration createdConfiguration = configNewPostTask1.getValue();
            System.out.println("BuilderController: Created Configuration = " + createdConfiguration.toString());
        });

        configNewPostTask1.setOnFailed((WorkerStateEvent e11) -> System.out.println("BuilderController: saving failed" + configNewPostTask1.getException()));
        new Thread(configNewPostTask1).start();

        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", false);
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
        setSubcatInitialSelectbox(cat1SelectName, 0);
        setSubcatInitialToggle(cat1TogglegroupColor,0);
        setSubcatInitialToggle(cat1TogglegroupSize,0);

        setSubcatInitialSelectbox(cat2SelectModel, 0);
        setSubcatInitialSelectbox(cat2SelectGrip, 0);
        setSubcatInitialToggle(cat2TogglegroupColor,0);

        setSubcatInitialSelectbox(cat3SelectModel, 0);
        setSubcatInitialSelectbox(cat3SelectTyre, 0);
        setSubcatInitialToggle(cat3TogglegroupColor,0);
        setSubcatInitialToggle(cat3TogglegroupSize,0);

        setSubcatInitialSelectbox(cat4SelectModel, 0);
        setSubcatInitialToggle(cat4TogglegroupColor,0);

        setSubcatInitialSelectbox(cat5SelectModel, 0);

        setSubcatInitialSelectbox(cat6SelectBell, 0);
        setSubcatInitialSelectbox(cat6SelectStand, 0);
        setSubcatInitialSelectbox(cat6SelectLight, 0);

        cat1FramePrice = getPriceBySizeFromModel(cat1SelectName.getValue(), svgInfoFrameSize);
        cat2HandlebarPrice = getPriceFromModel(cat2SelectModel.getValue());
        cat2GripPrice = getPriceFromModel(cat2SelectGrip.getValue());
        cat3WheelPrice = getPriceBySizeFromModel(cat3SelectModel.getValue(), svgInfoWheelsSize);
        cat3TyrePrice = getPriceFromModel(cat3SelectTyre.getValue());
        cat4SaddlePrice = getPriceFromModel(cat4SelectModel.getValue());
        cat5BrakePrice = getPriceFromModel(cat5SelectModel.getValue());
        cat6BellPrice = getPriceFromModel(cat6SelectBell.getValue());
        cat6StandPrice = getPriceFromModel(cat6SelectStand.getValue());
        cat6LightPrice = getPriceFromModel(cat6SelectLight.getValue());

        cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
        cat2LabelPrice.setText(strPriceBeautify(Float.sum(cat2HandlebarPrice, cat2GripPrice)));
        cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
        cat4LabelPrice.setText(strPriceBeautify(cat4SaddlePrice));
        cat5LabelPrice.setText(strPriceBeautify(cat5BrakePrice));
        cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
    }

    private void initFinalPriceArray() {
        catDefaultFinalPrice = 0.0f;
        finalPriceArray = FXCollections.observableFloatArray();
        finalPriceArray.addAll(cat1FramePrice, cat2HandlebarPrice, cat2GripPrice, cat3WheelPrice, cat3TyrePrice, cat4SaddlePrice, cat5BrakePrice, cat6BellPrice, cat6StandPrice, cat6LightPrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
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
        cat1SelectName.setItems(getNameFromTypeCollection("Rahmen"));
        cat2SelectModel.setItems(getNameFromTypeCollection("Lenker"));
        cat2SelectGrip.setItems(getNameFromTypeCollection("Griff"));
        cat3SelectModel.setItems(getNameFromTypeCollection("Laufrad"));
        cat3SelectTyre.setItems(getNameFromTypeCollection("Reifen"));
        cat4SelectModel.setItems(getNameFromTypeCollection("Sattel"));
        cat5SelectModel.setItems(getNameFromTypeCollection("Bremse"));
        cat6SelectBell.setItems(getNameFromTypeCollection("Klingel"));
        cat6SelectStand.setItems(getNameFromTypeCollection("Ständer"));
        cat6SelectLight.setItems(getNameFromTypeCollection("Licht"));
    }

    // AR: get model names from DB with the given type, remove duplicates
    private ObservableList<String> getNameFromTypeCollection(String typeString) {
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

    private String getProducerNameFromModel(String modelString) {
        String pName = "";
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(modelString)) {
                pName = i.producer;
            }
        }
        return pName;
    }

    private String getBrakesTypeNameFromModel(String modelString) {
        String tName = "";
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(modelString)) {
                tName = i.characteristic;
            }
        }
        return tName;
    }

    private String getDescriptionTextFromModel(String modelString) {
        String dText = "";
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(modelString)) {
                dText = i.description;
            }
        }
        return dText;
    }

    private float getPriceFromModel(String modelString) {
        float price = 0.0f;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(modelString)) {
                price = i.price;
            }
        }
        return price;
    }

    private float getPriceBySizeFromModel(String modelString, String size) {
        float price = 0.0f;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(modelString)) {
                if (i.characteristic.equals(size)) {
                    price = i.price;
                }
            }
        }
        return price;
    }

    private int getArticleIDFromModel(String modelString) {
        int id = 0;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(modelString)) {
                id = i.id;
            }
        }
        return id;
    }

    private int getArticleIDByColorFromModel(String modelString, String color) {
        int id = 0;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(modelString)) {
                if (i.hexColor.equals(color)) {
                    id = i.id;
                }
            }
        }
        return id;
    }

    private int getArticleIDBySizeAndColorFromModel(String modelString, String size, String color) {
        int id = 0;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(modelString)) {
                if (i.characteristic.equals(size) && i.hexColor.equals(color)) {
                    id = i.id;
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
                svgInfoFrameProducerName = getProducerNameFromModel(cat1SelectName.getValue());
                svgInfoFrameDesc = getDescriptionTextFromModel(cat1SelectName.getValue());
                renderSVGtextarea();
                switch (cat1SelectName.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        svgInfoFrameProducerName = getProducerNameFromModel(cat1SelectName.getValue());
                        svgInfoFrameDesc = getDescriptionTextFromModel(cat1SelectName.getValue());
                        svgManager.setFrame(SVGManager.FRAME1);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(0), 500);
                        cat1FramePrice = getPriceBySizeFromModel(cat1SelectName.getValue(), svgInfoFrameSize);
                        cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
                        break;
                    case 1:
                        svgInfoFrameProducerName = getProducerNameFromModel(cat1SelectName.getValue());
                        svgInfoFrameDesc = getDescriptionTextFromModel(cat1SelectName.getValue());
                        svgManager.setFrame(SVGManager.FRAME2);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(0), 500);
                        cat1FramePrice = getPriceBySizeFromModel(cat1SelectName.getValue(), svgInfoFrameSize);
                        cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
                        break;
                    case 2:
                        svgInfoFrameProducerName = getProducerNameFromModel(cat1SelectName.getValue());
                        svgInfoFrameDesc = getDescriptionTextFromModel(cat1SelectName.getValue());
                        svgManager.setFrame(SVGManager.FRAME3);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(0), 500);
                        cat1FramePrice = getPriceBySizeFromModel(cat1SelectName.getValue(), svgInfoFrameSize);
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
                            cat1FramePrice = getPriceBySizeFromModel(cat1SelectName.getValue(), svgInfoFrameSize);
                            cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
                            break;
                        case "cat1Size2":
                            svgInfoFrameSize = "M";
                            renderSVGtextarea();
                            cat1FramePrice = getPriceBySizeFromModel(cat1SelectName.getValue(), svgInfoFrameSize);
                            cat1LabelPrice.setText(strPriceBeautify(cat1FramePrice));
                            break;
                        case "cat1Size3":
                            svgInfoFrameSize = "L";
                            renderSVGtextarea();
                            cat1FramePrice = getPriceBySizeFromModel(cat1SelectName.getValue(), svgInfoFrameSize);
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
                svgInfoHandlebarProducerName = getProducerNameFromModel(cat2SelectModel.getValue());
                svgInfoHandlebarDesc = getDescriptionTextFromModel(cat2SelectModel.getValue());
                renderSVGtextarea();
                switch (cat2SelectModel.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        svgInfoHandlebarProducerName = getProducerNameFromModel(cat2SelectModel.getValue());
                        svgInfoHandlebarDesc = getDescriptionTextFromModel(cat2SelectModel.getValue());
                        svgManager.setHandlebar(SVGManager.HANDLEBAR1);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(3), 500);
                        cat2HandlebarPrice = getPriceFromModel(cat2SelectModel.getValue());
                        cat2GripPrice = getPriceFromModel(cat2SelectGrip.getValue());
                        cat2LabelPrice.setText(strPriceBeautify(Float.sum(cat2HandlebarPrice, cat2GripPrice)));
                        break;
                    case 1:
                        svgInfoHandlebarProducerName = getProducerNameFromModel(cat2SelectModel.getValue());
                        svgInfoHandlebarDesc = getDescriptionTextFromModel(cat2SelectModel.getValue());
                        svgManager.setHandlebar(SVGManager.HANDLEBAR2);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(3), 500);
                        cat2HandlebarPrice = getPriceFromModel(cat2SelectModel.getValue());
                        cat2GripPrice = getPriceFromModel(cat2SelectGrip.getValue());
                        cat2LabelPrice.setText(strPriceBeautify(Float.sum(cat2HandlebarPrice, cat2GripPrice)));
                        break;
                    case 2:
                        svgInfoHandlebarProducerName = getProducerNameFromModel(cat2SelectModel.getValue());
                        svgInfoHandlebarDesc = getDescriptionTextFromModel(cat2SelectModel.getValue());
                        svgManager.setHandlebar(SVGManager.HANDLEBAR3);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(3), 500);
                        cat2HandlebarPrice = getPriceFromModel(cat2SelectModel.getValue());
                        cat2GripPrice = getPriceFromModel(cat2SelectGrip.getValue());
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
                svgInfoHandlebarGripProducerName = getProducerNameFromModel(cat2SelectGrip.getValue());
                renderSVGtextarea();
                cat2HandlebarPrice = getPriceFromModel(cat2SelectModel.getValue());
                cat2GripPrice = getPriceFromModel(cat2SelectGrip.getValue());
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
                svgInfoWheelsProducerName = getProducerNameFromModel(cat3SelectModel.getValue());
                svgInfoWheelDesc = getDescriptionTextFromModel(cat3SelectModel.getValue());
                renderSVGtextarea();
                switch (cat3SelectModel.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        svgInfoWheelsProducerName = getProducerNameFromModel(cat3SelectModel.getValue());
                        svgInfoWheelDesc = getDescriptionTextFromModel(cat3SelectModel.getValue());
                        svgManager.setTire(SVGManager.TIRE1);
                        svgManager.setSVGSet();
                        fadeIn(SVGManager.svgGroup.getChildren().get(2), 1000);
                        cat3WheelPrice = getPriceBySizeFromModel(cat3SelectModel.getValue(), svgInfoWheelsSize);
                        cat3TyrePrice = getPriceFromModel(cat3SelectTyre.getValue());
                        cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                        break;
                    case 1:
                        svgInfoWheelsProducerName = getProducerNameFromModel(cat3SelectModel.getValue());
                        svgInfoWheelDesc = getDescriptionTextFromModel(cat3SelectModel.getValue());
                        svgManager.setTire(SVGManager.TIRE2);
                        svgManager.setSVGSet();
                        fadeIn(SVGManager.svgGroup.getChildren().get(2), 1000);
                        cat3WheelPrice = getPriceBySizeFromModel(cat3SelectModel.getValue(), svgInfoWheelsSize);
                        cat3TyrePrice = getPriceFromModel(cat3SelectTyre.getValue());
                        cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                        break;
                    case 2:
                        svgInfoWheelsProducerName = getProducerNameFromModel(cat3SelectModel.getValue());
                        svgInfoWheelDesc = getDescriptionTextFromModel(cat3SelectModel.getValue());
                        svgManager.setTire(SVGManager.TIRE3);
                        svgManager.setSVGSet();
                        fadeIn(SVGManager.svgGroup.getChildren().get(2), 1000);
                        cat3WheelPrice = getPriceBySizeFromModel(cat3SelectModel.getValue(), svgInfoWheelsSize);
                        cat3TyrePrice = getPriceFromModel(cat3SelectTyre.getValue());
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
                svgInfoTyresProducerName = getProducerNameFromModel(cat3SelectTyre.getValue());
                renderSVGtextarea();
                cat3WheelPrice = getPriceBySizeFromModel(cat3SelectModel.getValue(), svgInfoWheelsSize);
                cat3TyrePrice = getPriceFromModel(cat3SelectTyre.getValue());
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
                            cat3WheelPrice = getPriceBySizeFromModel(cat3SelectModel.getValue(), svgInfoWheelsSize);
                            cat3TyrePrice = getPriceFromModel(cat3SelectTyre.getValue());
                            cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                            break;
                        case "cat3Size2":
                            svgInfoWheelsSize = "M";
                            renderSVGtextarea();
                            cat3WheelPrice = getPriceBySizeFromModel(cat3SelectModel.getValue(), svgInfoWheelsSize);
                            cat3TyrePrice = getPriceFromModel(cat3SelectTyre.getValue());
                            cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                            break;
                        case "cat3Size3":
                            svgInfoWheelsSize = "L";
                            renderSVGtextarea();
                            cat3WheelPrice = getPriceBySizeFromModel(cat3SelectModel.getValue(), svgInfoWheelsSize);
                            cat3TyrePrice = getPriceFromModel(cat3SelectTyre.getValue());
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
                svgInfoSaddleProducerName = getProducerNameFromModel(cat4SelectModel.getValue());
                svgInfoSaddleDesc = getDescriptionTextFromModel(cat4SelectModel.getValue());
                renderSVGtextarea();
                switch (cat4SelectModel.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        svgInfoSaddleProducerName = getProducerNameFromModel(cat4SelectModel.getValue());
                        svgInfoSaddleDesc = getDescriptionTextFromModel(cat4SelectModel.getValue());
                        svgManager.setSeat(SVGManager.SEAT1);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(1), 500);
                        cat4SaddlePrice = getPriceFromModel(cat4SelectModel.getValue());
                        cat4LabelPrice.setText(strPriceBeautify(cat4SaddlePrice));
                        break;
                    case 1:
                        svgInfoSaddleProducerName = getProducerNameFromModel(cat4SelectModel.getValue());
                        svgInfoSaddleDesc = getDescriptionTextFromModel(cat4SelectModel.getValue());
                        svgManager.setSeat(SVGManager.SEAT2);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(1), 500);
                        cat4SaddlePrice = getPriceFromModel(cat4SelectModel.getValue());
                        cat4LabelPrice.setText(strPriceBeautify(cat4SaddlePrice));
                        break;
                    case 2:
                        svgInfoSaddleProducerName = getProducerNameFromModel(cat4SelectModel.getValue());
                        svgInfoSaddleDesc = getDescriptionTextFromModel(cat4SelectModel.getValue());
                        svgManager.setSeat(SVGManager.SEAT3);
                        svgManager.setSVGSet();
                        scaleIn(SVGManager.svgGroup.getChildren().get(1), 500);
                        cat4SaddlePrice = getPriceFromModel(cat4SelectModel.getValue());
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
                svgInfoBrakesProducerName = getProducerNameFromModel(cat5SelectModel.getValue());
                svgInfoBrakesTypeName = getBrakesTypeNameFromModel(cat5SelectModel.getValue());
                svgInfoBrakeDesc = getDescriptionTextFromModel(cat5SelectModel.getValue());
                renderSVGtextarea();
                cat5BrakePrice = getPriceFromModel(cat5SelectModel.getValue());
                cat5LabelPrice.setText(strPriceBeautify(cat5BrakePrice));
            }
        });

        // Zubehör
        cat6SelectBell.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoAttachmentsBell = newValue;
                svgInfoBellProducerName = getProducerNameFromModel(cat6SelectBell.getValue());
                renderSVGtextarea();
                cat6BellPrice = getPriceFromModel(cat6SelectBell.getValue());
                cat6StandPrice = getPriceFromModel(cat6SelectStand.getValue());
                cat6LightPrice = getPriceFromModel(cat6SelectLight.getValue());
                cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
            }
        });

        cat6SelectStand.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoAttachmentsStand = newValue;
                svgInfoStandProducerName = getProducerNameFromModel(cat6SelectStand.getValue());
                renderSVGtextarea();
                cat6BellPrice = getPriceFromModel(cat6SelectBell.getValue());
                cat6StandPrice = getPriceFromModel(cat6SelectStand.getValue());
                cat6LightPrice = getPriceFromModel(cat6SelectLight.getValue());
                cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
            }
        });

        cat6SelectLight.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                svgInfoAttachmentsLight = newValue;
                svgInfoLightProducerName = getProducerNameFromModel(cat6SelectLight.getValue());
                renderSVGtextarea();
                cat6BellPrice = getPriceFromModel(cat6SelectBell.getValue());
                cat6StandPrice = getPriceFromModel(cat6SelectStand.getValue());
                cat6LightPrice = getPriceFromModel(cat6SelectLight.getValue());
                cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
            }
        });

    }
}
