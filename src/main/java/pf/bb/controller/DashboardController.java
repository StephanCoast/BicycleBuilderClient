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

public class DashboardController {

    public JFXDrawer drawerAdmin, drawerProfile;
    public BorderPane bpAdmin, bpProfile;
    @FXML
    private TableView<BicycleConfiguration> dboard_table;
    @FXML
    private TableColumn<BicycleConfiguration, Integer> dboard_col1;
    @FXML
    private TableColumn<BicycleConfiguration, String> dboard_col2;
    @FXML
    private TableColumn<BicycleConfiguration, String> dboard_col3;
    @FXML
    private TableColumn<BicycleConfiguration, Integer> dboard_col4;
    @FXML
    private TableColumn<BicycleConfiguration, String> dboard_col5;

    ViewManager vm;

    public DashboardController() {
    }

    @FXML
    public void initialize() {
        vm = new ViewManager();
        vm.setDashboardDrawers(drawerAdmin, drawerProfile);
        setupTableView();
        closeBottomBar();
    }

    public void openDashboard(ActionEvent event) throws IOException {
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", "Dashboard.css");
    }

    public void openAdmin(ActionEvent event) throws IOException {
        vm.forceBottomDashboardView(event, drawerAdmin, bpAdmin);
    }

    public void openProfile(ActionEvent event) throws IOException {
        vm.forceBottomDashboardView(event, drawerProfile, bpProfile);
    }

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login", "Login.css");
    }

    public void openBuilder(ActionEvent event) throws IOException {
        vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator", "Builder.css");
    }

    public void onBottomBarClose(ActionEvent event) {
        closeBottomBar();
    }

    public void onBottomBarSaveAdmin(ActionEvent event) {
        closeBottomBar();
    }

    public void onBottomBarSaveProfile(ActionEvent event) {
        closeBottomBar();
    }

    private void setupTableView() {
        dboard_col1.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, Integer>("configID"));
        dboard_col2.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, String>("configDate"));
        dboard_col3.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, String>("configCustomer"));
        dboard_col4.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, Integer>("configCustomerID"));
        dboard_col5.setCellValueFactory(new PropertyValueFactory<BicycleConfiguration, String>("configState"));
        dboard_table.setItems( setDummyData() );
    }

    private ObservableList<BicycleConfiguration> setDummyData() {
        ObservableList<BicycleConfiguration> list = FXCollections.observableArrayList();
        list.add(new BicycleConfiguration(1, "2022-11-21", "Schulz", 111, "offen"));
        list.add(new BicycleConfiguration(2, "2022-11-21", "Schmidt", 222, "offen"));
        return list;
    }

    private void closeBottomBar() {
        drawerAdmin.close();
        drawerProfile.close();
        drawerAdmin.setVisible(false); /* AR: BugFix - JFXButton-Events not passing the JFXDrawer UI */
        drawerProfile.setVisible(false); /* AR: BugFix - JFXButton-Events not passing the JFXDrawer UI */
    }
}

