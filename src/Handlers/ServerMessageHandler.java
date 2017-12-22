package Handlers;




import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by Tim on 12.09.2017.
 */
public class ServerMessageHandler extends MessageHandler {

    private final String CLASSNAME = MessageType.SERVER.toString();

    public ServerMessageHandler(String message) throws UnknownFormatException {
        String mainHandler = splitMessage(message, MAIN_HANDLER_INDEX);
        if (!CLASSNAME.equals(mainHandler)) {
            throw new UnknownFormatException(message);
        }
    }

    public ServerMessageHandler() {
    }

    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {
        String subHandler = splitMessage(msgIn, SUB_HANDLER_INDEX);
        MessageHandler handler = MessageHandlerFactory.getMessageHandler(subHandler);
        handler.handleMessage(msgIn);
    }

    @Override //explain
    public void write(String message) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage);
    }

}

