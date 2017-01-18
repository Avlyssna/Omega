package omega;

import omega.EventAdapter;

public class ConnectionListener extends EventAdapter {
	private boolean state = false;

	// Getters and setters
	public synchronized boolean getConnectionState() {
		return state;
	}

	public synchronized void setConnectionState(boolean state) {
		this.state = state;
	}

	// Convenience functions
	public boolean isConnected() {
		return getConnectionState();
	}

	public boolean isNotConnected() {
		return !getConnectionState();
	}

	// Handling events
	@Override
	public void onConnect() {
		setConnectionState(true);
	}

	@Override
	public void onDisconnect() {
		setConnectionState(false);
	}
}
