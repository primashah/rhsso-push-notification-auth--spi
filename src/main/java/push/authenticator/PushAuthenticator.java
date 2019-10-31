package push.authenticator;

import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

import push.authenticator.challenge.SimpleChallenge;

import push.authenticator.firebase.FirebasePush;

public class PushAuthenticator implements Authenticator {

	private static final Logger logger = Logger.getLogger(PushAuthenticator.class.getPackage().getName());

	

	protected void processCode(AuthenticationFlowContext context, String enteredCode) {

		UserModel user = context.getUser();
		List<String> expectedCode = user.getAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE);
		List<String> timeToExpire = user.getAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE_EXPIRY);
		Response challenge;

		if (expectedCode != null) {
			logger.debug("Expected code = " + expectedCode.get(0) + "    entered code = " + enteredCode);
			String expectedCodeValue = expectedCode.get(0);
			String timeToExpireValue = timeToExpire.get(0);
			
			SimpleChallenge simpleChallenge = new SimpleChallenge();
			boolean isValid = simpleChallenge.isValidCode(enteredCode, expectedCodeValue);
			boolean isExpired = simpleChallenge.isExpired(timeToExpireValue);
			
			if (isExpired) {
				logger.info("code isExpired ");
				challenge = context.form()
						.setAttribute("username",
								context.getAuthenticationSession().getAuthenticatedUser().getUsername())
						.addError(new FormMessage(PushAuthConstants.ERROR_MESSAGE_EXPIRY_CODE_FORM))
						.createForm(PushAuthConstants.EXPIRED_ERROR_FORM);
				context.challenge(challenge);
			} else if (!isValid) {
				logger.info("code is Invalide ");
				challenge = context.form()
						.setAttribute("username",
								context.getAuthenticationSession().getAuthenticatedUser().getUsername())
						.addError(new FormMessage(PushAuthConstants.ERROR_MESSAGE_INVALIDE_CODE_FORM))
						.createForm(PushAuthConstants.VERIFICATION_ERROR_FORM);
				context.challenge(challenge);
			} else {
				logger.info("verify code check : OK");
				user.removeAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE);
				user.removeAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE_EXPIRY);
				
				context.success();
			}

		}

		
	}

	private String sendCodeWithChallenge(AuthenticationFlowContext context, String clientRegisterationToken) {
		SimpleChallenge challenge = new SimpleChallenge();
		String code = challenge.getCode();
		FirebasePush firebasePush = new FirebasePush();

		Map<String, String> messageData = new HashMap<String, String>();
		messageData.put(PushAuthConstants.USER_ATTR_PUSH_CODE, code);

		storeCode(context, code, challenge.getExpiryTime());

		return firebasePush.setTitle(PushAuthConstants.MESSAGE_TITLE).setToken(clientRegisterationToken)
				.setMessage(messageData).sendPushNotificaton();

	}

	
	private void storeCode(AuthenticationFlowContext context, String code, Long expiringAt) {

		List<String> values = Arrays.asList(code);
		context.getUser().setAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE, values);

		List<String> expiryValue = Arrays.asList((expiringAt).toString());
		context.getUser().setAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE_EXPIRY, expiryValue);

	}

	public void authenticate(AuthenticationFlowContext context) {
		final String METHOD_NAME = "authenticate";

		String redirectUri = context.getAuthenticationSession().getClientNotes().get("redirect_uri");
		String token = redirectUri.substring(redirectUri.indexOf('=') + 1);

		logger.infov(METHOD_NAME + ": REDIRECT URI {0}", redirectUri);
		logger.infov(METHOD_NAME + ": TOKEN {0}", token);

		try {
			Response challenge = context.form().createForm(PushAuthConstants.VERIFICATION_INPUT_FORM);
			context.challenge(challenge);

			String responseFromPush = sendCodeWithChallenge(context, token);

			logger.infov(METHOD_NAME + ": response from push notification {0}", responseFromPush);

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
		
		try {
			processCode(context, enteredCode);
		} catch (Exception e) {
			Response challenge = context.form()
					.setAttribute("username", context.getAuthenticationSession().getAuthenticatedUser().getUsername())
					.addError(new FormMessage(PushAuthConstants.ERROR_MESSAGE_INVALIDE_CODE_FORM))
					.createForm(PushAuthConstants.VERIFICATION_ERROR_FORM);
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