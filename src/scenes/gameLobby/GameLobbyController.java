package scenes.gameLobby;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;


public class GameLobbyController {

    private GameLobbyModel model;
    private GameLobbyView view;
    private SimpleBooleanProperty gameStarted;

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
                    gameStarted.setValue(true);
                    gameStarted.setValue(false);
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
}
