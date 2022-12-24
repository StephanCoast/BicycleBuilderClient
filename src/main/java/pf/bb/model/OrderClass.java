package pf.bb.model;

import com.google.gson.annotations.Expose;
import pf.bb.Main;

public class OrderClass extends EntityWithID {

	@Expose
	public Configuration configuration;
	@Expose
	public Customer customer;
	@Expose
	public float priceTotal;
	@Expose
	public Bill bill;
	@Expose
	public String dateCreated;

	public static String getUrl() {
		return Main.API_HOST + "/orders";
	}

	public OrderClass(Configuration configuration, Customer customer, float priceTotal) {
		this.configuration = configuration;
		this.customer = customer;
		this.priceTotal = priceTotal;
	}

	@Override
	public String toString() {
		return String.format(this.getClass().getName() + "[id=%d, name='%s']", id);
	}
}
