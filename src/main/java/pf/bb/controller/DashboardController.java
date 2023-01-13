package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pf.bb.Main;
import pf.bb.model.Configuration;
import pf.bb.task.GetConfigurationsTask;

import java.io.IOException;
import java.util.HashSet;

import static pf.bb.controller.LoginController.activeUser;

public class DashboardController {

    public RequiredFieldValidator validatorAdminUserID, validatorAdminFirstName, validatorAdminLastName, validatorAdminUserName, validatorAdminMail, validatorAdminPW;
    public RequiredFieldValidator validatorProfileUserID, validatorProfileFirstName, validatorProfileLastName, validatorProfileUserName, validatorProfileMail;
    public JFXTextField tfAdminUserID, tfAdminFirstName, tfAdminLastName, tfAdminUserName, tfAdminMail;
    public JFXTextField tfProfileUserID, tfProfileFirstName, tfProfileLastName, tfProfileUserName, tfProfileMail;
    public JFXPasswordField pfAdminPW;
    public JFXButton btnNewConfig;
    public JFXDrawer drawerAdmin, drawerProfile;
    private HashSet<JFXDrawer> drawersDashboard;
    public BorderPane bpAdmin, bpProfile;

    @FXML private TableView<Configuration> dboard_table;

    @FXML private TableColumn<Configuration, Integer> dboard_col1;
    @FXML private TableColumn<Configuration, String> dboard_col2;
    @FXML private TableColumn<Configuration, String> dboard_col3;
    @FXML private TableColumn<Configuration, Integer> dboard_col4;
    @FXML private TableColumn<Configuration, String> dboard_col5;
    @FXML private TableColumn<Configuration, Void> dboard_col6;

//    @FXML private TableView<DashboardRecord> dboard_table;
//    @FXML private TableColumn<DashboardRecord, Integer> dboard_col1;
//    @FXML private TableColumn<DashboardRecord, String> dboard_col2;
//    @FXML private TableColumn<DashboardRecord, String> dboard_col3;
//    @FXML private TableColumn<DashboardRecord, Integer> dboard_col4;
//    @FXML private TableColumn<DashboardRecord, String> dboard_col5;
//    @FXML private TableColumn<DashboardRecord, Void> dboard_col6;
//    private ObservableList<DashboardRecord> list = FXCollections.observableArrayList();
    ViewManager vm = ViewManager.getInstance();
    ValidatorManager validatorManager = ValidatorManager.getInstance();

    public DashboardController() {
    }

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
    }

    public void openDashboard(ActionEvent event) throws IOException {
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", false);
    }

    public void openAdmin(ActionEvent event) throws IOException {
        btnNewConfig.setDisable(true);
        closeAllDrawers();
        vm.forceDrawerView(drawerAdmin, bpAdmin);
    }

    public void openProfile(ActionEvent event) throws IOException {
        btnNewConfig.setDisable(true);
        closeAllDrawers();
        vm.forceDrawerView(drawerProfile, bpProfile);
    }

    public void logout(ActionEvent event) throws IOException {
        btnNewConfig.setDisable(false);
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login");
    }

    public void openBuilder(ActionEvent event) throws IOException {
        vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator", false);
    }

    public void onBottomBarClose(ActionEvent event) {
        btnNewConfig.setDisable(false);
        closeAllDrawers();
        setDefaultFocus();
    }

    // AR: hier speichert der Admin einen neuen User
    // todo: speichern des neuen Users
    public void onBottomBarSaveAdmin(ActionEvent event) {
        btnNewConfig.setDisable(false);
        closeAllDrawers();
        setDefaultFocus();
    }

    // AR: hier speichert der User neue Daten für sein Profil
    // todo: speichern des neuen Profils
    public void onBottomBarSaveProfile(ActionEvent event) {
        btnNewConfig.setDisable(false);
        closeAllDrawers();
        setDefaultFocus();
    }

    private void loadConfigs() {
        GetConfigurationsTask configurationsTask = new GetConfigurationsTask(activeUser);
        configurationsTask.setOnRunning((successEvent) -> System.out.println("DashboardController: loading  configurations..."));
        configurationsTask.setOnSucceeded((WorkerStateEvent e2) -> {
            System.out.println("DashboardController: configurations loaded.");
            Main.CONFIGURATIONS.clear();
            Main.CONFIGURATIONS.addAll(configurationsTask.getValue());
//            list.clear();
//            // todo: AR: get Customer + ID
//            for (Configuration config : Main.CONFIGURATIONS) {
//                list.add(new DashboardRecord(config.id, config.timestampCreated, "Mustermann, Max", 12345, config.status));
//            }
            dboard_table.setItems(Main.CONFIGURATIONS);
        });
        configurationsTask.setOnFailed((WorkerStateEvent e21) -> System.out.println("DashboardController: loading configuration failed."));
        new Thread(configurationsTask).start();
    }


    private void setupTableView() {

        dboard_col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        dboard_col2.setCellValueFactory(new PropertyValueFactory<>("timestampLastTouched"));
        dboard_col3.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dboard_col4.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        dboard_col5.setCellValueFactory(new PropertyValueFactory<>("status"));

//        dboard_col1.setCellValueFactory(new PropertyValueFactory<DashboardRecord, Integer>("configID"));
//        dboard_col2.setCellValueFactory(new PropertyValueFactory<DashboardRecord, String>("configDate"));
//        dboard_col3.setCellValueFactory(new PropertyValueFactory<DashboardRecord, String>("configCustomer"));
//        dboard_col4.setCellValueFactory(new PropertyValueFactory<DashboardRecord, Integer>("configCustomerID"));
//        dboard_col5.setCellValueFactory(new PropertyValueFactory<DashboardRecord, String>("configState"));
//        dboard_table.getColumns().forEach(e -> e.setReorderable(false)); /* AR: prevent column reorder */
//        dboard_table.setPlaceholder(new Label("Bitte starten Sie eine neue Konfiguration."));
        //dboard_table.setItems(setTableData());
    }

