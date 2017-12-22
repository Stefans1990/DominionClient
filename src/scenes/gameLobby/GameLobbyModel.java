package scenes.gameLobby;


import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import scenes.verification.VerificationModel;

public class GameLobbyModel {

    private ArrayList<String> topFives;
    private static SimpleStringProperty chatText= new SimpleStringProperty();


    public ArrayList<String> getTopFive() {
        if (topFives == null){
            topFives = new ArrayList<>(5);
            for(String s: topFives){
                s = "-";
            }
        }
        return topFives;
    }

    // @stefan adds a new chatmessage to the chat area
	public static void addChat(String chatMessage) {
		 Platform.runLater(() -> GameLobbyModel.updateChat(chatMessage));
		
	}
	public SimpleStringProperty getChatText() {
	       
    	return chatText;
    }


    // @stefan updates the chat area when a player writes a message
	protected static void updateChat(String chatMessage) {
			chatText.set(chatMessage);
		
	}
	

/*    public ArrayList<String> setTopFive(){

    }*/
}
