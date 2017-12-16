package Handlers;

import Handlers.MessageHandler;
import Handlers.ServerMessageType;
import Handlers.UnknownFormatException;
import scenes.gameLobby.GameLobbyController;


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
        String game = splitMessage(message,2); //todo define position
        String[] games= game.split("/");
        for(int i= 0;games.length<0; i++){
            GameLobbyController.setGameList(games[i]);
        }



    }

    public String getMessage(){
        return message;
    }

    public Socket getClientSocket(){
        return superHandler.getClientSocket();
    }

}
