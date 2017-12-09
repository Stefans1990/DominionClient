package scenes.verification;

import javafx.beans.property.SimpleBooleanProperty;

public class VerificationModel {


    private SimpleBooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    public boolean isLoggedIn() {
        return isLoggedIn.get();
    }

    public SimpleBooleanProperty isConnectedProperty() {
        return isLoggedIn;
    }

    public void setIsConnected(boolean bool) {
        this.isLoggedIn.set(bool);
    }
}
