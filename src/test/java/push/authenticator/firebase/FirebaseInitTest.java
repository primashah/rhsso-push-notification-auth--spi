package push.authenticator.firebase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class FirebaseInitTest {
	
	@Test
	public void getStream() {
		FirebaseInit firebaseInit = new FirebaseInit();
		InputStream inputStream = firebaseInit.getStream();
		assertNotNull(inputStream);
	}
	
	@Test
	public void initialize() {
		FirebaseInit firebaseInit = new FirebaseInit();
		boolean isInitSuccess = firebaseInit.initialize();
		assertTrue(isInitSuccess);
	}

}
