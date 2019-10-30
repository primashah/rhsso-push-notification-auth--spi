package push.authenticator;

import java.util.List;

import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import push.authenticator.firebase.FCMInitializer;



public class PushAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

	public static final String PROVIDER_ID = "push-notification-authenticator";
	private static final PushAuthenticator SINGLETON = new PushAuthenticator();
	private static FCMInitializer FCM = new FCMInitializer();
	private static final Logger logger = Logger.getLogger(PushAuthenticatorFactory.class.getPackage().getName());
	
	
	private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
			AuthenticationExecutionModel.Requirement.REQUIRED,
			AuthenticationExecutionModel.Requirement.DISABLED
	};

	

	public Authenticator create(KeycloakSession session) {
		return SINGLETON;
	}

	public String getId() {
		return PROVIDER_ID;
	}

	public void init(Scope scope) {
		FCM.initialize();
		logger.debug("Method [init]");
	}

	public void postInit(KeycloakSessionFactory factory) {
		logger.debug("Method [postInit]");
	}

	public List<ProviderConfigProperty> getConfigProperties() {
		return null;
	}

	public String getHelpText() {
		return "Two factor PUSH Authentication using Firebase...";
	}

	public String getDisplayType() {
		return "Push notification  - Firebase. ";
	}

	public String getReferenceCategory() {
		logger.debug("Method [getReferenceCategory]");
        return "push-notification-auth-code";
	}

	public boolean isConfigurable() {
		return true;
	}

	public Requirement[] getRequirementChoices() {
		return REQUIREMENT_CHOICES == null ? null : (Requirement[]) REQUIREMENT_CHOICES.clone();
	}

	public boolean isUserSetupAllowed() {
		return true;
	}

	public void close() {
		logger.debug("<<<<<<<<<<<<<<< PushAuthenticatorFactory close");
	}
}