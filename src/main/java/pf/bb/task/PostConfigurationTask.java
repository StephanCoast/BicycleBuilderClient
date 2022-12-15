package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

    public PostConfigurationTask(User user, Configuration configuration) {
        this.user = user;
        this.configuration = configuration;
        this.configJSON = new Gson().toJson(configuration);
    }

    @Override
    protected Configuration call() throws Exception {
        String url = Main.API_HOST + "/configurations";

//        jsonObject.addProperty("user", Main.API_HOST + "/users/" + configuration.user.id);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user", Main.API_HOST + "/users/" + configuration.user.id);
        jsonObject.addProperty("status", this.configuration.status);

        HttpResponse<JsonNode> res = Unirest.post(url).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(jsonObject.toString()).asJson();



        String location = res.getHeaders().getFirst("Location");
        configuration.id = Integer.parseInt(location.substring(location.lastIndexOf("/") + 1));
        return configuration;
    }
}
