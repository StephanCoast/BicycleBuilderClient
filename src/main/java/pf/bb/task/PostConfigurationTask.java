package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import javafx.concurrent.Task;
import pf.bb.Main;
import pf.bb.model.Configuration;
import pf.bb.model.User;

public class PostConfigurationTask extends Task<Configuration> {

    private final String configJSON;
    private final User user;
    private final Configuration configuration;

    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

    public PostConfigurationTask(User user, Configuration configuration) {
        this.user = user;
        this.configuration = configuration;

        //Only Serialize article IDs in GSON for less content in JSON
        this.configJSON = gson.toJson(configuration);
    }

    @Override
    protected Configuration call() throws Exception {
        String url = Main.API_HOST + "/configurations";

        //IDs or URLS necessary?
//        String articleIds[] = new String[this.configuration.articles.size()];
//
//        for (int i=0; i<this.configuration.articles.size(); i++) {
////            articleIds[i] = "{\"" + Main.API_HOST + "/articles/" + this.configuration.articles.get(i).id + "\"}";
//            Main.API_HOST + "/articles/" + this.configuration.articles.get(i).id
//        }
//        System.out.println(Arrays.toString(articleIds));
//        String articleIdJson = new Gson().toJson(articleIds);

//        // FLATTEN - MAP JAVA Object to reduced JSON-TransferObject
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("user", Main.API_HOST + "/users/" + configuration.user.id);
//        jsonObject.addProperty("status", this.configuration.status);
//        jsonObject.addProperty("writeAccess", this.configuration.writeAccess);
//        if (this.configuration.orderClass != null) {
//            jsonObject.addProperty("orderClass", this.configuration.orderClass.id);
//        }
//
////        jsonObject.addProperty("articles", articleIdJson);

        HttpResponse<JsonNode> res = Unirest.post(url).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(this.configJSON).asJson();
//        Configuration tempConfig = gson.fromJson(res.getBody().toString(),Configuration.class);

        // Somehow necessary to get Location header for completing task succesfully, security?
        String location = res.getHeaders().getFirst("Location");
        configuration.id = Integer.parseInt(location.substring(location.lastIndexOf("/") + 1));

        return configuration;
    }
}
