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
public class ServerJoinGameMessageHandler extends ServerMessageHandler {

    private final String CLASSNAME = ServerMessageType.NEWGAME.toString();
    private  MessageHandler superHandler;
    private String message =null;

    public ServerJoinGameMessageHandler(String message) throws UnknownFormatException {
        if(!CLASSNAME.equals(message)){
            throw new UnknownFormatException(message);
        }
    }
    public ServerJoinGameMessageHandler() {
    
    }
    public void write(String message,Boolean privateMessage) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage,privateMessage);
    }
    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {

        message=msgIn;
        //GameMessageHandler.games.add(new TempGame(...))
        String gameName = splitMessage(message, 5);//todo set Token

    }


      
    public String getMessage(){
        return message;
    }

    public Socket getClientSocket(){
        return superHandler.getClientSocket();
    }

}
