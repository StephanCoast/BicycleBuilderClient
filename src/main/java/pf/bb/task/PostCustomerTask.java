package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.model.Customer;
import pf.bb.model.User;

/**
 * Klasse für das Speichern von Kundendaten.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class PostCustomerTask extends Task<Customer> {

    private final String configJSON;
    private final User user;

    private final Customer customer;

    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

    public PostCustomerTask(User user, Customer customer) {
        this.user = user;
        this.customer = customer;

        //Only Serialize article IDs in GSON for less content in JSON
        this.configJSON = gson.toJson(customer);
    }

    @Override
    protected Customer call() throws Exception {

        try {
            HttpResponse<JsonNode> res = Unirest.post(Customer.getUrl()).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(this.configJSON).asJson();

            String location = res.getHeaders().getFirst("Location");
            customer.id = Integer.parseInt(location.substring(location.lastIndexOf("/") + 1));

            return customer;
        } catch (UnirestException e){
            e.printStackTrace();
        }
        return null;

    }
}
