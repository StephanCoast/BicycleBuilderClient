package pf.bb.model;
import pf.bb.Main;

/**
 * Klasse für das Rechnungsobjekt.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class Bill extends EntityWithID {

    public OrderClass order;

	public String timestampCreated;

	public static String getUrl() {
		return Main.API_HOST + "/bills";
	}


	public Bill (OrderClass order) {
		this.order = order;
	}

	public String toString() {
		return String.format(this.getClass().getName() + "[id=%d, dateCreated='%s']", id, timestampCreated);
	}

}
