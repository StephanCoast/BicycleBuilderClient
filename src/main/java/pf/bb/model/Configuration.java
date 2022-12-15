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
    //required
    public boolean writeAccess;
    public Date dateCreated;
    public Date dateLastChanged;
    public User user;
    public String status;

    //optional
    public List<Article> articles;
    public OrderClass orderClass = new OrderClass();

    public Configuration(User user) {

        String[] stats = {"ENTWURF", "ABGESCHLOSSEN", "EINKAUF", "STORNO"};

        this.user = user;
        this.status = stats[0];
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty dateCreatedProperty() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return new SimpleStringProperty(df.format(this.dateCreated));
    }

}
