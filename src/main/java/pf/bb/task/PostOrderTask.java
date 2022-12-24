package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.model.OrderClass;
import pf.bb.model.User;

public class PostOrderTask extends Task<OrderClass> {

    private final String configJSON;
    private final User user;
    private final OrderClass order;

    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

    public PostOrderTask(User user, OrderClass order) {
        this.user = user;
        this.order = order;

        //Only Serialize article IDs in GSON for less content in JSON
        this.configJSON = gson.toJson(order);
    }

    @Override
    protected OrderClass call() throws Exception {

        try {
            HttpResponse<JsonNode> res = Unirest.post(OrderClass.getUrl()).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(this.configJSON).asJson();
            String location = res.getHeaders().getFirst("Location");
            order.id = Integer.parseInt(location.substring(location.lastIndexOf("/") + 1));
            return order;

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
