package Handlers;

import scenes.verification.VerificationController;
import scenes.verification.VerificationModel;

/**
 * Created by Tim on 13.09.2017.
 */
public class ServerRegisterMessageHandler extends ServerMessageHandler {
    private String message = null;
    private final String CLASSNAME = ServerMessageType.REGISTER.toString();

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
        String successfulOrFailed = splitMessage(message, 2);

        if (successfulOrFailed.equalsIgnoreCase("successful")) {
            VerificationModel.setLoggedIn();
            VerificationController.setLocalName(splitMessage(message, 3));
        }
    }

    public void write(String message) {
        message = addDelimiter(message);
        String newMessage = CLASSNAME + message;
        super.write(newMessage);
    }

    public String getMessage() {
        return message;
    }
}
