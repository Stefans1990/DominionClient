package Handlers;

import util.LogHandling;


import java.util.logging.Level;

import scenes.MainModel;
import scenes.verification.VerificationModel;

/**
 * Created by Tim on 13.09.2017.
 */
public class ServerRegisterMessageHandler extends ServerMessageHandler {
	private String message = null;
	private final String CLASSNAME = ServerMessageType.REGISTER.toString();
	private MessageHandler superHandler;

	public ServerRegisterMessageHandler(String message) throws UnknownFormatException {
		if (!CLASSNAME.equals(message)) {
			throw new UnknownFormatException(message);
		}
	}

	public ServerRegisterMessageHandler() {

	}

	@Override
	public void handleMessage(String msgIn) throws UnknownFormatException {

		message = msgIn;
		 String successfulOrFailed = splitMessage(message, 2);//todo set token
	     
     	if(successfulOrFailed.equalsIgnoreCase("successful")){
     		VerificationModel.setLoggedIn();;}
	
	}

	public void write(String message,Boolean privateMessage) {
		message = addDelimiter(message);
		String newMessage = CLASSNAME + message;
		super.write(newMessage,privateMessage);
	}

	public String getMessage() {
		return message;
	}
}
