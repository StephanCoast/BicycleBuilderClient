package pf.bb.controller;

import com.jfoenix.controls.JFXDrawer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import pf.bb.model.BicycleConfiguration;

import java.io.IOException;
import java.util.HashSet;

public class DashboardController {

    public JFXDrawer drawerAdmin, drawerProfile;
    private HashSet<JFXDrawer> drawersDashboard;
    public BorderPane bpAdmin, bpProfile;
    @FXML private TableView<BicycleConfiguration> dboard_table;
    @FXML private TableColumn<BicycleConfiguration, Integer> dboard_col1;
    @FXML private TableColumn<BicycleConfiguration, String> dboard_col2;
    @FXML private TableColumn<BicycleConfiguration, String> dboard_col3;
    @FXML private TableColumn<BicycleConfiguration, Integer> dboard_col4;
    @FXML private TableColumn<BicycleConfiguration, String> dboard_col5;

    ViewManager vm = ViewManager.getInstance();

    public DashboardController() {
    }

    @FXML
    public void initialize() {
        setupDrawersSet();
        setupTableView();
        closeAllDrawers();
    }

    public void openDashboard(ActionEvent event) throws IOException {
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard");
    }

    public void openAdmin(ActionEvent event) throws IOException {
        closeAllDrawers();
        vm.forceDrawerView(drawerAdmin, bpAdmin);
    }

    public void openProfile(ActionEvent event) throws IOException {
        closeAllDrawers();
        vm.forceDrawerView(drawerProfile, bpProfile);
    }

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login");
    }

    public void openBuilder(ActionEvent event) throws IOException {
        vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator");
    }

    public void onBottomBarClose(ActionEvent event) {closeAllDrawers();}

    // AR: hier speichert der Admin einen neuen User
    // todo: speichern des neuen Users -> Server? bzw. DB?
    // Stephan
    public void onBottomBarSaveAdmin(ActionEvent event) {
        closeAllDrawers();
    }

    // AR: hier speichert der User neue Daten für sein Profil
    // todo: speichern des neuen Profils -> Server? bzw. DB?
    // Stephan
    public void onBottomBarSaveProfile(ActionEvent event) {
        closeAllDrawers();
    }

    private void setupTableView() {
        dboard_col1.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, Integer>("configID"));
        dboard_col2.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, String>("configDate"));
        dboard_col3.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, String>("configCustomer"));
        dboard_col4.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, Integer>("configCustomerID"));
        dboard_col5.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, String>("configState"));
        dboard_table.setItems(setTableData());
    }

    // todo: einzelne Methoden für CRUD-Operationen implementieren bzw ServerDB-Abfrage
    // Stephan
    // todo: Date korrekt formatieren
    private ObservableList<BicycleConfiguration> setTableData() {
        ObservableList<BicycleConfiguration> list = FXCollections.observableArrayList();
        // AR: add dummy data
        list.add(new BicycleConfiguration(1, "2022-11-21", "Schulz", 111, "offen"));
        list.add(new BicycleConfiguration(2, "2022-11-21", "Schmidt", 222, "offen"));

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
}

