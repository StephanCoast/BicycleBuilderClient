package pf.bb.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Configuration extends EntityWithID {
    public Date dateCreated = new Date();
    public List<Article> articles;
    public User user;

    public ConfigurationStatus status = new ConfigurationStatus();

    public Date dateLastChanged = null;

    public boolean writeAccess = true;

    public OrderClass orderClass = new OrderClass();


    public Configuration(User user) {
        this.user = user;
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty dateCreatedProperty() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return new SimpleStringProperty(df.format(dateCreated));
    }

}
