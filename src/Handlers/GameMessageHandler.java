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
public class GameMessageHandler extends MessageHandler implements Observer {
    private MessageHandler superHandler;
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

        //get the game name --> String gameName = splitMessage(message,3);
        //GameHandlers game = GameManager.getGame(gameName);
        //returnMessage = game.handleMessage()

        //die unteren muessen zu damiano in das jeweilige GameHandlers


        /*Player player = socketPlayerHashMap.get(getClientSocket().getInetAddress());
        if (gameList.get(player) == null || subHandler.equalsIgnoreCase("ENDGAME")) {
            MessageHandler handler = MessageHandlerFactory.getMessageHandler(subHandler);
            ;
        } else {
            gameList.get(player).readMessage(message);
        }*/


    }

    public void write(String message,Boolean privateMessage) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage,privateMessage);
    }


    @Override
    public void update(Observable o, Object arg) {
        String response = (String) arg;
        if (splitMessage(response, 0).equalsIgnoreCase("end")) {
            endGame();
        }
        write(response, false);
        //todo what to do with the response?
    }

    private void endGame() {

        topFiveUpdate();
        //showLobby();
        //deleteGameFromList();
    }

    private void topFiveUpdate() {

        //Database.getDatabase().updateAfterGame();
    }

  

    //sobald nachricht hierhin kommt muss geprüft werden ob game = null ist. wenn ja dann wird diese method ausgelöst?

    public Socket getClientSocket() {
        return superHandler.getClientSocket();
    }
    private void setGameMessage(String message) {
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
