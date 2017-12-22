package scenes;


import Handlers.GameStartGameMessageHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import scenes.game.GameController;
import scenes.gameLobby.GameLobbyController;
import scenes.serverConnection.ServerConnectionController;
import scenes.verification.VerificationController;
import scenes.verification.VerificationModel;
import util.LogHandling;

import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Handlers.MessageHandler;
import Handlers.MessageHandlerFactory;

public class MainController {

    private Stage stage;
    String[] list;
    private String localPlayerName;

    static MessageHandler messageHandler;

    public MainController() {

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
        verificationController.getIsLoggedIn().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                localPlayerName = verificationController.getLocalName();
                showGameLobbyScene();
            }
        });
    }

    private void showGameLobbyScene() {
        GameLobbyController gameLobbyController = new GameLobbyController();
        gameLobbyController.show(stage);
        listenForChat(gameLobbyController);
        listenForGameList(gameLobbyController);
        listenForTopFive(gameLobbyController);
        listenForStartGame(gameLobbyController);
        //TODO When we get the signal to show the game we show the game
        //TODO Listen for game start here

    }

    private void showGameScene() {

        GameController gameController = new GameController(localPlayerName);
        //TODO: Before showing the stage, set the players through Tim's playerlist message

        while (!GameStartGameMessageHandler.isConfigSet()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                LogHandling.logOnFile(Level.SEVERE, e.getMessage());
            }
        }
        String init = GameStartGameMessageHandler.getConfig();
        //gameController.init(init);
        //TODO: We need the game options somehow here, to pass to initGame();

        gameController.initGame(stage, init);
        gameController.show();
    }

    private void listenForChat(GameLobbyController gameLobbyController) {
        gameLobbyController.getChat().addListener((observable, oldValue, newValue) -> {
            //showGameScene();
            gameLobbyController.updateChat(newValue);
        });
    }

    private void listenForGameList(GameLobbyController gameLobbyController) {
        gameLobbyController.getGameList().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            gameLobbyController.updateGameList(newValue);
        });
    }

    private void listenForTopFive(GameLobbyController gameLobbyController) {
        gameLobbyController.getTopFive().addListener((observable, oldValue, newValue) -> {
            list = newValue.split(";");
            gameLobbyController.setTopFive(list);
        });
    }
    private void listenForStartGame(GameLobbyController gameLobbyController) {
        gameLobbyController.getGameStarted().addListener(((observable, oldValue, newValue) -> showGameScene()));
    }


}
