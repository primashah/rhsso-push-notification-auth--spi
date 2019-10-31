package push.authenticator;

public class PushAuthConstants {
	public static final String PUSH_CONFIG_FILE_PATH ="META-INF/google/push-notifications-example-firebase-adminsdk.json";
	public static final String MESSAGE_TITLE = "DOA Verification Code";
	public static final String USER_ATTR_PUSH_CODE = "pushVerificationCode";
	public static final String USER_ATTR_PUSH_CODE_EXPIRY = "pushVerificationCodeExpiryTime";
	
	public static final String VERIFICATION_ERROR_FORM = "push-validation-error.ftl";
	public static final String EXPIRED_ERROR_FORM = "push-validation-expire-error.ftl";
	public static final String VERIFICATION_INPUT_FORM = "push-input-validation.ftl";
	
	public static final String ERROR_MESSAGE_FORM ="sendCodeErrorMessage";
	public static final String ERROR_MESSAGE_INVALIDE_CODE_FORM ="invalidCodeMessage";
	
	public static final String ERROR_MESSAGE_EXPIRY_CODE_FORM = "expiryCodeTopMessage";
	
	public static final String FORM_INPUT_PUSH_CODE_TEXT_BOX ="pushCode";
	
	public static final int EXPIRY_TIME_IN_SECONDS =60000;
	

}