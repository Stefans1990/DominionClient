package scenes.game;

import Handlers.GameMessageHandler;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GameView {

	private boolean isOverlayVisible;
	private Stage stage;
	private SimpleIntegerProperty numberOfDeckCards;
	private GameView controller;
	private HashMap<String, Label> labelStringHashMap;
	private HashMap<String, String> cardMap;
	private ArrayList<Label> opponentLabels;
	GameMessageHandler gameMessageHandler= new GameMessageHandler();
	public GameView() {
	}

	GameView(GameModel model) {
		this.model = model;
	}

	public void init(Stage stage, int actionCards) {
		this.stage = stage;
		Parent root = tryToLoadFXML();
		correctForActionCards(actionCards);
		correctForNumberOfPlayers();
		initProperties();
		initStage(stage, root);
	}


	private void correctForActionCards(int actionCards) {
		if (actionCards == 5) {
			controller.cardRowBox.getChildren().get(1).setVisible(false);
		}
	}

	private void correctForNumberOfPlayers() {
		opponentLabels = new ArrayList<>();
		opponentLabels.add(controller.opponent1Label);
		opponentLabels.add(controller.opponent2Label);
		opponentLabels.add(controller.opponent3Label);
		Iterator<Label> iterator = opponentLabels.iterator();
		for (int i = 0; i < model.getPlayers().size(); i++) {
			Player player = model.getPlayers().get(i);
			if (!player.getPlayerName().equals(model.getLocalPlayerName())) {
				Label opponentLabel = iterator.next();
				opponentLabel.setVisible(true);
				opponentLabel.setText(player.getPlayerName() + "\nVP: 0");
			}
		}
	}

	private Parent tryToLoadFXML() {
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/gameView.fxml"));
			root = loader.load();
			controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	private void initProperties() {
		initUserText();
		initLabelMap();
		initCardMap();
		listenForDeckCardChange();
		listenForPlayerChange();

	}

	private void initUserText() {
		for (Player player : model.getPlayers()) {
			if (player.getPlayerName().equals(model.getLocalPlayerName())) {
				controller.playerInfoLabel.setText(player.getPlayerName() + " VP:" + player.getVictoryPoints());
				controller.userMessageLabel.setText("Buy: " + player.getBuy() + " Coins: " + player.getCoins() + " Actions: " + player.getActions());
				controller.discardLabel.setText("0");
				controller.deckLabel.setText("0");
			}
		}
	}

	private void initLabelMap() {
		labelStringHashMap = new HashMap<>();
		labelStringHashMap.put("village", controller.villageLabel);
		labelStringHashMap.put("woodcutter", controller.woodcutterLabel);
		labelStringHashMap.put("smithy", controller.smithyLabel);
		labelStringHashMap.put("councilroom", controller.councilRoomLabel);
		labelStringHashMap.put("workshop", controller.workshopLabel);

		labelStringHashMap.put("festival", controller.festivalLabel);
		labelStringHashMap.put("laboratory", controller.laboratoryLabel);
		labelStringHashMap.put("witch", controller.witchLabel);
		labelStringHashMap.put("chancellor", controller.chancellorLabel);
		labelStringHashMap.put("market", controller.marketLabel);

		labelStringHashMap.put("province", controller.provinceLabel);
		labelStringHashMap.put("duchy", controller.duchyLabel);
		labelStringHashMap.put("estate", controller.estateLabel);
		labelStringHashMap.put("curse", controller.curseLabel);
		labelStringHashMap.put("gold", controller.goldLabel);
		labelStringHashMap.put("silver", controller.silverLabel);
		labelStringHashMap.put("copper", controller.copperLabel);
	}

	public void updatePlayFieldCard(String cardName, String amountLeft) {
		Label label = labelStringHashMap.get(cardName);
		label.setText(amountLeft);
	}

	private void initCardMap() {
		cardMap = new HashMap<>();

		cardMap.put("village", "assets/actionCards/villagesmall.JPG");
		cardMap.put("woodcutter", "assets/actionCards/woodcuttersmall.JPG");
		cardMap.put("smithy", "assets/actionCards/smithysmall.JPG");
		cardMap.put("councilroom", "assets/actionCards/councilroomsmall.JPG");
		cardMap.put("workshop", "assets/actionCards/workshopsmall.JPG");
		cardMap.put("festival", "assets/actionCards/festivalsmall.JPG");
		cardMap.put("laboratory", "assets/actionCards/laboratorysmall.JPG");
		cardMap.put("witch", "assets/actionCards/witchsmall.JPG");
		cardMap.put("chancellor", "assets/actionCards/chancellorsmall.JPG");
		cardMap.put("market", "assets/actionCards/marketsmall.JPG");
		cardMap.put("province", "assets/victoryCards/provincesmall.JPG");
		cardMap.put("duchy", "assets/victoryCards/duchysmall.JPG");
		cardMap.put("estate", "assets/victoryCards/estatesmall.JPG");
		cardMap.put("curse", "assets/victoryCards/cursesmall.JPG");
		cardMap.put("gold", "assets/victoryCards/goldsmall.JPG");
		cardMap.put("silver", "assets/victoryCards/silversmall.JPG");
		cardMap.put("copper", "assets/victoryCards/coppersmall.JPG");
	}


	private void createUserMessageLabelMessage(Player player) {

		int buy = player.getBuy();
		int coins = player.getCoins();
		int actions = player.getActions();

		controller.userMessageLabel.setText("Buy: " + buy + " Coins: " + coins + " Actions: " + actions);
	}

	private void listenForDeckCardChange() {
		controller.numberOfDeckCards = new SimpleIntegerProperty();
		controller.numberOfDeckCards.addListener((observable, oldValue, newValue) -> controller.deckLabel.setText(newValue.toString()));
	}

	private void listenForPlayerChange() {
		for (Player player : model.getPlayers()) {
			if (player.getPlayerName().equals(model.getLocalPlayerName())) {
				player.victoryPointsProperty().addListener((observable, oldValue, newValue) -> {
					controller.playerInfoLabel.setText(player.getPlayerName() + " VP:" + newValue);
				});

				player.buyProperty().addListener((observable, oldValue, newValue) -> createUserMessageLabelMessage(player));
				player.coinsProperty().addListener((observable, oldValue, newValue) -> createUserMessageLabelMessage(player));
				player.actionsProperty().addListener((observable, oldValue, newValue) -> createUserMessageLabelMessage(player));
				player.discardedCardsProperty().addListener((observable, olsValue, newValue) -> controller.discardLabel.setText(String.valueOf(newValue)));
			}

			setVictoryPointsForOtherPlayers();
		}
	}

	private void setVictoryPointsForOtherPlayers() {
		Iterator<Label> iterator = opponentLabels.iterator();
		for (int i = 0; i < model.getPlayers().size(); i++) {
			Player player = model.getPlayers().get(i);
			if (!player.getPlayerName().equals(model.getLocalPlayerName())) {
				player.victoryPointsProperty().addListener((observable, oldValue, newValue) -> {
					Label opponentLabel = iterator.next();
					opponentLabel.setText(player.getPlayerName() + "\nVP: " + newValue);
				});
			}
		}
	}

	private void initStage(Stage stage, Parent root) {
		root.setStyle("-fx-background-color: #333333");
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/scenes/game/gameStyleSheet.css");
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setWidth(primaryScreenBounds.getWidth() / 1.2);
		stage.setHeight(primaryScreenBounds.getHeight() / 1.2);
		stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getMaxX() / 10);
		stage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getMaxY() / 10);
		stage.setScene(scene);
	}

	public void setNumberOfDeckCards(int numberOfDeckCards) {
		if(controller.numberOfDeckCards==null){
			controller.numberOfDeckCards = new SimpleIntegerProperty();
		}
		controller.numberOfDeckCards.set(numberOfDeckCards);
	}

	private GameModel model;

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
	private HBox playedCards;

	@FXML
	private Label userMessageLabel;

	@FXML
	private TextArea chatTextArea;

	@FXML
	private TextField chatTextInputField;

	@FXML
	private Button playButton;

	@FXML
	private Button treasureButton;

	@FXML
	private HBox handHBox;

	@FXML
	private FlowPane overlayPane;

	@FXML
	private ImageView overlayImageView;

	@FXML
	private StackPane mainStackPane;

	@FXML
	private VBox cardRowBox;

	@FXML
	void chancellorClicked(MouseEvent event) {
		String cardName = "chancellor";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void copperCardClicked(MouseEvent event) {
		String cardName = "copper";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("victoryCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void councilroomClicked(MouseEvent event) {
		String cardName = "councilroom";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void curseCardClicked(MouseEvent event) {
		String cardName = "curse";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("victoryCards/" + cardName);
		}
	}


	@FXML
	void duchyCardClicked(MouseEvent event) {
		String cardName = "duchy";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("victoryCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void estateCardClicked(MouseEvent event) {
		String cardName = "estate";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("victoryCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void festivalClicked(MouseEvent event) {
		String cardName = "festival";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void goldCardClicked(MouseEvent event) {
		String cardName = "gold";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("victoryCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void laboratoryClicked(MouseEvent event) {
		String cardName = "laboratory";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void marketClicked(MouseEvent event) {
		String cardName = "market";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void provinceCardClicked(MouseEvent event) {
		String cardName = "province";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("victoryCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void silverCardClicked(MouseEvent event) {
		String cardName = "silver";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("victoryCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void smithyClicked(MouseEvent event) {
		String cardName = "smithy";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void villageClicked(MouseEvent event) {
		String cardName = "village";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void witchClicked(MouseEvent event) {
		String cardName = "witch";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void woodcutterClicked(MouseEvent event) {
		String cardName = "woodcutter";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	@FXML
	void workshopClicked(MouseEvent event) {
		String cardName = "workshop";
		if (event.getButton() == MouseButton.SECONDARY) {
			showCardDetail("actionCards/" + cardName);
		}
		if (event.getButton() == MouseButton.PRIMARY) {
			buy(cardName);
		}
	}

	private void buy(String cardName) {
		gameMessageHandler.write("buy@"+cardName, false);
		// TODO: Damiano has to adjust his methods to accept only complete lowercase names: councilroom instead of councilRoom
		// TODO: Send "buy@cardName" with PlayerName, Ask Damiano

	}

	@FXML
	void treasureButtonClicked(ActionEvent event) {

		//TODO: send message to Damiano: "playTreasure" with playername. Ask Damiano
		gameMessageHandler.write("playTreasure", false);
		Button treasureButton = (Button) event.getSource();
		treasureButton.setVisible(false);
	}

	@FXML
	void playButtonClicked(ActionEvent event) {
		String currentPlayerName = model.getCurrentPlayerName();
		String localPlayerName = model.getLocalPlayerName();

		if (localPlayerName.equals(currentPlayerName)) {
			gameMessageHandler.write("endturn", false);
			// TODO: Message to send: "endTurn" and playerName. Ask Damiano
		}
	}

	@FXML
	void chatTextInputClicked(ActionEvent event) {

	}

	@FXML
	void overlayImageClicked(MouseEvent event) {
		isOverlayVisible = false;
		overlayImageView.setImage(null);
		swapOverlay();

	}

	@FXML
	void overlayPaneClicked(MouseEvent event) {
		isOverlayVisible = false;
		overlayImageView.setImage(null);
		swapOverlay();
	}

	private void showCardDetail(String cardPath) {
		Image img = new Image("assets/" + cardPath + ".JPG");
		overlayImageView.setImage(img);
		isOverlayVisible = true;
		swapOverlay();
	}

	private void swapOverlay() {
		if (isOverlayVisible) {
			overlayPane.setStyle("-fx-background-color:  rgba(64, 64, 64, 0.85)");
			overlayPane.toFront();
		} else {
			overlayPane.setStyle("-fx-background-color:  rgba(1, 1, 1, 0)");
			overlayPane.toBack();
		}
	}

	public void show() {
		stage.show();
	}


	private ArrayList<ImageView> handDeckList;

	public void updateHandCards() {
		if (controller.handDeckList == null) {
			controller.handDeckList = new ArrayList<>();
		}
		controller.handDeckList.clear();
		for (Player player : model.getPlayers()) {
			if (player.getPlayerName().equals(model.getLocalPlayerName())) {
				for (String cardName : player.getHandCards()) {
					String path = cardMap.get(cardName);
					Image card = new Image(path);
					ImageView imageView = new ImageView(card);
					imageView.setId(cardName);
					controller.handDeckList.add(imageView);
				}
				reDrawHandCards();
			}
		}
	}

	private void reDrawHandCards() {
		ObservableList<Node> childNode = controller.handHBox.getChildren();
		childNode.removeIf(node -> node instanceof ImageView);

		for (ImageView cardImageView : controller.handDeckList) {
			controller.handHBox.getChildren().add(cardImageView); //index = 0
			controller.handHBox.toFront();
			listenForHandCardClicked(cardImageView);
		}
	}

	private void listenForHandCardClicked(ImageView cardImageView) {
		cardImageView.setOnMouseClicked(event -> {
			ImageView clickedCardImage = (ImageView) event.getSource();
			if (model.getCurrentPlayerName().equals(model.getLocalPlayerName())) {
				String cardName = clickedCardImage.getId();
				for (Player player : model.getPlayers()) {
					if (player.getPlayerName().equals(model.getLocalPlayerName())) {
						//TODO: DO nothing else here, except sending the cardName to Damiano
						//TODO: send: "cardName" maybe with the playerName
						gameMessageHandler.write("play@"+cardName, false);
					}
				}
			}
		});
	}

	public void showCardInPlayedArea(String playedCard) {
		controller.playedCards.getChildren().add(0, new ImageView(controller.cardMap.get(playedCard)));
	}
}
