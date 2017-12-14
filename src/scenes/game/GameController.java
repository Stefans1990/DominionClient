package scenes.game;

import javafx.stage.Stage;

public class GameController {

    private GameModel model;
    private GameView view;

    public GameController(){
        this.model = new GameModel();
        this.view = new GameView(model);
    }

    public void show(Stage stage) {
        view.createLayout();
        view.show(stage);
    }

}
