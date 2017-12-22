package scenes.gameLobby;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import scenes.verification.VerificationModel;
import util.LogHandling;

import java.util.ArrayList;
import java.util.logging.Level;


public class GameLobbyController {

    private static GameLobbyModel model;
    private GameLobbyView view;
    private static SimpleBooleanProperty gameStarted;
    private static SimpleStringProperty topFive= new SimpleStringProperty();;

    private static SimpleStringProperty gameList;

    public static SimpleStringProperty getGameList() {
        return gameList;
    }

    public static void setGameList(String gameName) {
        Platform.runLater(() -> gameList.set(gameName));
    }

    // @stefan instantiates the model and the view
    public GameLobbyController() {
        this.model = new GameLobbyModel();
        this.view = new GameLobbyView(model);
        gameStarted = new SimpleBooleanProperty(false);
        gameList = new SimpleStringProperty();

    }
    // @stefan  get the view to show the layout and the stage
    public void show(Stage stage) {
        view.createLayout();
        view.show(stage);
    }

    // @stefan returns the information if the game got started or not
    public static boolean isGameStarted() {
        return gameStarted.getValue();
    }

    public SimpleBooleanProperty getGameStarted() {
        return gameStarted;
    }

    public static void setGameStarted(boolean started) {
        Platform.runLater(() -> gameStarted.set(started));
    }

    public static SimpleStringProperty getChat() {
        return model.getChatText();
    }
    // @stefan updates the chat with the chatmessage
    public void updateChat(String message) {
        LogHandling.logOnFile(Level.INFO, "Chat is appended " + message);
        message = message + "\n";
        view.chat.appendText(message);
    }
    // @stefan Updates the gamelist with new games
    public void updateGameList(String gameName) {
        if (!checkForGame(gameName)) {
            view.gameListView.getItems().add(gameName);
        }
    }
    // @stefan checks the gamelist for games and shos it on the view
    public boolean checkForGame(String gameName) {
        boolean found = false;
        for (int i = 0; view.gameListView.getItems().size() > i; i++) {
            if (view.gameListView.getItems().get(i).equalsIgnoreCase(gameName)) {
                found = true;
            }
        }
        if(gameName.equalsIgnoreCase("")){
            found=true;
        }
        return found;
    }

    public static SimpleStringProperty getTopFive() {
        return topFive;
    }

    public static void setTopFive(String[] listNames) {
        LogHandling.logOnFile(Level.INFO, "TopFive is updated"+listNames.length);
        for (int i = 0; i < listNames.length; i++) {

            if(!GameLobbyView.topLabels.isEmpty()) {
                GameLobbyView.topLabels.get(i).setText(i+1+". "+listNames[i]);
            }
        }
    }
    // @stefan updates the topfive when games have been played
    public static void updateTopFive(String message){
        Platform.runLater(() -> topFive.set(message));

    }
}
