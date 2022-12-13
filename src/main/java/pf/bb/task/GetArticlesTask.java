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

import java.util.ArrayList;
import java.util.List;

public class GetArticlesTask extends Task<List<Article>> {

	@Override
	protected List<Article> call() throws Exception {
		String url = Main.API_HOST + "/articles";
		HttpResponse<JsonNode> resJson = Unirest.get(url).header("Accept", "application/json").asJson();
		String json = resJson.getBody().toString();


		JsonArray jsonProducts = JsonParser.parseString(json).getAsJsonObject().getAsJsonObject("_embedded").getAsJsonArray("articles");
		List<Article> articles = new Gson().fromJson(jsonProducts, new TypeToken<ArrayList<Article>>() {}.getType());

		// parse product id from JSON response
		DocumentContext jsonContext = JsonPath.parse(json);
		for (int i = 0; i < articles.size(); i++) {
			String jsonPathSelector = "$._embedded.articles[" + i + "]._links.self.href";
			String articleUri = jsonContext.read(jsonPathSelector);
			articles.get(i).id = Integer.parseInt(articleUri.substring(articleUri.lastIndexOf("/") + 1));
		}

		return articles;
	}
}
