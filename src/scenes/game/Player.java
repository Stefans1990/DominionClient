package scenes.game;

import java.util.Hashtable;

public class Player {

    private final int ID;
    private int coins;
    private int actions;
    private int buy;

    private Hashtable<CardName, Integer> playerDeck;
    private Hashtable<CardName, Integer> discardDeck;
    private Hashtable<CardName, Integer> handDeck;

    public Player(int ID, Hashtable<CardName, Integer> playerDeck, Hashtable<CardName, Integer> discardDeck, Hashtable<CardName, Integer> handDeck) {
        this.ID = ID;
        this.playerDeck = playerDeck;
        this.discardDeck = discardDeck;
        this.handDeck = handDeck;
        this.coins = 0;
        this.actions = 0;
        this.buy = 0;
    }
}
