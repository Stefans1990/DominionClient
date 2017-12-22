package scenes.game;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {


    private final String playerName;

    //These need to be simpleIntegerProperties so that the view can listen for changed and update
    // the center label with the: "Buy: 6 Action: 5 Coin: 3"
    private SimpleIntegerProperty coins;
    private SimpleIntegerProperty actions;
    private SimpleIntegerProperty buy;
    private SimpleIntegerProperty victoryPoints;
    private SimpleIntegerProperty discardedCards;
    private SimpleIntegerProperty opponentVictoryPoints;
    private ArrayList<String> handCards;

    public Player(String playerName) {
        this.playerName = playerName;
        this.handCards = new ArrayList<>();
        coins = new SimpleIntegerProperty(0);
        actions = new SimpleIntegerProperty(1);
        buy = new SimpleIntegerProperty(1);
        victoryPoints = new SimpleIntegerProperty(0);
        discardedCards = new SimpleIntegerProperty(0);
    }

    public void resetValues() {
        coins.set(0);
        buy.set(1);
        actions.set(1);
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getCoins() {
        return coins.get();
    }

    public SimpleIntegerProperty coinsProperty() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins.set(coins);
    }

    public int getActions() {
        return actions.get();
    }

    public SimpleIntegerProperty actionsProperty() {
        return actions;
    }

    public void setActions(int actions) {
        this.actions.set(actions);
    }

    public int getBuy() {
        return buy.get();
    }

    public SimpleIntegerProperty buyProperty() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy.set(buy);
    }

    public int getVictoryPoints() {
        return victoryPoints.get();
    }

    public SimpleIntegerProperty victoryPointsProperty() {
        return victoryPoints;
    }


    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints.set(victoryPoints);
    }

    public int getDiscardedCards() {
        return discardedCards.get();
    }

    public SimpleIntegerProperty discardedCardsProperty() {
        return discardedCards;
    }

    public void setNumberOfDiscardedCards(int discardedCards) {
        this.discardedCards.set(discardedCards);
    }

    public void addCard(String cardName) {
        handCards.add(cardName.toLowerCase());
    }

    public ArrayList<String> getHandCards() {
        return handCards;
    }

    public void removeCardFromHand(String playedCard) {
        int index = handCards.indexOf(playedCard.toLowerCase());
        if (index != -1) {
            handCards.remove(index);
        }
    }

    public void removeCardFromHand(int index) {
        handCards.remove(index);
    }

    public void newCards(HashMap<String, Integer> handCardsMap) {
        handCards.clear();
        for (String cardName : handCardsMap.keySet()) {
            Integer cardAmount = handCardsMap.get(cardName);
            for (int i = 0; i < cardAmount; i++) {
                handCards.add(cardName.toLowerCase());
            }
        }
    }

    public void setOpponentVictoryPoints(int opponentVictoryPoints) {
        this.opponentVictoryPoints.set(opponentVictoryPoints);
    }
    public SimpleIntegerProperty getOpponentVictoryPoints(){
        if(opponentVictoryPoints==null){
            opponentVictoryPoints=new SimpleIntegerProperty();
        }
        return opponentVictoryPoints;
    }
}
