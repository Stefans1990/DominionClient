package scenes.verification;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

public class VerificationModel {

    /*@stefan the isLoggedin boolean is false as we first have to login, but gets set to true
    * as soon as the game is connected to the server
    */
    private static SimpleBooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    public boolean isLoggedIn() {
       
    	return isLoggedIn.get();
    }

    public SimpleBooleanProperty isConnectedProperty() {
        
    	return isLoggedIn;
    }

    public static void setIsConnected(boolean bool) {
    		isLoggedIn.set(bool);
    	
    }
    public static void setLoggedIn()
    {
      Platform.runLater(() -> VerificationModel.setIsConnected(true));
    }

}
