package push.authenticator.firebase;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import push.authenticator.PushAuthConstants;


public class FirebasePush {
	private static final Logger logger = Logger.getLogger(FirebasePush.class.getPackage().getName());
	private String title;
    private Map<String,String> message;
    private String topic;
    private String token;
    
    public FirebasePush setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public FirebasePush setMessage(Map<String,String> message) {
    	
    	
        this.message = message;
        return this;
    }
    
    public Map<String,String> getMessage() {
    	return this.message;
    }	

    public FirebasePush setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public FirebasePush setToken(String token) {
    	
        this.token = token.substring(token.indexOf('=') + 1);;
        return this;
    }
	
	public  String sendPushNotificaton() {
		try {
			Message message = Message.builder()
					.setNotification(new Notification(this.title, ""))
					.putAllData(this.message)
					.setToken(this.token)
					.build();
			
			String response = FirebaseMessaging.getInstance().send(message);
			
			logger.infov("Firebase Message sent sucessfully with device token: {0} and responseId {1} ", this.token, response);
			return response ;
		} catch (FirebaseMessagingException e) {
			logger.error("Error whilde sending message" + e.getMessage());
			return null;
		}

	}


}
