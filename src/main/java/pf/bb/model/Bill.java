package pf.bb.model;
import pf.bb.Main;


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
