package omega;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONArray;

import omega.ConnectionListener;
import omega.EventListener;
import omega.EventHandler;

import humanhttp.ParameterMap;
import humanhttp.HttpClient;

public class OmegaClient implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(OmegaClient.class.getName());

	private ConnectionListener connectionListener = new ConnectionListener();
	private EventHandler eventHandler = new EventHandler();
	private Random randomGenerator = new Random();
	private HttpClient httpClient = new HttpClient();

	private String randomId;
	private String clientId;

	public void addListener(EventListener listener) {
		eventHandler.addListener(listener);
	}

	public void removeListener(EventListener listener) {
		eventHandler.removeListener(listener);
	}

	public synchronized String getServer() {
		return httpClient.getBaseUrl();
	}

	public synchronized void setServer(String server) {
		httpClient.setBaseUrl(server);

		LOGGER.log(Level.INFO, "Set server: " + getServer());
	}

	public void setRandomServer() {
		setServer("http://front" + (randomGenerator.nextInt(10) + 1) + ".omega.localhost/");
	}

	public synchronized String getRandomId() {
		return randomId;
	}

	public synchronized void setRandomId(String randomId) {
		this.randomId = randomId;

		LOGGER.log(Level.INFO, "Set random ID: " + getRandomId());
	}

	public void setRandomRandomId() {
		StringBuilder stringBuilder = new StringBuilder();

		for (int a = 0; a < 8; a++) {
			stringBuilder.append("23456789ABCDEFGHJKLMNPQRSTUVWXYZ".charAt(randomGenerator.nextInt(32)));
		}

		setRandomId(stringBuilder.toString());
	}

	// API calls
	public JSONArray getEvents() {
		ParameterMap parameters = new ParameterMap();
		parameters.put("id", clientId);

		return new JSONArray(httpClient.post("events", parameters));
	}

	public boolean sendMessage(String message) {
		if (connectionListener.isNotConnected()) {
			return false;
		}

		ParameterMap parameters = new ParameterMap();
		parameters.put("id", clientId);
		parameters.put("msg", message);

		if (httpClient.post("send", parameters) == "win") {
			return true;
		} else {
			return false;
		}
	}

	public boolean startTyping() {
		if (connectionListener.isNotConnected()) {
			return false;
		}

		ParameterMap parameters = new ParameterMap();
		parameters.put("id", clientId);

		if (httpClient.post("typing", parameters) == "win") {
			return true;
		} else {
			return false;
		}
	}

	public boolean stopTyping() {
		if (connectionListener.isNotConnected()) {
			return false;
		}

		ParameterMap parameters = new ParameterMap();
		parameters.put("id", clientId);

		if (httpClient.post("stoppedtyping", parameters) == "win") {
			return true;
		} else {
			return false;
		}
	}

	public boolean connect() {
		return connect("");
	}

	public boolean connect(String interests) {
		ParameterMap parameters = new ParameterMap();
		parameters.put("rcs", 1);
		parameters.put("firstevents", 1);
		parameters.put("spid", "");
		parameters.put("randid", randomId);
		parameters.put("lang", "en");

		if (interests != "") {
			parameters.put("topics", interests);
		}

		JSONObject json = new JSONObject();

		for (int attempts = 0; attempts < 3; attempts++) {
			try {
				json = new JSONObject(httpClient.get("start", parameters));
			} catch (Exception exception) {
				LOGGER.log(Level.WARNING, exception.getMessage());
			}

			if (json.optString("clientID") != "") {
				break;
			}

			try {
				Thread.sleep(3000);
			} catch (Exception exception) {
				// Do nothing.
			}
		}

		clientId = json.optString("clientID");

		if (clientId == "") {
			return false;
		}

		eventHandler.handleEvents(json.getJSONArray("events"));

		while (connectionListener.isNotConnected()) {
			eventHandler.handleEvents(getEvents());
		}

		(new Thread(this)).start();

		return true;
	}

	public boolean disconnect() {
		if (connectionListener.isNotConnected()) {
			return false;
		}

		ParameterMap parameters = new ParameterMap();
		parameters.put("id", clientId);

		if (httpClient.post("disconnect", parameters) == "win") {
			return true;
		} else {
			return false;
		}
	}

	public void run() {
		while (connectionListener.isConnected()) {
			eventHandler.handleEvents(getEvents());
		}
	}

	public OmegaClient() {
		setRandomRandomId();
		setRandomServer();

		addListener(connectionListener);
	}
}
