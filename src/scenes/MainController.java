package scenes;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import scenes.game.GameController;
import scenes.gameLobby.GameLobbyController;
import scenes.serverConnection.ServerConnectionController;
import scenes.verification.VerificationController;

import java.net.Socket;
import java.util.logging.Logger;

public class MainController {

    private Stage stage;
    private final Logger logger;

    public MainController(){
        logger = Logger.getLogger(this.getClass().getCanonicalName());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void runApplication() {
        ServerConnectionController serverConnectionController = new ServerConnectionController();
        serverConnectionController.show(stage);

        Socket socket = serverConnectionController.tryToConnect();
        if (!serverConnectionController.getIsConnected()) {
            System.exit(0);
        } else {
            MainModel mainModel = new MainModel(socket);
            //mainModel.tryCreateReader();
            // mainModel.startListeningForMessages();
            //mainModel.tryCreaterWriter();
            showVerificationScene();
        }
    }

    private void showVerificationScene() {
        VerificationController verificationController = new VerificationController();
        verificationController.show(stage);
        listenForLogin(verificationController);
    }

    private void listenForLogin(VerificationController verificationController) {
        verificationController.getIsLoggedIn().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    showGameLobbyScene();
                }
            }
        });
    }

    private void showGameLobbyScene() {
        GameLobbyController gameLobbyController = new GameLobbyController();
        gameLobbyController.show(stage);
        //TODO When we get the signal to show the game we show the game
        //TODO Listen for game start here
        gameLobbyController.isGameStarted().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isGameStarted) {
                if (isGameStarted.booleanValue() == Boolean.TRUE) {
                    showGameScene();
                }
            }
        });
    }

    private void showGameScene(){
        GameController gameController = new GameController();
        gameController.show(stage);
    }

}
