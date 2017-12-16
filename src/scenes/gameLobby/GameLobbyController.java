package scenes.gameLobby;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import util.LogHandling;

import java.util.logging.Level;


public class GameLobbyController {

    private static GameLobbyModel model;
    private GameLobbyView view;
    private SimpleBooleanProperty gameStarted;
    private static SimpleStringProperty gameList = new SimpleStringProperty();

    public static SimpleStringProperty getGameList() {
        return gameList;
    }
    public static void setGameList(String gameName){
        Platform.runLater(new Runnable() {
            @Override public void run() {
                gameList.set(gameName);
            }
        });
    }



    public GameLobbyController() {
        this.model = new GameLobbyModel();
        this.view = new GameLobbyView(model);
        gameStarted = new SimpleBooleanProperty(false);
    }

    public void show(Stage stage) {
        view.createLayout();
        view.show(stage);
        listenForGameStart();
    }

    private void listenForGameStart() {
        view.isGameStartedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isGameStarted) {
                if (isGameStarted.booleanValue() == Boolean.TRUE) {
                    //gameStarted.setValue(true);
                    //gameStarted.setValue(false);
                }
            }
        });
    }

    public SimpleBooleanProperty isGameStarted() {
        return gameStarted;
    }

    public SimpleBooleanProperty getGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(SimpleBooleanProperty gameStarted) {
        this.gameStarted = gameStarted;
    }
    public static SimpleStringProperty getChat() {
        return model.getChatText();
     }

    public void updateChat(String message) {
    	LogHandling.logOnFile(Level.INFO, "Chat is appended " +message);
        message =message +"\n";
    	view.chat.appendText(message);
    }
    public void updateGameList(String gameName){
        if(!checkForGame(gameName)){
            view.gameListView.getItems().add(gameName);
        }
    }
    public boolean checkForGame(String gameName){
        boolean found= false;
        for(int i= 0;view.gameListView.getItems().size()>i;i++){
            if(view.gameListView.getItems().get(i).equalsIgnoreCase(gameName)){
                found= true;
            }
        }
        return found;
    }
}
