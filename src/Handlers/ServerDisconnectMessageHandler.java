package Handlers;

import Handlers.MessageHandler;
import Handlers.ServerMessageType;
import Handlers.UnknownFormatException;

/**
 * Created by Tim on 12.09.2017.
 */
public class ServerDisconnectMessageHandler extends ServerMessageHandler{
   private final String CLASSNAME = ServerMessageType.DISCONNECT.toString();
    private String message =null;
    private MessageHandler superHandler;

    public ServerDisconnectMessageHandler(String message) throws UnknownFormatException {
        if(!CLASSNAME.equals(message)){
            throw new UnknownFormatException(message);
        }
    }

    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {
   
        //return player to loginField +end game+ end thread

    }
    public void write(String message,Boolean privateMessage) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage,privateMessage);
    }
    public String getMessage(){
        return message;
    }
}