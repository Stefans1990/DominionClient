package scenes.game;

import javafx.beans.property.SimpleStringProperty;
import util.LogHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
/*
@Tim
 */

public class GameModel {


    private String localPlayerName;
    private static SimpleStringProperty currentPlayerName;
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
		return currentPlayerName.getValue();
	}

	public void setCurrentPlayerName(String currentPlayer) {
		if(currentPlayerName==null){
			currentPlayerName=new SimpleStringProperty();
		}
		currentPlayerName.set(currentPlayer);
	}
	public static SimpleStringProperty getCurrentPlayerProperty(){
		if(currentPlayerName==null){
			currentPlayerName=new SimpleStringProperty();
		}
		return currentPlayerName;
	}
	//@Tim
	//iterates through the players and sets the currentPlayer to the next player
	//if there is no next player it is set to the first player
	public void setNextPlayerCurrentPlayer(){
		boolean isChanged=false;
		Iterator<Player> iterator = players.iterator();
		while(iterator.hasNext()){
			Player player = iterator.next();
			if(player.getPlayerName().equalsIgnoreCase(getCurrentPlayerName())){
                if(iterator.hasNext()){
                    setCurrentPlayerName(iterator.next().getPlayerName());
                    isChanged = true;
                }
			}
		}
		if(!isChanged){
            setCurrentPlayerName(players.get(0).getPlayerName());
        }
        LogHandling.logOnFile(Level.INFO, "The current player is: "+getCurrentPlayerName());

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
