package Handlers;




import util.LogHandling;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by Tim on 23.08.2017.
 */
public class MessageHandler  {
   

    private static Socket socket;


    private static boolean running = true;
    private static BufferedWriter writer;
    private static BufferedReader reader;

    public final int MAIN_HANDLER_INDEX = 0;
    public final int SUB_HANDLER_INDEX = 1;

    private static final String DELIMITER = "@";

    public MessageHandler() {
    	
    }

	public static void openResources() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            LogHandling.logOnFile(Level.INFO, "Resources are opened");
            addMessageHandler();
        } catch (IOException e) {
            LogHandling.logOnFile(Level.WARNING, e.getMessage());
        }
    }

    public static void read() {
    	Thread thread1 = new Thread(() -> {
           while(true){
               tryReadMessage(reader);
           }
        });
    	thread1.start();
    }

    private static void tryReadMessage(BufferedReader input) {
        String message;
        try {
            while ((message = input.readLine()) != null) {
            	System.out.println("YEah");
                LogHandling.logOnFile(Level.INFO,"incoming message: "+message);
                MessageHandler handler = MessageHandlerFactory.getMessageHandler(message);
                handler.handleMessage(message);
            }
        } catch (IOException e) {
            closeResources();
            LogHandling.logOnFile(Level.WARNING,e.getMessage());
        } catch (UnknownFormatException e) {
            LogHandling.logOnFile(Level.WARNING, e.getMessage());
        }
    }
   
    public void write(String message) {
        LogHandling.logOnFile(Level.INFO, "The message is: "+message);
        try {
        	writer.write(message + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


    public static String addDelimiter(String message) {
        return DELIMITER + message;
    }

    public String splitMessage(String message, int tokenIndex) {
        String[] tokens = message.split(DELIMITER);
        return tokens[tokenIndex];
    }

    public void handleMessage(String msgIn) throws UnknownFormatException {
    }

    private static void closeResources() {
        try {
            reader.close();
            writer.close();
            socket.close();
            LogHandling.closeResources();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {
        return socket;
    }
    public static void setSocket(Socket serverSocket) {
    	socket = serverSocket;
    }

    private static void addMessageHandler() {
        LogHandling.logOnFile(Level.INFO, "Adding MessageHandlers");

        MessageHandlerFactory.addHandler("Handlers.GameMessageHandler");
        MessageHandlerFactory.addHandler("Handlers.ServerMessageHandler");

        MessageHandlerFactory.addHandler("Handlers.ServerLoginMessageHandler");
        MessageHandlerFactory.addHandler("Handlers.ServerRegisterMessageHandler");
        MessageHandlerFactory.addHandler("Handlers.ServerNewGameMessageHandler");
        MessageHandlerFactory.addHandler("Handlers.ServerLobbyMessageHandler");

        MessageHandlerFactory.addHandler("Handlers.GameStartGameMessageHandler");
        MessageHandlerFactory.addHandler("Handlers.ServerChatMessageHandler");
        MessageHandlerFactory.addHandler("Handlers.GameEndGameMessageHandler");

        LogHandling.logOnFile(Level.INFO, "All MessageHandlers added successfully");
    }
}


