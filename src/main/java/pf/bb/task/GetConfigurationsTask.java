package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import javafx.concurrent.Task;
import pf.bb.Main;
import pf.bb.model.Article;
import pf.bb.model.Configuration;

import java.util.ArrayList;
import java.util.List;

public class GetConfigurationsTask extends Task<List<Configuration>> {

	@Override
	protected List<Configuration> call() throws Exception {
		String url = Main.API_HOST + "/configurations";
		HttpResponse<JsonNode> resJson = Unirest.get(url).header("Accept", "application/json").asJson();
		String json = resJson.getBody().toString();
		List<Configuration> configurations = new Gson().fromJson(json, new TypeToken<ArrayList<Configuration>>() {}.getType());

		return configurations;
	}
}
