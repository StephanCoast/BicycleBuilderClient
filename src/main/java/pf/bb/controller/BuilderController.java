package pf.bb.controller;

import com.jfoenix.controls.JFXDrawer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class BuilderController {

    public ToggleGroup catsTogglegroup;
    public BorderPane cat1, cat2, cat3, cat4, cat5, cat6, cat7, cat8, catDefault, catFinish, bpCats, bpCustomerData;
    ViewManager vm;

    public JFXDrawer drawerDefault, drawerCat1, drawerCat2, drawerCat3, drawerCat4, drawerCat5, drawerCat6, drawerCat7, drawerCat8, drawerBottomCats, drawerBottomData, drawerFinish;

    public BuilderController() {
    }

    @FXML
    public void initialize() throws IOException {
        vm = new ViewManager();
        vm.setBuilderSideDrawers(drawerDefault, drawerFinish, drawerCat1, drawerCat2, drawerCat3, drawerCat4, drawerCat5, drawerCat6, drawerCat7, drawerCat8);
        vm.setBuilderBottomDrawers(drawerBottomCats, drawerBottomData);
        vm.forceSidebarInitView(drawerDefault, catDefault);
        setBottomBarDefault();
    }

    public void logout(ActionEvent event) throws IOException {
        vm.forceLoginView(event, "Login.fxml", "Bicycle Builder - Login", "Login.css");
    }

    public void openDashboard(ActionEvent event) throws IOException {
        vm.forceView(event, "Dashboard.fxml", "Bicycle Builder - Dashboard", "Dashboard.css");
    }

    public void openCustomerDataView(ActionEvent event) throws IOException {
        vm.forceBottomBuilderView(event, drawerBottomData, bpCustomerData);
    }

    public void openSidebarDefault(ActionEvent event) throws IOException {
        vm.forceSidebarView(event, drawerDefault, catDefault);
        if (catsTogglegroup.getSelectedToggle() != null) {
            catsTogglegroup.getSelectedToggle().setSelected(false);
        }
    }

    public void openSidebarCat1(ActionEvent event) throws IOException {
        vm.forceSidebarView(event, drawerCat1, cat1);
    }

    public void openSidebarCat2(ActionEvent event) throws IOException {
        vm.forceSidebarView(event, drawerCat2, cat2);
    }

    public void openSidebarCat3(ActionEvent event) throws IOException {
        vm.forceSidebarView(event, drawerCat3, cat3);
    }

    public void openSidebarCat4(ActionEvent event) throws IOException {
        vm.forceSidebarView(event, drawerCat4, cat4);
    }

    public void openSidebarCat5(ActionEvent event) throws IOException {
        vm.forceSidebarView(event, drawerCat5, cat5);
    }

    public void openSidebarCat6(ActionEvent event) throws IOException {
        vm.forceSidebarView(event, drawerCat6, cat6);
    }

    public void openSidebarCat7(ActionEvent event) throws IOException {
        vm.forceSidebarView(event, drawerCat7, cat7);
    }

    public void openSidebarCat8(ActionEvent event) throws IOException {
        vm.forceSidebarView(event, drawerCat8, cat8);
    }

    public void onCat1Save(ActionEvent event) throws IOException {
        onCatSave(event);
    }

    public void onCat2Save(ActionEvent event) throws IOException {
        onCatSave(event);
    }

    public void onCat3Save(ActionEvent event) throws IOException {
        onCatSave(event);
    }

    public void onCat4Save(ActionEvent event) throws IOException {
        onCatSave(event);
    }

    public void onCat5Save(ActionEvent event) throws IOException {
        onCatSave(event);
    }

    public void onCat6Save(ActionEvent event) throws IOException {
        onCatSave(event);
    }

    public void onCat7Save(ActionEvent event) throws IOException {
        onCatSave(event);
    }

    public void onCat8Save(ActionEvent event) throws IOException {
        onCatSave(event);
    }

    public void onBottomBarClose(ActionEvent event) throws IOException {
        setBottomBarDefault();
        vm.forceBottomBuilderView(event, drawerBottomCats, bpCats);
    }

    public void onBottomBarFinish(ActionEvent event) throws IOException {
        setBottomBarDefault();
        vm.forceBottomBuilderView(event, drawerBottomCats, bpCats);
        vm.forceSidebarView(event, drawerFinish, catFinish);
        catsTogglegroup.getToggles().forEach(toggle -> {
            Node node = (Node) toggle ;
            node.setDisable(true);
        });
    }

    private void onCatSave(ActionEvent e) throws IOException {
        vm.forceSidebarView(e, drawerDefault, catDefault);
        if (catsTogglegroup.getSelectedToggle() != null) {
            catsTogglegroup.getSelectedToggle().setSelected(false);
        }
    }

    private void setBottomBarDefault() {
        drawerBottomData.close();
        drawerBottomData.setVisible(false); /* AR: BugFix - JFXButton-Events not passing the JFXDrawer UI */
    }
}
