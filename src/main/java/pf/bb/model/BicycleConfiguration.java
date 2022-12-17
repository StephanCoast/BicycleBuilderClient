package pf.bb.model;

public class BicycleConfiguration {

    int configID;
    String configDate;
    String configCustomer;
    int configCustomerID;
    String configState;

    // constructor
    public BicycleConfiguration(int configID, String configDate, String configCustomer, int configCustomerID, String configState) {
        this.configID = configID;
        this.configDate = configDate;
        this.configCustomer = configCustomer;
        this.configCustomerID = configCustomerID;
        this.configState = configState;
    }

    // setter
    public void setConfigID(int id) { this.configID = id; }
    public void setConfigDate(String date) { this.configDate = date; }
    public void setConfigCustomer(String customer) { this.configCustomer = customer; }
    public void setConfigCustomerID(int customerID) { this.configCustomerID = customerID; }
    public void setConfigState(String configState) { this.configState = configState; }

    //getter
    public int getConfigID() {
        return configID;
    }
    public String getConfigDate() {
        return configDate;
    }
    public String getConfigCustomer() {
        return configCustomer;
    }
    public int getConfigCustomerID() {
        return configCustomerID;
    }
    public String getConfigState() {
        return configState;
    }
}
