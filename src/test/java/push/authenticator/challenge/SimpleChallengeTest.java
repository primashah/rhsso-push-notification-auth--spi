package push.authenticator.challenge;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SimpleChallengeTest {
	
	@Test
	public void instance() {
		SimpleChallenge challenge = new SimpleChallenge();
		assertNotNull(challenge);
	}
	
	@Test
	public void getCode() {
		SimpleChallenge challenge = new SimpleChallenge();
		String code = challenge.getCode();
		System.out.print("length " + code.length());
		assertTrue(code.length() == 4);
		
		
	}
	@Test
	public void isValid() {
		SimpleChallenge challenge = new SimpleChallenge();
		String code = challenge.getCode();
		assertTrue(challenge.isValidCode(code, code));
		
		
	}
	@Test
	public void isNotValid() {
		SimpleChallenge challenge = new SimpleChallenge();
		String code = challenge.getCode();
		assertFalse(challenge.isValidCode("1234", code));
		
		
	}
	@Test
	public void isExpired() {
		SimpleChallenge challenge = new SimpleChallenge();
		Long timeToExpire = challenge.getExpiryTime();
		assertFalse(challenge.isExpired(timeToExpire.toString()));
		
		
	}

}
