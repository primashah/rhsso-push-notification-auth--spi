package push.authenticator.model;

import java.util.Map;

public class PushNotificationRequest {
	  private String title;
	    private Map<String,String> message;
	    private String topic;
	    private String token;

	    public PushNotificationRequest() {
	    }

	    public PushNotificationRequest(String title, Map<String,String> messageBody, String topicName) {
	        this.title = title;
	        this.message = messageBody;
	        this.topic = topicName;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public Map<String,String> getMessage() {
	        return message;
	    }

	    public void setMessage(Map<String,String> message) {
	        this.message = message;
	    }

	    public String getTopic() {
	        return topic;
	    }

	    public void setTopic(String topic) {
	        this.topic = topic;
	    }

	    public String getToken() {
	        return token;
	    }

	    public void setToken(String token) {
	        this.token = token;
	    }

}