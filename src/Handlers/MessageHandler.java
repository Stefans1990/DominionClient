package Handlers;




import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.application.Platform;
import scenes.verification.VerificationModel;
import util.LogHandling;

/**
 * Created by Tim on 23.08.2017.
 */
public class MessageHandler  implements Observer {
   

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
    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run(){           
               while(true){
                   tryReadMessage(reader);
               }
            }
        });
    	executor.submit(thread1);
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
   
    public void write(String message, Boolean privateMessage) {
        LogHandling.logOnFile(Level.INFO, "The message is: "+message+" It is a private message: "+privateMessage);
        try {
        	writer.write(message + "\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

    public BufferedWriter getWriter() {
        return this.writer;
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
        //MessageHandlerFactory.addHandler("Handlers.GameMessageHandler");
        //MessageHandlerFactory.addHandler("Handlers.GameMessageHandler");

        LogHandling.logOnFile(Level.INFO, "All MessageHandlers added successfully");
    }
    

    @Override
    public void update(Observable o, Object arg) {

    }
    public static void reportAndLogException(final Throwable t)
    {
      Platform.runLater(new Runnable() {
        @Override public void run() {
        	VerificationModel.setIsConnected(true);
        }
      });
    }
}


