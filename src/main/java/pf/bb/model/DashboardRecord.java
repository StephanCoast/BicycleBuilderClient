package pf.bb.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DashboardRecord extends EntityWithID {

    private SimpleIntegerProperty configID;
    private SimpleStringProperty configDate;
    private SimpleStringProperty configCustomer;
    private SimpleIntegerProperty configCustomerID;
    private SimpleStringProperty configState;


    // constructor
    public DashboardRecord(int configID, String configDate, String configCustomer, int configCustomerID, String configState) {
        this.configID = new SimpleIntegerProperty(configID);
        this.configDate = new SimpleStringProperty(configDate);
        this.configCustomer = new SimpleStringProperty(configCustomer);
        this.configCustomerID = new SimpleIntegerProperty(configCustomerID);
        this.configState = new SimpleStringProperty(configState);
    }

    // setter
    public void setConfigID(int id) { this.configID = new SimpleIntegerProperty(id); }
    public void setConfigDate(String date) { this.configDate = new SimpleStringProperty(date); }
    public void setConfigCustomer(String customer) { this.configCustomer = new SimpleStringProperty(customer); }
    public void setConfigCustomerID(int customerID) { this.configCustomerID = new SimpleIntegerProperty(customerID); }
    public void setConfigState(String configState) { this.configState = new SimpleStringProperty(configState); }

    //getter
    public int getConfigID() { return configID.get(); }
    public String getConfigDate() {
        return configDate.get();
    }
    public String getConfigCustomer() {
        return configCustomer.get();
    }
    public int getConfigCustomerID() {
        return configCustomerID.get();
    }
    public String getConfigState() {
        return configState.get();
    }
}
