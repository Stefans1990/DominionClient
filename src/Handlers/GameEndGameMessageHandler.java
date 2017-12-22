package Handlers;



/**
 * Created by Tim on 23.09.2017.
 */
public class GameEndGameMessageHandler extends GameMessageHandler {
    private final String CLASSNAME = GameMessageType.ENDGAME.toString();
    private String message = null;

    public GameEndGameMessageHandler(String message) throws UnknownFormatException {
        if (!CLASSNAME.equals(message)) {
            throw new UnknownFormatException(message);
        }
    }

    public GameEndGameMessageHandler() {
    }

    public void write(String message) {
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


