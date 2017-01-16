package omega;

import omega.EventListener;

public class TestEventListener implements EventListener {
	@Override
	public void onConnect() {
		System.out.println("Connected");
	}

	@Override
	public void onDisconnect() {
		System.out.println("Disconnected");
	}

	@Override
	public void onWait() {
		System.out.println("Waiting");
	}

	@Override
	public void onRecaptchaRequired(String url) {
		System.out.println("Captcha required: " + url);
	}

	@Override
	public void onRecaptchaRejected(String url) {
		System.out.println("Captcha rejected: " + url);
	}

	@Override
	public void onMessageRecieved(String message) {
		System.out.println("Message recieved: " + message);
	}

	@Override
	public void onTypingStart() {
		System.out.println("Typing started");
	}

	@Override
	public void onTypingStop() {
		System.out.println("Typing stopped");
	}
}
