package scenes.serverConnection;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import util.ServiceLocator;

import java.util.ResourceBundle;


public class ServerConnectionView {


    private final ServerConnectionModel model;
    private HBox parent;
    private ResourceBundle bundle;

    public ServerConnectionView(ServerConnectionModel model) {
        this.model = model;
    }

    public void createLayout() {

        bundle = ServiceLocator.getResourceBundle();


        parent = new HBox(20);
        Label label = new Label(bundle.getString("sc_connecting") + " " + model.getHostAddress());

        parent.getChildren().add(label);
    }

    public void show(Stage stage) {
        Scene scene = new Scene(parent);
        stage.setTitle("Dominion");
        stage.setWidth(200);
        stage.setScene(scene);
        stage.show();
    }


    public void showConnectionAlert(boolean isConnected) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        if (isConnected) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("sc_success_title"));
            alert.setContentText(bundle.getString("sc_success_context"));
            alert.showAndWait();
        }else{
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("sc_failed_title"));
            alert.setContentText(bundle.getString("sc_failed_context"));
            alert.showAndWait();
        }
    }
}
