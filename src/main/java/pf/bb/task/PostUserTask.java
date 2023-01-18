package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.model.User;

/**
 * Klasse für das Speichern von neuen Benutzern.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class PostUserTask extends Task<User> {

    private final String newUserJson;
    private final User activeUser;
    private final User newUser;
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.create();



    public PostUserTask(User activeUser, User newUser) {
        this.activeUser = activeUser;
        this.newUser = newUser;

        //Only Serialize article IDs in GSON for less content in JSON
        this.newUserJson = gson.toJson(newUser);

    }

    @Override
    protected User call() throws Exception {

        try{
            System.out.println("Sending POST request to: " + User.getUrl());
            HttpResponse<JsonNode> res = Unirest.post(User.getUrl()).header("Content-Type", "application/json").header("Authorization", activeUser.jsonWebToken).body(this.newUserJson).asJson();

            String location = res.getHeaders().getFirst("Location");
            if (location == null) {
                this.updateMessage(res.getStatus() + "");
            } else {
                newUser.id = Integer.parseInt(location.substring(location.lastIndexOf("/") + 1));
                // Lesbares Passwort gleich wieder löschen
                newUser.passwordHash = null;
                return newUser;
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

}
