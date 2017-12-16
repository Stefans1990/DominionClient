package Handlers;

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