//    private void setupTableView() {
//        dboard_col1.setCellValueFactory(new PropertyValueFactory<DashboardRecord, Integer>("configID"));
//        dboard_col2.setCellValueFactory(new PropertyValueFactory<DashboardRecord, String>("configDate"));
//        dboard_col3.setCellValueFactory(new PropertyValueFactory<DashboardRecord, String>("configCustomer"));
//        dboard_col4.setCellValueFactory(new PropertyValueFactory<DashboardRecord, Integer>("configCustomerID"));
//        dboard_col5.setCellValueFactory(new PropertyValueFactory<DashboardRecord, String>("configState"));
//        dboard_table.getColumns().forEach(e -> e.setReorderable(false)); /* AR: prevent column reorder */
//        dboard_table.setPlaceholder(new Label("Bitte starten Sie eine neue Konfiguration."));
//        //dboard_table.setItems(setTableData());
//    }

/*
    private ObservableList<DashboardRecord> setTableData() {
        ObservableList<DashboardRecord> list = FXCollections.observableArrayList();
        // AR: add dummy data
        list.add(new DashboardRecord(12345, "2022-11-21", "Schulz, Tom", 12345, "offen"));
        list.add(new DashboardRecord(56789, "2022-11-21", "Schmidt, Hans", 56789, "offen"));

        return list;
    }
 */

    private void setupDrawersSet() {
        drawersDashboard = new HashSet<>();
        drawersDashboard.add(drawerAdmin);
        drawersDashboard.add(drawerProfile);
    }

    private void closeAllDrawers() {
        for (JFXDrawer i : drawersDashboard) {
            i.close();
            i.setVisible(false);
        }
    }

    private void setDefaultFocus() {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                dboard_table.requestFocus();
                dboard_table.getSelectionModel().select(0);
                dboard_table.getFocusModel().focus(0);
            }
        });
    }

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

                detailButton.setOnAction(event -> {
//                    DashboardRecord bc = getTableView().getItems().get(getIndex());
//                    System.out.println("row-ID detailButton: " + bc.getConfigID());
                    Configuration config = getTableView().getItems().get(getIndex());
                    System.out.println("row-ID detailButton: " + config.id);
                });

                removeButton.setOnAction(event -> {
//                    DashboardRecord bc = getTableView().getItems().get(getIndex());
//                    System.out.println("row-ID removeButton: " + bc.getConfigID());
                    Configuration config = getTableView().getItems().get(getIndex());
                    System.out.println("row-ID removeButton: " + config.id);
                });

                setGraphic(empty ? null : hBox);
            }
        });
    }

    private void setupValidators() {
        validatorManager.initTextValidators(tfAdminUserID, validatorAdminUserID);
        validatorManager.initTextValidators(tfAdminFirstName, validatorAdminFirstName);
        validatorManager.initTextValidators(tfAdminLastName, validatorAdminLastName);
        validatorManager.initTextValidators(tfAdminUserName, validatorAdminUserName);
        validatorManager.initTextValidators(tfAdminMail, validatorAdminMail);
        validatorManager.initPasswordValidators(pfAdminPW, validatorAdminPW);

        validatorManager.initTextValidators(tfProfileUserID, validatorProfileUserID);
        validatorManager.initTextValidators(tfProfileFirstName, validatorProfileFirstName);
        validatorManager.initTextValidators(tfProfileLastName, validatorProfileLastName);
        validatorManager.initTextValidators(tfProfileUserName, validatorProfileUserName);
        validatorManager.initTextValidators(tfProfileMail, validatorProfileMail);
    }
}

