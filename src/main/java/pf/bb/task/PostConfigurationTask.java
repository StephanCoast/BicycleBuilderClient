package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
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

        try {
            HttpResponse<JsonNode> res = Unirest.post(Configuration.getUrl()).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(this.configJSON).asJson();
            String json = res.getBody().toString();
            Configuration tempConfig = new Gson().fromJson(json, Configuration.class);
            // Add db-generated values to Object
            this.configuration.id = tempConfig.id;
            this.configuration.timestampCreated = tempConfig.timestampCreated;
            this.configuration.timestampLastTouched = tempConfig.timestampLastTouched;
            return this.configuration;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
