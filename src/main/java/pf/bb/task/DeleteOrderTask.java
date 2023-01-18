package pf.bb.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.model.OrderClass;
import pf.bb.model.User;

/**
 * Klasse für das Löschen eines Auftrags.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class DeleteOrderTask extends Task<Boolean> {

	private final User user;
	private final Integer orderId;

	public DeleteOrderTask(User user, Integer orderId) {
		this.user = user;
		this.orderId = orderId;
	}

	@Override
	protected Boolean call() throws Exception {
		try{
			String url = OrderClass.getUrl() + "/" + orderId;
			System.out.println("Sending DELETE request to: " + url);
			HttpResponse<JsonNode> res = Unirest.delete(url).header("Authorization", user.jsonWebToken).asJson();
			return res.getStatus() == 204;
		} catch (UnirestException e) {
			e.printStackTrace();
		}
        return null;

	}
}
