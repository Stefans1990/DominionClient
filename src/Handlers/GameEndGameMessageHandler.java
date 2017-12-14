package Handlers;



/**
 * Created by Tim on 23.09.2017.
 */
public class GameEndGameMessageHandler extends GameMessageHandler {
    private final String CLASSNAME = GameMessageType.ENDGAME.toString();
    private String message = null;
    private MessageHandler superHandler;
    //List<Observer> observers;


    public GameEndGameMessageHandler(String message) throws UnknownFormatException {
        if (!CLASSNAME.equals(message)) {
            throw new UnknownFormatException(message);
        }
    }

    public GameEndGameMessageHandler() {

    }

    public void write(String message,Boolean privateMessage) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage,privateMessage);
    }

    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {
   
        message = msgIn;
        //todo solve the following points
        //write to clients, that the game is over and who won with how many points and so on.
        //update database and send topFive
        //remove the game out of the list and maps. put the players into the lobbylist again
        //timer until i put them into the Lobby (is it a seperate window or new?) if they close the window they return to the lobby?


        //Database.getDatabase().updateAfterGame();

    }

    public String getMessage() {
        return message;
    }
}


