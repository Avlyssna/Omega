package omega;

import omega.TestEventListener;
import omega.OmegaClient;

public class Example {
	public static void main(String args[]) {
		OmegaClient client = new OmegaClient();
		client.addListener(new TestEventListener());
		client.connect();
		client.sendMessage("Hello");
	}
}
