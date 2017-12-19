package scenes.verification;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import scenes.MainModel;

public class VerificationModel {


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
      Platform.runLater(new Runnable() {
        @Override public void run() {
        	VerificationModel.setIsConnected(true);
        }
      });
    }

}
