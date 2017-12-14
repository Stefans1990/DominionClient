package Handlers;

import Handlers.MessageHandler;
import Handlers.ServerMessageType;
import Handlers.UnknownFormatException;


import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Tim on 14.09.2017.
 */
public class ServerNewGameMessageHandler extends ServerMessageHandler {

    private final String CLASSNAME = ServerMessageType.NEWGAME.toString();
    private  MessageHandler superHandler;
    private String message =null;

    public ServerNewGameMessageHandler(String message) throws UnknownFormatException {
        if(!CLASSNAME.equals(message)){
            throw new UnknownFormatException(message);
        }
    }
    public ServerNewGameMessageHandler(){
       
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
        String gameName = splitMessage(message,4); //todo define position
        int cardNumbers= Integer.parseInt(splitMessage(message, 5)); //todo define position
        int  maxPlayers= Integer.parseInt(splitMessage(message, 6));



    }

    public String getMessage(){
        return message;
    }

    public Socket getClientSocket(){
        return superHandler.getClientSocket();
    }

}
