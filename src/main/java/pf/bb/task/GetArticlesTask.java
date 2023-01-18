package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.Main;
import pf.bb.model.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse für das Abrufen von Artikeln.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class GetArticlesTask extends Task<List<Article>> {
	
	@Override
	protected List<Article> call() throws Exception {

		try{
			String url = Main.API_HOST + "/articles";
			HttpResponse<JsonNode> resJson = Unirest.get(url).header("Accept", "application/json").asJson();
			String json = resJson.getBody().toString();

			return new Gson().fromJson(json, new TypeToken<ArrayList<Article>>() {}.getType());
		} catch (UnirestException e) {
			e.printStackTrace();
		}
        return null;

	}
}
