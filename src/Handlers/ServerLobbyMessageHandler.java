package Handlers;

import scenes.gameLobby.GameLobbyController;
import util.LogHandling;

import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by Tim on 27.11.2017.
 */
public class ServerLobbyMessageHandler extends ServerMessageHandler {
    private final String CLASSNAME = ServerMessageType.LOBBY.toString();
    private String message = null;
    private MessageHandler superHandler;
    String replyMessage;


    public ServerLobbyMessageHandler(String message) throws UnknownFormatException {
        if (!CLASSNAME.equals(message)) {
            throw new UnknownFormatException(message);
        }
    }

    public ServerLobbyMessageHandler() {
    }

    public void write(String message,Boolean privateMessage) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage,privateMessage);
    }

    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {
      
        message = msgIn;
        String fiveOrGamesList = splitMessage(message, 2);
        LogHandling.logOnFile(Level.INFO,"MessageType: " +fiveOrGamesList);
        String game = splitMessage(message,2); //todo define position
        String[] games= game.split("/");
        if(game.contains("GameList")){
        for(int i= 1;games.length>i; i++){
            GameLobbyController.setGameList(games[i]);
        }}
        if(fiveOrGamesList.equalsIgnoreCase("topfive")){
            GameLobbyController.updateTopFive(splitMessage(message, 3));
           // GameLobbyController.updateTopFive(splitMessage(message, 3));
        }
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String splitMessage(String message, int tokenIndex) {
        return super.splitMessage(message, tokenIndex);
    }

    public Socket getClientSocket() {
        return superHandler.getClientSocket();
    }


}

