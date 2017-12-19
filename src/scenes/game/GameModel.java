package scenes.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class GameModel {


    private String localPlayerName;
    private String currentPlayerName;
    private ArrayList<Player> players;

    private int numberOfActionCards;
	private HashMap<String, Integer> actionCardMap;

	GameModel(String localPlayerName) {
	    this.localPlayerName = localPlayerName;
        this.players = new ArrayList<>();
	    initActionCardMap();

    }

	private void initActionCardMap() {
		actionCardMap = new HashMap<>();

		for (ActionCardName actionCardName : ActionCardName.values()) {
			actionCardMap.put(actionCardName.toString().toLowerCase(), 0);
		}
	}

	public String getLocalPlayerName() {
		return localPlayerName;
	}

	public String getCurrentPlayerName() {
		return currentPlayerName;
	}

	public void setCurrentPlayerName(String currentPlayerName) {
		this.currentPlayerName = currentPlayerName;
	}

	public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setNumberOfActionCards(int numberOfActionCards) {
        this.numberOfActionCards = numberOfActionCards;
    }

    public int getNumberOfActionCards() {
        return numberOfActionCards;
    }

	public HashMap<String, Integer> getActionCardMap() {
		return actionCardMap;
	}
}
