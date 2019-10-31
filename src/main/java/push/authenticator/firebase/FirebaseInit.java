package push.authenticator.firebase;

import java.io.InputStream;

import org.jboss.logging.Logger;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import push.authenticator.PushAuthConstants;



public class FirebaseInit {
	 private static final Logger logger = Logger.getLogger(FirebaseInit.class.getPackage().getName());

	    public FirebaseInit() {}
	    
	    protected InputStream getStream() {
	    		return getClass().getClassLoader().getResourceAsStream(PushAuthConstants.PUSH_CONFIG_FILE_PATH);
	    }
	    
		public boolean initialize() {
			
			try {
				
				InputStream stream = this.getStream();
				if(stream != null) {
				FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(stream)).build();
				logger.infov("Firebase options {0} :", options);
				
				if (FirebaseApp.getApps().isEmpty()) {
					logger.info("Firebase application initializing");
					FirebaseApp.initializeApp(options);
					logger.info("Firebase application has been initialized.");
					return true;
				}
				}else {
					logger.info("Firebase cannot read configurations");
					return false;
				}
				return false;
			} catch (Exception e) {
				logger.errorv("Error while checking firebse apps {0}" , e.getMessage());
				return false;
			}
			
		}

}
