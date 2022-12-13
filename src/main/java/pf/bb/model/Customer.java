package pf.bb.model;

public class Customer extends EntityWithID {

    public String email;
    public String forename;
    public String lastname;
    public String street;
    public int houseNumber;
    public String zipCode;
    public String city;

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
