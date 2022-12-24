package pf.bb.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import pf.bb.Main;
import pf.bb.model.User;

public class PutConfigurationWriteAccessTask extends Task<String> {

	private final int configId;
	private final User user;

	public PutConfigurationWriteAccessTask(User user, int configId) {
		this.user = user;
		this.configId = configId;
	}

	@Override
	protected String call() throws Exception {
		try {
		String url = Main.API_HOST + "/configurations/" + configId + "/writeAccess";
		HttpResponse<String> resJson = Unirest.put(url).header("Accept", "application/json").header("Authorization", user.jsonWebToken).asString();
		String answer = resJson.getBody();
		return answer; // ["ACCESS GRANTED", "ACCESS DENIED", "NOT FOUND"]] == possible server answers

		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}
}
