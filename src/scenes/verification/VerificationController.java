package scenes.verification;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

public class VerificationController {

    private final VerificationModel model;
    private final VerificationView view;


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
}
