package pf.bb.model;

import com.google.gson.annotations.Expose;
import pf.bb.Main;

public class Customer extends EntityWithID {
    @Expose
    public String email;
    @Expose
    public String forename;
    @Expose
    public String lastname;
    @Expose
    public String street;
    @Expose
    public int houseNumber;
    @Expose
    public String zipCode;
    @Expose
    public String city;

    public static String getUrl() {
        return Main.API_HOST + "/customers";
    }

    public Customer(String email, String forename, String lastname, String street, int houseNumber, String zipCode, String city) {

        this.email = email;
        this.forename = forename;
        this.lastname = lastname;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.city = city;
    }

    @Override
    public String toString() {
        return String.format(this.getClass().getName() + "[id=%d, vorname='%s', nachname='%s', email='%s']", id, forename, lastname, email);
    }
}
