package pf.bb.model;

import java.util.ArrayList;
import java.util.List;

public class User {

	public Integer id;
	public String name;
	public String jsonWebToken;

//	public List<Configuration> configurations = new ArrayList<>();

	public User(String name, String jsonWebToken) {
		this.name = name;
		this.jsonWebToken = jsonWebToken;
	}
}
