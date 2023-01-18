package pf.bb.model;

import com.google.gson.annotations.Expose;
import pf.bb.Main;

/**
 * Klasse für das Benutzerobjekt.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class User extends EntityWithID {

	@Expose
	public String name;
	@Expose
	public String passwordHash;

	@Expose
	public String email;

	public String jsonWebToken;

	@Expose
	public String forename;

	@Expose
	public String lastname;

	@Expose
	public String role;

	public static String getUrl() {
		return Main.API_HOST + "/users";
	}


	public User(String name, String jsonWebToken) {
		this.name = name;
		this.jsonWebToken = jsonWebToken;
	}


	public User(String name, String passwordHash, String email, String forename, String lastname, String role) {
		this.name = name;
		this.passwordHash = passwordHash;
		this.email = email;
		this.forename = forename;
		this.lastname = lastname;
		this.role = role;
	}

	public String toString() {
		return String.format(this.getClass().getName() + "[id=%d, dateCreated='%s']", id, name);
	}


}
