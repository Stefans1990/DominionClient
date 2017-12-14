package Handlers;


import java.util.ArrayList;

/**
 * Created by Tim on 23.09.2017.
 */
public class GameStartGameMessageHandler extends GameMessageHandler {
    private final String CLASSNAME = GameMessageType.STARTGAME.toString();
    private String message = null;
    private MessageHandler superHandler;
    //ArrayList<TempGame> list;

    //List<Observer> observers;


    public GameStartGameMessageHandler(String message) throws UnknownFormatException {
        if (!CLASSNAME.equals(message)) {
            throw new UnknownFormatException(message);
        }
    }

    public GameStartGameMessageHandler() {

    }

    public void write(String message,Boolean privateMessage) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage,privateMessage);
    }

    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {

        message = msgIn;
        String gameName = splitMessage(message, 5); //todo set token

        //code with observable and observer -- notify and update() -- send this with it write getMessage Method to return the string to the model
    }

    public String getMessage() {
        return message;
    }


}
