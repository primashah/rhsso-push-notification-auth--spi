package push.authenticator;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

import push.authenticator.firebase.FCMService;
import push.authenticator.model.PushNotificationRequest;



public class PushAuthenticator implements Authenticator {

	private static final Logger logger = Logger.getLogger(PushAuthenticator.class.getPackage().getName());

	protected PushVerificationCode.STATUS validateCode(UserModel user, String enteredCode) {
		PushVerificationCode.STATUS result = PushVerificationCode.STATUS.INVALID;
		List<String> expectedCode = user.getAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE);
		List<String> expTimeString = user.getAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE_EXPIRY);

		if (expectedCode != null) {
			logger.debug("Expected code = " + expectedCode.get(0) + "    entered code = " + enteredCode);
			String expectedCodeValue = expectedCode.get(0);
			String expTimeStringValue = expTimeString.get(0);
			result = enteredCode.equals(expectedCodeValue) ? PushVerificationCode.STATUS.VALID
					: PushVerificationCode.STATUS.INVALID;
			long now = new Date().getTime();
			logger.debug("Valid code expires in " + (Long.parseLong(expTimeStringValue) - now) + " ms");
			if (result == PushVerificationCode.STATUS.VALID) {
				if (Long.parseLong(expTimeStringValue) < now) {
					logger.debug("Code is expired !!");
					result = PushVerificationCode.STATUS.EXPIRED;
				}
			}
		}

		return result;
	}

	private PushNotificationRequest getPushRequest(String clientRegisterationToken) {
		PushNotificationRequest pushRequest = new PushNotificationRequest();
		Map<String, String> messageData = new HashMap<String, String>();
		messageData.put(PushAuthConstants.USER_ATTR_PUSH_CODE, getRandomCode(4)); // TODO: replace with config
		pushRequest.setTitle(PushAuthConstants.MESSAGE_TITLE);
		pushRequest.setToken(clientRegisterationToken);
		pushRequest.setMessage(messageData);
		return pushRequest;

	}

	private String getRandomCode(long nrOfDigits) {
		if (nrOfDigits < 1) {
			throw new RuntimeException("Nr of digits must be bigger than 0");
		}

		double maxValue = Math.pow(10.0, nrOfDigits); // 10 ^ nrOfDigits;
		Random r = new Random();
		long code = (long) (r.nextFloat() * maxValue);
		return Long.toString(code);

	}

	// Store the code + expiration time in a UserAttribute. RHSSO will persist these
	// in the DB.
	private void storeCode(AuthenticationFlowContext context, String code, Long expiringAt) {

		List<String> values = Arrays.asList(code);
		context.getUser().setAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE, values);

		List<String> expiryValue = Arrays.asList((expiringAt).toString());
		context.getUser().setAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE_EXPIRY, expiryValue);

	}

	public void authenticate(AuthenticationFlowContext context) {
		final String METHOD_NAME = "authenticate";
		logger.debug("Method [authenticate]");
		String redirectUri = context.getAuthenticationSession().getClientNotes().get("redirect_uri");
		String token = redirectUri.substring(redirectUri.indexOf('=') + 1);
		logger.infov(METHOD_NAME + ": REDIRECT URI {0}", redirectUri);
		logger.infov(METHOD_NAME + ": TOKEN {0}", token);

		try {
			Response challenge = context.form().createForm(PushAuthConstants.VERIFICATION_INPUT_FORM);
			context.challenge(challenge);
			PushNotificationRequest requestToSend = getPushRequest(token);
			storeCode(context, requestToSend.getMessage().get(PushAuthConstants.USER_ATTR_PUSH_CODE),
					new Date().getTime() + (PushAuthConstants.EXPIRY_TIME_IN_SECONDS * 1000)); // s --> ms
			FCMService.sendMessageToToken(requestToSend);

			
		} catch (Exception e) {

			Response challenge = context.form().addError(new FormMessage(PushAuthConstants.ERROR_MESSAGE_FORM))
					.createForm(PushAuthConstants.VERIFICATION_ERROR_FORM);
			context.challenge(challenge);
		}

	}

	public void action(AuthenticationFlowContext context) {
		logger.debug("Method [action]");
		MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
		String enteredCode = inputData.getFirst(PushAuthConstants.FORM_INPUT_PUSH_CODE_TEXT_BOX);
		UserModel user = context.getUser();
		try {
		validateCode(user, enteredCode).processValididty(context, user);
		}
		catch(Exception e) {
			Response challenge = context.form()
					.setAttribute("username", context.getAuthenticationSession().getAuthenticatedUser().getUsername())
					.addError(new FormMessage(PushAuthConstants.ERROR_MESSAGE_INVALIDE_CODE_FORM)).createForm(PushAuthConstants.VERIFICATION_ERROR_FORM);
			context.challenge(challenge);
			
		}

	}

	public boolean requiresUser() {
		logger.debug("Method [requiresUser]");
		return false;
	}

	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		logger.debug("Method [configuredFor]");
		return false;
	}

	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

	}

	public void close() {
		logger.debug("<<<<<<<<<<<<<<< PushNotificationAuthenticator close");
	}

}