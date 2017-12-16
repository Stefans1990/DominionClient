package scenes.game;

import Handlers.GameMessageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView {


	private GameModel model;
    GameMessageHandler gameHandler;

	@FXML
    private ImageView provinceCard;

    @FXML
    private Label provinceLabel;

    @FXML
    private ImageView goldCard;

    @FXML
    private Label goldLabel;

    @FXML
    private ImageView duchyCard;

    @FXML
    private Label duchyLabel;

    @FXML
    private ImageView silverCard;

    @FXML
    private Label silverLabel;

    @FXML
    private ImageView estateCard;

    @FXML
    private Label estateLabel;

    @FXML
    private ImageView copperCard;

    @FXML
    private Label copperLabel;

    @FXML
    private ImageView curseCard;

    @FXML
    private Label curseLabel;

    @FXML
    private Label playerInfoLabel;

    @FXML
    private ImageView discardCard;

    @FXML
    private Label discardLabel;

    @FXML
    private ImageView deckCard;

    @FXML
    private Label deckLabel;

    @FXML
    private Label opponent1Label;

    @FXML
    private Label opponent2Label;

    @FXML
    private Label opponent3Label;

    @FXML
    private ImageView chancellorCard;

    @FXML
    private Label chancellorLabel;

    @FXML
    private ImageView councilroomCard;

    @FXML
    private Label councilRoomLabel;

    @FXML
    private ImageView festivalCard;

    @FXML
    private Label festivalLabel;

    @FXML
    private ImageView laboratoryCard;

    @FXML
    private Label laboratoryLabel;

    @FXML
    private ImageView marketCard;

    @FXML
    private Label marketLabel;

    @FXML
    private ImageView smithyCard;

    @FXML
    private Label smithyLabel;

    @FXML
    private ImageView villageCard;

    @FXML
    private Label villageLabel;

    @FXML
    private ImageView witchCard;

    @FXML
    private Label witchLabel;

    @FXML
    private ImageView woodcutterCard;

    @FXML
    private Label woodcutterLabel;

    @FXML
    private ImageView workshopCard;

    @FXML
    private Label workshopLabel;

    @FXML
    private Label userMessageLabel;

    @FXML
    private ImageView playerCard1;


    @FXML
    private ImageView playerCard2;

    @FXML
    private ImageView playerCard3;

    @FXML
    private ImageView playerCard4;

    @FXML
    private ImageView playerCard5;

    @FXML
    private TextArea chatTextArea;

    @FXML
    private TextField chatTextInputField;

    @FXML
    private Button playButton;

	public GameView() {
	}

	public GameView(GameModel model) {
		this.model = model;
	}

	public void show(Stage stage) throws IOException {
        gameHandler = new GameMessageHandler();
		Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/gameView.fxml"));
		root.setStyle("-fx-background-color: #333333");
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/scenes/game/gameStyleSheet.css");
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setWidth(primaryScreenBounds.getWidth() / 1.2);
		stage.setHeight(primaryScreenBounds.getHeight() / 1.2);
		stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getMaxX() / 10);
		stage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getMaxY() / 10);
		stage.setScene(scene);
		stage.show();
	}


	@FXML
    void chancellorClicked(MouseEvent event) {
        gameHandler.write("Buy/",true);
    }

    @FXML
    void chatTextInputClicked(ActionEvent event) {

    }

    @FXML
    void copperCardClicked(MouseEvent event) {

    }

    @FXML
    void councilroomClicked(MouseEvent event) {

    }

    @FXML
    void curseCardClicked(MouseEvent event) {

    }

    @FXML
    void deckCardClicked(MouseEvent event) {

    }

    @FXML
    void discardCardClicked(MouseEvent event) {

    }

    @FXML
    void duchyCardClicked(MouseEvent event) {

    }

    @FXML
    void estateCardClicked(MouseEvent event) {

    }

    @FXML
    void festivalClicked(MouseEvent event) {

    }

    @FXML
    void goldCardClicked(MouseEvent event) {

    }

    @FXML
    void laboratoryClicked(MouseEvent event) {

    }

    @FXML
    void marketClicked(MouseEvent event) {

    }

    @FXML
    void playButtonClicked(ActionEvent event) {

    }

    @FXML
    void playerCard1Clicked(MouseEvent event) {

    }

    @FXML
    void playerCard2Clicked(MouseEvent event) {

    }

    @FXML
    void playerCard3Clicked(MouseEvent event) {

    }

    @FXML
    void playerCard4Clicked(MouseEvent event) {

    }

    @FXML
    void playerCard5Clicked(MouseEvent event) {

    }

    @FXML
    void provinceCardClicked(MouseEvent event) {

    }

    @FXML
    void silverCardClicked(MouseEvent event) {

    }

    @FXML
    void smithyClicked(MouseEvent event) {

    }

    @FXML
    void villageClicked(MouseEvent event) {

    }

    @FXML
    void witchClicked(MouseEvent event) {

    }

    @FXML
    void woodcutterClicked(MouseEvent event) {

    }

    @FXML
    void workshopClicked(MouseEvent event) {

    }
}
