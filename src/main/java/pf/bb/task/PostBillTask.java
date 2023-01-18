package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.model.Bill;
import pf.bb.model.User;

/**
 * Klasse für das Speichern von Rechnungen.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class PostBillTask extends Task<Bill> {

    private final String configJSON;
    private final User user;
    private final Bill bill;

    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.create();

    public PostBillTask(User user, Bill bill) {
        this.user = user;
        this.bill = bill;

        //Only Serialize article IDs in GSON for less content in JSON
        this.configJSON = gson.toJson(bill);
    }

    @Override
    protected Bill call() throws Exception {

        try{
            HttpResponse<JsonNode> res = Unirest.post(Bill.getUrl()).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(this.configJSON).asJson();
            String json = res.getBody().toString();
            Bill tempBill = new Gson().fromJson(json, Bill.class);
            this.bill.id = tempBill.id;
            this.bill.timestampCreated = tempBill.timestampCreated;

            return bill;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
