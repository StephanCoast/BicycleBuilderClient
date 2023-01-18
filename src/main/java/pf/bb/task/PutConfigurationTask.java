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

/**
 * Klasse für das Aktualisieren von Konfigurationen.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class PutConfigurationTask extends Task<Configuration> {

    private final String configJSON;
    private final User user;
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();     // Wichtig damit @Expose Annotation greift
    private final int oldConfigId;
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

        try {
            String url = Configuration.getUrl() + "/" + this.oldConfigId;
            System.out.println("Sending put request to: " + url);

            HttpResponse<JsonNode> res = Unirest.put(url).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(this.configJSON).asJson();

            if(res.getStatus() == 403) { //FORBIDDEN
              System.out.println("WRITE ACCESS TO CONFIGURATION DENIED, you must get write access via PutConfigurationWriteAccessTask before you can run a PutConfigurationTask!");
            }
            //System.out.println("Answer to PUT body:" + res.getHeaders() + "\n" + res.getBody());
            return updatedConfig;

            } catch (UnirestException e) {
                e.printStackTrace();
            }
        return null;
    }
}
