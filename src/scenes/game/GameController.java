package scenes.game;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import util.LogHandling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class GameController {

    private final String localPlayerName;
    private GameModel model;
    private GameView view;
    private static SimpleStringProperty newMessage = new SimpleStringProperty();
    private static SimpleStringProperty chatText = new SimpleStringProperty();


    public GameController(String localPlayerName) {
        this.localPlayerName = localPlayerName;
        this.model = new GameModel(localPlayerName);
        this.view = new GameView(model);
    }

    //@Tim
    //incoming Message: config@5@player1/player2/player3
    //the Game is initialized with the
    //the number of action cards is sent to the view, to show the right actionCard amount
    public void initGame(Stage stage, String initConfig) {
        String[] splitMessage = initConfig.split("@");
        int numberOfActionsCards = Integer.parseInt(splitMessage[1]);
        model.setNumberOfActionCards(numberOfActionsCards);
        String[] playerNames = splitMessage[2].split("/");
        listenForCurrentPlayer();
        model.setCurrentPlayerName(playerNames[0]);
        for (String name : playerNames) {
            Player player = new Player(name);
            model.getPlayers().add(player);
        }
        view.init(stage, numberOfActionsCards);
    }

    //@Tim
    //if the currentPlayerProperty is changed, the label with the current player is changed
    private void listenForCurrentPlayer() {
        GameModel.getCurrentPlayerProperty().addListener(((observable, oldValue, newValue) -> {
            view.updateUserText(newValue);
        }));
    }

    public void show() {
        view.show();
        listenForMessages();
    }

    //@Tim
    //messages which are set over the MessageHandler
    //Platform.runLater is used as it is executed by a not JavaFX application
    public static void setNewMessage(String message) {
        Platform.runLater(() -> newMessage.set(message));

    }

    public static SimpleStringProperty getNewMessage() {
        return newMessage;
    }

    //@Tim
    //if the newMessage is added it is being processed
    private void listenForMessages() {
        newMessage.addListener((observable, oldMessage, newMessage) -> processMessage(newMessage));
    }

    //@Tim
    //incoming Message: endTurn@player2@victoryPoints,3@discard,6@curse,10
    //the message is split and with the switch statement the right method to execute is selected
    private void processMessage(String newMessage) {
        LogHandling.logOnFile(Level.INFO, "Message being processed: " + newMessage);
        int index = newMessage.indexOf("@");

        String messageType = newMessage.substring(0, index);

        String message = newMessage.substring(index + 1);

        System.out.println(messageType);
        System.out.println(message);

        switch (messageType) {
            case "initCards":
                initCards(message);
                break;
            case "newCards":
                newCards(message);
                break;
            case "buy":
                buyCards(message);
                break;
            case "playTreasures":
                playTreasure(message);
                break;
            case "play":
                playCard(message);
                break;
            case "endTurn":
                endTurn(message);
                break;
            case "newDeckValues":
                newDeckValues(message);
                break;
            case "end":
                end(message);
                break;
            default:
                LogHandling.logOnFile(Level.SEVERE, message);
                break;
        }

    }

    //@Tim
    //incoming Message: playername@woodcutter@gold@market@market
    //the cards are added to the hand
    //this method is only executed in the first round
    //it is always checked if the playerName is the localPlayer
    private void initCards(String message) {
        String[] messageParts = split(message, "@");

        String playerName = messageParts[0];
        if (playerName.equals(localPlayerName)) {
            for (Player player : model.getPlayers()) {
                if (player.getPlayerName().equals(localPlayerName)) {
                    String[] cardNames = Arrays.copyOfRange(messageParts, 1, messageParts.length);
                    for (String cardName : cardNames) {
                        player.addCard(cardName);
                        view.updateHandCards();
                    }
                }
            }
        }

    }

    //@Tim
    // Player gets a new card: His hand and number of cards in his deck get updated
    // playername@hand/copper,3;estate,2@deck,5
    private void newCards(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName
        // 1: hand/copper,3;estate,2
        // 2: deck,5

        String playerName = messageParts[0];
        if (playerName.equals(localPlayerName)) {
            for (Player player : model.getPlayers()) {
                if (playerName.equals(player.getPlayerName())) {

                    // 1: hand/copper,3;estate,2
                    HashMap<String, Integer> handCardsMap = processNewHand(messageParts[1]);
                    player.newCards(handCardsMap);
                    view.updateHandCards();

                    // 2: deck,5
                    String[] deckInfo = split(messageParts[2], ",");
                    LogHandling.logOnFile(Level.INFO, "Expected: deck, 5 in this case: " + deckInfo[0] + ", " + deckInfo[1]);
                    int numberOfDeckCards = Integer.parseInt(deckInfo[1]);
                    view.setNumberOfDeckCards(numberOfDeckCards);
                }
            }
        }
    }

    //@Tim
    //incoming message: 1: hand/copper,3;estate,2
    //the message is split and the hand is updated with the new cards
    private HashMap<String, Integer> processNewHand(String messagePart) {
        // 1: hand/copper,3;estate,2
        int index = messagePart.indexOf("/");
        String temp = messagePart.substring(index + 1);
        String[] cardsAndAmounts = split(temp, ";");

        HashMap<String, Integer> handCardsMap = new HashMap<>();

        for (String cardAndAmount : cardsAndAmounts) {
            String[] split = split(cardAndAmount, ",");
            handCardsMap.put(split[0].toLowerCase(), Integer.parseInt(split[1]));
        }
        return handCardsMap;
    }

    //@Tim
    //incoming message: playername@buyValue,0@coinValue,2@actionCards/woodcutter,9@discard,4
    //message is split and the different values are updated. Furthermore is the actioncard which is bought
    //displayed on all clients -1
    private void buyCards(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName
        // 1: buyValue,0
        // 2: coinValue,2
        // 3: actionCards/woodcutter,9
        // 4: discard,4


        // Update only for localPlayer
        String playerName = messageParts[0];
        if (playerName.equals(localPlayerName)) {

            // 1: buyValue,0
            int buy = Integer.parseInt(split(messageParts[1], ",")[1]);

            // 2: coinValue,2
            int coin = Integer.parseInt(split(messageParts[2], ",")[1]);

            // 4: discard,4
            for (Player player : model.getPlayers()) {
                if (player.getPlayerName().equals(localPlayerName)) {
                    player.setBuy(buy);
                    player.setCoins(coin);
                    int discard = Integer.parseInt(split(messageParts[4], ",")[1]);
                    player.setNumberOfDiscardedCards(discard);
                }
            }
        }

        // Update center cards for all players
        // 3: actionCards/woodcutter,9
        int index = messageParts[3].indexOf("/");
        String cardNameAndNumber = messageParts[3].substring(index + 1);
        String[] cardNameAndNumberSplit = split(cardNameAndNumber, ",");
        String cardName = cardNameAndNumberSplit[0].toLowerCase();
        String amountLeft = cardNameAndNumberSplit[1];
        view.updatePlayFieldCard(cardName, amountLeft);

    }

    //@Tim
    //incoming message: playername@playedCard/copper,3;gold,1@hand/estate,1@coinValue,1
    //messages are split
    //the hand cards and the coinValue are updated just for the player who is addressed
    //the played Cards are updated for all clients
    private void playTreasure(String message) {

        String[] messageParts = split(message, "@");

        // 0: playerName
        // 1: playedCard/copper,3;gold,1
        // 2: hand/estate,1
        // 3: coinValue,1
        String playerName = messageParts[0];
        String[] playedCards = split(messageParts[1], "/");
        String[] cardSplit = split(playedCards[1], ";");

        for (String cardAndAmount : cardSplit) {
            String[] cardAndAmountParts = split(cardAndAmount, ",");
            String cardPlayed = cardAndAmountParts[0];
            int cardAmount = Integer.parseInt(cardAndAmountParts[1]);
            for (int i = 0; i < cardAmount; i++) {
                if (playerName.equals(localPlayerName)) {
                    for (Player player : model.getPlayers()) {
                        if (player.getPlayerName().equals(localPlayerName)) {
                            LogHandling.logOnFile(Level.INFO, cardPlayed + "is removed from Hand");
                            player.removeCardFromHand(cardPlayed);
                        }
                    }
                }
                LogHandling.logOnFile(Level.INFO, cardPlayed + " is added to PlayedArea");
                view.showCardInPlayedArea(cardPlayed);
            }
        }


        playerName = messageParts[0];
        System.out.println("" + localPlayerName + playerName + model.getCurrentPlayerName() + model.getLocalPlayerName());
        if (playerName.equals(localPlayerName)) {
            for (Player player : model.getPlayers()) {
                if (player.getPlayerName().equals(localPlayerName)) {

                    // 2: hand/estate,1;woodcutter,1

                    String[] handCardsParts = split(messageParts[2], "/");

                    String[] cardNamesSplit = split(handCardsParts[1], ";");
                    //String[] cardNames = Arrays.copyOfRange(handCardsParts, 1, messageParts.length);
                    if (cardNamesSplit[0].equalsIgnoreCase("empty")) {
                        player.getHandCards().clear();
                    } else {
                        player.getHandCards().clear();
                        for (String cardAndAmount : cardNamesSplit) {
                            String[] cardAndAmountParts = split(cardAndAmount, ",");
                            System.out.println(cardAndAmountParts);
                            String cardName = cardAndAmountParts[0];
                            int cardAmount = Integer.parseInt(cardAndAmountParts[1]);
                            for (int i = 0; i < cardAmount; i++) {
                                player.addCard(cardName);
                            }
                        }
                    }


                    // 3: coinValue,1
                    String[] coinParts = split(messageParts[3], ",");
                    //LogHandling.logOnFile(Level.INFO, "The coins are"+ coinParts[0]+", "+coinParts[1] + "expected is coinValue, 1");
                    player.setCoins(Integer.parseInt(coinParts[1]));

                    view.updateHandCards();
                }
            }
        }


    }

    //@Tim
    //incoming: playername@woodcutter@actionValue,1
    //the message is split and the played card is added in the playedArea for all clients
    //for addresses client the action, buy and coin values are updated
    private void playCard(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName
        // 1: woodcutter
        // 2: actionValue,1
        // 3: coinValue,1
        // 4: buyValue,1

        //playerName
        String playerName = messageParts[0];
        String playedCard = messageParts[1];
        view.showCardInPlayedArea(playedCard);
        if (playerName.equals(localPlayerName)) {
            for (Player player : model.getPlayers()) {
                if (player.getPlayerName().equals(localPlayerName)) {
                    // 1: woodcutter
                    player.removeCardFromHand(playedCard);
                    view.updateHandCards();

                    // 2: actionValue,1
                    String[] actionParts = split(messageParts[2], ",");
                    player.setActions(Integer.parseInt(actionParts[1]));

                    // 3: coinValue,1
                    String[] coinParts = split(messageParts[3], ",");
                    player.setCoins(Integer.parseInt(coinParts[1]));

                    // 4: buyValue,1
                    String[] buyParts = split(messageParts[4], ",");
                    player.setBuy(Integer.parseInt(buyParts[1]));
                }
            }
        }
    }

    //@Tim
    //incoming message: playername@victoryPoints,1@discard,5
    //for the addressed Player the victoryPoints and the discard Deck Label are updated
    //for all clients the played cards area is cleared and the curse card Label is updated
    //the current player is set to the next player
    private void endTurn(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName
        // 1: victoryPoints,1
        // 2: discard,5
        // 3: curse, 6
        view.clearCardInPlayedArea();


        String playerName = messageParts[0];


        for (Player player : model.getPlayers()) {
            if (playerName.equals(localPlayerName)) {
                if (player.getPlayerName().equals(localPlayerName)) {
                    // 1: victoryPoints,1
                    String victoryPointsString = split(messageParts[1], ",")[1];
                    int victoryPoints = Integer.parseInt(victoryPointsString);
                    player.setVictoryPoints(victoryPoints);

                    // 2: discard,5
                    int discard = Integer.parseInt(split(messageParts[2], ",")[1]);
                    player.setNumberOfDiscardedCards(discard);
                    player.resetValues();
                }
            }
        }
        // 3: curse, 6
        String curseCount = split(messageParts[3], ",")[1];
        view.updatePlayFieldCard("curse", curseCount);

        model.setNextPlayerCurrentPlayer();
    }

    //@Tim
    //incoming message: playername@discard,2@deck,3
    //message is split and for the player addressed the discard and deck label are updated
    private void newDeckValues(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName
        // 1: discard,2
        // 2: deck,3

        String playerName = messageParts[0];
        if (playerName.equals(localPlayerName)) {
            for (Player player : model.getPlayers()) {
                if (player.getPlayerName().equals(localPlayerName)) {

                    // 1: discard,2
                    String discardValue = split(messageParts[1], ",")[1];
                    int discard = Integer.parseInt(discardValue);
                    player.setNumberOfDiscardedCards(discard);

                    // 2: deck,3
                    String numberOfDeckString = split(messageParts[2], ",")[1];
                    int numberOfDeck = Integer.parseInt(numberOfDeckString);
                    view.setNumberOfDeckCards(numberOfDeck);
                }
            }
        }
    }

    //@Tim
    //incoming message: playername1,12@playername,5@playername,6
    //the message is split and sent to the view
    private void end(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName1,3
        // 1: playerName,5
        // 2: playerName,6
        view.showGameOver(messageParts);

    }
    //@Tim
    //the message is split with the delimiter
    private String[] split(String message, String splitter) {
        return message.split(splitter);
    }

}
