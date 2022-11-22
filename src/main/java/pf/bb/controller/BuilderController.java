package pf.bb.controller;

import javafx.event.ActionEvent;

import java.io.IOException;

public class BuilderController {

    ViewManager vm = new ViewManager();

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login", "Login.css");
    }

    public void openDashboard(ActionEvent event) throws IOException {
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", "Dashboard.css");
    }

    public void openOverview(ActionEvent event) throws IOException {
        vm.forceView(event, "Overview.fxml", "Bicycle Builder - Auftragsübersicht", "Overview.css");
    }

    public void openCustomerData(ActionEvent event) throws IOException {
        vm.forceView(event, "CustomerData.fxml", "Bicycle Builder - Kundendaten", "CustomerData.css");
    }
}
