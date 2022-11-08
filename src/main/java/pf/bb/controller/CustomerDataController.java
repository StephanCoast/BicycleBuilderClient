package pf.bb.controller;

import javafx.event.ActionEvent;

import java.io.IOException;

public class CustomerDataController {

    ViewManager vm = new ViewManager();

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login");
    }

    public void openOverview(ActionEvent event) throws IOException {
        vm.forceView(event, "Overview.fxml", "Bicycle Builder - Auftragsübersicht");
    }

    public void openBuilder(ActionEvent event) throws IOException {
        vm.forceView(event, "Builder.fxml", "Bicycle Builder - Konfigurator");
    }
}
