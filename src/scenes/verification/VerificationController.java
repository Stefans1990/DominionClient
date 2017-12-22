package scenes.verification;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

public class VerificationController {

    private final VerificationModel model;
    private final VerificationView view;
    private static String localName;


    //@Stefan: instantiates a new model and a new view which is dependent on the model
    public VerificationController(){
        this.model = new VerificationModel();
        this.view = new VerificationView(model);
    }
    // @stefan sets up the verification stage
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
