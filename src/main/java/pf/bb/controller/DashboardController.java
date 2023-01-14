package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pf.bb.Main;
import pf.bb.model.Configuration;
import pf.bb.task.DeleteBillTask;
import pf.bb.task.DeleteConfigurationTask;
import pf.bb.task.DeleteOrderTask;
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
    public JFXButton btnNewConfig, btnCreateUser;
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
        setCreateUserBtn();
        initTextFieldListeners();
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
    }

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
                    Configuration config = getTableView().getItems().get(getIndex());
                    System.out.println("row-ID detailButton: " + config.id);
                    Main.currentConfig = config;
                    try {
                        vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator", false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                removeButton.setOnAction(event -> {
                    Configuration config = getTableView().getItems().get(getIndex());
                    System.out.println("row-ID removeButton: " + config.id);

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
                });
                setGraphic(empty ? null : hBox);
            }
        });
    }

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

    private void setCreateUserBtn() {
        switch (activeUser.role) {
            case "ADMIN":
                btnCreateUser.setDisable(false);
                break;
            default:
                btnCreateUser.setDisable(true);
        }
    }

    private void initTextFieldListeners() {
        setTextFieldRules(tfAdminUserID, "[\\d+]");
        setTextFieldRules(tfAdminUserName, "[a-zA-Z0-9]");
        setTextFieldRules(tfAdminMail, "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        setTextFieldRules(tfAdminFirstName, "[a-zA-Z-'`´]");
        setTextFieldRules(tfAdminLastName, "[a-zA-Z-'`´]");

        setTextFieldRules(tfProfileUserID, "[\\d+]");
        setTextFieldRules(tfProfileUserName, "[a-zA-Z0-9]");
        setTextFieldRules(tfProfileMail, "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        setTextFieldRules(tfProfileFirstName, "[a-zA-Z-'`´]");
        setTextFieldRules(tfProfileLastName, "[a-zA-Z-'`´]");

        int maxLengthID = 10;

        tfAdminUserID.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tfAdminUserID.getText().length() > maxLengthID) {
                String str = tfAdminUserID.getText().substring(0, maxLengthID);
                tfAdminUserID.setText(str);
            }
        });

        tfProfileUserID.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tfProfileUserID.getText().length() > maxLengthID) {
                String str = tfProfileUserID.getText().substring(0, maxLengthID);
                tfProfileUserID.setText(str);
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

