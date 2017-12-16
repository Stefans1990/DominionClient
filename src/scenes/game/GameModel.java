package scenes.game;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Hashtable;


public class GameModel {

    private static SimpleStringProperty chatText= new SimpleStringProperty();

    private int turnCount;
    ArrayList<Player> players;
    Hashtable<CardName, Integer> actionCardList;
    Hashtable<CardName, Integer> coinCardList;
    Hashtable<CardName, Integer> victoryCardList;

    public GameModel() {
        this.turnCount = 0;
        this.players = new ArrayList<>();
        this.actionCardList = new Hashtable<>();
        this.coinCardList = new Hashtable<>();
        this.victoryCardList = new Hashtable<>();
    }

    public static void addChat(String chatMessage) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                GameModel.updateChat(chatMessage);
            }
        });

    }
    public SimpleStringProperty getChatText() {

        return chatText;
    }



    protected static void updateChat(String chatMessage) {
        chatText.set(chatMessage);

    }


    public int getActionCardSize(){
        int numberOfdistinctActionCards = 0;

        for (CardName cardName : actionCardList.keySet()) {
            Integer amountOfCards = actionCardList.get(cardName);
            if (amountOfCards != 0 ){
                numberOfdistinctActionCards++;
            }
        }
        return numberOfdistinctActionCards;
    }





    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Hashtable<CardName, Integer> getActionCardList() {
        return actionCardList;
    }

    public Hashtable<CardName, Integer> getCoinCardList() {
        return coinCardList;
    }

    public Hashtable<CardName, Integer> getVictoryCardList() {
        return victoryCardList;
    }
}
