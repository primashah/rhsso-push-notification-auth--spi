# rhsso-push-notification-authenticator-spi

This authentication SPI allows to implement 

## Pre-requiste

* RHSSO instance
* mvn 3.6.2+
* java jdk 1.8
* Firebase configuration 
	
	* Provide  Firebase Admin SDK, JSON-formatted file (service account credentials) and place it inside the project (eg: _src/resources/META-INF/google_)
	* Change the path and defaults in **pushauthconstants.java**


## Build 

	mvn clean install

## Deploy on RHSSO :

    Add the jar to the rhsso server:
        $ cp target/rhsso-push-authenticator-jar-with-dependencies.jar _RHSSO_HOME_/providers/

    Add two templates to the rhsso server:
        $ cp themes/push-notification-2fa _RHSSO_HOME_/themes/
        
## Hot Deploy on RHSSO :
	
	Add the jar to the rhsso server:
		mvn clean install wildfly:deploy
		
	Add two templates to the rhsso server:
        $ cp themes/push-notification-2fa _RHSSO_HOME_/themes/

## Configure RHSSO to use Push notification Two Factor Authentication.
 
1. Create a new Realm (eg. Push 2FA)

2. Under new Realm > Under Authentication > Flows:

    + Copy 'Browse' flow and set name as '2FA Push Browser' flow
    + Click on 'Actions > Add execution on the '2FA Push Browser Forms' line and add the 'Push notification  - Firebase'
    + Set 'Push notification  - Firebase' to 'REQUIRED'

3. Under new Ream > Under Authentication > Bindings:

    * Select '2FA Push Browser' as the 'Browser Flow' for the REALM.
    
4. Under new Ream > Themes:

    * Set Login theme as 'push-notification-2fa'.
