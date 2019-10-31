package push.authenticator.challenge;

import java.util.Date;

import java.util.Random;

import org.jboss.logging.Logger;


import push.authenticator.PushAuthConstants;


public class SimpleChallenge {
	public static final int MAX_NO_OF_DIGITS = 4;
	 private static final Logger logger = Logger.getLogger(SimpleChallenge.class.getPackage().getName());
	public String getCode() {
		

		double maxValue = Math.pow(10.0, MAX_NO_OF_DIGITS); // 10 ^ nrOfDigits;
		Random r = new Random();
		long code = (long) (r.nextFloat() * maxValue);
		return Long.toString(code);
	}
	public Long getExpiryTime() {
		return new Date().getTime() + (PushAuthConstants.EXPIRY_TIME_IN_SECONDS * 1000);
	}
	
	
	public boolean isExpired(String timeToExpire) {
		long now = new Date().getTime();
		logger.debug("Valid code expires in " + (Long.parseLong(timeToExpire) - now) + " ms");
		return Long.parseLong(timeToExpire) < now;
	}
	public boolean isValidCode(String userEnterdCode, String expectedCode) {
		
		 return userEnterdCode.equals(expectedCode);
	}
}
