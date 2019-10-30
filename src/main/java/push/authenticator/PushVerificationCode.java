package push.authenticator;

import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;



public class PushVerificationCode {
	public static enum STATUS {
        EXPIRED {
        	public void processValididty(AuthenticationFlowContext context, UserModel user) {
        		Response challenge = context.form()
    					.setAttribute("username", context.getAuthenticationSession().getAuthenticatedUser().getUsername())
    					.addError(new FormMessage(PushAuthConstants.ERROR_MESSAGE_EXPIRY_CODE_FORM)).createForm(PushAuthConstants.VERIFICATION_ERROR_FORM);
    			context.challenge(challenge);
    			
        	}
        },
        INVALID {
        	public void processValididty(AuthenticationFlowContext context, UserModel user) {
        		Response challenge = context.form()
    					.setAttribute("username", context.getAuthenticationSession().getAuthenticatedUser().getUsername())
    					.addError(new FormMessage(PushAuthConstants.ERROR_MESSAGE_INVALIDE_CODE_FORM)).createForm(PushAuthConstants.VERIFICATION_ERROR_FORM);
    			context.challenge(challenge);
    			
        	}
        },
        VALID {
        	public void processValididty(AuthenticationFlowContext context, UserModel user) {
        		user.removeAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE);
    			user.removeAttribute(PushAuthConstants.USER_ATTR_PUSH_CODE_EXPIRY);
    			Logger.getLogger(PushVerificationCode.class.getPackage().getName()).info("verify code check : OK");
    			context.success();
    			
        	}
        };
	  
	  public abstract void processValididty(AuthenticationFlowContext context, UserModel user) ;
    }

}