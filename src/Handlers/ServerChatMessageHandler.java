package Handlers;

import scenes.game.GameController;
import scenes.gameLobby.GameLobbyController;
import scenes.gameLobby.GameLobbyModel;
import util.LogHandling;

import java.util.logging.Level;

/**
 * Created by Tim on 23.09.2017.
 */
public class ServerChatMessageHandler extends ServerMessageHandler {
    private final String CLASSNAME = ServerMessageType.CHAT.toString();
    private String message = null;

    public ServerChatMessageHandler(String message) throws UnknownFormatException {
        if (!CLASSNAME.equals(message)) {
            throw new UnknownFormatException(message);
        }
    }

    public ServerChatMessageHandler() {

    }

    public void write(String message) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage);
    }

    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {
        LogHandling.logOnFile(Level.INFO, "Message Handling: " + msgIn);
        message = msgIn;
        String chatMessage = splitMessage(message, 2);

        GameLobbyModel.addChat(chatMessage);

        //code with observable and observer -- notify and update() -- send this with it write getMessage Method to return the string to the model
    }

    public String getMessage() {
        return message;
    }
}


