package pf.bb.controller;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pf.bb.Main;
import pf.bb.model.*;
import pf.bb.task.*;

import java.io.IOException;
import java.util.HashSet;

import static pf.bb.controller.LoginController.activeUser;

/**
 * Diese Klasse steuert die Konfigurator-Ansicht des BicycleBuilder und alle enthaltenen Elemente.
 * @author Alexander Rauch
 * supported by Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class BuilderController {

    /**
     * Variablendeklaration für die Validierung's-Objekte der Kundendaten Eingabefelder.
     * Wurden in Builder.fxml gesetzt.
     */
    public RequiredFieldValidator validatorCustomerID, validatorCustomerFirstName, validatorCustomerLastName, validatorCustomerMail;
    public RequiredFieldValidator validatorCustomerStreet, validatorCustomerNr, validatorCustomerZipcode, validatorCustomerCity;
    /**
     * Variablendeklaration für die Text-Boxen Kundendaten-Container, Kundendaten-Übersicht.
     */
    public JFXTextField tfCustomerFirstName, tfCustomerLastName, tfCustomerMail, tfCustomerStreet, tfCustomerNr, tfCustomerZipcode, tfCustomerCity;
    public TextField tfFinishOrderID, tfFinishCustomerID, tfFinishFirstName, tfFinishLastName, tfFinishStreet, tfFinishNr, tfFinishZipCode, tfFinishCity, tfFinishMail;
    /**
     * Variablendeklaration für den Hauptcontainer des Konfigurator's.
     */
    public AnchorPane anchorContainer;
    /**
     * Variablendeklaration für die Buttons: Home-Icon(Kopfbereich), Entwurf speichern, Kundendaten eingeben, Dashboard(Sidebar), Abschliessen.
     */
    public JFXButton btnHeaderHome, btnSaveDraft, btnAddCustomerData, btnSidebarHome, btnCustomerFinish, btnLogout;
    /**
     * Variablendeklaration für die Text-Area der technischen Informationen.
     */
    public JFXTextArea svgTextarea;
    /**
     * Variablendeklaration für den Halte-Container der SVG Pfade.
     */
    public StackPane spaneSVG;
    /**
     * Variablendeklaration für die Farben- und Größen-Toggles in den Unterkategorien.
     */
    public JFXToggleNode cat1Color1, cat1Color2, cat1Color3, cat1Size1, cat1Size2, cat1Size3;
    public JFXToggleNode cat2Color1, cat2Color2, cat2Color3;
    public JFXToggleNode cat3Color1, cat3Color2, cat3Color3, cat3Size1, cat3Size2, cat3Size3;
    public JFXToggleNode cat4Color1, cat4Color2, cat4Color3;
    /**
     * Variablendeklaration für die technische Übersicht neben dem SVG Container.
     * ...Model = Name des Artikels
     * ...Color = Farbe des Artikels
     * ...ColorHex = Hex-Wert der Farbe des Artikels
     * ...Size = Größe des Artikels
     * ...ProducerName = Produzent des Artikels
     * ...Desc = Beschreibung des Artikels
     * ...Tyre = Name des Reifen-Artikel's
     * ...Bell = Name des Klingel-Artikel's
     * ...Stand = Name des Ständer-Artikel's
     * ...Light = Name des Licht-Artikel's
     */
    private String svgInfoFrameModel, svgInfoFrameColor, svgInfoFrameProducerName, svgInfoFrameDesc;
    public String svgInfoFrameSize, svgInfoFrameColorHex;
    private String svgInfoHandlebarModel, svgInfoHandlebarColor, svgInfoHandlebarGrip, svgInfoHandlebarProducerName, svgInfoHandlebarGripProducerName, svgInfoHandlebarDesc;
    public String svgInfoHandlebarColorHex;
    private String svgInfoWheelsModel, svgInfoWheelsColor, svgInfoWheelsTyre, svgInfoWheelsProducerName, svgInfoTyresProducerName, svgInfoWheelDesc;
    public String svgInfoWheelsSize, svgInfoWheelsColorHex;
    private String svgInfoSaddleModel, svgInfoSaddleColor, svgInfoSaddleProducerName, svgInfoSaddleDesc;
    public String svgInfoSaddleColorHex;
    private String svgInfoBrakesModel, svgInfoBrakesProducerName, svgInfoBrakesTypeName, svgInfoBrakeDesc;
    private String svgInfoAttachmentsBell, svgInfoAttachmentsStand, svgInfoAttachmentsLight, svgInfoBellProducerName, svgInfoStandProducerName, svgInfoLightProducerName;
    /**
     * Variablendeklaration für die Preise einzelnen Artikelgruppen.
     */
    private float cat1FramePrice, cat2HandlebarPrice, cat2GripPrice, cat3WheelPrice, cat3TyrePrice, cat4SaddlePrice, cat5BrakePrice, cat6BellPrice, cat6StandPrice, cat6LightPrice, catDefaultFinalPrice;
    /**
     * Variablendeklaration für das Label(Textanzeige) der Preise, sowohl Gesamtpreis, als auch Zwischenpreise der Unterkategorien.
     */
    public Label cat1LabelPrice, cat2LabelPrice, cat3LabelPrice, cat4LabelPrice, cat5LabelPrice, cat6LabelPrice;
    public Label catLabelDefaultPrice, catLabelFinishPrice;
    /**
     * Variablendeklaration für eine Gruppe von Preisen und Artikeln für spätere Berechnungen.
     */
    public ObservableFloatArray finalPriceArray;
    public ObservableIntegerArray finalArticleIdArray;
    /**
     * Variablendeklaration für ein Boolean zur Erfassung, wenn eine Unterkategorie geöffnet ist.
     */
    private boolean catIsOpen;
    /**
     * Variablendeklaration für die verschiedenen Toggle-Gruppen der Unterkategorien.
     * Wird benötigt, um ein bestimmtes Verhalten der einzelnen Toggle-Buttons zu erreichen.
     */
    public ToggleGroup catsTogglegroup, cat1TogglegroupColor, cat1TogglegroupSize, cat2TogglegroupColor, cat3TogglegroupColor, cat3TogglegroupSize, cat4TogglegroupColor;
    /**
     * Variablendeklaration für die Auswahl-Boxen in den Unterkategorien.
     */
    public JFXComboBox<String> cat1SelectName, cat2SelectModel, cat2SelectGrip, cat3SelectModel, cat3SelectTyre, cat4SelectModel, cat5SelectModel, cat6SelectBell, cat6SelectStand, cat6SelectLight;
    /**
     * Variablendeklaration für die Halte-Container der Side- und Bottombar.
     */
    public BorderPane cat1, cat2, cat3, cat4, cat5, cat6, catDefault, catFinish, bpCats, bpCustomerData;
    /**
     * Variablendeklaration für die JFX-Drawer der Side- und Bottombar.
     */
    public JFXDrawer drawerDefault, drawerCat1, drawerCat2, drawerCat3, drawerCat4, drawerCat5, drawerCat6, drawerBottomCats, drawerBottomData, drawerFinish;
    /**
     * Variablendeklaration für ein Hashset in dem nur einzigartige Elemente aufgenommen werden können.
     * Zur Anzeigen-Hilfe von verschiedenen JFX-Drawer.
     */
    private HashSet<JFXDrawer> drawersBuilderSide, drawersBuilderBottom;
    /**
     * Variablendeklaration für verschiedene Singleton-Instanzen.
     * ViewManager = steuert die verschiedenen Ansichten, stellt Methoden bereit
     * ValidatorManager = initialisiert einzelne Validator, stellt Methoden bereit
     * SVGManager = steuert die verschiedenen SVG-Sets, stellt Methoden bereit
     */
    ViewManager vm = ViewManager.getInstance();
    ValidatorManager validatorManager = ValidatorManager.getInstance();
    SVGManager svgManager = SVGManager.getInstance();

    /**
     * Standard-Konstruktor der Klasse
     */
    public BuilderController() {
    }

    /**
     * FXML Konstruktor der Klasse
     * @throws IOException
     * Zusammenfassung aller Funktionen, die beim Start geladen werden sollen.
     * Regelt den Initial-Status des Konfigurators.
     */
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
        initTextFieldListeners();
        initIconButtonTooltips();
        loadFinishedConfig();
        getAllCustomers();
    }

    /* =====================================
     * BUTTONS
    ========================================= */

    /**
     * Logout-Icon-Button im Header-Bereich
     * @param event Click-Event des Buttons
     * @throws IOException
     * Login-Fenster wird über den ViewManager angefordert.
     */
    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login");
    }

    /**
     * X-Icon-Buttons der Unterkategorien
     * @throws IOException
     * Wird von verschiedenen X-Icon-Buttons in den Unterkategorien ausgelöst.
     * Default-Fenster der Sidebar wird über den ViewManager angefordert.
     */
    public void openSidebarDefault() throws IOException {
        catIsOpen = false;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerDefault, catDefault);
        deselectAllToggles();
    }

    /**
     * Rahmen-Toggle-Button
     * @throws IOException
     * Kategorie 1 wird in der Sidebar über den ViewManger angefordert.
     */
    public void openSidebarCat1() throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat1, cat1);
    }

    /**
     * Lenker-Toggle-Button
     * @throws IOException
     * Kategorie 2 wird in der Sidebar über den ViewManger angefordert.
     */
    public void openSidebarCat2() throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat2, cat2);
    }

    /**
     * Räder-Toggle-Button
     * @throws IOException
     * Kategorie 3 wird in der Sidebar über den ViewManger angefordert.
     */
    public void openSidebarCat3() throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat3, cat3);
    }

    /**
     * Sattel-Toggle-Button
     * @throws IOException
     * Kategorie 4 wird in der Sidebar über den ViewManger angefordert.
     */
    public void openSidebarCat4() throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat4, cat4);
    }

    /**
     * Bremsen-Toggle-Button
     * @throws IOException
     * Kategorie 5 wird in der Sidebar über den ViewManger angefordert.
     */
    public void openSidebarCat5() throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat5, cat5);
    }

    /**
     * Zubehör-Toggle-Button
     * @throws IOException
     * Kategorie 6 wird in der Sidebar über den ViewManger angefordert.
     */
    public void openSidebarCat6() throws IOException {
        catIsOpen = true;
        closeAllSideDrawers();
        vm.forceDrawerView(drawerCat6, cat6);
    }

    /**
     * Kundendaten eingeben Button
     * @throws IOException
     * Kundendaten-Container wird in der Bottombar über den ViewManager angefordert.
     * Buttons der Default-Sidebar werden deaktiviert.
     */
    public void openCustomerDataView() throws IOException {
        vm.forceDrawerView(drawerBottomData, bpCustomerData);
        deactivateButtonsOnCustomerEdit();

        tfCustomerFirstName.clear();
        tfCustomerLastName.clear();
        tfCustomerMail.clear();
        tfCustomerStreet.clear();
        tfCustomerNr.clear();
        tfCustomerZipcode.clear();
        tfCustomerCity.clear();

        tfCustomerFirstName.resetValidation();
        tfCustomerLastName.resetValidation();
        tfCustomerMail.resetValidation();
        tfCustomerStreet.resetValidation();
        tfCustomerNr.resetValidation();
        tfCustomerZipcode.resetValidation();
        tfCustomerCity.resetValidation();
    }

    /**
     * Home-Icon-Button, Dashboard-Button(Default-Sidebar), Dashboard-Button(Final-Sidebar)
     * @param event Click-Event des Buttons
     * @throws IOException Fehlerbehandlung Input/Output
     * supported by SK
     * Schreibzugriff für die Konfiguration wird gesteuert.
     * PUT Configuration Task wird ausgeführt.
     * Dashboard-Ansicht wird über den ViewManager angefordert.
     */
    public void openDashboard(ActionEvent event) throws IOException {
        // when writeAccess was Given it needs to be returned
        if(Main.writeAccessGiven) {
            //return write Access
            PutConfigurationWriteAccessTask writeAccessTask1 = new PutConfigurationWriteAccessTask(activeUser, Main.currentConfig.id);
            writeAccessTask1.setOnRunning((runningEvent) -> System.out.println("trying switch writeAccess for configuration..."));
            writeAccessTask1.setOnSucceeded((WorkerStateEvent switchSuccess) -> {
                System.out.println("OnExitBuilderController: writeAccess for configuration " + Main.currentConfig.id + " switched to: " + writeAccessTask1.getValue());
                if (writeAccessTask1.getValue().equals("ACCESS OPENED")) {
                    Main.writeAccessGiven = false; // flag zurücksetzen
                    Main.currentConfig = null;
                    try {
                        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("OnExitBuilderController: writeAccess for configuration " + Main.currentConfig.id + " could not be returned. Please try again!");
                }
            });
            writeAccessTask1.setOnFailed((writeAccessFailed) -> System.out.println("Couldn't switch writeAccess for configuration"));
            //Tasks in eigenem Thread ausführen
            new Thread(writeAccessTask1).start();

        } else {
            Main.currentConfig = null;
            vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", false);
        }
    }

    // AR: hier wird "einzeln" bestimmt was nach dem Speichern der Kategorie 1-6 passieren soll

    /**
     * Speichern Button Kategorie 1
     * @param event Click-Event des Buttons
     * @throws IOException
     * Preis-Array wird aktualisiert und kalkuliert, Labels zur Textausgabe werden aktualisiert, Abschlussfunktion wird aufgerufen.
     */
    public void onCat1Save(ActionEvent event) throws IOException {
        finalPriceArray.set(0, cat1FramePrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }

    /**
     * Speichern Button Kategorie 2
     * @param event Click-Event des Buttons
     * @throws IOException
     * Preis-Array wird aktualisiert und kalkuliert, Labels zur Textausgabe werden aktualisiert, Abschlussfunktion wird aufgerufen.
     */
    public void onCat2Save(ActionEvent event) throws IOException {
        finalPriceArray.set(1, cat2HandlebarPrice);
        finalPriceArray.set(2, cat2GripPrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }

    /**
     * Speichern Button Kategorie 3
     * @param event Click-Event des Buttons
     * @throws IOException
     * Preis-Array wird aktualisiert und kalkuliert, Labels zur Textausgabe werden aktualisiert, Abschlussfunktion wird aufgerufen.
     */
    public void onCat3Save(ActionEvent event) throws IOException {
        finalPriceArray.set(3, cat3WheelPrice);
        finalPriceArray.set(4, cat3TyrePrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }

    /**
     * Speichern Button Kategorie 4
     * @param event Click-Event des Buttons
     * @throws IOException
     * Preis-Array wird aktualisiert und kalkuliert, Labels zur Textausgabe werden aktualisiert, Abschlussfunktion wird aufgerufen.
     */
    public void onCat4Save(ActionEvent event) throws IOException {
        finalPriceArray.set(5, cat4SaddlePrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }

    /**
     * Speichern Button Kategorie 5
     * @param event Click-Event des Buttons
     * @throws IOException
     * Preis-Array wird aktualisiert und kalkuliert, Labels zur Textausgabe werden aktualisiert, Abschlussfunktion wird aufgerufen.
     */
    public void onCat5Save(ActionEvent event) throws IOException {
        finalPriceArray.set(6, cat5BrakePrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }

    /**
     * Speichern Button Kategorie 6
     * @param event Click-Event des Buttons
     * @throws IOException
     * Preis-Array wird aktualisiert und kalkuliert, Labels zur Textausgabe werden aktualisiert, Abschlussfunktion wird aufgerufen.
     */
    public void onCat6Save(ActionEvent event) throws IOException {
        finalPriceArray.set(7, cat6BellPrice);
        finalPriceArray.set(8, cat6StandPrice);
        finalPriceArray.set(9, cat6LightPrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        onCatSave(event);
    }

    /**
     * Abschlussfunktion zur Speicherung der Unterkategorien.
     * @param event Click-Event des Buttons
     * @throws IOException
     * Save Configuration Task wird ausgeführt, Default-Sidebar wird über den ViewManager angefordert.
     * Toggle-Buttons der Kategorien werden zurückgesetzt.
     */
    private void onCatSave(ActionEvent event) throws IOException {
        startDraftSaveConfigurationTask(event, true);
        closeAllSideDrawers();
        vm.forceDrawerView(drawerDefault, catDefault);
        catIsOpen = false;
        deselectAllToggles();
    }

    /**
     * Entwurf speichern Button
     * @param event
     * Save Configuration Task wird ausgeführt.
     * Boolean wird gesetzt, um zu bestimmen, ob die Speicherung von einer Unterkategorie kommt.
     */
    public void onSaveDraft(ActionEvent event) {
        startDraftSaveConfigurationTask(event, false);
    }

    /**
     * Abschliessen Button
     * supported by SK
     * Textfelder der Kundendaten werden durch den ValidatorManager überprüft.
     * E-Mail-Adresse in den Textfeldern wird auf das @-Symbol geprüft.
     * Save Configuration Task wird ausgeführt.
     * Kunde wird erstellt und gespeichert.
     * Gesamtpreis wird berechnet, Auftrag und Rechnung wird erstellt.
     * Status der Konfiguration wird auf "ABGESCHLOSSEN" gesetzt.
     * Finale Ansicht in der Sidebar wird über den ViewManager angefordert.
     * Kategorie-Ansicht wird in der Bottombar über den ViewManager angefordert.
     * Kundendaten werden in die finale Ansicht übertragen.
     * Toggle-Buttons der Kategorien werden deaktiviert.
     */
    public void onBottomBarFinish() {
        if (validatorManager.textFieldIsEmpty(tfCustomerFirstName) ||
                validatorManager.textFieldIsEmpty(tfCustomerLastName) ||
                validatorManager.textFieldIsEmpty(tfCustomerMail) ||
                validatorManager.textFieldIsEmpty(tfCustomerStreet) ||
                validatorManager.textFieldIsEmpty(tfCustomerNr) ||
                validatorManager.textFieldIsEmpty(tfCustomerZipcode) ||
                validatorManager.textFieldIsEmpty(tfCustomerCity)) {
            vm.createWarningAlert("Bicycle Builder - Warnung", "Bitte füllen Sie alle Felder aus.", null);
        } else if (validatorManager.textFieldNotHaveSymbol(tfCustomerMail, "@")) {
            vm.createWarningAlert("Bicycle Builder - Warnung", "Die E-Mail Adresse muss ein @-Symbol enthalten.", null);
        } else if (validatorManager.textFieldCustomerMailExists(tfCustomerMail.getText())) {
            vm.createWarningAlert("Bicycle Builder - Warnung", "Die gewünschte E-Mail Adresse existiert bereits.", null);
        } else {
            SaveConfigurationTask saveConfigTask1 = new SaveConfigurationTask(activeUser, this, "ABGESCHLOSSEN");
            saveConfigTask1.setOnRunning((runningEvent) -> System.out.println("trying to save configuration..."));
            saveConfigTask1.setOnSucceeded((WorkerStateEvent saveConfig1Success) -> {
                System.out.println("configuration saved: " + saveConfigTask1.getValue());
                Main.currentConfig = saveConfigTask1.getValue();

                // Order mit Kundendaten und Gesamtpreis zur Konfiguration hinzufügen
                Customer newCustomer = new Customer(tfCustomerMail.getText(), tfCustomerFirstName.getText(), tfCustomerLastName.getText(), tfCustomerStreet.getText(), Integer.parseInt(tfCustomerNr.getText()), tfCustomerZipcode.getText(), tfCustomerCity.getText());
                // Customer in DB erzeugen, um unique ID zu bekommen
                PostCustomerTask customerTask1 = new PostCustomerTask(activeUser, newCustomer);
                customerTask1.setOnRunning((runningEvent) -> System.out.println("trying to create customer..."));
                customerTask1.setOnFailed((WorkerStateEvent createUserFailed) -> System.out.println("creating customer failed..."));
                customerTask1.setOnSucceeded((WorkerStateEvent customerCreated) -> {
                    System.out.println("customer created id:" + customerTask1.getValue().id);

                    float priceTotal = 0;
                    for (Article article : saveConfigTask1.getValue().articles) {
                        priceTotal += article.price;
                    }

                    // CREATE ORDER
                    OrderClass newOrder = new OrderClass(Main.currentConfig, customerTask1.getValue(), priceTotal);
                    PostOrderTask orderTask1 = new PostOrderTask(activeUser, newOrder);
                    //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                    orderTask1.setOnSucceeded((WorkerStateEvent orderCreated) -> {
                        System.out.println("order created id:" + orderTask1.getValue().id);
                        //Client Config Objekt aktualisieren
                        Main.currentConfig.setOrder(orderTask1.getValue());

                        // CREATE BILL
                        Bill newBill = new Bill(orderTask1.getValue());
                        PostBillTask billTask1 = new PostBillTask(activeUser, newBill);
                        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
                        billTask1.setOnSucceeded((WorkerStateEvent billCreated) -> {
                            System.out.println("bill created id:" + billTask1.getValue().id);
                            //Client Config Objekt für PDFs aktualisieren
                            Main.currentConfig.order.setBill(billTask1.getValue());
                            Main.currentConfig.status = Configuration.stats[1]; // ABGESCHLOSSEN

                            // SWITCH UI
                            closeAllBottomDrawers();
                            try {
                                vm.forceDrawerView(drawerBottomCats, bpCats);
                                vm.forceDrawerView(drawerFinish, catFinish);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            tfFinishOrderID.clear();
                            tfFinishCustomerID.clear();
                            tfCustomerFirstName.clear();
                            tfFinishLastName.clear();
                            tfFinishStreet.clear();
                            tfFinishNr.clear();
                            tfFinishZipCode.clear();
                            tfFinishCity.clear();
                            tfFinishMail.clear();

                            tfFinishOrderID.setText(String.valueOf(orderTask1.getValue().id));
                            tfFinishCustomerID.setText(String.valueOf(newCustomer.id));
                            tfFinishFirstName.setText(newCustomer.forename);
                            tfFinishLastName.setText(newCustomer.lastname);
                            tfFinishStreet.setText(newCustomer.street);
                            tfFinishNr.setText(String.valueOf(newCustomer.houseNumber));
                            tfFinishZipCode.setText(newCustomer.zipCode);
                            tfFinishCity.setText(newCustomer.city);
                            tfFinishMail.setText(newCustomer.email);

                            catsTogglegroup.getToggles().forEach(toggle -> {
                                Node node = (Node) toggle ;
                                node.setDisable(true);
                            });
                            setDefaultFocus();
                            activateButtonsOnCustomerEdit();
                        });
                        //Tasks in eigenem Thread ausführen
                        new Thread(billTask1).start();
                    });
                    //Tasks in eigenem Thread ausführen
                    new Thread(orderTask1).start();
                });
                //Tasks in eigenem Thread ausführen
                new Thread(customerTask1).start();


            });
            saveConfigTask1.setOnFailed((saveConfigFail1) -> System.out.println("saving configuration failed"));
            //Tasks in eigenem Thread ausführen
            new Thread(saveConfigTask1).start();
        }
    }

    /**
     * X-Icon Button im Kundendaten-Container
     * @throws IOException
     * Kategorie-Ansicht wird in der Bottombar über den ViewManager angefordert.
     * Buttons der Default-Sidebar werden aktiviert.
     */
    public void onBottomBarClose() throws IOException {
        closeAllBottomDrawers();
        vm.forceDrawerView(drawerBottomCats, bpCats);
        setDefaultFocus();
        activateButtonsOnCustomerEdit();
    }

    /**
     * Auftrag Button
     * PDF-Dokument wird über die Configuration-Klasse erzeugt und ausgegeben.
     */
    public void onFinalOrder() { Main.currentConfig.createAndOpenTempPdf("ORDER"); }

    /**
     * Rechnung Button
     * PDF-Dokument wird über die Configuration-Klasse erzeugt und ausgegeben.
     */
    public void onFinalInvoice() { Main.currentConfig.createAndOpenTempPdf("INVOICE"); }

    /* =====================================
     * SETUP
     ========================================= */

    /**
     * JFX-Drawer für die Sidebar werden dem Hashset-Datentyp hinzugefügt.
     * @param drawerDefault Default-Sidebar
     * @param drawerFinish Final-Sidebar
     * @param drawerCat1 Kategorie 1
     * @param drawerCat2 Kategorie 2
     * @param drawerCat3 Kategorie 3
     * @param drawerCat4 Kategorie 4
     * @param drawerCat5 Kategorie 5
     * @param drawerCat6 Kategorie 6
     */
    private void setupSideDrawersSet(JFXDrawer drawerDefault, JFXDrawer drawerFinish, JFXDrawer drawerCat1, JFXDrawer drawerCat2, JFXDrawer drawerCat3, JFXDrawer drawerCat4, JFXDrawer drawerCat5, JFXDrawer drawerCat6) {
        drawersBuilderSide = new HashSet<>();
        drawersBuilderSide.add(drawerDefault);
        drawersBuilderSide.add(drawerFinish);
        drawersBuilderSide.add(drawerCat1);
        drawersBuilderSide.add(drawerCat2);
        drawersBuilderSide.add(drawerCat3);
        drawersBuilderSide.add(drawerCat4);
        drawersBuilderSide.add(drawerCat5);
        drawersBuilderSide.add(drawerCat6);
    }

    /**
     * JFX-Drawer für die Bottombar werden dem Hashset-Datentyp hinzugefügt.
     * @param drawerCats Kategorie Toggles
     * @param drawerData Kundendaten
     */
    private void setupBottomDrawersSet(JFXDrawer drawerCats, JFXDrawer drawerData) {
        drawersBuilderBottom = new HashSet<>();
        drawersBuilderBottom.add(drawerCats);
        drawersBuilderBottom.add(drawerData);
    }

    /**
     * Validatoren werden über den ValidatorManager den Kundendaten-Textfeldern zugeordnet.
     */
    private void setupValidators() {
        validatorManager.initTextValidators(tfCustomerFirstName, validatorCustomerFirstName);
        validatorManager.initTextValidators(tfCustomerLastName, validatorCustomerLastName);
        validatorManager.initTextValidators(tfCustomerMail, validatorCustomerMail);
        validatorManager.initTextValidators(tfCustomerStreet, validatorCustomerStreet);
        validatorManager.initTextValidators(tfCustomerNr, validatorCustomerNr);
        validatorManager.initTextValidators(tfCustomerZipcode, validatorCustomerZipcode);
        validatorManager.initTextValidators(tfCustomerCity, validatorCustomerCity);
    }

    /**
     * Setzt den Fokus auf den Hauptcontainer.
     */
    private void setDefaultFocus() {
        Platform.runLater(() -> anchorContainer.requestFocus());
    }

    /**
     * Bestimmt über den SVGManager das initiale SVG-Set.
     * Wenn keine Konfiguration vorhanden ist, wird eine Default-Konfiguration geladen.
     * Wenn eine Konfiguration vorhanden ist, wird die SVG-Gruppe aktualisiert.
     * Die Start-Animation der SVG-Gruppe wird gesetzt.
     */
    private void initFirstSVGSet() {
        if (Main.currentConfig == null) {
            svgManager.setFrame(SVGManager.FRAME1);
            svgManager.setFrameColor(SVGManager.COLOR_BLACK);
            svgManager.setHandlebar(SVGManager.HANDLEBAR1);
            svgManager.setHandlebarColor(SVGManager.COLOR_BLACK);
            svgManager.setTire(SVGManager.TIRE1);
            svgManager.setTireColor(SVGManager.COLOR_BLACK);
            svgManager.setSeat(SVGManager.SEAT1);
            svgManager.setSeatColor(SVGManager.COLOR_BLACK);
            svgManager.setSVGSet();
        }
        spaneSVG.getChildren().clear();
        spaneSVG.getChildren().add(SVGManager.svgGroup);
        fadeIn(SVGManager.svgGroup.getChildren().get(0));
        scaleInX(SVGManager.svgGroup.getChildren().get(1));
        scaleIn(SVGManager.svgGroup.getChildren().get(2), 1000);
        scaleInX(SVGManager.svgGroup.getChildren().get(3));
    }

    /**
     * Die Auswahl-Boxen der Unterkategorien werden mit den vorhandenen Artikeln der jeweiligen Kategorie aus der Datenbank gefüllt.
     */
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

    /**
     * Eingabe-Regeln werden über den ValidatorManager den Kundendaten-Textfeldern zugeordnet.
     * Über RegEx-Pattern werden Symbol-Regeln erstellt.
     * Kundendaten-Textfelder für Hausnummer und PLZ bekommen Limitierungen im Listener.
     */
    private void initTextFieldListeners() {
        validatorManager.setTextFieldRules(tfCustomerFirstName, "[a-zA-Z-'`´]");
        validatorManager.setTextFieldRules(tfCustomerLastName, "[a-zA-Z-'`´]");
        validatorManager.setTextFieldRules(tfCustomerMail, "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        validatorManager.setTextFieldRules(tfCustomerStreet, "[a-zA-Z-'`´]");
        validatorManager.setTextFieldRules(tfCustomerNr, "[0-9]");
        validatorManager.setTextFieldRules(tfCustomerZipcode, "[\\d{0,5}]");
        validatorManager.setTextFieldRules(tfCustomerCity, "[a-zA-Z-'`´]");

        int maxLengthZipCode = 5;
        int maxLengthHouseNumber = 3;

        tfCustomerNr.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tfCustomerNr.getText().length() > maxLengthHouseNumber) {
                String str = tfCustomerNr.getText().substring(0, maxLengthHouseNumber);
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

    /**
     * Tooltips erstellen und den Icon-Buttons im Kopfbereich zuordnen.
     */
    private void initIconButtonTooltips() {
        Tooltip ttIconHome = new Tooltip("Dashboard");
        Tooltip ttIconLogout = new Tooltip("Logout");
        btnHeaderHome.setTooltip(ttIconHome);
        btnLogout.setTooltip(ttIconLogout);
        btnHeaderHome.getStyleClass().add("header-icon-tooltip");
        btnLogout.getStyleClass().add("header-icon-tooltip");
    }

    /* =====================================
     * LOGIC
     ========================================= */

    /**
     * Wenn keine Konfiguration vorhanden ist, werden Default-Auswahlmöglichkeiten in den Unterkategorien gesetzt.
     * Wenn eine Konfiguration vorhanden ist, werden die Auswahlmöglichkeiten anhand der gespeicherten Konfiguration in den Unterkategorien gesetzt.
     * supported by SK
     */
    private void initSubcatsInitialValues() {
        if (Main.currentConfig == null) {
            // SET DEFAULT CONFIG VALUES
            setSubcatSelectboxDefault(cat1SelectName);
            setSubcatToggleDefault(cat1TogglegroupColor);
            setSubcatToggleDefault(cat1TogglegroupSize);

            setSubcatSelectboxDefault(cat2SelectModel);
            setSubcatSelectboxDefault(cat2SelectGrip);
            setSubcatToggleDefault(cat2TogglegroupColor);

            setSubcatSelectboxDefault(cat3SelectModel);
            setSubcatSelectboxDefault(cat3SelectTyre);
            setSubcatToggleDefault(cat3TogglegroupColor);
            setSubcatToggleDefault(cat3TogglegroupSize);

            setSubcatSelectboxDefault(cat4SelectModel);
            setSubcatToggleDefault(cat4TogglegroupColor);

            setSubcatSelectboxDefault(cat5SelectModel);

            setSubcatSelectboxDefault(cat6SelectBell);
            setSubcatSelectboxDefault(cat6SelectStand);
            setSubcatSelectboxDefault(cat6SelectLight);
        } else {
            // SET CURRENT CONFIG VALUES
            // !! There must be one article of every type in the configuration! Make sure client side before save in the database!
            // RAHMEN
            cat1SelectName.getSelectionModel().select(Main.currentConfig.getArticleByType("Rahmen").name);// There must be only one RAHMEN in every config!
            // FARBE RAHMEN
            String frameHexColorString = Main.currentConfig.getArticleByType("Rahmen").hexColor;
            setSubcatColorToggleOnLoad(frameHexColorString, cat1Color1, cat1Color2, cat1Color3);
            // SIZE RAHMEN
            String frameSizeString = Main.currentConfig.getArticleByType("Rahmen").characteristic;
            setSubcatSizeToggleOnLoad(frameSizeString, cat1Size1, cat1Size2, cat1Size3);
            // LENKER & GRIFFE
            cat2SelectModel.getSelectionModel().select(Main.currentConfig.getArticleByType("Lenker").name);
            cat2SelectGrip.getSelectionModel().select(Main.currentConfig.getArticleByType("Griff").name);
            // FARBE LENKER
            String handlebarHexColorString = Main.currentConfig.getArticleByType("Lenker").hexColor;
            setSubcatColorToggleOnLoad(handlebarHexColorString, cat2Color1, cat2Color2, cat2Color3);
            // RÄDER & REIFEN
            cat3SelectModel.getSelectionModel().select(Main.currentConfig.getArticleByType("Laufrad").name);
            cat3SelectTyre.getSelectionModel().select(Main.currentConfig.getArticleByType("Reifen").name);
            // FARBE RÄDER
            String wheelHexColorString = Main.currentConfig.getArticleByType("Laufrad").hexColor;
            setSubcatColorToggleOnLoad(wheelHexColorString, cat3Color1, cat3Color2, cat3Color3);
            // SIZE RÄDER
            String wheelSizeString = Main.currentConfig.getArticleByType("Laufrad").characteristic;
            setSubcatSizeToggleOnLoad(wheelSizeString, cat3Size1, cat3Size2, cat3Size3);
            // SATTEL
            cat4SelectModel.getSelectionModel().select(Main.currentConfig.getArticleByType("Sattel").name);
            // FARBE SATTEL
            String saddleHexColorString = Main.currentConfig.getArticleByType("Sattel").hexColor;
            setSubcatColorToggleOnLoadSaddle(saddleHexColorString, cat4Color1, cat4Color2, cat4Color3);
            // BREMSEN
            cat5SelectModel.getSelectionModel().select(Main.currentConfig.getArticleByType("Bremse").name);
            // ZUBEHÖR
            cat6SelectBell.getSelectionModel().select(Main.currentConfig.getArticleByType("Klingel").name);
            cat6SelectStand.getSelectionModel().select(Main.currentConfig.getArticleByType("Ständer").name);
            cat6SelectLight.getSelectionModel().select(Main.currentConfig.getArticleByType("Licht").name);
        }
    }

    /**
     * Die verschiedenen Zwischenpreise werden in einem Array zusammengefasst.
     * Der Gesamtpreis wird berechnet und in den Anzeige-Labeln ausgegeben.
     */
    private void initFinalPriceArray() {
        catDefaultFinalPrice = 0.0f;
        finalPriceArray = FXCollections.observableFloatArray();
        finalPriceArray.addAll(cat1FramePrice, cat2HandlebarPrice, cat2GripPrice, cat3WheelPrice, cat3TyrePrice, cat4SaddlePrice, cat5BrakePrice, cat6BellPrice, cat6StandPrice, cat6LightPrice);
        calcFinalPriceFromArray(finalPriceArray);
        catLabelDefaultPrice.setText(strPriceBeautify(catDefaultFinalPrice));
        catLabelFinishPrice.setText(strPriceBeautify(catDefaultFinalPrice));
    }

    /**
     * Ausgabe der technischen Informationen zur aktuellen Auswahl in der Text-Area.
     * Trennzeichen und Zeilenumbrüche werden vorbestimmt.
     * Ausgabestruktur wird mit entsprechenden Variablen zusammengestellt.
     */
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

    /**
     * Save Configuration Task.
     * Wird jeweils von "Entwurf speichern" und den Unterkategorien aufgerufen.
     * @param event Click-Event des Buttons
     * @param comingFromSubCat Boolean, um zu wissen, ob aus einer Unterkategorie gespeichert wurde
     * supported by SK
     * Konfiguration wird gespeichert und je nach Boolean wird über den ViewManager die Dashboard-Ansicht angefordert.
     */
    private void startDraftSaveConfigurationTask(ActionEvent event, boolean comingFromSubCat) {
        SaveConfigurationTask saveConfigTask1 = new SaveConfigurationTask(activeUser, this, "ENTWURF");
        saveConfigTask1.setOnRunning((runningEvent) -> System.out.println("trying to save configuration..."));
        saveConfigTask1.setOnSucceeded((WorkerStateEvent saveConfigSucceded) -> {
            System.out.println("configuration saved: " + saveConfigTask1.getValue());
            try {
                if (!comingFromSubCat) {
                    openDashboard(event);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        saveConfigTask1.setOnFailed((saveConfigFailed) -> System.out.println("saving configuration failed"));
        //Tasks in eigenem Thread ausführen
        new Thread(saveConfigTask1).start();
    }

    /**
     * Lädt eine Konfiguration in der Konfigurator-Ansicht, die den Status "ABGESCHLOSSEN" aufweist.
     * @throws IOException
     * Prüft, ob eine Konfiguration vorhanden ist und ob sie den Status "ABGESCHLOSSEN" hat.
     * Öffnet die finale Ansicht in der Sidebar.
     * Deaktiviert alle Kategorie-Toggles in der Bottombar.
     * Zeigt die Kundendaten in den Textfeldern der Sidebar an.
     *
     */
    private void loadFinishedConfig() throws IOException {
        if (Main.currentConfig != null) {
            if (Main.currentConfig.status.equals("ABGESCHLOSSEN")) {
                closeAllSideDrawers();
                closeAllBottomDrawers();
                vm.forceDrawerView(drawerBottomCats, bpCats);
                vm.forceDrawerView(drawerFinish, catFinish);

                tfFinishOrderID.clear();
                tfFinishCustomerID.clear();
                tfCustomerFirstName.clear();
                tfFinishLastName.clear();
                tfFinishStreet.clear();
                tfFinishNr.clear();
                tfFinishZipCode.clear();
                tfFinishCity.clear();
                tfFinishMail.clear();

                tfFinishOrderID.setText(String.valueOf(Main.currentConfig.order.id));
                tfFinishCustomerID.setText(String.valueOf(Main.currentConfig.order.customer.id));
                tfFinishFirstName.setText(Main.currentConfig.order.customer.forename);
                tfFinishLastName.setText(Main.currentConfig.order.customer.lastname);
                tfFinishStreet.setText(Main.currentConfig.order.customer.street);
                tfFinishNr.setText(String.valueOf(Main.currentConfig.order.customer.houseNumber));
                tfFinishZipCode.setText(Main.currentConfig.order.customer.zipCode);
                tfFinishCity.setText(Main.currentConfig.order.customer.city);
                tfFinishMail.setText(Main.currentConfig.order.customer.email);

                catsTogglegroup.getToggles().forEach(toggle -> {
                    Node node = (Node) toggle ;
                    node.setDisable(true);
                });
                setDefaultFocus();
            }
        }
    }

    /**
     * Initialisiert und steuert alle Change-Listeners der Auswahl-Boxen, Größen- und Farben-Toggles in den Unterkategorien.
     * Je nach Auswahl werden technische Informationen, SVG-Pfade, Preise, Größen und Farben gesetzt und aktualisiert.
     */
    private void initSubcatsListeners() {
        // Rahmen
        cat1SelectName.valueProperty().addListener((observable, oldValue, newValue) -> {
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
        });

        cat1TogglegroupColor.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
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
        });

        cat1TogglegroupSize.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
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
        });

        // Lenker
        cat2SelectModel.valueProperty().addListener((observable, oldValue, newValue) -> {
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
        });

        cat2SelectGrip.valueProperty().addListener((observable, oldValue, newValue) -> {
            svgInfoHandlebarGrip = newValue;
            svgInfoHandlebarGripProducerName = getProducerByArticleName(cat2SelectGrip.getValue());
            renderSVGtextarea();
            cat2HandlebarPrice = getPriceByArticleName(cat2SelectModel.getValue());
            cat2GripPrice = getPriceByArticleName(cat2SelectGrip.getValue());
            cat2LabelPrice.setText(strPriceBeautify(Float.sum(cat2HandlebarPrice, cat2GripPrice)));
        });

        cat2TogglegroupColor.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
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
        });

        // Räder
        cat3SelectModel.valueProperty().addListener((observable, oldValue, newValue) -> {
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
                    fadeIn(SVGManager.svgGroup.getChildren().get(2));
                    cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                    cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                    cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                    break;
                case 1:
                    svgInfoWheelsProducerName = getProducerByArticleName(cat3SelectModel.getValue());
                    svgInfoWheelDesc = getDescriptionByArticleName(cat3SelectModel.getValue());
                    svgManager.setTire(SVGManager.TIRE2);
                    svgManager.setSVGSet();
                    fadeIn(SVGManager.svgGroup.getChildren().get(2));
                    cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                    cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                    cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                    break;
                case 2:
                    svgInfoWheelsProducerName = getProducerByArticleName(cat3SelectModel.getValue());
                    svgInfoWheelDesc = getDescriptionByArticleName(cat3SelectModel.getValue());
                    svgManager.setTire(SVGManager.TIRE3);
                    svgManager.setSVGSet();
                    fadeIn(SVGManager.svgGroup.getChildren().get(2));
                    cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
                    cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
                    cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
                    break;
            }
            spaneSVG.getChildren().clear();
            spaneSVG.getChildren().add(SVGManager.svgGroup);
        });

        cat3SelectTyre.valueProperty().addListener((observable, oldValue, newValue) -> {
            svgInfoWheelsTyre = newValue;
            svgInfoTyresProducerName = getProducerByArticleName(cat3SelectTyre.getValue());
            renderSVGtextarea();
            cat3WheelPrice = getPriceByArticleNameAndSize(cat3SelectModel.getValue(), svgInfoWheelsSize);
            cat3TyrePrice = getPriceByArticleName(cat3SelectTyre.getValue());
            cat3LabelPrice.setText(strPriceBeautify(Float.sum(cat3WheelPrice, cat3TyrePrice)));
        });

        cat3TogglegroupColor.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
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
        });

        cat3TogglegroupSize.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
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
        });

        // Sattel
        cat4SelectModel.valueProperty().addListener((observable, oldValue, newValue) -> {
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
        });

        cat4TogglegroupColor.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
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
        });

        // Bremsen
        cat5SelectModel.valueProperty().addListener((observable, oldValue, newValue) -> {
            svgInfoBrakesModel = newValue;
            svgInfoBrakesProducerName = getProducerByArticleName(cat5SelectModel.getValue());
            svgInfoBrakesTypeName = getCharacteristicByArticleName(cat5SelectModel.getValue());
            svgInfoBrakeDesc = getDescriptionByArticleName(cat5SelectModel.getValue());
            renderSVGtextarea();
            cat5BrakePrice = getPriceByArticleName(cat5SelectModel.getValue());
            cat5LabelPrice.setText(strPriceBeautify(cat5BrakePrice));
        });

        // Zubehör
        cat6SelectBell.valueProperty().addListener((observable, oldValue, newValue) -> {
            svgInfoAttachmentsBell = newValue;
            svgInfoBellProducerName = getProducerByArticleName(cat6SelectBell.getValue());
            renderSVGtextarea();
            cat6BellPrice = getPriceByArticleName(cat6SelectBell.getValue());
            cat6StandPrice = getPriceByArticleName(cat6SelectStand.getValue());
            cat6LightPrice = getPriceByArticleName(cat6SelectLight.getValue());
            cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
        });

        cat6SelectStand.valueProperty().addListener((observable, oldValue, newValue) -> {
            svgInfoAttachmentsStand = newValue;
            svgInfoStandProducerName = getProducerByArticleName(cat6SelectStand.getValue());
            renderSVGtextarea();
            cat6BellPrice = getPriceByArticleName(cat6SelectBell.getValue());
            cat6StandPrice = getPriceByArticleName(cat6SelectStand.getValue());
            cat6LightPrice = getPriceByArticleName(cat6SelectLight.getValue());
            cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
        });

        cat6SelectLight.valueProperty().addListener((observable, oldValue, newValue) -> {
            svgInfoAttachmentsLight = newValue;
            svgInfoLightProducerName = getProducerByArticleName(cat6SelectLight.getValue());
            renderSVGtextarea();
            cat6BellPrice = getPriceByArticleName(cat6SelectBell.getValue());
            cat6StandPrice = getPriceByArticleName(cat6SelectStand.getValue());
            cat6LightPrice = getPriceByArticleName(cat6SelectLight.getValue());
            cat6LabelPrice.setText(strPriceBeautify(Float.sum(Float.sum(cat6BellPrice, cat6StandPrice), cat6LightPrice)));
        });

    }

    /**
     * Überführe alle existierenden Kunden in die Array-Liste des ValidatorManager.
     */
    private void getAllCustomers() {
        GetCustomersTask customersTask = new GetCustomersTask(activeUser);
        customersTask.setOnSucceeded((WorkerStateEvent getCustomers) -> {
            validatorManager.CUSTOMERS.clear();
            validatorManager.CUSTOMERS.addAll(customersTask.getValue());
        });
        new Thread(customersTask).start();
    }

    /* =====================================
     * UTILITIES
     ========================================= */

    /**
     * Schließt alle Bottonbar-Container-JFXDrawer
     */
    private void closeAllBottomDrawers() {
        for (JFXDrawer i : drawersBuilderBottom) {
            i.close();
            i.setVisible(false);
        }
    }

    /**
     * Schließt alle Sidebar-Container-JFXDrawer
     */
    private void closeAllSideDrawers() {
        for (JFXDrawer i : drawersBuilderSide) {
            i.close();
            i.setVisible(false);
        }
    }

    /**
     * Verhindert, dass die Kategorie-Toggles in der Bottombar deselektiert werden können.
     */
    private void onToggleDeselect() {
        catsTogglegroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (catIsOpen) {
                if (newValue == null) {
                    oldValue.setSelected(true);
                }
            }
        });
    }

    /**
     * Verhindert, dass die Kategorie-Toggles in der Sidebar deselektiert werden können.
     * @param tg Toggle-Gruppe der Unterkategorie
     */
    private void onToggleDeselectSubCat(ToggleGroup tg) {
        tg.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            }
        });
    }

    /**
     * Deselektiert alle Kategorie-Toggles in der Bottombar.
     */
    private void deselectAllToggles() {
        if (catsTogglegroup.getSelectedToggle() != null) {
            catsTogglegroup.getSelectedToggle().setSelected(false);
        }
    }

    /**
     * Deaktiviert die Buttons in der Default-Sidebar, wenn die Kundendaten eingegeben werden.
     */
    private void deactivateButtonsOnCustomerEdit() {
        btnHeaderHome.setDisable(true);
        btnAddCustomerData.setDisable(true);
        btnSaveDraft.setDisable(true);
        btnSidebarHome.setDisable(true);
    }

    /**
     * Aktiviert die Buttons in der Default-Sidebar, wenn der Kundendaten-Container geschlossen wurde.
     */
    private void activateButtonsOnCustomerEdit() {
        btnHeaderHome.setDisable(false);
        btnAddCustomerData.setDisable(false);
        btnSaveDraft.setDisable(false);
        btnSidebarHome.setDisable(false);
    }

    /**
     * Starte eine Fade-Animation mit dem empfangenen Objekt.
     * @param node Das Objekt(SVG Pfad als Beispiel)
     */
    private void fadeIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * Starte eine Scale-Animation mit dem empfangenen Objekt.
     * @param node Das Objekt(SVG Pfad als Beispiel)
     * @param ms Millisekunden
     */
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

    /**
     * Starte eine ScaleX-Animation mit dem empfangenen Objekt.
     * @param node Das Objekt(SVG Pfad als Beispiel)
     */
    private void scaleInX(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(1000), node);
        st.setFromX(0);
        st.setByX(1);
        st.play();
    }

    /**
     * Vergleicht den Hex-Color String aus der Bedingung und setzt entsprechend den Farben-Toggle Button.
     * @param conditionColorString Hex-Color String als Bedingung
     * @param tn1 Toggle-Button 1
     * @param tn2 Toggle-Button 2
     * @param tn3 Toggle-Button 3
     */
    private void setSubcatColorToggleOnLoad(String conditionColorString, JFXToggleNode tn1, JFXToggleNode tn2, JFXToggleNode tn3) {
        switch (conditionColorString) {
            case "#000000":
                tn1.setSelected(true);
                break;
            case "#FFFFFF":
                tn2.setSelected(true);
                break;
            case "#808080":
                tn3.setSelected(true);
                break;
        }
    }

    /**
     * Vergleicht den Hex-Color String aus der Bedingung und setzt entsprechend den Farben-Toggle Button.
     * @param conditionColorString Hex-Color String als Bedingung
     * @param tn1 Toggle-Button 1
     * @param tn2 Toggle-Button 2
     * @param tn3 Toggle-Button 3
     */
    private void setSubcatColorToggleOnLoadSaddle(String conditionColorString, JFXToggleNode tn1, JFXToggleNode tn2, JFXToggleNode tn3) {
        switch (conditionColorString) {
            case "#000000":
                tn1.setSelected(true);
                break;
            case "#FFFFFF":
                tn3.setSelected(true);
                break;
            case "#4b2c20":
                tn2.setSelected(true);
                break;
        }
    }

    /**
     * Vergleicht den Hex-Color String aus der Bedingung und setzt entsprechend den Farben-Toggle Button.
     * @param conditionSizeString Size String als Bedingung (S, M, L)
     * @param tn1 Toggle-Button 1
     * @param tn2 Toggle-Button 2
     * @param tn3 Toggle-Button 3
     */
    private void setSubcatSizeToggleOnLoad(String conditionSizeString, JFXToggleNode tn1, JFXToggleNode tn2, JFXToggleNode tn3) {
        switch (conditionSizeString) {
            case "S":
                tn1.setSelected(true);
                break;
            case "M":
                tn2.setSelected(true);
                break;
            case "L":
                tn3.setSelected(true);
                break;
        }
    }

    /**
     * Hilfsfunktion zum initialen Setzen der Auswahlbox einer Unterkategorie.
     * @param cb Auswahlbox
     */
    private void setSubcatSelectboxDefault(JFXComboBox cb) {
        if (cb.getSelectionModel().isEmpty()) {
            cb.getSelectionModel().select(0);
        }
    }

    /**
     * Hilfsfunktion zum initialen Setzen eines Toggles einer Unterkategorie.
     * @param tg Toggle-Gruppe
     */
    private void setSubcatToggleDefault(ToggleGroup tg) {
        if (tg.getSelectedToggle() == null) {
            tg.getToggles().get(0).setSelected(true);
        }
    }

    /**
     * Die Preise etwas gebräuchlicher ausgeben, mit Komma und gerundet auf zwei Nachkommastellen.
     * @param value Preis-Variable
     * @return Preis als String
     */
    private String strPriceBeautify(float value) {
        return String.valueOf(String.format("%.02f", value));
    }

    /**
     * Preisberechnung aus dem Array der Zwischenpreise.
     * @param fArray Preis-Array
     */
    private void calcFinalPriceFromArray(ObservableFloatArray fArray) {
        catDefaultFinalPrice = 0.0f;
        for (int i = 0; i < fArray.size(); i++) {
            catDefaultFinalPrice += fArray.get(i);
        }
    }

    /**
     * Server DB-Abfrage für alle Elemente.
     * Artikelname mit dem gegebenen Typ.
     * @param typeString zum Beispiel "RAHMEN"
     * @return Liste mit allen Elementen
     * Duplikate werden entfernt.
     */
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

    /**
     * Server DB-Abfrage für ein Element.
     * Produzentenname mit dem gegebenen Artikelnamen.
     * @param name Artikelname
     * @return Produzentenname als String
     */
    private String getProducerByArticleName(String name) {
        String pName = "";
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                pName = i.producer;
                break;
            }
        }
        return pName;
    }

    /**
     * Server DB-Abfrage für ein Element.
     * Characteristic mit dem gegebenen Artikelnamen.
     * @param name Artikelname
     * @return Characteristic als String
     */
    private String getCharacteristicByArticleName(String name) {
        String tName = "";
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                tName = i.characteristic;
                break;
            }
        }
        return tName;
    }

    /**
     * Server DB-Abfrage für ein Element.
     * Artikelbeschreibung mit dem gegebenen Artikelnamen.
     * @param name Artikelname
     * @return Beschreibung als String
     */
    private String getDescriptionByArticleName(String name) {
        String dText = "";
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                dText = i.description;
                break;
            }
        }
        return dText;
    }

    /**
     * Server DB-Abfrage für ein Element.
     * Preis mit dem gegebenen Artikelnamen.
     * @param name Artikelname
     * @return Preis als Float
     */
    private float getPriceByArticleName(String name) {
        float price = 0.0f;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                price = i.price;
                break;
            }
        }
        return price;
    }

    /**
     * Server DB-Abfrage für ein Element.
     * Preis mit dem gegebenen Artikelnamen und Größe.
     * @param name Artikelname
     * @param size Größen-String (S, M, L)
     * @return Preis als String
     */
    private float getPriceByArticleNameAndSize(String name, String size) {
        float price = 0.0f;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                if (i.characteristic.equals(size)) {
                    price = i.price;
                    break;
                }
            }
        }
        return price;
    }

    /**
     * Server DB-Abfrage für ein Element.
     * Artikel-ID mit dem gegebenen Artikelnamen.
     * @param name Artikelname
     * @return Artikel-ID als Integer
     */
    public int getArticleIdByName(String name) {
        int id = 0;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                id = i.id;
                break;
            }
        }
        return id;
    }

    /**
     * Server DB-Abfrage für ein Element.
     * Artikel-ID mit dem gegebenen Artikelnamen und Farbe.
     * @param name Artikelname
     * @param color Farben-Hex-Color-String
     * @return Artikel-ID als Integer
     */
    public int getArticleIDByNameAndColor(String name, String color) {
        int id = 0;
        for (Article i : Main.ARTICLES) {
            if (i.name.equals(name)) {
                if (i.hexColor.equals(color)) {
                    id = i.id;
                    break;
                }
            }
        }
        return id;
    }

    /**
     * Server DB-Abfrage für ein Element.
     * Artikel-ID mit dem gegebenen Artikelnamen, Farbe und Größe.
     * @param name Artikelname
     * @param color Farben-Hex-Color-String
     * @param size Größen-String
     * @return Artikel-ID als Integer
     */
    public int getArticleIDByNameSizeAndColor(String name, String size, String color) {
        int id = 0;
        for (Article a : Main.ARTICLES) {
            if (a.name.equals(name)) {
                if (a.characteristic.equals(size) && a.hexColor.equals(color)) {
                    id = a.id;
                    break;
                }
            }
        }
        return id;
    }

}
