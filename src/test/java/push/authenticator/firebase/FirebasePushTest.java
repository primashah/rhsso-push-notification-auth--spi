package push.authenticator.firebase;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import push.authenticator.PushAuthConstants;


public class FirebasePushTest {
	
	@Test
	public void instance() {
		FirebasePush firebasePush = new FirebasePush();
		assertNotNull(firebasePush);
		
	}
	@Test
	public void sendPushNotificaton() {
		FirebasePush firebasePush = new FirebasePush();
		HashMap<String,String> pushData = new HashMap<String, String>();
		pushData.put(PushAuthConstants.USER_ATTR_PUSH_CODE, "fjdiosf");
		String response = firebasePush.setMessage(pushData)
						.setToken("xxx")
						.setTitle("")
						.sendPushNotificaton();
		
		assertTrue(response == null);
		
	}
	
	

}
