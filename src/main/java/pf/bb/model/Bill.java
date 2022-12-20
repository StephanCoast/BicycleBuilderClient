package pf.bb.model;

import java.util.Date;

public class Bill extends EntityWithID {


    public OrderClass orderClass;

	public Date dateCreated = new Date();

	public String toString() {
		return String.format(this.getClass().getName() + "[id=%d, dateCreated='%s']", id, dateCreated);
	}

}
