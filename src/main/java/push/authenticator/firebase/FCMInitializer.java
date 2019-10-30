package push.authenticator.firebase;

import java.io.InputStream;

import org.jboss.logging.Logger;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import push.authenticator.PushAuthConstants;


public class FCMInitializer {
	
	  private static final Logger logger = Logger.getLogger(FCMInitializer.class.getPackage().getName());

	    public FCMInitializer() {}
	    
		public void initialize() {
			
			try {
				
				InputStream stream = getClass().getClassLoader().getResourceAsStream(PushAuthConstants.PUSH_CONFIG_FILE_PATH);
				if(stream != null) {
				FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(stream)).build();
				logger.infov("Firebase options {0} :", options);
				if (FirebaseApp.getApps().isEmpty()) {
					logger.info("Firebase application initializing");
					FirebaseApp.initializeApp(options);
					logger.info("Firebase application has been initialized.");
				}
				}else {
					logger.info("Firebase cannot read configurations");
				}
			} catch (Exception e) {
				logger.errorv("Error while checking firebse apps {0}" , e.getMessage());
			}
			
		}

}
