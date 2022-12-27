package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.model.User;

public class PutUserTask extends Task<User> {

    private final String configJSON;
    private final User user;
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();     // Wichtig damit @Expose Annotation greift
    private final int oldUserId;
    private final User updatedUser;

    public PutUserTask(User user, User updatedUser, int oldUserId) {
        this.user = user;
        this.updatedUser = updatedUser;
        this.oldUserId = oldUserId;

        //Only Serialize article IDs in GSON for less content in JSON
        this.configJSON = gson.toJson(updatedUser);
    }

    @Override
    protected User call() throws Exception {

        try {
            String url = User.getUrl() + "/" + this.oldUserId;
            System.out.println("Sending put request to: " + url);

            JsonObject obj = JsonParser.parseString(configJSON).getAsJsonObject();
            HttpResponse<JsonNode> res = Unirest.put(url).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(obj.toString()).asJson();
            //System.out.println("Answer to PUT body:" + res.getHeaders() + "\n" + res.getBody());

            return updatedUser;

            } catch (UnirestException e) {
                e.printStackTrace();
            }
        return null;
    }
}
