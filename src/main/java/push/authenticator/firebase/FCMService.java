package push.authenticator.firebase;

import org.jboss.logging.Logger;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;


import push.authenticator.model.PushNotificationRequest;

public class FCMService {
	
	private static final Logger logger = Logger.getLogger(FCMService.class.getPackage().getName());
	public static void sendToToken(String token) {
		try {

			String registrationToken = token;
			Message message = Message.builder().putData("code", "850").setToken(registrationToken).build();
			String response = FirebaseMessaging.getInstance().send(message);
			logger.infov("Firebase Message sent sucessfully with token: {0} and responseid {1} ", token, response);
		} catch (Throwable t) {
			logger.info("Error while sending message", t);

		}

	}
	public static void sendMessageToToken(PushNotificationRequest request) {
		try {
			Message message = Message.builder()
					.setNotification(new Notification(request.getTitle(), ""))
					.putAllData(request.getMessage())
					.setToken(request.getToken())
					.build();
			
			String response = FirebaseMessaging.getInstance().send(message);
			logger.infov("Firebase Message sent sucessfully with device token: {0} and responseId {1} ", request.getToken(), response);
		} catch (Throwable t) {
			logger.info("Error while sending message", t);

		}

	}

}
