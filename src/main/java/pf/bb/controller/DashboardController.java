package pf.bb.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pf.bb.model.BicycleConfiguration;

import java.io.IOException;

public class DashboardController {

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

    ViewManager vm = new ViewManager();

    public DashboardController() {
    }

    @FXML
    public void initialize() {
        setupTableView();
    }

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login", "Login.css");
    }

    public void openBuilder(ActionEvent event) throws IOException {
        vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator", "Builder.css");
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

}

