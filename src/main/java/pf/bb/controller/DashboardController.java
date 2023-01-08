package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pf.bb.model.BicycleConfiguration;

import java.io.IOException;
import java.util.HashSet;

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
    @FXML private TableView<BicycleConfiguration> dboard_table;
    @FXML private TableColumn<BicycleConfiguration, Integer> dboard_col1;
    @FXML private TableColumn<BicycleConfiguration, String> dboard_col2;
    @FXML private TableColumn<BicycleConfiguration, String> dboard_col3;
    @FXML private TableColumn<BicycleConfiguration, Integer> dboard_col4;
    @FXML private TableColumn<BicycleConfiguration, String> dboard_col5;
    @FXML private TableColumn<BicycleConfiguration, Void> dboard_col6;

    ViewManager vm = ViewManager.getInstance();
    ValidatorManager validatorManager = ValidatorManager.getInstance();

    public DashboardController() {
    }

    @FXML
    public void initialize() {
        setupDrawersSet();
        setupTableView();
        closeAllDrawers();
        addActionButtonsToTable();
        setupValidators();
        setDefaultFocus();
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
    // todo: speichern des neuen Users -> Server? bzw. DB?
    // Stephan
    public void onBottomBarSaveAdmin(ActionEvent event) {
        btnNewConfig.setDisable(false);
        closeAllDrawers();
        setDefaultFocus();
    }

    // AR: hier speichert der User neue Daten für sein Profil
    // todo: speichern des neuen Profils -> Server? bzw. DB?
    // Stephan
    public void onBottomBarSaveProfile(ActionEvent event) {
        btnNewConfig.setDisable(false);
        closeAllDrawers();
        setDefaultFocus();
    }

    private void setupTableView() {
        dboard_col1.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, Integer>("configID"));
        dboard_col2.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, String>("configDate"));
        dboard_col3.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, String>("configCustomer"));
        dboard_col4.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, Integer>("configCustomerID"));
        dboard_col5.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, String>("configState"));
        dboard_table.setItems(setTableData());
        dboard_table.getColumns().forEach(e -> e.setReorderable(false)); /* AR: prevent column reorder */
    }

    // todo: einzelne Methoden für CRUD-Operationen implementieren bzw ServerDB-Abfrage
    // Stephan
    // todo: Date korrekt formatieren
    private ObservableList<BicycleConfiguration> setTableData() {
        ObservableList<BicycleConfiguration> list = FXCollections.observableArrayList();
        // AR: add dummy data
        list.add(new BicycleConfiguration(12345, "2022-11-21", "Schulz, Tom", 12345, "offen"));
        list.add(new BicycleConfiguration(56789, "2022-11-21", "Schmidt, Hans", 56789, "offen"));

        return list;
    }

    private void setupDrawersSet() {
        drawersDashboard = new HashSet<JFXDrawer>();
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
                    BicycleConfiguration bc = getTableView().getItems().get(getIndex());
                    System.out.println("row-ID detailButton: " + bc.getConfigID());
                });

                removeButton.setOnAction(event -> {
                    BicycleConfiguration bc = getTableView().getItems().get(getIndex());
                    System.out.println("row-ID removeButton: " + bc.getConfigID());
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

