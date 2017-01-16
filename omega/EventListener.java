package omega;

public interface EventListener {
	public void onConnect();

	public void onDisconnect();

	public void onWait();

	public void onRecaptchaRequired(String url);

	public void onRecaptchaRejected(String url);

	public void onMessageRecieved(String message);

	public void onTypingStart();

	public void onTypingStop();
}
