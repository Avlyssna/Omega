package omega;

import omega.EventListener;

public class EventAdapter implements EventListener {
	@Override
	public void onConnect() {}

	@Override
	public void onDisconnect() {}

	@Override
	public void onWait() {}

	@Override
	public void onRecaptchaRequired(String url) {}

	@Override
	public void onRecaptchaRejected(String url) {}

	@Override
	public void onMessageRecieved(String message) {}

	@Override
	public void onTypingStart() {}

	@Override
	public void onTypingStop() {}
}
