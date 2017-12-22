package scenes.game;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    /*
    @Tim
    the Player has its own values and deck/discard/hand

     */
    private final String playerName;
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
    //@Tim
    //the values are reset to the original state
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

    //@Tim
    //the card is removed from the hand
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
    public void setOpponentVictoryPoints(int opponentVictoryPoint) {
        this.opponentVictoryPoints.set(opponentVictoryPoint);
    }
    public SimpleIntegerProperty getOpponentVictoryPoints(){
        if(opponentVictoryPoints==null){
            opponentVictoryPoints=new SimpleIntegerProperty();
        }
        return opponentVictoryPoints;
    }
}
