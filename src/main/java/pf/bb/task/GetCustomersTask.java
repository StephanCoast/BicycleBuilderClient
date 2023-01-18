package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.Main;
import pf.bb.model.Customer;
import pf.bb.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse für das Abrufen von Konfigurationen.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class GetCustomersTask extends Task<List<Customer>> {

	private final User user;

	public GetCustomersTask(User user) {
		this.user = user;
	}
	
	@Override
	protected List<Customer> call() throws Exception {
		try{
			String url = Main.API_HOST + "/customers";
			HttpResponse<JsonNode> res = Unirest.get(url).header("Accept", "application/json").header("Authorization", user.jsonWebToken).asJson();
			String json = res.getBody().toString();
			return new Gson().fromJson(json, new TypeToken<ArrayList<Customer>>() {}.getType());

		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}
}
