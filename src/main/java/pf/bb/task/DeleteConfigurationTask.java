package pf.bb.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import javafx.concurrent.Task;
import pf.bb.Main;
import pf.bb.model.User;

public class DeleteConfigurationTask extends Task<Boolean> {

	private final User user;
	private final Integer configId;

	public DeleteConfigurationTask(User user, Integer configId) {
		this.user = user;
		this.configId = configId;
	}

	@Override
	protected Boolean call() throws Exception {
		String url = Main.API_HOST + "/configurations/" + configId;
		HttpResponse<JsonNode> res = Unirest.delete(url).header("Authorization", user.jsonWebToken).asJson();
		return res.getStatus() == 204;
	}
}
