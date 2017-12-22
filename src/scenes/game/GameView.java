package scenes.game;

import Handlers.GameMessageHandler;
import Handlers.ServerChatMessageHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
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
import sun.rmi.runtime.Log;
import util.LogHandling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class GameView {

    private boolean isOverlayVisible;
    private Stage stage;
    private SimpleIntegerProperty numberOfDeckCards;
    private GameView controller;
    private HashMap<String, Label> labelStringHashMap;
    private HashMap<String, String> cardMap;
    private ArrayList<Label> opponentLabels;
    GameMessageHandler gameMessageHandler = new GameMessageHandler();
    ServerChatMessageHandler chatHandler = new ServerChatMessageHandler();

    public GameView() {
    }
    // @stefan makes a new gameview with the model as logic behind it
    GameView(GameModel model) {
        this.model = model;
    }
    // @stefan Loads the fxml file to display the correct gameview
    public void init(Stage stage, int actionCards) {
        this.stage = stage;
        Parent root = tryToLoadFXML();
        correctForActionCards(actionCards);
        correctForNumberOfPlayers();
        initProperties();
        initStage(stage, root);
    }

    // @stefan shows only 5 actioncards, if the option 5 actioncards was clicked on and sets the 2nd row to invisible
    private void correctForActionCards(int actionCards) {
        if (actionCards == 5) {
            controller.cardRowBox.getChildren().get(1).setVisible(false);
        }

    }
    // @stefan sets up the opponent labels for all selected opponent players (1,2 or 3)
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
                opponentLabel.setText(player.getPlayerName() + " VP: 3");
            }
        }
    }
    /*
     @stefan tries to load the fxml file and sets it on the root, trying to get the controller and the model from the fxml file
      */
    private Parent tryToLoadFXML() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/gameView.fxml"));
            root = loader.load();
            controller = loader.getController();
            controller.setModel(this.model);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
    // @stefan sets up the initialised properties to start the game
    private void initProperties() {
        initUserText();
        initLabelMap();
        initCardMap();
        listenForDeckCardChange();
        listenForPlayerChange();

    }
    // @stefan gets the logic in the model for each player, each player has a name, buys and deck as well as discard
    private void initUserText() {
        for (Player player : model.getPlayers()) {
            if (player.getPlayerName().equals(model.getLocalPlayerName())) {
                controller.playerInfoLabel.setText(player.getPlayerName() + " VP:" + player.getVictoryPoints());
                controller.userMessageLabel.setText("Buy: " + player.getBuy() + " Coins: " + player.getCoins() + " Actions: " + player.getActions());
                controller.discardLabel.setText("0");
                //controller.deckLabel.setText("0");
            }
        }
    }
    // @stefan Updates the victory points for the players and who's turn it is(current player)
    protected void updateUserText(String currentPlayer) {
        for (Player player : model.getPlayers()) {
            if(player.getPlayerName().equalsIgnoreCase(model.getLocalPlayerName())){
                controller.playerInfoLabel.setText(player.getPlayerName()+ " VP: " + player.getVictoryPoints() + "\n" + "Current player: " + "\n"+currentPlayer);
            }
        }
    }
    // @stefan makes a map with the card name and the corresponding label showing the amount of them on the field
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

    // @stefan updates the label of the above cards, when a card gets bought from a player
    public void updatePlayFieldCard(String cardName, String amountLeft) {
        labelStringHashMap.get(cardName).setText(amountLeft);
    }
    // @stefan connects the card name to the corresponding image of the card
    private void initCardMap() {
        controller.cardMap = new HashMap<>();

        controller.cardMap.put("village", "assets/actionCards/villagesmall.JPG");
        controller.cardMap.put("woodcutter", "assets/actionCards/woodcuttersmall.JPG");
        controller.cardMap.put("smithy", "assets/actionCards/smithysmall.JPG");
        controller.cardMap.put("councilroom", "assets/actionCards/councilroomsmall.JPG");
        controller.cardMap.put("workshop", "assets/actionCards/workshopsmall.JPG");
        controller.cardMap.put("festival", "assets/actionCards/festivalsmall.JPG");
        controller.cardMap.put("laboratory", "assets/actionCards/laboratorysmall.JPG");
        controller.cardMap.put("witch", "assets/actionCards/witchsmall.JPG");
        controller.cardMap.put("chancellor", "assets/actionCards/chancellorsmall.JPG");
        controller.cardMap.put("market", "assets/actionCards/marketsmall.JPG");
        controller.cardMap.put("province", "assets/victoryCards/provincesmall.JPG");
        controller.cardMap.put("duchy", "assets/victoryCards/duchysmall.JPG");
        controller.cardMap.put("estate", "assets/victoryCards/estatesmall.JPG");
        controller.cardMap.put("curse", "assets/victoryCards/cursesmall.JPG");
        controller.cardMap.put("gold", "assets/victoryCards/goldsmall.JPG");
        controller.cardMap.put("silver", "assets/victoryCards/silversmall.JPG");
        controller.cardMap.put("copper", "assets/victoryCards/coppersmall.JPG");
    }

    // @stefan creates the labels of the number of buys, coins and actions left of the player
    private void createUserMessageLabelMessage(Player player) {

        int buy = player.getBuy();
        int coins = player.getCoins();
        int actions = player.getActions();

        controller.userMessageLabel.setText("Buy: " + buy + " Coins: " + coins + " Actions: " + actions);
    }
    // @stefan updates the label showing the amount of cards remaining in the deck
    private void listenForDeckCardChange() {
        controller.numberOfDeckCards = new SimpleIntegerProperty();
        controller.numberOfDeckCards.addListener((observable, oldValue, newValue) -> controller.deckLabel.setText(newValue.toString()));
    }
    // @stefan this method displays changes on the label from the opponents turns, like the amount of victorypoints
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
                player.getOpponentVictoryPoints().addListener((observable, oldValue, newValue) -> {
                    Label opponentLabel = iterator.next();
                    System.out.print(newValue);
                    opponentLabel.setText(player.getPlayerName() + " VP: " + newValue);
                });
            }
        }
    }
    // @stefan sets the init stage with the scene and instantiates the gamestylesheet
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
        if (controller.numberOfDeckCards == null) {
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
    protected TextArea chatTextArea;

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
    /*
     @stefan all of the next ...clicked display on rightclick a bigger, detailed version of the card and on leftclick it buys the card
      */
    @FXML
    void chancellorClicked(MouseEvent event) {
        String cardName = "chancellor";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void copperCardClicked(MouseEvent event) {
        String cardName = "copper";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("victoryCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void councilroomClicked(MouseEvent event) {
        String cardName = "councilroom";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
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
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void estateCardClicked(MouseEvent event) {
        String cardName = "estate";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("victoryCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void festivalClicked(MouseEvent event) {
        String cardName = "festival";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void goldCardClicked(MouseEvent event) {
        String cardName = "gold";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("victoryCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void laboratoryClicked(MouseEvent event) {
        String cardName = "laboratory";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void marketClicked(MouseEvent event) {
        String cardName = "market";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void provinceCardClicked(MouseEvent event) {
        String cardName = "province";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("victoryCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void silverCardClicked(MouseEvent event) {
        String cardName = "silver";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("victoryCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void smithyClicked(MouseEvent event) {
        String cardName = "smithy";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void villageClicked(MouseEvent event) {
        String cardName = "village";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void witchClicked(MouseEvent event) {
        String cardName = "witch";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void woodcutterClicked(MouseEvent event) {
        String cardName = "woodcutter";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    @FXML
    void workshopClicked(MouseEvent event) {
        String cardName = "workshop";
        if (event.getButton() == MouseButton.SECONDARY) {
            showCardDetail("actionCards/" + cardName);
        }
        if (event.getButton() == MouseButton.PRIMARY && model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            buy(cardName);
        }
    }

    private void buy(String cardName) {
        gameMessageHandler.write("buy@" + cardName);
    }
    // @stefan makes the message for the logger of which player clicked on the treasurebutton
    @FXML
    void treasureButtonClicked(ActionEvent event) {
        if (model.getLocalPlayerName().equals(model.getCurrentPlayerName())) {
            gameMessageHandler.write("playTreasure");
            Button treasureButton = (Button) event.getSource();
            treasureButton.setVisible(true);
        }
    }
    // @stefan logs which player clicked the endTurn button
    @FXML
    void playButtonClicked(ActionEvent event) {
        String currentPlayerName = model.getCurrentPlayerName();
        String localPlayerName = model.getLocalPlayerName();

        LogHandling.logOnFile(Level.INFO, "Current Player: " + currentPlayerName + "Local Player: " + localPlayerName);
        if (localPlayerName.equals(currentPlayerName)) {
            gameMessageHandler.write("endTurn");
        }
    }

    @FXML
    void chatTextInputClicked(ActionEvent event) {
        chatHandler.write(chatTextInputField.getText());
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
    // @stefan shows the stage of the game
    public void show() {
        stage.show();
    }


    private ArrayList<ImageView> handDeckList;
    // @stefan updates which handcards are shown to the player
    public void updateHandCards() {
        if (controller.handDeckList == null) {
            controller.handDeckList = new ArrayList<>();
        }
        controller.handDeckList.clear();
        for (Player player : model.getPlayers()) {
            if (player.getPlayerName().equals(model.getLocalPlayerName())) {
                for (String cardName : player.getHandCards()) {
                    String path = controller.cardMap.get(cardName);
                    //LogHandling.logOnFile(Level.INFO, "ImagePath is: " + path);
                    Image card = new Image(path);
                    ImageView imageView = new ImageView(card);
                    if (imageView.getImage() == null) {
                        // LogHandling.logOnFile(Level.SEVERE, "Image is null");
                    } else {
                        //LogHandling.logOnFile(Level.SEVERE, "Image is " + imageView.getImage());
                    }
                    imageView.setId(cardName);
                    imageView.getStyleClass().add("card");
                    controller.handDeckList.add(imageView);
                }
                reDrawHandCards();
            }
        }
    }
    // @stefan Creates a box for each handcard that gets drawn
    private void reDrawHandCards() {
        ObservableList<Node> childNode = controller.handHBox.getChildren();
        //LogHandling.logOnFile(Level.INFO, "Size: " + controller.handHBox.getChildren().size());
        childNode.removeIf(node -> node instanceof ImageView);
        //LogHandling.logOnFile(Level.INFO, "Size after delete: " + controller.handHBox.getChildren().size());
        for (ImageView cardImageView : controller.handDeckList) {
            //cardImageView.setStyle("-fx-background-color: brown");
            cardImageView.setFitHeight(150);
            cardImageView.setFitWidth(100);
            cardImageView.setVisible(true);
            //LogHandling.logOnFile(Level.INFO, "Imageview is: " + cardImageView.getImage() + "Card ID: " + cardImageView.getId());
            controller.handHBox.getChildren().add(cardImageView); //index = 0
            //LogHandling.logOnFile(Level.INFO, "Size in loop: " + controller.handHBox.getChildren().size());

            listenForHandCardClicked(cardImageView);
        }
    }
    // @stefan listens if the player clicks on an actioncard
    private void listenForHandCardClicked(ImageView cardImageView) {
        cardImageView.setOnMouseClicked(event -> {
            ImageView clickedCardImage = (ImageView) event.getSource();
            if (model.getCurrentPlayerName().equals(model.getLocalPlayerName())) {
                String cardName = clickedCardImage.getId();
                for (Player player : model.getPlayers()) {
                    if (player.getPlayerName().equals(model.getLocalPlayerName())) {
                        gameMessageHandler.write("play@" + cardName);
                    }
                }
            }
        });
    }
    // @stefan determines how the played cards are seen by the user in the played area
    public void showCardInPlayedArea(String playedCard) {
        if (playedCard != null) {
            ImageView imageView = new ImageView(controller.cardMap.get(playedCard));
            imageView.setFitWidth(50);
            imageView.setFitHeight(75);
            controller.playedCards.getChildren().add(0, imageView);
        }
    }
    // @stefan clears the card after getting invoked by the logic
    public void clearCardInPlayedArea() {
        controller.playedCards.getChildren().clear();
    }

    public void setModel(GameModel model) {
        this.model = model;
    }
    // @stefan Creates the message about the end of the game, which player got how many victory points and who won this round
    public void showGameOver(String[] messageParts) {
        ArrayList<Label> playerLabels = new ArrayList<>();
        String winner = null;
        int currentRecord = 0;

        for (String playerName  :messageParts ) {
            String[] playerSplit = playerName.split(",");
            int playerScore = Integer.parseInt(playerSplit[1]);
            if (playerScore>currentRecord){
                currentRecord = playerScore;
                winner = playerSplit[0];
            }
        }
        for (String playerName  :messageParts ) {
            String[] playerSplit = playerName.split(",");
            Label label = new Label();

            if (playerSplit[0].equals(winner)){
                label.setText("  " + playerSplit[0]+ " Victory Points: "+ playerSplit[1]+ " winner  ");
                label.setId("winnerStyle");
                label.setStyle("-fx-text-fill: white");
                label.setStyle("-fx-font-size: 40px");

            } else {
                label.setText("  " + playerSplit[0]+ " Victory Points: "+ playerSplit[1] + "  ");
                label.setId("loserStyle");
                label.setStyle("-fx-font-size: 20px");
                label.setStyle("-fx-text-fill: white");
            }
            playerLabels.add(label);
        }
        controller.overlayPane.getChildren().clear();
        ObservableList<Label> labels = FXCollections.observableArrayList(playerLabels);
        controller.overlayPane.getChildren().addAll(labels);
        controller.isOverlayVisible = true;
        controller.swapOverlay();
    }
}
