package Handlers;



import java.net.Socket;

import scenes.MainModel;
import scenes.verification.VerificationModel;

/**
 * Created by Tim on 12.09.2017.
 */
public class ServerLoginMessageHandler extends ServerMessageHandler  {
    private final String CLASSNAME = ServerMessageType.LOGIN.toString();
    private String message = null;
    private MessageHandler superHandler;

    public ServerLoginMessageHandler(String message) throws UnknownFormatException {
        if (!CLASSNAME.equals(message)) {
            throw new UnknownFormatException(message);
        }
    }
    public ServerLoginMessageHandler(){
    }

    public void write(String message,Boolean privateMessage) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage,privateMessage);
    }

    @Override
    public  void handleMessage(String msgIn) throws UnknownFormatException {
  System.out.println("Hithere");
        message = msgIn;
        String successfulOrFailed = splitMessage(message, 2);//todo set token
     
        	if(successfulOrFailed.equalsIgnoreCase("successful")){
        		VerificationModel.setLoggedIn();;}
         
        
        

        //code with observable and observer -- notify and update() -- send this with it write getMessage Method to return the string to the model

    }

    public String getMessage(){
        return message;
    }

    @Override
    public String splitMessage(String message, int tokenIndex) {
        return super.splitMessage(message, tokenIndex);
    }

    public Socket getClientSocket(){
        return superHandler.getClientSocket();
    }

}
