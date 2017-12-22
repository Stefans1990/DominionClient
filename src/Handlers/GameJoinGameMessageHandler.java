package Handlers;

/**
 * Created by Tim on 05.12.2017.
 */

import Handlers.MessageHandler;
import Handlers.ServerMessageType;
import Handlers.UnknownFormatException;

import java.net.Socket;

/**
 * Created by Tim on 14.09.2017.
 */
public class GameJoinGameMessageHandler extends GameMessageHandler {

    private final String CLASSNAME = GameMessageType.JOINGAME.toString();
    private String message = null;

    public GameJoinGameMessageHandler(String message) throws UnknownFormatException {
        if (!CLASSNAME.equals(message)) {
            throw new UnknownFormatException(message);
        }
    }

    public GameJoinGameMessageHandler() {

    }

    public void write(String message) {
        GameMessageHandler.setGameName(message);
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage);
    }

    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {
        message = msgIn;
    }

    public String getMessage() {
        return message;
    }
}
