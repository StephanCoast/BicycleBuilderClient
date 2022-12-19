package pf.bb.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import pf.bb.Main;
import pf.bb.model.*;

public class TestApi {

    private static String username = "Consultant";
    private static String password = "osmi";
    private static boolean loggedIn = false;
    private static User user = null;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();



    public static void main(String[] args) throws UnirestException {

        // LOGIN get jsonWebToken for request-header
        String url = Main.API_HOST + "/login";
        String json = "{\"name\": \"" + username + "\", \"passwordHash\": \"" + password + "\"}";

        try {
            HttpResponse<String> res = Unirest.post(url).header("Content-Type", "application/json").body(json).asString();
            if (res.getStatus() != 403) {
                String jsonWebToken = res.getHeaders().getFirst("Authorization");
                user = new User(username, jsonWebToken);

                try {
                    testApiGet("configurations");
                    testApiGet("customers");
                } catch (UnirestException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public static void testApiGet(String subUrl) throws UnirestException {

        // NOW allowed to query other URLs
        String url = Main.API_HOST + "/" + subUrl;
        HttpResponse<JsonNode> resJson = Unirest.get(url).header("Accept", "application/json").header("Authorization", user.jsonWebToken).asJson();
        String json = resJson.getBody().toString();

        JsonElement je = JsonParser.parseString(json);
        String prettyJsonString = gson.toJson(je);

        System.out.println("#1 GET Request to URL: /" + subUrl);
        System.out.println("#1 GET Request Response: /" + prettyJsonString);


    }




}
