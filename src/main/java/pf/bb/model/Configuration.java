package pf.bb.model;

import com.google.gson.annotations.Expose;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pf.bb.Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Configuration extends EntityWithID {
    //required
    @Expose
    public boolean writeAccess;

    public Date dateCreated; //Do not expose for serialization because creation Date is created serverside and cannot be changed
    @Expose
    public Date dateLastChanged;
    @Expose
    public User user;
    @Expose
    public String status;

    //optional
    @Expose
    public ArrayList<Article> articles;
    @Expose
    public OrderClass orderClass;

    public static String[] stats = {"ENTWURF", "ABGESCHLOSSEN", "EINKAUF", "STORNO"};
    private static String url = Main.API_HOST + "/configurations";
    public static String getUrl() {
        return url;
    }

    public Configuration(User user) {

        this.writeAccess = true;
        this.user = user;
        this.status = stats[0];
        this.articles = new ArrayList<>();
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty dateCreatedProperty() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return new SimpleStringProperty(df.format(this.dateCreated));
    }

}
