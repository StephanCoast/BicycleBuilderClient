package pf.bb.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

import java.util.Date;

public class OrderClass extends EntityWithID {


	public Configuration configuration;

	public Customer customer;

	public float priceTotal;

	public Bill bill;

	public Date dateCreated = new Date();

	public FloatProperty priceTotalProperty() {
		return new SimpleFloatProperty(priceTotal);

		//TODO sum the price of all configuration.articles

	}

	@Override
	public String toString() {
		return String.format(this.getClass().getName() + "[id=%d, name='%s']", id);
	}
}
