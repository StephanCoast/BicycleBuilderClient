package pf.bb.model;

import com.google.gson.annotations.Expose;
import pf.bb.Main;

/**
 * Klasse für das Auftragsobjekt.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class OrderClass extends EntityWithID {

	@Expose
	public Configuration configuration;
	@Expose
	public Customer customer;
	@Expose
	public float priceTotal;

	public void setBill(Bill bill) {
		bill.order = null; // must be null otherwise GSON serialize in loop
		this.bill = bill;
	}

	@Expose
	public Bill bill;
	@Expose
	public String timestampCreated;

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
		return String.format(this.getClass().getName() + "[id=%d, created='%s']", id, timestampCreated);
	}
}
