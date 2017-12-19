package scenes;

import util.ServiceLocator;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Handlers.MessageHandler;
import Handlers.MessageHandlerFactory;
import Handlers.UnknownFormatException;
import javafx.application.Platform;
import scenes.verification.VerificationModel;

public class MainModel {

    private Socket socket;
    private final Logger logger;
    private BufferedReader in;

    public MainModel(Socket socket) {
        logger = Logger.getLogger(this.getClass().getCanonicalName());
        this.socket = socket;
    }

    public void tryCreateReader() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not create BufferedInputStream reader");
        }
    }

   public void startListeningForMessages() {
        new Thread(() -> {
            String message ;
            try {
                while ((message = in.readLine()) != null){
                    //todo send message to messagehandlerFactory
                    MessageHandler handler = MessageHandlerFactory.getMessageHandler(message);
                    handler.handleMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnknownFormatException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void tryCreaterWriter() {
        try {
            BufferedOutputStream  out = new BufferedOutputStream(socket.getOutputStream());
            //TODO: when tim is done with the messageclass send the output to the messageclass
            ServiceLocator.setWriter(out);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not create BufferedOutputStream writer");
        }
    }
   
}
