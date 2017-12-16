package scenes;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import scenes.game.GameController;
import scenes.gameLobby.GameLobbyController;
import scenes.serverConnection.ServerConnectionController;
import scenes.verification.VerificationController;
import util.LogHandling;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Handlers.MessageHandler;
import Handlers.MessageHandlerFactory;

public class MainController {

    private Stage stage;

    static MessageHandler messageHandler;

    public MainController(){

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
        	messageHandler = new MessageHandler();
            MessageHandler.setSocket(socket);
            
            MessageHandler.openResources();
            MessageHandler.read();
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
        listenForChat(gameLobbyController);
        listenForGameList(gameLobbyController);
        //TODO When we get the signal to show the game we show the game
        //TODO Listen for game start here
        gameLobbyController.isGameStarted().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isGameStarted) {
                if (isGameStarted.booleanValue() == Boolean.TRUE) {
                   // showGameScene();
                }
            }
        });
    }

    private void showGameScene(){
        GameController gameController = new GameController();
        gameController.show(stage);
        
    }
    private void listenForChat(GameLobbyController gameLobbyController) {
        gameLobbyController.getChat().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                showGameScene();
                gameLobbyController.updateChat(newValue);
            }
        });
    }
    private void listenForGameList(GameLobbyController gameLobbyController) {
        gameLobbyController.getGameList().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
                gameLobbyController.updateGameList(newValue);
            }
        });
    }
    
}
