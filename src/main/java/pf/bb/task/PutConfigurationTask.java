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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PutConfigurationTask extends Task<Configuration> {

    private final String configJSON;
    private final User user;

    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

    private final int oldConfigId;

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private final Configuration updatedConfig;

    public PutConfigurationTask(User user, Configuration updatedConfig, int oldConfigId) {
        this.user = user;
        this.updatedConfig = updatedConfig;
        this.oldConfigId = oldConfigId;

        //Only Serialize article IDs in GSON for less content in JSON
        this.configJSON = gson.toJson(updatedConfig);
    }

    @Override
    protected Configuration call() throws Exception {

//        Configuration mappedUpdatedConfig = new Configuration(this.user);
//        mappedUpdatedConfig.dateCreated = df.format(updatedConfig.dateCreated);
//        mappedUpdatedConfig.dateLastChanged = df.format(new Date()); // set date of update
//        mappedUpdatedConfig.status = updatedConfig.status;
//        mappedUpdatedConfig.writeAccess = updatedConfig.writeAccess;
//        mappedUpdatedConfig.orderClass = updatedConfig.orderClass;
//        mappedUpdatedConfig.user = updatedConfig.user; // user who last changed it becomes new owner

        String url = Configuration.getUrl() + "/" + this.oldConfigId;
        System.out.println("Sending put request to: " + url);

        HttpResponse<JsonNode> res = Unirest.put(url).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(this.configJSON).asJson();

//        System.out.println("Answer to PUT body:" + res.getHeaders() + "\n" + res.getBody());

        return updatedConfig;
    }
}
