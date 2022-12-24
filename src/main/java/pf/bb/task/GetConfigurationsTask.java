package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.Main;
import pf.bb.model.Configuration;
import pf.bb.model.User;

import java.util.ArrayList;
import java.util.List;

public class GetConfigurationsTask extends Task<List<Configuration>> {

	private final User user;

	public GetConfigurationsTask(User user) {
		this.user = user;
	}
	
	@Override
	protected List<Configuration> call() throws Exception {
		try{
		String url = Main.API_HOST + "/configurations";
		HttpResponse<JsonNode> resJson = Unirest.get(url).header("Accept", "application/json").header("Authorization", user.jsonWebToken).asJson();
		String json = resJson.getBody().toString();
		return new Gson().fromJson(json, new TypeToken<ArrayList<Configuration>>() {}.getType());

		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}
}
