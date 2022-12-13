package pf.bb.model;

import java.util.ArrayList;
import java.util.List;

public class User extends EntityWithID {

	public String name;
	public String jsonWebToken;

	public String forename;

	public String lastname;

	public UserRole userRole;

    public List<Configuration> configurations = new ArrayList<>();

	public User(String name, String jsonWebToken) {
		this.name = name;
		this.jsonWebToken = jsonWebToken;
	}

}
