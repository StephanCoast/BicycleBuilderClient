package pf.bb.task;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.Main;
import pf.bb.model.User;

/**
 * Klasse für das Abrufen von Benutzerinformationen.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class GetUserDetailsTask extends Task<User> {

	private final User user;

	public GetUserDetailsTask(User user) {
		this.user = user;
	}

	@Override
	protected User call() throws Exception {
		try {
		String url = Main.API_HOST + "/users/" + user.name;
		HttpResponse<JsonNode> resJson = Unirest.get(url).header("Accept", "application/json").header("Authorization", user.jsonWebToken).asJson();
		String json = resJson.getBody().toString();
		return new Gson().fromJson(json, User.class);

		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}
}
