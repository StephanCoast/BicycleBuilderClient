package pf.bb.model;

import java.util.ArrayList;
import java.util.List;

public class User extends EntityWithID {

	public String name;
	public String jsonWebToken;

	public String forename;

	public String lastname;

	public String role;

    public List<Configuration> configurations = new ArrayList<>();

	public User(String name, String jsonWebToken) {
		this.name = name;
		this.jsonWebToken = jsonWebToken;
	}

	public String toString() {
		return String.format(this.getClass().getName() + "[id=%d, dateCreated='%s']", id, name);
	}


}
