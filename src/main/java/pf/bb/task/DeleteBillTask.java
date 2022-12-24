package pf.bb.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.model.Bill;
import pf.bb.model.User;

public class DeleteBillTask extends Task<Boolean> {

	private final User user;
	private final Integer billId;

	public DeleteBillTask(User user, Integer billId) {
		this.user = user;
		this.billId = billId;
	}

	@Override
	protected Boolean call() throws Exception {
		try{
			String url = Bill.getUrl() + "/" + billId;
			System.out.println("Sending DELETE request to: " + url);
			HttpResponse<JsonNode> res = Unirest.delete(url).header("Authorization", user.jsonWebToken).asJson();
			return res.getStatus() == 204;
		} catch (UnirestException e) {
			e.printStackTrace();
		}
        return null;

	}
}
