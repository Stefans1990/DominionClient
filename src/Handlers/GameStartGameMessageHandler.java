package Handlers;


import scenes.game.GameController;
import scenes.gameLobby.GameLobbyController;
import scenes.gameLobby.GameLobbyModel;
import util.LogHandling;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by Tim on 23.09.2017.
 */
public class GameStartGameMessageHandler extends GameMessageHandler {
    private final String CLASSNAME = GameMessageType.STARTGAME.toString();
    private String message = null;
    private MessageHandler superHandler;
    private static String config;
    //ArrayList<TempGame> list;

    //List<Observer> observers;


    public GameStartGameMessageHandler(String message) throws UnknownFormatException {
        if (!CLASSNAME.equals(message)) {
            throw new UnknownFormatException(message);
        }
    }

    public GameStartGameMessageHandler() {

    }

    public void write(String message, Boolean privateMessage) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage, privateMessage);
    }

    @Override
    public void handleMessage(String msgIn) throws UnknownFormatException {
        GameLobbyController.setGameStarted(true);
        LogHandling.logOnFile(Level.INFO, "Game is starting");
        message = msgIn;

        if (message.contains("config")) {
            setConfig(message);
        }else {
            setGameMessage(message);
        }
        GameLobbyController.setGameStarted(true);

        //GameController.setGameMessage("startGame");

        //code with observable and observer -- notify and update() -- send this with it write getMessage Method to return the string to the model
    }

    private void setGameMessage(String message) {
        int i= message.indexOf("/");
        GameController.setNewMessage(message.substring(i+1));
    }

    public String getMessage() {
        return message;
    }

    public static void setConfig(String configMessage) {
        int i= configMessage.indexOf("*");
        config = configMessage.substring(i+1);
    }

    public static String getConfig() {
        return config;
    }

    public static boolean isConfigSet() {
        if (config == null) {
            return false;
        } else {
            return true;
        }
    }

}
