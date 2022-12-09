package pf.bb.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Configuration {
    public Integer id;
    public Date dateCreated = new Date();
    public Integer bookingPrice;
    public List<Article> articles;
    public User user;

    public Configuration(Integer bookingPrice, List<Article> articles, User user) {
        this.bookingPrice = bookingPrice;
        this.articles = articles;
        this.user = user;
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty dateCreatedProperty() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return new SimpleStringProperty(df.format(dateCreated));
    }

    public IntegerProperty bookingPriceProperty() {
        return new SimpleIntegerProperty(bookingPrice);
    }
}
