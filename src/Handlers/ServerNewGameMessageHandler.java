package Handlers;

import scenes.gameLobby.GameLobbyController;

/**
 * Created by Tim on 14.09.2017.
 */
public class ServerNewGameMessageHandler extends ServerMessageHandler {

    private final String CLASSNAME = ServerMessageType.NEWGAME.toString();
    private String message = null;

    public ServerNewGameMessageHandler(String message) throws UnknownFormatException {
        if (!CLASSNAME.equals(message)) {
            throw new UnknownFormatException(message);
        }
    }

    public ServerNewGameMessageHandler() {

    }

    public void write(String message) {
        GameMessageHandler.setGameName(splitMessage(message, 0));
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage);
    }

    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {
        message = msgIn;
        //GameMessageHandler.games.add(new TempGame(...))
        String game = splitMessage(message, 2);
        String[] games = game.split("/");
        for (int i = 0; games.length < 0; i++) {
            GameLobbyController.setGameList(games[i]);
        }
    }

    public String getMessage() {
        return message;
    }
}
