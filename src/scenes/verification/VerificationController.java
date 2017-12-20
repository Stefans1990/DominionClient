package scenes.verification;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

public class VerificationController {

    private final VerificationModel model;
    private final VerificationView view;
    private static String localName;



    public VerificationController(){
        this.model = new VerificationModel();
        this.view = new VerificationView(model);
    }

    public void show(Stage stage) {
        view.createLayout();
        view.show(stage);
    }

    public SimpleBooleanProperty getIsLoggedIn() {
       return model.isConnectedProperty();
    }
    public static void setLocalName(String name){
        localName= name;
    }
    public String getLocalName(){
        return localName;
    }
}
