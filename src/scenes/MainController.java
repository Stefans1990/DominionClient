package scenes;


import Handlers.GameStartGameMessageHandler;
import javafx.stage.Stage;
import scenes.game.GameController;
import scenes.gameLobby.GameLobbyController;
import scenes.serverConnection.ServerConnectionController;
import scenes.verification.VerificationController;
import util.LogHandling;
import java.net.Socket;
import java.util.logging.Level;
import Handlers.MessageHandler;


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

    }
    private void showGameScene() {
        GameController gameController = new GameController(localPlayerName);
        while (!GameStartGameMessageHandler.isConfigSet()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                LogHandling.logOnFile(Level.SEVERE, e.getMessage());
            }
        }
        String init = GameStartGameMessageHandler.getConfig();
        gameController.initGame(stage, init);
        gameController.show();
    }

    private void listenForChat(GameLobbyController gameLobbyController) {
        gameLobbyController.getChat().addListener((observable, oldValue, newValue) -> {
            gameLobbyController.updateChat(newValue);
        });
    }

    private void listenForGameList(GameLobbyController gameLobbyController) {
        gameLobbyController.getGameList().addListener((observable, oldValue, newValue) -> {
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
