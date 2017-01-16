package omega;

import omega.OmegaClient;
import omega.TestEventListener;

public class UnitTest {
	public static void main(String args[]) {
		OmegaClient client = new OmegaClient();
		client.addListener(new TestEventListener());
		client.connect();
		client.sendMessage("Hello");
	}
}
