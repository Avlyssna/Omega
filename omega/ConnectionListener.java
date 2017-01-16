package omega;

import omega.EventAdapter;

public class ConnectionListener extends EventAdapter {
	private Boolean status = Boolean.FALSE;

	// Getters and setters
	public synchronized Boolean getConnectionStatus() {
		return status;
	}

	public synchronized void setConnectionStatus(Boolean status) {
		this.status = status;
	}

	// Convenience functions
	public Boolean isConnected() {
		return getConnectionStatus();
	}

	public Boolean isNotConnected() {
		return !getConnectionStatus();
	}

	// Handling events
	@Override
	public void onConnect() {
		setConnectionStatus(Boolean.TRUE);
	}

	@Override
	public void onDisconnect() {
		setConnectionStatus(Boolean.FALSE);
	}
}
