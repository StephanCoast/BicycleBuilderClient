package pf.bb.controller;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pf.bb.Main;
import pf.bb.model.Article;
import pf.bb.model.Configuration;
import pf.bb.model.User;
import pf.bb.task.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import static pf.bb.controller.LoginController.activeUser;

/**
 * Diese Klasse steuert die Dashboard-Ansicht des BicycleBuilder und alle enthaltenen Elemente.
 * @author Alexander Rauch
 * @supportedby Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class DashboardController {

    /**
     * Variablendeklaration für die Validierung's-Objekte der Eingabefelder im Admin- und Profilbereich.
     * Wurden in Dashboard.fxml gesetzt.
     */
    public RequiredFieldValidator validatorAdminFirstName, validatorAdminLastName, validatorAdminUserName, validatorAdminMail, validatorAdminPW;
    public RequiredFieldValidator validatorProfileFirstName, validatorProfileLastName, validatorProfileUserName, validatorProfileMail;
    /**
     * Variablendeklaration für die Text-Boxen im Admin- und Profilbereich.
     */
    public JFXTextField tfAdminFirstName, tfAdminLastName, tfAdminUserName, tfAdminMail;
    public JFXTextField tfProfileFirstName, tfProfileLastName, tfProfileUserName, tfProfileMail;
    public JFXPasswordField pfAdminPW;
    /**
     * Variablendeklaration für die Buttons: Neuer-User-Icon und Profil-Icon im Kopfbereich.
     */
    public JFXButton btnNewConfig, btnCreateUser;
    /**
     * Variablendeklaration für die JFX-Drawer der Bottombar.
     */
    public JFXDrawer drawerAdmin, drawerProfile;
    /**
     * Variablendeklaration für ein Hashset in dem nur einzigartige Elemente aufgenommen werden können.
     * Zur Anzeigen-Hilfe von verschiedenen JFX-Drawer.
     */
    private HashSet<JFXDrawer> drawersDashboard;
    /**
     * Variablendeklaration für die Halte-Container der Bottombar.
     */
    public BorderPane bpAdmin, bpProfile;
    /**
     * FXML-Variablendeklaration für den Tableview(Tabelle) und Tabellenspalten.
     */
    @FXML private TableView<Configuration> dboard_table;
    @FXML private TableColumn<Configuration, Integer> dboard_col1;
    @FXML private TableColumn<Configuration, String> dboard_col2;
    @FXML private TableColumn<Configuration, String> dboard_col3;
    @FXML private TableColumn<Configuration, Integer> dboard_col4;
    @FXML private TableColumn<Configuration, String> dboard_col5;
    @FXML private TableColumn<Configuration, Void> dboard_col6;
    /**
     * Variablendeklaration für verschiedene Singleton-Instanzen.
     * ViewManager = steuert die verschiedenen Ansichten, stellt Methoden bereit
     * ValidatorManager = initialisiert einzelne Validator, stellt Methoden bereit
     */
    ViewManager vm = ViewManager.getInstance();
    ValidatorManager validatorManager = ValidatorManager.getInstance();

    /**
     * Standard-Konstruktor der Klasse
     */
    public DashboardController() {
    }

    /**
     * FXML Konstruktor der Klasse
     * Zusammenfassung aller Funktionen, die beim Start geladen werden sollen.
     * Regelt den Initial-Status des Dashboards.
     */
    @FXML
    public void initialize() {
        dboard_table.refresh();
        setupDrawersSet();
        closeAllDrawers();
        setupValidators();
        setDefaultFocus();
        setupTableView();
        addActionButtonsToTable();
        loadConfigs();
        setCreateUserBtn();
        initTextFieldListeners();
    }

    /* =====================================
     * BUTTONS
    ========================================= */

    /**
     * Neuen Benutzer Hinzufügen Button im Kopfbereich.
     * @throws IOException Fehlerbehandlung
     * Admin-Fenster der Bottombar wird über den ViewManager angefordert.
     * Button "Neue Konfiguration" wird deaktiviert.
     * Validierung der Eingabefelder wird zurückgesetzt.
     */
    public void openAdmin() throws IOException {
        btnNewConfig.setDisable(true);
        closeAllDrawers();
        vm.forceDrawerView(drawerAdmin, bpAdmin);

        tfAdminUserName.clear();
        tfAdminFirstName.clear();
        tfAdminLastName.clear();
        tfAdminMail.clear();
        pfAdminPW.clear();

        tfAdminUserName.resetValidation();
        tfAdminFirstName.resetValidation();
        tfAdminLastName.resetValidation();
        tfAdminMail.resetValidation();
        pfAdminPW.resetValidation();
    }

    /**
     * Profil Öffnen Button im Kopfbereich.
     * @throws IOException Fehlerbehandlung
     * Profil-Fenster der Bottombar wird über den ViewManager angefordert.
     * Profildaten des angemeldeten Benutzers werden geladen und in den Textfeldern angezeigt.
     * Validierung der Eingabefelder wird zurückgesetzt.
     */
    public void openProfile() throws IOException {
        btnNewConfig.setDisable(true);
        closeAllDrawers();
        vm.forceDrawerView(drawerProfile, bpProfile);

        tfProfileFirstName.clear();
        tfProfileLastName.clear();
        tfProfileUserName.clear();
        tfProfileMail.clear();

        tfProfileFirstName.setText(activeUser.forename);
        tfProfileLastName.setText(activeUser.lastname);
        tfProfileUserName.setText(activeUser.name);
        tfProfileMail.setText(activeUser.email);

        tfProfileFirstName.resetValidation();
        tfProfileLastName.resetValidation();
        tfProfileUserName.resetValidation();
        tfProfileMail.resetValidation();
    }

    /**
     * Logout Button im Kopfbereich.
     * @param event Click-Event des Buttons
     * @throws IOException Fehlerbehandlung
     * Login-Fenster wird über den ViewManager angefordert.
     * Button "Neue Konfiguration" wird wieder aktiviert.
     */
    public void logout(ActionEvent event) throws IOException {
        btnNewConfig.setDisable(false);
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login");
    }

    /**
     * Neue Konfiguration Button
     * @param event Click-Event des Buttons
     * @throws IOException Fehlerbehandlung
     * Konfigurator-Fenster wird über den ViewManager angefordert.
     */
    public void openBuilder(ActionEvent event) throws IOException {
        vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator", false);
    }

    /**
     * X-Icon-Buttons der Bottombar.
     * Bottombar wird geschlossen.
     * Button "Neue Konfiguration" wird wieder aktiviert.
     */
    public void onBottomBarClose() {
        btnNewConfig.setDisable(false);
        closeAllDrawers();
        setDefaultFocus();
    }

    /**
     * Speichern Button im Admin-Neuer-Benutzer Container
     * @supportedby SK
     * Eingabefelder werden überprüft.
     * Neuer Nutzer wird im Backend gespeichert.
     * Button "Neue Konfiguration" wird wieder aktiviert.
     */
    public void onBottomBarSaveAdmin() {
        if (ValidatorManager.textFieldIsEmpty(tfAdminUserName) || ValidatorManager.textFieldIsEmpty(tfAdminFirstName) || ValidatorManager.textFieldIsEmpty(tfAdminLastName) || ValidatorManager.textFieldIsEmpty(tfAdminMail) || ValidatorManager.pwFieldIsEmpty(pfAdminPW)) {
            ViewManager.createWarningAlert("Bicycle Builder - Warnung", "Bitte füllen Sie alle Felder aus.", null);
        } else if (ValidatorManager.textFieldNotHaveSymbol(tfAdminMail, "@")) {
            ViewManager.createWarningAlert("Bicycle Builder - Warnung", "Die E-Mail Adresse muss ein @-Symbol enthalten.", null);
        } else {
            User newUser = new User(tfAdminUserName.getText(), pfAdminPW.getText(), tfAdminMail.getText(), tfAdminFirstName.getText(), tfAdminLastName.getText(), "CONSULTANT");
            PostUserTask userTaskNewUser = new PostUserTask(activeUser, newUser);
            userTaskNewUser.setOnSucceeded((WorkerStateEvent userCreated) -> {
                if(userTaskNewUser.getValue() != null) {
                    System.out.println("DashboardController: user id=" + userTaskNewUser.getValue().id + " created");
                    ViewManager.createInfoAlert("Bicycle Builder - Info", "Der neue Benutzer wurde gespeichert.", null);
                }
                else {
                    System.out.println("DashboardController: user creation failed for: " + newUser.id + " result: " + userTaskNewUser.getMessage());
                    ViewManager.createWarningAlert("Bicycle Builder - Warnung", "Die Erstellung des neuen Benutzers ist fehlgeschlagen.", null);
                }
            });
            userTaskNewUser.setOnFailed((WorkerStateEvent userCreatedFailed) -> System.out.println("DashboardController: user creation failed for: " + newUser.id + " result: " + userTaskNewUser.getMessage()));
            new Thread(userTaskNewUser).start();

            btnNewConfig.setDisable(false);
            closeAllDrawers();
            setDefaultFocus();
        }
    }

    /**
     * Speichern Button im Profil-Benutzer Container
     * @supportedby SK
     * Eingabefelder werden überprüft.
     * Aktualisierte Profildaten werden im Backend gespeichert.
     * Button "Neue Konfiguration" wird wieder aktiviert.
     */
    public void onBottomBarSaveProfile() {
        if (ValidatorManager.textFieldIsEmpty(tfProfileUserName) || ValidatorManager.textFieldIsEmpty(tfProfileFirstName) || ValidatorManager.textFieldIsEmpty(tfProfileLastName) || ValidatorManager.textFieldIsEmpty(tfProfileMail)) {
            ViewManager.createWarningAlert("Bicycle Builder - Warnung", "Bitte füllen Sie alle Felder aus.", null);
        } else if (ValidatorManager.textFieldNotHaveSymbol(tfProfileMail, "@")) {
            ViewManager.createWarningAlert("Bicycle Builder - Warnung", "Die E-Mail Adresse muss ein @-Symbol enthalten.", null);
        } else {
            User updatedUser = activeUser;
            updatedUser.id = activeUser.id;
            updatedUser.forename = tfProfileFirstName.getText();
            updatedUser.lastname = tfProfileLastName.getText();
            updatedUser.name = tfProfileUserName.getText();
            updatedUser.email = tfProfileMail.getText();

            PutUserTask userTaskUpdatedUser = new PutUserTask(activeUser, updatedUser, activeUser.id);
            userTaskUpdatedUser.setOnSucceeded((WorkerStateEvent userUpdated) -> {
                if (userTaskUpdatedUser.getValue() != null) {
                    System.out.println("DashboardController: user id=" + userTaskUpdatedUser.getValue().id + " updated");
                    ViewManager.createInfoAlert("Bicycle Builder - Info", "Das Nutzerprofil wurde aktualisiert.", null);
                } else {
                    System.out.println("DashboardController: user update failed for: " + updatedUser.id + " result: " + userTaskUpdatedUser.getMessage());
                    ViewManager.createWarningAlert("Bicycle Builder - Warnung", "Die Erstellung des neuen Benutzers ist fehlgeschlagen.", null);
                }
            });
            userTaskUpdatedUser.setOnFailed((WorkerStateEvent userUpdatedFailed) -> System.out.println("DashboardController: user update failed for: " + updatedUser.id + " result: " + userTaskUpdatedUser.getMessage()));
            new Thread(userTaskUpdatedUser).start();

            btnNewConfig.setDisable(false);
            closeAllDrawers();
            setDefaultFocus();
        }
    }

    /* =====================================
     * SETUP
     ========================================= */

    /**
     * Den Tabellenspalten werden Variablen aus dem Configuration-Objekt zugeordnet.
     */
    private void setupTableView() {
        dboard_col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        dboard_col2.setCellValueFactory(new PropertyValueFactory<>("timestampLastTouched"));
        dboard_col3.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dboard_col4.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        dboard_col5.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * Die JFX-Drawer der Bottombar werden dem Hashset zugeordnet.
     */
    private void setupDrawersSet() {
        drawersDashboard = new HashSet<>();
        drawersDashboard.add(drawerAdmin);
        drawersDashboard.add(drawerProfile);
    }

    /**
     * Validatoren werden über den ValidatorManager den Textfeldern zugeordnet.
     */
    private void setupValidators() {
        validatorManager.initTextValidators(tfAdminFirstName, validatorAdminFirstName);
        validatorManager.initTextValidators(tfAdminLastName, validatorAdminLastName);
        validatorManager.initTextValidators(tfAdminUserName, validatorAdminUserName);
        validatorManager.initTextValidators(tfAdminMail, validatorAdminMail);
        validatorManager.initPasswordValidators(pfAdminPW, validatorAdminPW);

        validatorManager.initTextValidators(tfProfileFirstName, validatorProfileFirstName);
        validatorManager.initTextValidators(tfProfileLastName, validatorProfileLastName);
        validatorManager.initTextValidators(tfProfileUserName, validatorProfileUserName);
        validatorManager.initTextValidators(tfProfileMail, validatorProfileMail);
    }

    /**
     * Eingabe-Regeln werden über den ValidatorManager den Textfeldern zugeordnet.
     * Über RegEx-Pattern werden Symbol-Regeln erstellt.
     */
    private void initTextFieldListeners() {
        ValidatorManager.setTextFieldRules(tfAdminUserName, "[a-zA-Z0-9]");
        ValidatorManager.setTextFieldRules(tfAdminMail, "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        ValidatorManager.setTextFieldRules(tfAdminFirstName, "[a-zA-Z-'`´]");
        ValidatorManager.setTextFieldRules(tfAdminLastName, "[a-zA-Z-'`´]");

        ValidatorManager.setTextFieldRules(tfProfileUserName, "[a-zA-Z0-9]");
        ValidatorManager.setTextFieldRules(tfProfileMail, "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        ValidatorManager.setTextFieldRules(tfProfileFirstName, "[a-zA-Z-'`´]");
        ValidatorManager.setTextFieldRules(tfProfileLastName, "[a-zA-Z-'`´]");
    }

    /**
     * Admin-Icon Button im Kopfbereich wird deaktiviert, wenn man nicht als Admin angemeldet ist.
     */
    private void setCreateUserBtn() {
        switch (activeUser.role) {
            case "ADMIN":
                btnCreateUser.setDisable(false);
                break;
            default:
                btnCreateUser.setDisable(true);
        }
    }

    /* =====================================
     * LOGIC
     ========================================= */

    /**
     * Bestehende Konfigurationen werden aus dem Backend geladen und der Tabelle hinzugefügt.
     * @supportedby SK
     */
    private void loadConfigs() {
        GetConfigurationsTask configurationsTask = new GetConfigurationsTask(activeUser);
        configurationsTask.setOnRunning((successEvent) -> System.out.println("DashboardController: loading  configurations..."));
        configurationsTask.setOnSucceeded((WorkerStateEvent e2) -> {
            System.out.println("DashboardController: configurations loaded.");
            Main.CONFIGURATIONS.clear();
            Main.CONFIGURATIONS.addAll(configurationsTask.getValue());
            dboard_table.setItems(Main.CONFIGURATIONS);
        });
        configurationsTask.setOnFailed((WorkerStateEvent e21) -> System.out.println("DashboardController: loading configuration failed."));
        new Thread(configurationsTask).start();
    }

    /**
     * Jedem Datensatz der Tabelle werden in einer zusätzlichen Spalte zwei Buttons zugeordnet.
     * Lupe-Icon Button für das Öffnen einer bestehenden Konfiguration.
     * Eimer-Icon Button für das Löschen einer bestehenden Konfiguration.
     * @supportedby SK, detail/remove Button setOnAction
     * 6. Spalte, 2 Buttons, 2 Icons, eine HBox werden erzeugt.
     * Den Buttons werden CSS-Klassen und Tooltips zugewiesen.
     * Lupe-Icon Button: Konfigurator öffnet sich mit Schreibzugriff je nach Konfigurationsstatus.
     * Eimer-Icon Button: Konfigurationsdatensatz wird nach bestätigtem Dialog gelöscht.
     * Dem Bestätigungsdialog werden Zusatzinformationen zugewiesen.
     */
    private void addActionButtonsToTable() {
        dboard_col6.setCellFactory(col -> new TableCell<>() {

            private final FontAwesomeIconView iconSearch = new FontAwesomeIconView(FontAwesomeIcon.SEARCH, "18");
            private final FontAwesomeIconView iconRemove = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT, "18");
            private final JFXButton detailButton = new JFXButton();
            private final JFXButton removeButton = new JFXButton();
            private final HBox hBox = new HBox(detailButton, removeButton);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                iconSearch.setStyleClass("table-icon-search");
                iconRemove.setStyleClass("table-icon-remove");
                detailButton.getStyleClass().add("btn-table-icon-search");
                removeButton.getStyleClass().add("btn-table-icon-remove");
                detailButton.setGraphic(iconSearch);
                removeButton.setGraphic(iconRemove);
                detailButton.setTooltip(new Tooltip("Konfiguration öffnen"));
                removeButton.setTooltip(new Tooltip("Konfiguration löschen"));

                // AR: Lupe-Icon-Button
                detailButton.setOnAction(event -> {
                    Configuration config = getTableView().getItems().get(getIndex());
                    System.out.println("row-ID detailButton: " + config.id);
                    Main.currentConfig = config;
                    // if config is in status ENTWURF, check if someone else is editing it ATM
                    if (Main.currentConfig.status.equals(Configuration.stats[0]))  {
                        // ENTWURF
                        // Get write access for Configuration in case another user is editing it atm
                        PutConfigurationWriteAccessTask writeAccessTask1 = new PutConfigurationWriteAccessTask(activeUser, Main.currentConfig.id);
                        writeAccessTask1.setOnRunning((runningEvent) -> System.out.println("trying to get writeAccess for configuration..."));
                        writeAccessTask1.setOnSucceeded((WorkerStateEvent writeAccess) -> {
                            System.out.println("OnExitDashboardController: writeAccess for configuration " + Main.currentConfig.id + " switched to: " + writeAccessTask1.getValue());
                            if (writeAccessTask1.getValue().equals("ACCESS GRANTED")) {
                                // Lokales Objekt aktualisieren
                                Main.currentConfig.writeAccess = activeUser.name;
                                Main.writeAccessGiven = true;
                                try {
                                    vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator", false);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                        writeAccessTask1.setOnFailed((writeAccessFailed) -> {
                            // TODO user notification
                            System.out.println("Couldn't get writeAccess for configuration." + writeAccessTask1.getMessage());
                        });
                        //Tasks in eigenem Thread ausführen
                        new Thread(writeAccessTask1).start();
                    } else {
                        try {
                            vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator", false);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });

                // AR: Mülleimer-Icon-Button
                removeButton.setOnAction(event -> {
                    Configuration config = getTableView().getItems().get(getIndex());

                    String newline = "\n";
                    String configID = String.valueOf(config.id);
                    String configDate = config.timestampCreated;
                    String configCustomerName = config.getCustomerName();
                    String configCustomerId = config.getCustomerId();
                    String configState = config.status;
                    ArrayList<String> configList = new ArrayList<>();
                    ArrayList<Float> priceList = new ArrayList<>();
                    float finalPrice = 0.0f;

                    for (Article a : config.articles) {
                        configList.add(a.name);
                        priceList.add(a.price);
                    }

                    for (float f : priceList) {
                        finalPrice += f;
                    }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
                    alert.setTitle("Bicycle Builder - Konfiguration");
                    alert.setHeaderText("Möchten Sie die ausgewählte Konfiguration entfernen?");
                    alert.setContentText("Konfiguration-ID: " + configID + newline
                            + "Erstellungsdatum: " + configDate + newline
                            + "Kundenname: " + configCustomerName + newline
                            + "Kunden-ID: " + configCustomerId + newline
                            + "Status: " + configState + newline
                            + "Gesamtpreis: "  + strPriceBeautify(finalPrice) + newline
                            + "Artikelliste: " + configList.toString().replace("[", "").replace("]", "").trim() + newline + newline);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        // DELETE A CONFIGURATION REQUIRES: DELETE BILL -> DELETE ORDER -> DELETE CONFIGURATION  !! FOREIGN KEY Constraints
                        // ASSUMPTION HERE: Bill is automatically created with order
                        if (config.order != null) {
                            // DELETE BILL FROM DB
                            DeleteBillTask billDeleteTask1 = new DeleteBillTask(activeUser, config.order.bill.id);
                            billDeleteTask1.setOnSucceeded((WorkerStateEvent billDeleted) -> {
                                System.out.println("bill id=" + config.order.bill.id + " deleted=" +  billDeleteTask1.getValue());

                                // DELETE ORDER FROM DB
                                DeleteOrderTask orderDeleteTask1 = new DeleteOrderTask(activeUser, config.order.id);
                                orderDeleteTask1.setOnSucceeded((WorkerStateEvent orderDeleted) -> {
                                    System.out.println("order id=" + config.order.id + " deleted=" + orderDeleteTask1.getValue());
                                    deleteConfigFromDb(config.id);
                                });
                                //Tasks in eigenem Thread ausführen
                                new Thread(orderDeleteTask1).start();
                            });
                            //Tasks in eigenem Thread ausführen
                            new Thread(billDeleteTask1).start();
                        } else {
                            deleteConfigFromDb(config.id);
                        }
                    }
                });
                setGraphic(empty ? null : hBox);
            }
        });
    }

    /**
     * Löschen der Konfiguration.
     * by Stephan Kost
     * @param configId Aktuelle Konfiguration's-ID
     */
    private void deleteConfigFromDb(int configId) {
        // DELETE CONFIGURATION FROM DB
        DeleteConfigurationTask configDeleteTask1 = new DeleteConfigurationTask(activeUser, configId);
        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
        configDeleteTask1.setOnSucceeded((WorkerStateEvent configDeleted) -> {
            System.out.println("configuration id=" + configId + " deleted=" + configDeleteTask1.getValue());

            // DELETE config FROM LOCAL Main.CONFIGURATIONS for sync
            Configuration localConfObject = Main.findConfigurationById(configId);
            if (localConfObject != null) {
                Main.CONFIGURATIONS.remove(localConfObject);
            }
        });
        //Tasks in eigenem Thread ausführen
        new Thread(configDeleteTask1).start();
    }

    /* =====================================
     * UTILITIES
     ========================================= */

    /**
     * Schließt alle Dashboard-Container-JFXDrawer
     */
    private void closeAllDrawers() {
        for (JFXDrawer i : drawersDashboard) {
            i.close();
            i.setVisible(false);
        }
    }

    /**
     * Setzt den Anwendungsfokus auf das Hauptfenster und selektiert den ersten Tabelleneintrag.
     */
    private void setDefaultFocus() {
        Platform.runLater(() -> {
            dboard_table.requestFocus();
            dboard_table.getSelectionModel().select(0);
            dboard_table.getFocusModel().focus(0);
        });
    }

    /**
     * Die Preise etwas gebräuchlicher ausgeben, mit Komma und gerundet auf zwei Nachkommastellen.
     * @param value Preis-Variable
     * @return Preis als String
     */
    private String strPriceBeautify(float value) {
        return String.valueOf(String.format("%.02f", value));
    }
}

