package omega;

import java.util.Random;
import org.json.JSONObject;
import org.json.JSONArray;
import omega.EventHandler;
import omega.ConnectionListener;
import humanhttp.ParameterMap;
import humanhttp.HttpClient;

public class OmegaClient extends EventHandler implements Runnable {
	private ConnectionListener connectionListener = new ConnectionListener();
	private Random randomGenerator = new Random();
	private HttpClient httpClient = new HttpClient();
	private String randomId;
	private String clientId;

	// Random selections
	private String selectServer() {
		return "http://front" + (randomGenerator.nextInt(10) + 1) + ".omega.localhost/";
	}

	private String selectRandomId() {
		StringBuilder stringBuilder = new StringBuilder();

		for (int a = 0; a < 8; a++) {
			stringBuilder.append("23456789ABCDEFGHJKLMNPQRSTUVWXYZ".charAt(randomGenerator.nextInt(32)));
		}

		return stringBuilder.toString();
	}

	// API calls
	public Boolean sendMessage(String message) {
		ParameterMap parameterMap = new ParameterMap();
		parameterMap.put("id", clientId);
		parameterMap.put("msg", message);

		if (httpClient.post("send", parameterMap) == "win") {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Boolean startTyping() {
		ParameterMap parameterMap = new ParameterMap();
		parameterMap.put("id", clientId);

		if (httpClient.post("typing", parameterMap) == "win") {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Boolean stopTyping() {
		ParameterMap parameterMap = new ParameterMap();
		parameterMap.put("id", clientId);

		if (httpClient.post("stoppedtyping", parameterMap) == "win") {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Boolean connect() {
		return connect("");
	}

	public Boolean connect(String interests) {
		ParameterMap parameterMap = new ParameterMap();
		parameterMap.put("rcs", 1);
		parameterMap.put("firstevents", 1);
		parameterMap.put("spid", "");
		parameterMap.put("randid", randomId);
		parameterMap.put("lang", "en");

		if (interests != "") {
			parameterMap.put("topics", interests);
		}

		// Handle response
		JSONObject json = new JSONObject(httpClient.get("start", parameterMap));
		JSONArray events = json.getJSONArray("events");
		clientId = json.getString("clientID");
		handleEvents(events);

		while (connectionListener.isNotConnected()) {
			handleEvents(getEvents());
		}

		(new Thread(this)).start();

		return Boolean.TRUE;
	}

	public Boolean disconnect() {
		ParameterMap parameterMap = new ParameterMap();
		parameterMap.put("id", clientId);

		if (httpClient.post("disconnect", parameterMap) == "win") {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public JSONArray getEvents() {
		ParameterMap parameterMap = new ParameterMap();
		parameterMap.put("id", clientId);

		return new JSONArray(httpClient.post("events", parameterMap));
	}

	public void run() {
		while (connectionListener.isConnected()) {
			handleEvents(getEvents());
		}
	}

	public OmegaClient() {
		super();

		randomId = selectRandomId();
		httpClient.setBaseUrl(selectServer());
		addListener(connectionListener);
	}
}
