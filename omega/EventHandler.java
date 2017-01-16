package omega;

import org.json.JSONArray;
import java.util.LinkedList;
import java.util.List;
import omega.EventListener;

public class EventHandler {
	protected static final String EVENT_CONNECTED = "connected";
	protected static final String EVENT_DISCONNECTED = "strangerDisconnected";
	protected static final String EVENT_WAITING = "waiting";
	protected static final String EVENT_RECAPTCHA_REQUIRED = "recaptchaRequired";
	protected static final String EVENT_RECAPTCHA_REJECTED = "recaptchaRejected";
	protected static final String EVENT_MESSAGE_RECIEVED = "gotMessage";
	protected static final String EVENT_TYPING_STARTED = "typing";
	protected static final String EVENT_TYPING_STOPPED = "stoppedTyping";

	private List<EventListener> listeners = new LinkedList<EventListener>();

	public void addListener(EventListener listener) {
		listeners.add(listener);
	}

	public void removeListener(EventListener listener) {
		listeners.remove(listener);
	}

	public void handleEvent(JSONArray event) {
		switch (event.getString(0)) {
			case EVENT_CONNECTED:
				for (EventListener listener : listeners) {
					listener.onConnect();
				}
				break;
			case EVENT_DISCONNECTED:
				for (EventListener listener : listeners) {
					listener.onDisconnect();
				}
				break;
			case EVENT_WAITING:
				for (EventListener listener : listeners) {
					listener.onWait();
				}
				break;
			case EVENT_RECAPTCHA_REQUIRED:
				for (EventListener listener : listeners) {
					listener.onRecaptchaRequired(event.getString(1));
				}
				break;
			case EVENT_RECAPTCHA_REJECTED:
				for (EventListener listener : listeners) {
					listener.onRecaptchaRejected(event.getString(1));
				}
				break;
			case EVENT_MESSAGE_RECIEVED:
				for (EventListener listener : listeners) {
					listener.onMessageRecieved(event.getString(1));
				}
				break;
			case EVENT_TYPING_STARTED:
				for (EventListener listener : listeners) {
					listener.onTypingStart();
				}
				break;
			case EVENT_TYPING_STOPPED:
				for (EventListener listener : listeners) {
					listener.onTypingStop();
				}
				break;
		}
	}

	public void handleEvents(JSONArray events) {
		for (int a = 0; a < events.length(); a++) {
			handleEvent(events.getJSONArray(a));
		}
	}
}
