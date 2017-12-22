package scenes.serverConnection;

import javafx.stage.Stage;

import java.net.Socket;

public class ServerConnectionController {

    private final ServerConnectionModel model;
    private final ServerConnectionView view;

    /* @Stefan
    Instantiates a new model and a new view which is depending on the model
     */
    public ServerConnectionController(){
        this.model = new ServerConnectionModel();
        this.view = new ServerConnectionView(model);
    }
    /*
    * sets up the layout and the stage in the view model
     */
    public void show(Stage stage) {
        view.createLayout();
        view.show(stage);
    }
    /*
    * Tries to connect to the server via the model and the view and gets the logic behind it from the model
     */
    public Socket tryToConnect() {
        model.tryConnect();
        view.showConnectionAlert(getIsConnected());
        return model.getSocket();
    }

    public boolean getIsConnected() {
        return model.isConnected;
    }
}
