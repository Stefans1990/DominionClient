package scenes.game;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import util.LogHandling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class GameController {

    private final String localPlayerName;
    private GameModel model;
    private GameView view;

    //TODO: Replace this with Tim's SimpleStringProperty
    private static SimpleStringProperty newMessage = new SimpleStringProperty();


    public GameController(String localPlayerName) {
        this.localPlayerName = localPlayerName;
        this.model = new GameModel(localPlayerName);
        this.view = new GameView(model);
    }

    public void init(String initConfig) {
        String[] splitMessage = initConfig.split("@");
        String numberOfActionsCards = splitMessage[1];
        model.setNumberOfActionCards(Integer.parseInt(numberOfActionsCards));
        String[] playerNames = splitMessage[2].split("/");
        for (String name : playerNames) {
            Player player = new Player(name);
            model.getPlayers().add(player);
        }
    }

    public void initGame(Stage stage, int actionCards) {
        view.init(stage, actionCards);
    }

    public void show() {
        view.show();
        listenForMessages();
    }

    public static void setNewMessage(String message) {
        Platform.runLater(()->newMessage.set(message));

    }

    public static SimpleStringProperty getNewMessage() {
        return newMessage;
    }

    private void listenForMessages() {
        newMessage.addListener((observable, oldMessage, newMessage) -> processMessage(newMessage));
        // TODO: Delete the next line. It is an example to test.
        //newMessage.set("newCards@playername@hand/councilroom,3;estate,2@deck,5");
    }

    private void processMessage(String newMessage) {
        LogHandling.logOnFile(Level.INFO, "Message being processed: "+newMessage);
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
        }
    }

    // TODO: Damiano needs to send this message for each player separately in the beginning
    // TODO: I need to know the initial hand cards to display. We forgot that these are different
    // TODO: every time.
    // TODO: I am assuming that the message looks like the next line
    // playername@woodcutter@gold@market@market
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

    // Player gets a new card: His hand and number of cards in his deck get updated
    // playername@hand/copper,3;estate,2@deck,5
    private void newCards(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName
        // 1: hand/copper,3;estate,2
        // 2: deck,5

        String playerName = messageParts[0];
        for (Player player : model.getPlayers()) {
            if (playerName.equals(player.getPlayerName())) {

                // 1: hand/copper,3;estate,2
                HashMap<String, Integer> handCardsMap = processNewHand(messageParts[1]);
                player.newCards(handCardsMap);
                view.updateHandCards();

                // 2: deck,5
                String[] deckInfo = split(messageParts[2], ",");
                int numberOfDeckCards = Integer.parseInt(deckInfo[1]);
                view.setNumberOfDeckCards(numberOfDeckCards);
            }
        }
    }

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

    // Player buys a card and has a new buy value and a new coin value. And the center action
    // card numbers need to be updated. For each card that has changed. There is max. 1
    // actionCard. The card that has been bought goes into the discard deck.

    // playername@buyValue,0@coinValue,2@actionCards/woodcutter,9@discard,4
    private void buyCards(String message) {
        String[] messageParts = split(message, "@");
        // 0: buyValue,0
        // 1: coinValue,2
        // 2: actionCards/woodcutter,9
        // 3: discard,4

        // Update only for localPlayer
        String playerName = messageParts[0];
        if (playerName.equals(localPlayerName)) {

            // 0: buyValue,0
            int buy = Integer.parseInt(split(messageParts[0], ",")[1]);

            // 1: coinValue,2
            int coin = Integer.parseInt(split(messageParts[1], ",")[1]);

            // 3: discard,4
            for (Player player : model.getPlayers()) {
                if (player.getPlayerName().equals(localPlayerName)) {
                    player.setBuy(buy);
                    player.setCoins(coin);
                    int discard = Integer.parseInt(split(messageParts[3], ",")[1]);
                    player.setNumberOfDiscardedCards(discard);
                }
            }
        }

        // Update center cards for all players
        // 2: actionCards/woodcutter,9
        int index = messageParts[2].indexOf("/");
        String cardNameAndNumber = messageParts[2].substring(index + 1);
        String[] cardNameAndNumberSplit = split(cardNameAndNumber, ",");
        String cardName = cardNameAndNumberSplit[0].toLowerCase();
        String amountLeft = cardNameAndNumberSplit[1];
        view.updatePlayFieldCard(cardName, amountLeft);

    }

    // Player plays a card which needs to disappear from his hand but show up in the center
    // space to show that it is being played. The center Label for action value changes too.

    //playername@woodcutter@actionValue,1
    private void playCard(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName
        // 1: woodcutter
        // 2: actionValue,1

        //playerName
        String playerName = messageParts[0];
        if (playerName.equals(localPlayerName)) {
            for (Player player : model.getPlayers()) {
                if (player.getPlayerName().equals(localPlayerName)) {

                    // 1: woodcutter
                    String playedCard = messageParts[1];
                    player.removeCardFromHand(playedCard);
                    view.updateHandCards();
                    view.showCardInPlayedArea(playedCard);

                    // 2: actionValue,1
                    String[] actionParts = split(messageParts[2], ",");
                    player.setActions(Integer.parseInt(actionParts[1]));
                }
            }
        }
    }

    // Player finished a his round. Make sure to reset the BuyValue:1, ActionValue:1 and
    // CoinValue:0 This will not be in the message endTurn, because it is always the same. In
    // the player class there is a reset method

    // playername@victoryPoints,1@discard,5
    private void endTurn(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName
        // 1: victoryPoints,1
        // 2: discard,5

        String playerName = messageParts[0];
        if (playerName.equals(localPlayerName)) {
            for (Player player : model.getPlayers()) {
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
    }

    // Sometime the discard Deck and the Player deck have to be updated with a new value.
    // Update only the labels of the decks in the lower left corner through the view class
    // with a simpleIntProperty and the settText method of the corresponding label in the view.

    // playername@discard,2@deck,3
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

    // When the game is over show a game over screen with all the player names and their
    // victory points provided in the message. The winner is the one with the most victory
    // points. There can also be draws.

    // playername1,3@playername,5@playername,6
    private void end(String message) {
        String[] messageParts = split(message, "@");
        // 0: playerName1,3
        // 1: playerName,5
        // 2: playerName,6

        //TODO: Still need to do this.


    }

    private String[] split(String message, String splitter) {
        return message.split(splitter);
    }

}
