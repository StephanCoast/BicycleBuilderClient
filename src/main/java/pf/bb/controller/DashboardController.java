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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static pf.bb.controller.LoginController.activeUser;

/**
 * Diese Klasse steuert die Dashboard-Ansicht des BicycleBuilder und alle enthaltenen Elemente.
 * @author Alexander Rauch
 * supported by Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
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
     * Variablendeklaration für die Buttons: Neue Konfiguration und Nutzer erstellen
     */
    public JFXButton btnNewConfig, btnCreateUser, btnOpenProfile, btnLogout;
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
        initIconButtonTooltips();
        getAllUsers();
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
     * supported by SK
     * Eingabefelder werden überprüft.
     * Neuer Nutzer wird im Backend gespeichert.
     * Button "Neue Konfiguration" wird wieder aktiviert.
     */
    public void onBottomBarSaveAdmin() {
        if (validatorManager.textFieldIsEmpty(tfAdminUserName) || validatorManager.textFieldIsEmpty(tfAdminFirstName) || validatorManager.textFieldIsEmpty(tfAdminLastName) || validatorManager.textFieldIsEmpty(tfAdminMail) || validatorManager.pwFieldIsEmpty(pfAdminPW)) {
            vm.createWarningAlert("Bicycle Builder - Warnung", "Bitte füllen Sie alle Felder aus.", null);
        } else if (validatorManager.textFieldNotHaveSymbol(tfAdminMail, "@")) {
            vm.createWarningAlert("Bicycle Builder - Warnung", "Die E-Mail Adresse muss ein @-Symbol enthalten.", null);
        } else if (validatorManager.textFieldUserMailExists(tfAdminMail.getText())) {
            vm.createWarningAlert("Bicycle Builder - Warnung", "Die gewünschte E-Mail Adresse existiert bereits.", null);
        } else if (validatorManager.textFieldUserNameExists(tfAdminUserName.getText())) {
            vm.createWarningAlert("Bicycle Builder - Warnung", "Der gewünschte Benutzername existiert bereits.", null);
        } else {
            User newUser = new User(tfAdminUserName.getText(), pfAdminPW.getText(), tfAdminMail.getText(), tfAdminFirstName.getText(), tfAdminLastName.getText(), "CONSULTANT");
            PostUserTask userTaskNewUser = new PostUserTask(activeUser, newUser);
            userTaskNewUser.setOnSucceeded((WorkerStateEvent userCreated) -> {
                if(userTaskNewUser.getValue() != null) {
                    System.out.println("DashboardController: user id=" + userTaskNewUser.getValue().id + " created");
                    vm.createInfoAlert("Bicycle Builder - Info", "Der neue Benutzer wurde gespeichert.", null);
                } else {
                    System.out.println("DashboardController: user creation failed for: " + newUser.id + " result: " + userTaskNewUser.getMessage());
                    vm.createWarningAlert("Bicycle Builder - Warnung", "Die Erstellung des neuen Benutzers ist fehlgeschlagen.", null);
                }
            });
            userTaskNewUser.setOnFailed((WorkerStateEvent userCreatedFailed) -> System.out.println("DashboardController setOnFailed(): user creation failed for: " + newUser.id + " result: " + userTaskNewUser.getMessage()));
            new Thread(userTaskNewUser).start();

            btnNewConfig.setDisable(false);
            closeAllDrawers();
            setDefaultFocus();
        }
    }

    /**
     * Speichern Button im Profil-Benutzer Container
     * supported by SK
     * Eingabefelder werden überprüft.
     * Update User Methode wird aufgerufen.
     */
    public void onBottomBarSaveProfile() {
        if (validatorManager.textFieldIsEmpty(tfProfileUserName) || validatorManager.textFieldIsEmpty(tfProfileFirstName) || validatorManager.textFieldIsEmpty(tfProfileLastName) || validatorManager.textFieldIsEmpty(tfProfileMail)) {
            vm.createWarningAlert("Bicycle Builder - Warnung", "Bitte füllen Sie alle Felder aus.", null);
        } else if (validatorManager.textFieldNotHaveSymbol(tfProfileMail, "@")) {
            vm.createWarningAlert("Bicycle Builder - Warnung", "Die E-Mail Adresse muss ein @-Symbol enthalten.", null);
        } else if (!tfProfileMail.getText().equals(activeUser.email)) {
            if (validatorManager.textFieldUserMailExists(tfProfileMail.getText())) {
                vm.createWarningAlert("Bicycle Builder - Warnung", "Die gewünschte E-Mail Adresse existiert bereits.", null);
            } else { updateUserTask(); }
        } else if (!tfProfileUserName.getText().equals(activeUser.name)) {
            if (validatorManager.textFieldUserNameExists(tfProfileUserName.getText())) {
                vm.createWarningAlert("Bicycle Builder - Warnung", "Der gewünschte Benutzername existiert bereits.", null);
            } else { updateUserTask(); }
        } else { updateUserTask(); }
    }

    /**
     * Aktualisierte Profildaten werden im Backend gespeichert.
     * Button "Neue Konfiguration" wird wieder aktiviert.
     */
    private void updateUserTask() {
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
                vm.createInfoAlert("Bicycle Builder - Info", "Das Nutzerprofil wurde aktualisiert.", null);
            } else {
                System.out.println("DashboardController: user update failed for: " + updatedUser.id + " result: " + userTaskUpdatedUser.getMessage());
                vm.createWarningAlert("Bicycle Builder - Warnung", "Die Erstellung des neuen Benutzers ist fehlgeschlagen.", null);
            }
        });
        userTaskUpdatedUser.setOnFailed((WorkerStateEvent userUpdatedFailed) -> System.out.println("DashboardController: user update failed for: " + updatedUser.id + " result: " + userTaskUpdatedUser.getMessage()));
        new Thread(userTaskUpdatedUser).start();

        btnNewConfig.setDisable(false);
        closeAllDrawers();
        setDefaultFocus();
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
        validatorManager.setTextFieldRules(tfAdminUserName, "[a-zöäüß][A-ZÖÄÜ][0-9]");
        validatorManager.setTextFieldRules(tfAdminMail, "[a-zöäüß][A-ZÖÄÜ][0-9][._%+-]+@[a-zöäü][A-ZÖÄÜ][0-9][.-]+\\.[a-zöäü][A-ZÖÄÜ]{2,6}$");
        validatorManager.setTextFieldRules(tfAdminFirstName, "[a-zöäüß][A-ZÖÄÜ][-'`´]");
        validatorManager.setTextFieldRules(tfAdminLastName, "[a-zöäüß][A-ZÖÄÜ][-'`´]");

        validatorManager.setTextFieldRules(tfProfileUserName, "[a-zöäüß][A-ZÖÄÜ][0-9]");
        validatorManager.setTextFieldRules(tfProfileMail, "[a-zöäüß][A-ZÖÄÜ][0-9][._%+-]+@[a-zöäü][A-ZÖÄÜ][0-9][.-]+\\.[a-zöäü][A-ZÖÄÜ]{2,6}$");
        validatorManager.setTextFieldRules(tfProfileFirstName, "[a-zöäüß][A-ZÖÄÜ][-'`´]");
        validatorManager.setTextFieldRules(tfProfileLastName, "[a-zöäüß][A-ZÖÄÜ][-'`´]");
    }

    /**
     * Admin-Icon Button im Kopfbereich wird deaktiviert, wenn man nicht als Admin angemeldet ist.
     */
    private void setCreateUserBtn() {
        btnCreateUser.setDisable(!"ADMIN".equals(activeUser.role));
    }

    /**
     * Tooltips erstellen und den Icon-Buttons im Kopfbereich zuordnen.
     */
    private void initIconButtonTooltips() {
        Tooltip ttIconCreateUser = new Tooltip("Nutzer anlegen");
        Tooltip ttIconOpenProfile = new Tooltip("Benutzer: " + activeUser.name + " - ID: " + activeUser.id);
        Tooltip ttIconLogout = new Tooltip("Logout");
        btnCreateUser.setTooltip(ttIconCreateUser);
        btnOpenProfile.setTooltip(ttIconOpenProfile);
        btnLogout.setTooltip(ttIconLogout);
        ttIconCreateUser.getStyleClass().add("header-icon-tooltip");
        ttIconOpenProfile.getStyleClass().add("header-icon-tooltip");
        ttIconLogout.getStyleClass().add("header-icon-tooltip");
    }

    /* =====================================
     * LOGIC
     ========================================= */

    /**
     * Bestehende Konfigurationen werden aus dem Backend geladen und der Tabelle hinzugefügt.
     * supported by SK
     */
    private void loadConfigs() {
        GetConfigurationsTask configurationsTask = new GetConfigurationsTask(activeUser);
        configurationsTask.setOnRunning((successEvent) -> System.out.println("DashboardController: loading  configurations..."));
        configurationsTask.setOnSucceeded((WorkerStateEvent e2) -> {
            System.out.println("DashboardController: configurations loaded.");
            Main.CONFIGURATIONS.clear();
            Main.CONFIGURATIONS.addAll(configurationsTask.getValue());
            dboard_table.setItems(Main.CONFIGURATIONS);
            formatDateColumn();
        });
        configurationsTask.setOnFailed((WorkerStateEvent e21) -> System.out.println("DashboardController: loading configuration failed."));
        new Thread(configurationsTask).start();
    }

    /**
     * Formatiert den Zeitstempel der Tabelle in eine gebräuchliche Reihenfolge.
     */
    private void formatDateColumn() {
        dboard_col2.setCellFactory(col -> new TableCell<>() {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.GERMANY);

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    try {
                        setText(df.format(sdf.parse(item)));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    /**
     * Jedem Datensatz der Tabelle werden in einer zusätzlichen Spalte zwei Buttons zugeordnet.
     * Lupe-Icon Button für das Öffnen einer bestehenden Konfiguration.
     * Eimer-Icon Button für das Löschen einer bestehenden Konfiguration.
     * supported by SK, detail/remove Button setOnAction
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

                Tooltip tt1 = new Tooltip("Konfiguration öffnen");
                Tooltip tt2 = new Tooltip("Konfiguration löschen");
                detailButton.setTooltip(tt1);
                removeButton.setTooltip(tt2);
                tt1.getStyleClass().add("table-tooltip");
                tt2.getStyleClass().add("table-tooltip");

                // AR: Lupe-Icon-Button
                detailButton.setOnAction(event -> {
                    Main.currentConfig = getTableView().getItems().get(getIndex());
                    // if config is in status ENTWURF, check if someone else is editing it ATM
                    if (Main.currentConfig.status.equals(Configuration.stats[0]))  {
                        // ENTWURF
                        // Get write access for Configuration in case another user is editing it atm
                        PutConfigurationWriteAccessTask writeAccessTask1 = new PutConfigurationWriteAccessTask(activeUser, Main.currentConfig.id);
                        writeAccessTask1.setOnRunning((runningEvent) -> System.out.println("trying to get writeAccess for configuration..."));
                        writeAccessTask1.setOnSucceeded((WorkerStateEvent writeAccess) -> {
                            System.out.println("OnExitDashboardController: writeAccess for configuration " + Main.currentConfig.id + " switched to: " + writeAccessTask1.getValue());
                            String newline = "\n";
                            String answer = writeAccessTask1.getValue();
                            if (writeAccessTask1.getValue().startsWith("ACCESS GRANTED")) {
                                // Lokales Objekt aktualisieren
                                Main.currentConfig.writeAccess = activeUser.name;
                                Main.writeAccessGiven = true;
                                try {
                                    vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator", false);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (writeAccessTask1.getValue().startsWith("ACCESS DENIED")){
                                String username = answer.substring(answer.lastIndexOf(':') + 1);
                                vm.createErrorAlert("Bicycle Builder - Fehler", "Zugriffsfehler", "Die gewählte Konfiguration wird gerade von " + newline + username + " bearbeitet."  + newline);
                            } else {
                                System.out.println("Write access wasn't returned correctly last time, because the program crashed in Builder view. Please try to open the configuration again.");
                                vm.createErrorAlert("Bicycle Builder - Fehler", "Zugriffsfehler", "Die Berechtigung zum Bearbeiten der Konfiguration wurde fehlerhaft zurückgegeben. Bitte versuchen Sie die Konfiguration erneut zu öffnen." + newline + newline);
                            }
                        });
                        writeAccessTask1.setOnFailed((writeAccessFailed) -> System.out.println("Couldn't get writeAccess for configuration. Server does not respond." + writeAccessTask1.getMessage()));
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

                    if ((!activeUser.role.equals("ADMIN")) && (config.status.equals("ABGESCHLOSSEN"))) {
                        System.out.println("DashboardController: no access authorization to remove finished config's...");
                        vm.createErrorAlert("Bicycle Builder - Fehler", "Zugriffsfehler", "Die gewählte Konfiguration ist bereits abgeschlossen und kann nur von einem Administrator entfernt werden." + newline + newline);
                    } else {
                        // DELETE DIALOG
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
                        alert.setTitle("Konfiguration löschen");
                        alert.setHeaderText("Möchten Sie die ausgewählte Konfiguration entfernen?");
                        alert.setContentText("Konfiguration-ID: " + config.id + newline
                                + "Erstellungsdatum: " + config.timestampCreated + newline
                                + "Kundenname: " + config.getCustomerName() + newline
                                + "Kunden-ID: " + config.getCustomerId() + newline
                                + "Status: " + config.status + newline
                                + "Gesamtpreis: "  + strPriceBeautify(config.getPriceTotal()) + " €" + newline + newline);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.YES) {
                            // DELETE A CONFIGURATION REQUIRES: DELETE BILL -> DELETE ORDER -> DELETE CONFIGURATION  !! FOREIGN KEY Constraints

                            // ENTWURF: Get write access for Configuration in ENTWURF status in case another user is editing it atm -> doesn't need to be returned since config is deleted
                            if (config.status.equals(Configuration.stats[0]))  {
                                PutConfigurationWriteAccessTask writeAccessTask2 = new PutConfigurationWriteAccessTask(activeUser, config.getId());
                                writeAccessTask2.setOnRunning((runningEvent) -> System.out.println("trying to get writeAccess for configuration..."));
                                writeAccessTask2.setOnSucceeded((WorkerStateEvent writeAccess) -> {
                                    System.out.println("OnDeleteDashboardController: writeAccess for configuration " + config.getId() + " switched to: " + writeAccessTask2.getValue());
                                    String answer = writeAccessTask2.getValue();
                                    if (writeAccessTask2.getValue().startsWith("ACCESS GRANTED")) {
                                        // ENTWURF has no foreign key constraints -> can be deleted directly
                                        deleteConfigFromDb(config.id);

                                    } else if (writeAccessTask2.getValue().startsWith("ACCESS DENIED")){
                                        String username = answer.substring(answer.lastIndexOf(':') + 1);
                                        vm.createErrorAlert("Bicycle Builder - Fehler", "Zugriffsfehler", "Die gewählte Konfiguration wird gerade von " + newline + username + " bearbeitet."  + newline);
                                    } else {
                                        System.out.println("Write access wasn't returned correctly last time, because the program crashed in Builder view. Please try to open the configuration again.");
                                        vm.createErrorAlert("Bicycle Builder - Fehler", "Zugriffsfehler", "Die Berechtigung zum Bearbeiten der Konfiguration wurde fehlerhaft zurückgegeben. Bitte versuchen Sie die Konfiguration erneut zu öffnen." + newline + newline);
                                    }
                                });
                                writeAccessTask2.setOnFailed((writeAccessFailed) -> System.out.println("Couldn't get writeAccess for configuration. Server does not respond." + writeAccessTask2.getMessage()));
                                //Tasks in eigenem Thread ausführen
                                new Thread(writeAccessTask2).start();
                            } else {
                            // ABGESCHLOSSEN: DELETE BY ADMIN WITHOUT WRITE ACCESS FOR CONFIGURATION IN FINISHED STATUS
                            // ASSUMPTION HERE: Bill and Order are automatically created with transition to status "ABGESCHLOSSEN"
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
                            }
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
        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig, dann starten
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

    /**
     * Überführe alle existierenden User in die Array-Liste des ValidatorManager.
     */
    private void getAllUsers() {
        GetUsersTask usersTask = new GetUsersTask(activeUser);
        usersTask.setOnSucceeded((WorkerStateEvent getUsers) -> {
            validatorManager.USERS.clear();
            validatorManager.USERS.addAll(usersTask.getValue());
        });
        new Thread(usersTask).start();
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

