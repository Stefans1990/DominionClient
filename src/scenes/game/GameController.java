package scenes.game;

import javafx.stage.Stage;

import java.io.IOException;

public class GameController {

    private GameModel model;
    private GameView view;

    public GameController(){
        this.model = new GameModel();
        this.view = new GameView(model);
    }

    public void show(Stage stage) {
	    try {
		    view.show(stage);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
    }

}
