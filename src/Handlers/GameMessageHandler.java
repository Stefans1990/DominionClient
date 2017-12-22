package Handlers;



import scenes.game.GameController;
import util.LogHandling;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

/**
 * Created by Tim on 23.08.2017.
 */
public class GameMessageHandler extends MessageHandler {
    private static String gameName;

    private final String CLASSNAME = MessageType.GAME.toString();


    public GameMessageHandler(String message) throws UnknownFormatException {
        String mainHandler = splitMessage(message, MAIN_HANDLER_INDEX);
        if (!CLASSNAME.equals(mainHandler)) {
            throw new UnknownFormatException(message);
        }
    }

    public GameMessageHandler() {

    }

    @Override
    public void handleMessage(String message) throws UnknownFormatException {
        String subHandler = splitMessage(message, SUB_HANDLER_INDEX);

        if(subHandler.equalsIgnoreCase("startgame")||subHandler.equalsIgnoreCase("Joingame")||subHandler.equalsIgnoreCase("endgame")){
            MessageHandler handler = MessageHandlerFactory.getMessageHandler(subHandler);
            handler.handleMessage(message);
        }else{
            setGameMessage(message);
        }
    }

    public void write(String message) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage);
    }



    private void endGame() {
        topFiveUpdate();
        //showLobby();
        //deleteGameFromList();
    }

    private void topFiveUpdate() {
        //Database.getDatabase().updateAfterGame();
    }
    private void setGameMessage(String message) {
        if(message.charAt(message.length()-1)=='@'){
            message = message.substring(0, message.length()-1);
        }
        int i= message.indexOf("/");
        GameController.setNewMessage(message.substring(i+1));
    }
    public static String getGameName(){
        return gameName;
    }
    public static void setGameName(String name){
        LogHandling.logOnFile(Level.INFO, "GameName is set to: "+name);
       gameName=name;
    }
}
